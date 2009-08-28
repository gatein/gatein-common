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
package org.gatein.common.jar;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.jar.JarEntry;

/**
 * Enhance jar entry object by adding more info.
 *
 * @author <a href="mailto:julien@jboss.org">Julien Viet</a>
 * @version $Revision: 7448 $
 */
public class JarEntryInfo implements Comparable
{

   /** The real jar entry. */
   private final JarEntry entry;

   /** . */
   private final List names;

   public JarEntryInfo(JarEntry entry)
   {
      if (entry == null)
      {
         throw new IllegalArgumentException();
      }

      //
      String entryName = entry.getName();
      ArrayList atoms = new ArrayList();
      
      //add the root element since this is not actually included in the jar as a entry
      atoms.add("/");
      
      int previous = -1;
      while (true)
      {
         int current = entryName.indexOf('/', previous + 1);
         if (current == -1)
         {
            current = entryName.length();
         }
         if (current >= entryName.length() - 1)
         {
            if (current - previous > 1)
            {
               atoms.add(entryName.substring(previous + 1, current));
            }
            break;
         }
         if (current - previous > 1)
         {
            atoms.add(entryName.substring(previous + 1, current));
         }
         previous = current;
      }

      //
      this.entry = entry;
      this.names = Collections.unmodifiableList(atoms);
   }

   public JarEntry getEntry()
   {
      return entry;
   }

   public boolean isDirectory()
   {
      return entry.isDirectory();
   }

   public int size()
   {
      return names.size();
   }

   public List getNames()
   {
      return names;
   }

   public String getName(int index)
   {
      return (String)names.get(index);
   }

   public boolean isChildOf(JarEntryInfo parent) throws IllegalArgumentException
   {
      if (parent == null)
      {
         throw new IllegalArgumentException();
      }
      if (!parent.isDirectory())
      {
         return false;
      }
      if (parent.size() + 1 != names.size())
      {
         return false;
      }
      return parent.names.equals(parent.names.subList(0, names.size() - 1));
   }

   public boolean isDescendantOf(JarEntryInfo ancestor) throws IllegalArgumentException
   {
      if (ancestor == null)
      {
         throw new IllegalArgumentException();
      }
      if (!ancestor.isDirectory())
      {
         return false;
      }
      if (ancestor.names.size() >= names.size())
      {
         return false;
      }
      return ancestor.names.equals(names.subList(0, ancestor.size()));
   }

   public URL toURL(URL jarURL) throws IllegalArgumentException, IllegalStateException, MalformedURLException
   {
      if (jarURL == null)
      {
         throw new IllegalArgumentException("No null jarURL");
      }
      if (isDirectory())
      {
         throw new IllegalStateException("Cannot create dir URL");
      }
      StringBuffer tmp = new StringBuffer(jarURL.toString()).append("!/");
      for (int i = 0; i < names.size(); i++)
      {
         String atom = (String)names.get(i);
         tmp.append(i > 0 ? "/" : "").append(atom);
      }
      return new URL("jar", "", tmp.toString());
   }

   public String toString()
   {
      StringBuffer tmp = new StringBuffer();
      for (int i = 0; i < names.size(); i++)
      {
         String atom = (String)names.get(i);
         tmp.append(i > 0 ? "/" : "").append(atom);
      }
      if (entry.isDirectory())
      {
         tmp.append("/");
      }
      return tmp.toString();
   }

   public boolean equals(Object obj)
   {
      if (obj == this)
      {
         return true;
      }
      if (obj instanceof JarEntryInfo)
      {
         return compareTo(obj) == 0;
      }
      return false;
   }

   public int compareTo(Object obj)
   {
      JarEntryInfo that = (JarEntryInfo)obj;
      Iterator i1 = this.getNames().iterator();
      Iterator i2 = that.getNames().iterator();
      while (true)
      {
         if (i1.hasNext())
         {
            Object o = i1.next();
            String s1 = (String)o;
            if (i2.hasNext())
            {
               String s2 = (String)i2.next();
               int res = s1.compareTo(s2);
               if (res != 0)
               {
                  return res;
               }
            }
            else
            {
               return 1;
            }
         }
         else
         {
            if (i2.hasNext())
            {
               return -1;
            }
            else
            {
               return 0;
            }
         }
      }
   }
}
