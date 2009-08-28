/******************************************************************************
 * JBoss, a division of Red Hat                                               *
 * Copyright 2008, Red Hat Middleware, LLC, and individual                    *
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
package org.gatein.common.net.media;

import java.util.Locale;

/**
 * @author <a href="mailto:julien@jboss-portal.org">Julien Viet</a>
 * @version $Revision: 630 $
 */
public final class SubtypeDef
{

   /** . */
   public static final SubtypeDef HTML = new SubtypeDef("html");

   /** . */
   public static final SubtypeDef XML = new SubtypeDef("xml");

   /** . */
   public static final SubtypeDef CSS = new SubtypeDef("css");

   /** . */
   public static final SubtypeDef PLAIN = new SubtypeDef("plain");

   /** . */
   public static final SubtypeDef JAVASCRIPT = new SubtypeDef("javascript");

   /** . */
   public static final SubtypeDef X_WWW_FORM_URLENCODED = new SubtypeDef("x-www-form-urlencoded");

   /** . */
   public static final SubtypeDef FORM_DATA = new SubtypeDef("form-data");

   /**
    * Returns a corresponding subtype definition for the specified subtype name.
    *
    * @param subtypeName the subtype name
    * @return a subtype definition
    * @throws IllegalArgumentException if the subtype name is null or is empty
    */
   public static SubtypeDef create(String subtypeName) throws IllegalArgumentException
   {
      if (subtypeName == null)
      {
         throw new IllegalArgumentException("No null subtype name accepted");
      }
      if (subtypeName.length() == 0)
      {
         throw new IllegalArgumentException("No empty subtype name accepted");
      }
      return new SubtypeDef(subtypeName);
   }

   /** . */
   private final String name;

   /** . */
   private volatile String toString;

   SubtypeDef(String name)
   {
      if (name == null)
      {
         throw new IllegalArgumentException("No null name allowed");
      }

      //
      this.name = name.toLowerCase(Locale.ENGLISH);
   }

   public String getName()
   {
      return name;
   }

   public int hashCode()
   {
      return name.hashCode();
   }

   public boolean equals(Object obj)
   {
      if (obj == this)
      {
         return true;
      }
      if (obj instanceof SubtypeDef)
      {
         SubtypeDef that = (SubtypeDef)obj;
         return name.equals(that.name);
      }
      return false;
   }

   public String toString()
   {
      if (toString == null)
      {
         toString = "SubtypeDef[" + name + "]";
      }
      return toString;
   }
}
