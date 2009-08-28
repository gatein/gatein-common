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
 * The result of a request to a mapper.
 *
 * @author <a href="mailto:julien@jboss.org">Julien Viet</a>
 * @version $Revision: 5448 $
 */
public class PathMapperResult
{

   /** . */
   private final Object target;

   /** . */
   private final String targetPath;

   /** . */
   private final String targetPathInfo;

   public PathMapperResult(Object target, String targetPath, String targetPathInfo)
   {
      this.target = target;
      this.targetPath = targetPath;
      this.targetPathInfo = targetPathInfo;
   }

   public Object getTarget()
   {
      return target;
   }

   public String getTargetPathInfo()
   {
      return targetPathInfo;
   }

   public int hashCode()
   {
      int hashCode = (target != null ? target.hashCode() : 0) +
         (targetPath != null ? targetPath.hashCode() : 0) +
         (targetPathInfo != null ? targetPathInfo.hashCode() : 0);
      return hashCode;
   }

   public boolean equals(Object obj)
   {
      if (obj == this)
      {
         return true;
      }
      if (obj instanceof PathMapperResult)
      {
         PathMapperResult other = (PathMapperResult)obj;
         return (target == null ? (other.target == null) : target.equals(other.target)) &&
            (targetPath == null ? (other.targetPath == null) : targetPath.equals(other.targetPath)) &&
            (targetPathInfo == null ? (other.targetPathInfo == null) : targetPathInfo.equals(other.targetPathInfo));
      }
      return false;
   }

   public String toString()
   {
      StringBuffer buffer = new StringBuffer("MappingResult[");
      buffer.append(target == null ? "-" : target.toString());
      buffer.append(',');
      buffer.append(targetPath == null ? "-" : targetPath);
      buffer.append(',');
      buffer.append(targetPathInfo == null ? "-" : targetPathInfo);
      buffer.append("]");
      return buffer.toString();
   }
}
