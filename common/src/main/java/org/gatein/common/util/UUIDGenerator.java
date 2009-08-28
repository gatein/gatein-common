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

import java.net.InetAddress;
import java.security.SecureRandom;

/**
 * Adapted from ejb plugin key generated.
 * 
 * The implementation of UUID key generator
 * based on the algorithm from Floyd Marinescu's EJB Design Patterns.
 * 
 * @author <a href="mailto:alex.loubyansky@jboss.org">Alex Loubyansky</a>
 * @author <a href="mailto:julien@jboss.org">Julien Viet</a>
 * @version $Revision: 5451 $
 */
public class UUIDGenerator
{

   // Attributes ---------------------------------------------------

   /** Hex digits */
   private static final char[] hexDigits = "0123456789ABCDEF".toCharArray();

   // Static --------------------------------------------------------

   /** secure random to provide nonrepeating seed */
   private SecureRandom seeder;

   /** cached middle value */
   private String midValue;

   // Constructor --------------------------------------------------

   public UUIDGenerator()
   {
      try
      {
         // Cache the middle part for UUID
         StringBuffer buffer = new StringBuffer( 16 );

         // Construct host part of the uuid (8 hex digits)
         byte[] addr = InetAddress.getLocalHost().getAddress();
         buffer.append( toHex( toInt(addr), 8 ) );

         // Append the hash code for this object (8 hex digits)
         buffer.append( toHex( System.identityHashCode(this), 8 ) );

         // Set up midValue
         midValue = buffer.toString();

         // Load up the randomizer
         seeder = new SecureRandom();
         int node = seeder.nextInt();
      }
      catch (Exception e)
      {
         throw new Error("Not possible");
      }
   }

   public String generateKey()
   {
      StringBuffer buffer = new StringBuffer(32);

      // Append current time as unsigned int value
      buffer.append(toHex((int)(System.currentTimeMillis() & 0xFFFFFFFF), 8));

      // Append cached midValue
      buffer.append( midValue );

      // Append the next random int
      buffer.append( toHex( seeder.nextInt(), 8 ) );

      // Return the result
      return buffer.toString();
   }

   // Private ------------------------------------------------------
   
   /**
    * Converts int value to string hex representation
    */
   private String toHex(int value, int length)
   {
      StringBuffer buffer = new StringBuffer(length);
      int shift = (length - 1) << 2;
      int i = -1;
      while(++i < length)
      {
         buffer.append(hexDigits[(value >> shift) & 0x0000000F]);
         value <<= 4;
      }
      return buffer.toString();
   }

   /**
    * Constructs int value from byte array
    */
   private static int toInt( byte[] bytes )
   {
      int value = 0;
      int i = -1;
      while (++i < bytes.length)
      {
         value <<= 8;
         int b = bytes[ i ] & 0xff;
         value |= b;
      }
      return value;
   }
}
