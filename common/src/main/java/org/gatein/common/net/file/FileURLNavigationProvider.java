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
package org.gatein.common.net.file;

import org.gatein.common.net.URLNavigationProvider;
import org.gatein.common.net.URLVisitor;
import org.gatein.common.net.URLFilter;
import org.apache.log4j.Logger;

import java.util.Arrays;
import java.net.URL;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.File;

/**
 * @author <a href="mailto:julien@jboss.org">Julien Viet</a>
 * @version $Revision: 7448 $
 */
public class FileURLNavigationProvider implements URLNavigationProvider
{

   /** The logger. */
   private Logger log = Logger.getLogger(FileURLNavigationProvider.class);

   /** Trace. */
   private boolean trace = log.isDebugEnabled();

   public void visit(URL url, URLVisitor visitor, URLFilter filter) throws IllegalArgumentException, IOException
   {
      File file = new File(url.getFile()).getCanonicalFile();
      visit(file, visitor, filter);
   }

   private void visit(File file, URLVisitor visitor, URLFilter filter) throws IllegalArgumentException, IOException
   {
      if (!file.exists())
      {
         throw new FileNotFoundException();
      }
      else
      {    
         String name = file.getName();
         if (file.isDirectory())
         {
            if (trace)
            {
               log.debug("entering directory" + file.getAbsolutePath());
            }
            URL url = file.toURL();
            boolean visit = filter == null || filter.acceptDir(url);
            if (visit)
            {
               visitor.startDir(url, name);
               File[] childrenFiles = file.listFiles();
               Arrays.sort(childrenFiles);
               for (int i = 0; i < childrenFiles.length; i++)
               {
                  File childFile = childrenFiles[i];
                  visit(childFile, visitor, filter);
               }
               visitor.endDir(file.toURL(), name);
               if (trace)
               {
                  log.debug("leaving directory" + file.getAbsolutePath());
               }
            }
         }
         else
         {
            if (trace)
            {
               log.debug("visiting file " + file.getAbsolutePath());
            }
            URL url = file.toURL();
            File file2 = new File(url.getFile());
            if (file.equals(file2) && filter.acceptFile(url))
            {
               visitor.file(url, name);
            }
            else if (trace)
            {
               log.debug("The file does not respect url format");
            }
         }
      }
   }
}
