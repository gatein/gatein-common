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
package org.gatein.common.path;

/**
 * A simple mapper implementation. The limitations is that only the root and its children can have children
 *
 * @author <a href="mailto:julien@jboss.org">Julien Viet</a>
 * @version $Revision: 5448 $
 */
public class SimplePathMapper implements PathMapper
{
   public PathMapperResult map(PathMapperContext ctx, String path)
   {
      Object root = ctx.getRoot();
      Object target = null;
      String targetPath = null;
      String targetPathInfo = null;

      if (path == null || path.length() == 0)
      {
         targetPath = null;
         targetPathInfo = null;
      }
      else if ("/".equals(path))
      {
         targetPath = null;
         targetPathInfo = "/";
      }
      else
      {
         int firstSlashPos = path.indexOf('/', 1);
         if (firstSlashPos == -1)
         {
            String firstChunk = path.substring(1);
            target = ctx.getChild(root, firstChunk);
            if (target != null)
            {
               targetPath = path;
               targetPathInfo = null;
            }
            else
            {
               targetPath = null;
               targetPathInfo = path;
            }
         }
         else if (firstSlashPos == 1)
         {
            targetPath = null;
            targetPathInfo = "/";
         }
         else
         {
            String firstChunk = path.substring(1, firstSlashPos);
            target = ctx.getChild(root, firstChunk);
            if (target != null)
            {
               int secondSlashPos = path.indexOf('/', firstSlashPos + 1);
               if (secondSlashPos == -1)
               {
                  String secondChunck = path.substring(firstSlashPos + 1);
                  if (secondChunck.length() == 0)
                  {
                     targetPath = "/" + firstChunk;
                     targetPathInfo = "/";
                  }
                  else
                  {
                     Object child = ctx.getChild(target, secondChunck);
                     if (child != null)
                     {
                        target = child;
                        targetPath = path;
                        targetPathInfo = null;
                     }
                     else
                     {
                        targetPath = "/" + firstChunk;
                        targetPathInfo = "/" + secondChunck;
                     }
                  }
               }
               else
               {
                  targetPath = "/" + firstChunk;
                  targetPathInfo = path.substring(firstSlashPos);
               }
            }
            else
            {
               targetPath = null;
               targetPathInfo = path;
            }
         }
      }
      return new PathMapperResult(target, targetPath, targetPathInfo);
   }
}
