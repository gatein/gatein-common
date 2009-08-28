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
package org.gatein.common.util;

import org.gatein.common.net.URLTools;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/**
 * @author <a href="mailto:julien@jboss.org">Julien Viet</a>
 * @version $Revision: 7379 $
 */
public class CLResourceLoader implements ResourceLoader
{

   private final ClassLoader loader;

   public CLResourceLoader(ClassLoader loader)
   {
      if (loader == null)
      {
         throw new IllegalArgumentException("No classloader provided");
      }
      this.loader = loader;
   }

   public CLResourceLoader()
   {
      this(Thread.currentThread().getContextClassLoader());
   }

   public InputStream load(String location)
   {
      return loader.getResourceAsStream(location);
   }

   public LoaderResource getResource(String location)
   {
      if (location == null)
      {
         throw new IllegalArgumentException("Location is null");
      }
      URL url = loader.getResource(location);
      return new URLResource(location, url);
   }

   private static class URLResource extends LoaderResource
   {

      private final URL url;

      public URLResource(String location, URL url)
      {
         super(location);
         this.url = url;
      }

      public boolean exists()
      {
         return URLTools.exists(url);
      }

      public InputStream asInputStream() throws IllegalStateException
      {
         if (!exists())
         {
            throw new IllegalStateException("Resource " + location + " does not exist");
         }
         try
         {
            return url.openStream();
         }
         catch (IOException e)
         {
            throw new IllegalStateException("Cannot open resource stream " + location);
         }
      }

      public String toString()
      {
         return "Resource[" + location + "," + url + "]";
      }
   }
}
