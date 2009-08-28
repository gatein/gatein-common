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
package org.gatein.common.invocation;

/**
 * The scope of an attribute.
 *
 * @author <a href="mailto:julien@jboss.org">Julien Viet</a>
 * @version $Revision: 7228 $
 */
public class Scope
{

   /** Principal scope. */
   public static final Scope PRINCIPAL_SCOPE = new Scope("principal");

   /** Request scope. */
   public static final Scope REQUEST_SCOPE = new Scope("request");

   /** Thread scope. */
   public static final Scope THREAD_SCOPE = new Scope("thread");

   /** Session scope. */
   public static final Scope SESSION_SCOPE = new Scope("session");

   /** Invocation scope. */
   public static final Scope INVOCATION_SCOPE = new Scope("invocation");

   /** The value. */
   private final String value;

   /**
    * @param value the scope value
    * @throws IllegalArgumentException if the value is null
    */
   public Scope(String value) throws IllegalArgumentException
   {
      if (value == null)
      {
         throw new IllegalArgumentException();
      }
      this.value = value;
   }

   public int hashCode()
   {
      return value.hashCode();
   }

   public boolean equals(Object obj)
   {
      if (obj == this)
      {
         return true;
      }
      if (obj instanceof Scope)
      {
         Scope that = (Scope)obj;
         return value.equals(that.value);
      }
      return false;
   }

   public String toString()
   {
      return value;
   }
}
