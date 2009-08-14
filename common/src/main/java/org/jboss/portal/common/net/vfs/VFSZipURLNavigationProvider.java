/******************************************************************************
 * JBoss, a division of Red Hat                                               *
 * Copyright 2009, Red Hat Middleware, LLC, and individual                    *
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
package org.jboss.portal.common.net.vfs;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;

import org.apache.log4j.Logger;
import org.jboss.portal.common.net.URLFilter;
import org.jboss.portal.common.net.URLNavigationProvider;
import org.jboss.portal.common.net.URLVisitor;
import org.jboss.virtual.VirtualFile;
import org.jboss.virtual.plugins.vfs.VirtualFileURLConnection;

/**
 * @author <a href="mailto:julien@jboss.org">Julien Viet</a>
 * @version $Revision: 7448 $
 */
public class VFSZipURLNavigationProvider implements URLNavigationProvider
{

   /** The logger. */
   private Logger log = Logger.getLogger(VFSZipURLNavigationProvider.class);

   /** Trace. */
   private boolean trace = log.isDebugEnabled();
   
   public void visit(URL url, URLVisitor visitor, URLFilter filter) throws IllegalArgumentException, IOException
   {
      if (url == null)
      {
         throw new IllegalArgumentException("Null URL not accepted");
      }
      if (!"vfszip".equals(url.getProtocol()))
      {
         throw new IllegalArgumentException("Only jar URL are accepted, not " + url.getProtocol());
      }
      VirtualFileURLConnection conn = (VirtualFileURLConnection) url.openConnection();
      VirtualFile vFile = conn.getContent();
      
      visit(vFile, visitor, filter);
   }

   private void visit(VirtualFile file, URLVisitor visitor, URLFilter filter) throws IOException
   {      
      if (!file.exists())
      {
         throw new FileNotFoundException();
      }
      else
      {    
         String name = file.getName();
         if (!file.isLeaf())
         {
            if (trace)
            {
               log.debug("entering directory" + file.getPathName());
            }
            try
            {
               URL url = file.toURL();
               boolean visit = filter == null || filter.acceptDir(url);
               if (visit)
               {
                  visitor.startDir(url, name);
                  for (VirtualFile child : file.getChildren())
                  {
                     visit(child, visitor, filter);
                  }

                  visitor.endDir(file.toURL(), name);
                  if (trace)
                  {
                     log.debug("leaving directory" + file.getPathName());
                  }
               }
            }
            catch (URISyntaxException e)
            {
               throw new IOException("Error trying to get Virtual File for " + file.toString() + ". " + e);
            }
         }
         else
         {
            try
            {
               if (trace)
               {
                  log.debug("visiting file " + file.getPathName());
               }
               URL url = file.toURL();
               if (filter.acceptFile(url))
               {
                  visitor.file(url, name);
               }
               else if (trace)
               {
                  log.debug("The file does not respect url format");
               }
            }
            catch (URISyntaxException e)
            {
               throw new IOException("Error trying to get Virtual File for " + file.toString() + ". " + e);
            }
         }
      }
   }
   
}
