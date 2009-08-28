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
package org.gatein.common.text;

/**
 * @author <a href="mailto:julien@jboss.org">Julien Viet</a>
 * @version $Revision: 1.1 $
 */
public class TextTools
{

   /**
    * Returns true if the char c is alpha numeric i.e it belongs to one of the following ranges [0,9], [A,Z] or
    * [a,z]
    * @param c the char to test
    * @return true if c is alpha numeric
    */
   public static boolean isAlphaNumeric(char c)
   {
      return (c >= '0' && c <= '9') || (c >= 'A' && c <= 'Z') || (c >= 'a' && c <= 'z');
   }

   /**
    * Returns the hexadecimal value of the provided numeric value.
    * @param z the numeric value to convert
    * @return the hexadecimal char
    * @throws IllegalArgumentException if the value is not between 0 and 15
    */
   public static char toHex(int z) throws IllegalArgumentException
   {
      if (z >= 0 && z < 10)
      {
         return (char)('0' + z);
      }
      else if (z >= 10 && z < 16)
      {
         return (char)('A' - 10 + z);
      }
      else
      {
         throw new IllegalArgumentException("Wrong character");
      }
   }
}
