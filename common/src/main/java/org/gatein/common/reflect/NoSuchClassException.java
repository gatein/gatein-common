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
package org.gatein.common.reflect;

/**
 * A generic exception that can be reused when an API expose a class loading service.
 * The goal is to wrap any kind of throwable that would signal a class loading failure such
 * as <code>NoClassDefFoundError</code> or <code>ClassNotFoundException</code> and make the client
 * of the API deal with only one kind of exception.
 *
 * @author <a href="mailto:julien@jboss.org">Julien Viet</a>
 * @version $Revision: 630 $
 */
public class NoSuchClassException extends Exception
{

   /** . */
   private final String className;

   public NoSuchClassException(String className)
   {
      this.className = className;
   }

   public NoSuchClassException(String className, String message)
   {
      super(message);

      //
      this.className = className;
   }

   public NoSuchClassException(String className, String message, Throwable cause)
   {
      super(message, cause);

      //
      this.className = className;
   }

   public NoSuchClassException(String className, Throwable cause)
   {
      super(cause);

      //
      this.className = className;
   }

   public String getClassName()
   {
      return className;
   }
}
