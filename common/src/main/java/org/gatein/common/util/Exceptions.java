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
 
/**
 * @author <a href="mailto:julien@jboss.org">Julien Viet</a>
 * @version $Revision: 7395 $
 */
public class Exceptions
{

   public static Throwable unwrap(Throwable throwable) throws IllegalArgumentException
   {
      if (throwable == null)
      {
         throw new IllegalArgumentException("Cannot unwrap null throwable");
      }
      for (Throwable current = throwable;current != null;current = current.getCause())
      {
         throwable = current;
      }
      return throwable;
   }

   public static String toHTML(Throwable throwable)
   {
      return toHTML(throwable, false);
   }

   public static String toHTML(Throwable throwable, boolean deep)
   {
      StringBuffer tmp = new StringBuffer();
      appendHTMLTo(tmp, throwable, deep);
      return tmp.toString();
   }

   public static void appendHTMLTo(StringBuffer buffer, Throwable throwable)
   {
      appendHTMLTo(buffer, throwable, false);
   }

   public static void appendHTMLTo(StringBuffer buffer, Throwable throwable, boolean deep)
   {
      buffer.append("<div><pre style=\"text-align:left;\"><code>");
      while (throwable != null)
      {
         buffer.append(throwable.toString()).append('\n');
         StackTraceElement[] elts = throwable.getStackTrace();
         for (int j = 0; j < elts.length; j++)
         {
            StackTraceElement elt = elts[j];
            buffer.append("\tat ").append(elt).append("\n");
         }
         throwable = throwable.getCause();
      }
      buffer.append("</code></pre></div>");
   }
}
