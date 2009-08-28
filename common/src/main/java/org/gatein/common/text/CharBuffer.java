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

import org.gatein.common.util.ParameterValidation;

/**
 * A simple char buffer that implements the <code>CharWriter</code> interface
 *
 * @author <a href="mailto:julien@jboss.org">Julien Viet</a>
 * @version $Revision: 7228 $
 */
public class CharBuffer implements CharWriter
{

   /** . */
   protected char[] buffer;

   /** . */
   protected int length;

   /**
    * @param size the initial size
    * @throws IllegalArgumentException if the initial size is not a positive value.
    */
   public CharBuffer(int size) throws IllegalArgumentException
   {
      if (size < 0)
      {
         throw new IllegalArgumentException("Size must be positive!");
      }
      this.buffer = new char[size];
      this.length = 0;
   }

   public CharBuffer()
   {
      this.buffer = new char[512];
      this.length = 0;
   }

   public CharBuffer append(String s, CharEncoder encoder)
   {
      ParameterValidation.throwIllegalArgExceptionIfNull(s, "String");
      ParameterValidation.throwIllegalArgExceptionIfNull(encoder, "CharEncoder");
      encoder.encode(s, this);
      return this;
   }

   public CharWriter append(CharSequence s)
   {
      ParameterValidation.throwIllegalArgExceptionIfNull(s, "CharSequence");
      appendNoCheck(s);
      return this;
   }

   public CharWriter append(char[] chars, int off, int len)
   {
      ParameterValidation.throwIllegalArgExceptionIfRangeInvalid(chars, off, len);

      //
      appendNoCheck(chars, off, len);

      //
      return this;
   }


   public CharWriter append(char c)
   {
      ensureCapacity(length + 1);

      //
      buffer[length++] = c;

      //
      return this;
   }

   public CharWriter append(char[] chars)
   {
      ParameterValidation.throwIllegalArgExceptionIfNull(chars, "char[]");

      //
      appendNoCheck(chars, 0, chars.length);

      //
      return this;
   }

   public String asString()
   {
      return asString(true);
   }

   public String asString(boolean reset)
   {
      String s = new String(buffer, 0, length);
      if (reset)
      {
         reset();
      }
      return s;
   }

   public int getCapacity()
   {
      return buffer.length;
   }

   public int getLength()
   {
      return length;
   }

   public void setLength(int length)
   {
      if (length < 0)
      {
         throw new IllegalArgumentException("Length must be positive!");
      }

      //
      this.length = length;

      //
      if (length > buffer.length)
      {
         char[] tmp = new char[length];
         System.arraycopy(buffer, 0, tmp, 0, buffer.length);
         buffer = tmp;
      }
   }

   public void reset()
   {
      this.length = 0;
   }

   protected final void appendNoCheck(char[] chars, int off, int len)
   {
      ensureCapacity(length + len);
      if (len < 10)
      {
         int toCopy = off + len;
         while (off < toCopy)
         {
            buffer[length++] = chars[off++];
         }
      }
      else
      {
         System.arraycopy(chars, off, buffer, length, len);
         length += len;
      }
   }

   protected final void appendNoCheck(CharSequence s)
   {
      ensureCapacity(length + s.length());

      //
      if (s instanceof String)
      {
         ((String)s).getChars(0, s.length(), buffer, length);
         length += s.length();
      }
      else
      {
         for (int i = 0; i < s.length(); i++)
         {
            char c = s.charAt(i);
            buffer[length++] = c;
         }
      }
   }

   protected final void ensureCapacity(int minimumCapacity)
   {
      int capacity = buffer.length;
      if (capacity < minimumCapacity)
      {
         while (capacity < minimumCapacity)
         {
            capacity = capacity * 2 + 1;
         }
         char[] tmp = new char[capacity];
         System.arraycopy(buffer, 0, tmp, 0, length);
         buffer = tmp;
      }
   }

}
