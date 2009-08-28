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

import java.lang.reflect.Field;

/**
 * Extends the java Modifier object in order to add more utility methods.
 *
 * @author <a href="mailto:julien@jboss.org">Julien Viet</a>
 * @version $Revision: 7228 $
 */
public class Modifier extends java.lang.reflect.Modifier
{

   /**
    * Return true if the field is considered as a readable property which means that it is public and non static.
    *
    * @param f the field to test
    * @return true if the field is a readable property
    * @throws IllegalArgumentException if the field argument is null
    */
   public static boolean isReadableProperty(Field f) throws IllegalArgumentException
   {
      if (f == null)
      {
         throw new IllegalArgumentException("No field provided");
      }

      //
      int modifiers = f.getModifiers();
      return (modifiers & STATIC) == 0 && (modifiers & PUBLIC) != 0;
   }

   /**
    * Return true if the field is considered as a writable property which means that it is public, non static and non
    * final.
    *
    * @param f the field to test
    * @return true if the field is a writable property
    * @throws IllegalArgumentException if the field argument is null
    */
   public static boolean isWritableProperty(Field f)
   {
      if (f == null)
      {
         throw new IllegalArgumentException("No field provided");
      }

      //
      int modifiers = f.getModifiers();
      return (modifiers & (STATIC | FINAL)) == 0 && (modifiers & PUBLIC) != 0;
   }
}
