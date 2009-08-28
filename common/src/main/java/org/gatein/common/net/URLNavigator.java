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
package org.gatein.common.net;

import org.gatein.common.net.file.FileURLNavigationProvider;
import org.gatein.common.net.jar.JarURLNavigationProvider;

import java.io.IOException;
import java.net.URL;

/**
 * The URLNavigator class is a registry for various URLNavigationProvider.
 *
 * @author <a href="mailto:julien@jboss.org">Julien Viet</a>
 * @version $Revision: 6852 $
 */
public class URLNavigator
{

   private static final URLNavigationProvider fileNav = new FileURLNavigationProvider();

   private static final URLNavigationProvider jarNav = new JarURLNavigationProvider();

   private static final URLFilter NULL_FILTER = new URLFilter()
   {
      public boolean acceptFile(URL url)
      {
         return true;
      }

      public boolean acceptDir(URL url)
      {
         return true;
      }
   };

   public static void visit(URL url, URLVisitor visitor, URLFilter filter) throws IllegalArgumentException, IOException
   {
      URLNavigationProvider provider = getProvider(url);

      if (filter == null)
      {
         filter = NULL_FILTER;
      }

      provider.visit(url, visitor, filter);
   }

   public static void visit(URL url, URLVisitor visitor) throws IOException
   {
      visit(url, visitor, null);
   }

   /**
    * Return an URLNavigationProvider for the specified URL.
    *
    * @param url the target url
    * @return the corresponding URL navigator
    * @throws IllegalArgumentException if the url is null or no provider is found
    */
   private static URLNavigationProvider getProvider(URL url) throws IllegalArgumentException
   {
      if (url == null)
      {
         throw new IllegalArgumentException("Null not accepted");
      }
      String protocol = url.getProtocol();
      if ("file".equals(protocol))
      {
         return fileNav;
      }
      else if ("jar".equals(protocol))
      {
         return jarNav;
      }
      else if ("vfsfile".equals(protocol))
      {
    	  return fileNav;
      }
      else
      {
         throw new IllegalArgumentException("Not recognized " + protocol);
      }
   }

}
