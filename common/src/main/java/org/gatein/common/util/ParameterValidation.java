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
 * @author <a href="mailto:chris.laprun@jboss.com">Chris Laprun</a>
 * @version $Revision: 5757 $
 * @since 2.4 (May 31, 2006)
 */
public class ParameterValidation
{
   public static void throwIllegalArgExceptionIfNullOrEmpty(String valueToCheck, String valueName, String contextName)
   {
      if (isNullOrEmpty(valueToCheck))
      {
         throw new IllegalArgumentException((contextName != null ? contextName + " r" : "R") + "equires a non-null, non-empty " + valueName);
      }
   }

   public static void throwIllegalArgExceptionIfRangeInvalid(char[] chars, int offset, int length)
   {
      ParameterValidation.throwIllegalArgExceptionIfNull(chars, "characters");

      if (offset < 0)
      {
         throw new IllegalArgumentException("Offset must be positive!");
      }
      if (length < 0)
      {
         throw new IllegalArgumentException("Length must be positive!");
      }
      if (offset + length > chars.length)
      {
         throw new IllegalArgumentException("Specified character range is outside of the given char[]!");
      }
   }

   /** @since 2.6 */
   public static boolean isNullOrEmpty(String valueToCheck)
   {
      return valueToCheck == null || valueToCheck.length() == 0;
   }

   public static void throwIllegalArgExceptionIfNull(Object objectToTest, String name)
   {
      if (objectToTest == null)
      {
         throw new IllegalArgumentException("Must pass a non null " + name);
      }
   }

   /** @since 2.4.1 */
   public static void throwIllegalArgExceptionIfNullOrEmpty(Object[] array, String name)
   {
      if (array == null)
      {
         throw new IllegalArgumentException("Must pass a non-null " + name);
      }
      if (array.length == 0)
      {
         throw new IllegalArgumentException("Must pass a non-empty " + name);
      }
   }
}
