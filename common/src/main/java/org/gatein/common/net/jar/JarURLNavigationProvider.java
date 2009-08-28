/******************************************************************************
 * JBoss, a division of Red Hat                                               *
 * Copyright 2006, Red Hat Middleware, LLC, and individual                    *
 * contributors as indicated by the @authors tag. See the                     *
 * copyright.txt in the distribution for a full listing of                    *
 * individual contributors.                                                   *
 *                                                                            *
 * This is free software; you can redistribute it and/or modify it            *
 * under the terms of the GNU Lesser General Public License as                *
 * published by the Free Software Foundation; either version 2.1 of           *
 * the License, or (at your option) any later version.                        *
 *                                                                            *
 * This software is distributed in the hope that it will be useful,           *
 * but WITHOUT ANY WARRANTY; without even the implied warranty of             *
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU           *
 * Lesser General Public License for more details.                            *
 *                                                                            *
 * You should have received a copy of the GNU Lesser General Public           *
 * License along with this software; if not, write to the Free                *
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA         *
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.                   *
 ******************************************************************************/
package org.gatein.common.net.jar;

import org.gatein.common.net.URLNavigationProvider;
import org.gatein.common.net.URLVisitor;
import org.gatein.common.net.URLFilter;
import org.gatein.common.jar.JarEntryInfo;
import org.gatein.common.jar.JarInfo;

import java.io.IOException;
import java.net.JarURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * @author <a href="mailto:julien@jboss.org">Julien Viet</a>
 * @version $Revision: 7448 $
 */
public class JarURLNavigationProvider implements URLNavigationProvider
{

   public void visit(URL url, URLVisitor visitor, URLFilter filter) throws IllegalArgumentException, IOException
   {
      if (url == null)
      {
         throw new IllegalArgumentException("Null URL not accepted");
      }
      if (!"jar".equals(url.getProtocol()))
      {
         throw new IllegalArgumentException("Only jar URL are accepted, not " + url.getProtocol());
      }
      JarURLConnection conn = (JarURLConnection)url.openConnection();
      visit(conn, visitor, filter);
   }

   private void visit(JarURLConnection conn, URLVisitor visitor, URLFilter filter) throws IOException
   {
      JarFile jarFile = conn.getJarFile();
      URL jarURL = conn.getJarFileURL();
      JarEntry rootEntry = conn.getJarEntry();

      // if the URL specifies a directory without a "/" at the end
      // the entry will be found, except it won't actually exist.
      // To get around this issue, we need to check if an entry exists
      // with a "/" at the end.
      if (rootEntry != null)
      {
         JarEntry testDirEntry = conn.getJarFile().getJarEntry(rootEntry + "/");
         if (testDirEntry != null)
         {
            rootEntry = testDirEntry;
        }
      }
      else
      {
         // if rootEntry == null then the url points to the root of the jar. The problem
         // is that the root of the jar doesn't actually exist in the jar.
         // We need to create a fake jar entry to mimic this behavior
         rootEntry = new JarEntry("/");
      }

      // Get the root entry
      JarEntryInfo rootEntryInfo = new JarEntryInfo(rootEntry);

      // The entries we will browse
      JarInfo jarInfo = new JarInfo(jarFile);

      boolean enabled = true;

      //
      Stack stack = new Stack(jarURL, rootEntryInfo);
      for (Iterator i = jarInfo.entries(); i.hasNext();)
      {
         JarEntryInfo entryInfo = (JarEntryInfo)i.next();

         // Only consider descendant of the root or root itself
         if (entryInfo.equals(rootEntryInfo) || entryInfo.isDescendantOf(rootEntryInfo))
         {
            List relPath;
            // The relative path from the root
            if (rootEntryInfo.size() > 1){
               relPath = entryInfo.getNames().subList(rootEntryInfo.size() - 1, entryInfo.size());
            }
            else
            {
               relPath = entryInfo.getNames(); 
            }

            // Enter intermediate dirs
            while (stack.size() < relPath.size() - 1 && enabled)
            {
               String name = (String)relPath.get(stack.size());
               stack.push(name, true);
               URL url = stack.getURL();
               boolean visit = filter == null || filter.acceptDir(url);
               if (visit)
               {
                  visitor.startDir(url, name);
               }
               else
               {
                  enabled = false;
               }
            }

            // Leave intermediate dirs
            while (stack.size() > relPath.size() - 1)
            {
               URL url = stack.getURL();
               Stack.Entry entry = stack.pop();
               if (enabled)
               {
                  String name = entry.name;
                  visitor.endDir(url, name);
               }
               enabled = true;
            }

            //
            if (enabled)
            {
               if (entryInfo.isDirectory())
               {
                  String name = (String)relPath.get(relPath.size() - 1);
                  stack.push(name, true);
                  URL url = stack.getURL();
                  boolean visit = filter == null || filter.acceptDir(url);
                  if (visit)
                  {
                     visitor.startDir(url, name);
                  }
                  else
                  {
                     enabled = false;
                  }
               }
               else
               {
                  String name = (String)relPath.get(relPath.size() - 1);
                  stack.push(name, false);
                  URL url = stack.getURL();
                  if (filter.acceptFile(url))
                  {
                     visitor.file(url, name);
                  }
                  stack.pop();
               }
            }
         }
      }

      //
      while (stack.size() > 0)
      {
         URL url = stack.getURL();
         Stack.Entry entry = stack.pop();
         if (enabled)
         {
            String name = entry.name;
            visitor.endDir(url, name);
         }
         enabled = true;
      }
   }

   private static class Stack
   {

      // The jar URL
      final URL jarURL;

      // The root entry
      final JarEntryInfo root;

      // The list of names relative to the root entry
      final LinkedList entries;

      //
      boolean enabled;

      public Stack(URL jarURL, JarEntryInfo root)
      {
         this.jarURL = jarURL;
         this.root = root;
         this.entries = new LinkedList();
      }

      int size()
      {
         return entries.size();
      }

      void push(String name, boolean dir)
      {
         entries.addLast(new Entry(name, dir));
      }

      Entry pop()
      {
         return (Entry)entries.removeLast();
      }

      Entry peek()
      {
         return (Entry)entries.getLast();
      }

      /**
       * Generate the full URL for the current entry.
       *
       * @return
       * @throws MalformedURLException
       */
      URL getURL() throws MalformedURLException
      {
         StringBuffer tmp = new StringBuffer(jarURL.toString()).append("!/");
         for (int i = 0; i < root.size() - 1; i++)
         {
            String name = root.getName(i);
            if (!tmp.toString().endsWith("/"))
            {
            	tmp.append("/");
            }
            // we don't want to add an extra '/' if the actual jar root element
            if (!name.equals("/"))
            {
               tmp.append(name);
            }
         }
         for (int i = 0; i < entries.size(); i++)
         {
            Entry entry = (Entry)entries.get(i);
            String name = entry.name;
            if (!tmp.toString().endsWith("/"))
            {
            	tmp.append("/");
            }
            if (!name.equals("/"))
            {
               tmp.append(name);
            }

         }
         Entry entry = peek();
         if (entry.dir && !entry.name.equals("/") && !(tmp.toString().endsWith("/")))
         {
        	 tmp.append('/');
         }
         return new URL("jar", "", tmp.toString());
      }

      static class Entry
      {
         /** The entry name. */
         final String name;

         /** Whether the name represents a dir or not. */
         final boolean dir;

         public Entry(String name, boolean dir)
         {
            this.name = name;
            this.dir = dir;
         }
      }
   }
}
