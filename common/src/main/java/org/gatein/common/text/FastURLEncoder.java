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

import org.gatein.common.io.UndeclaredIOException;
import org.gatein.common.util.ParameterValidation;

import java.io.IOException;
import java.io.Writer;

/**
 * An implementation based on a table for lookups.
 *
 * @author <a href="mailto:julien@jboss.org">Julien Viet</a>
 * @version $Revision: 7228 $
 */
public class FastURLEncoder extends AbstractCharEncoder
{

   /** . */
   private static final FastURLEncoder DEFAULT_ENCODER = new FastURLEncoder(CharToByteEncoder.Generic.UTF8);

   public static FastURLEncoder getUTF8Instance()
   {
      return DEFAULT_ENCODER;
   }

   /** . */
   private final char[][] table;

   /** . */
   private static final char MAX = (char)0x10FFFD;

   public FastURLEncoder(CharToByteEncoder encoder) throws IllegalArgumentException
   {
      if (encoder == null)
      {
         throw new IllegalArgumentException("No encoding provided");
      }

      //
      this.table = new char[MAX + 1][];

      //
      for (char c = 0; c <= MAX; c++)
      {
         char[] z;
         if (TextTools.isAlphaNumeric(c))
         {
            z = new char[]{c};
         }
         else
         {
            switch (c)
            {
               case ' ':
                  z = new char[]{'+'};
                  break;
               case '.':
               case '-':
               case '*':
               case '_':
                  z = new char[]{c};
                  break;
               default:
                  byte[] v = encoder.encode(c);
                  if (v.length > 0)
                  {
                     z = new char[v.length * 3];
                     int index = 0;
                     for (int i = 0; i < v.length; i++)
                     {
                        byte b = v[i];
                        z[index++] = '%';
                        z[index++] = TextTools.toHex((b & 0xF0) >> 4);
                        z[index++] = TextTools.toHex(b & 0x0F);
                     }
                  }
                  else
                  {
                     z = null;
                  }
                  break;
            }
         }
         table[c] = z;
      }
   }

   public void encode(char c, CharWriter writer)
   {
      ParameterValidation.throwIllegalArgExceptionIfNull(writer, "CharWriter");

      //
      char[] z = getEncoding(c);
      writer.append(z);
   }

   private char[] getEncoding(char c)
   {
      char[] z = table[c];
      if (z == null)
      {
         throw new MalformedInputException("Couldn't find appropriate encoding for '" + c + "'");
      }
      return z;
   }

   protected void safeEncode(char[] chars, int off, int len, CharWriter writer)
   {
      for (int i = off; i < len; i++)
      {
         char c = chars[i];
         char[] z = getEncoding(c);
         writer.append(z);
      }
   }

   public void encode(String s, Writer out) throws IllegalArgumentException, UndeclaredIOException
   {
      ParameterValidation.throwIllegalArgExceptionIfNull(s, "String");
      ParameterValidation.throwIllegalArgExceptionIfNull(out, "Writer");

      try
      {
         for (int i = 0; i < s.length(); i++)
         {
            char c = s.charAt(i);
            char[] z = getEncoding(c);
            out.write(z);
         }
      }
      catch (IOException e)
      {
         throw new UndeclaredIOException(e);
      }
   }

   public String toString()
   {
      return "FastURLEncoder[" + "" + ",[" + 0 + "," + MAX + "]]";
   }
}
