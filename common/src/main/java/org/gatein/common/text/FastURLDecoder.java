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
public class FastURLDecoder extends AbstractCharEncoder
{

   /** . */
   private static final FastURLDecoder UTF8 = new FastURLDecoder(CharToByteEncoder.Generic.UTF8);

   /** . */
   private static final FastURLDecoder UTF8_STRICT = new FastURLDecoder(CharToByteEncoder.Generic.UTF8, true);

   public static FastURLDecoder getUTF8Instance()
   {
      return UTF8;
   }

   public static FastURLDecoder getUTF8StrictInstance()
   {
      return UTF8_STRICT;
   }

   /** . */
   private static final char FROM = 0;

   /** . */
   private static final char TO = (char)0x10FFFD;

   /** . */
   private static final int AS_IS = 0;

   /** . */
   private static final int PLUS = 1;

   /** . */
   private static final int ESCAPE = 2;

   /** . */
   private static final int ERROR = 3;

   /** . */
   private final LookupNonTerm root;

   /** . */
   private final int[] decisions;

   /** . */
   private final int[][] conversions;

   /** . */
   private final boolean strict;

   public FastURLDecoder(CharToByteEncoder encoder)
   {
      this(encoder, false);
   }

   public FastURLDecoder(CharToByteEncoder encoder, boolean strict)
   {
      this.strict = strict;

      //
      root = new LookupNonTerm();
      for (char c = FROM; c <= TO; c++)
      {
         byte[] v = encoder.encode(c);
         LookupNonTerm node = root;
         int k = v.length;
         for (int i = 0; i < k; i++)
         {
            int index = (int)v[i] & 0xFF;
            if (i == k - 1)
            {
               node.children[index] = new LookupTerm(c);
            }
            else
            {
               if (node.children[index] == null)
               {
                  node.children[index] = new LookupNonTerm();
               }
               node = (LookupNonTerm)node.children[index];
            }
         }
      }

      //
      conversions = new int[256][256];
      for (char i = 0; i < 256; i++)
      {
         int x = hexValue(i);
         for (char j = 0; j < 256; j++)
         {
            int y = hexValue(j);
            if (x != -1 && y != -1)
            {
               conversions[i][j] = (x << 4) + y;
            }
            else
            {
               conversions[i][j] = -1;
            }
         }
      }

      //
      decisions = new int[256];
      for (int i = 0; i < decisions.length; i++)
      {
         if (TextTools.isAlphaNumeric((char)i))
         {
            decisions[i] = AS_IS;
         }
         else
         {
            switch (i)
            {
               case '+':
                  decisions[i] = PLUS;
                  break;
               case '.':
               case '-':
               case '*':
               case '_':
                  decisions[i] = AS_IS;
                  break;
               case '%':
                  decisions[i] = ESCAPE;
                  break;
               default:
                  decisions[i] = ERROR;
                  break;
            }
         }
      }
   }


   protected void safeEncode(char[] chars, int i, int length, CharWriter tmp)
   {
      while (i < length)
      {
         char c = chars[i++];
         int decision = c < 256 ? decisions[c] : ERROR;
         switch (decision)
         {
            case AS_IS:
               tmp.append(c);
               break;
            case PLUS:
               tmp.append(' ');
               break;
            case ESCAPE:
               int j = i;

               //
               LookupNonTerm current = root;
               while (true)
               {
                  if (j + 2 > length)
                  {
                     throw new MalformedInputException("Not enough chars to decode an escaped value length should have been" +
                        (j + 2) + " but is " + length);
                  }

                  //
                  char c1 = chars[j++];
                  char c2 = chars[j++];

                  //
                  if (c1 > 255)
                  {
                     throw new MalformedInputException("Input out of the lookup range (" + c1 + "," + c2 + ")");
                  }
                  if (c2 > 255)
                  {
                     throw new MalformedInputException("Input out of the lookup range (" + c1 + "," + c2 + ")");
                  }


                  int z = conversions[c1][c2];

                  //
                  if (z == -1)
                  {
                     throw new MalformedInputException("Input out of the lookup range (" + c1 + "," + c2 + ")");
                  }

                  //
                  LookupNode child = current.children[z];
                  if (child instanceof LookupTerm)
                  {
                     LookupTerm term = (LookupTerm)child;
                     tmp.append(term.c);
                     i = j;
                     break;
                  }
                  else
                  {
                     current = (LookupNonTerm)child;
                  }

                  //
                  j++;
               }
               break;
            case ERROR:
               if (strict)
               {
                  throw new MalformedInputException("Cannot decode char '" + c + "'");
               }
               else
               {
                  tmp.append(c);
               }
               break;
         }
      }
   }

   public boolean isStrict()
   {
      return strict;
   }

   private static class LookupNode
   {
   }

   private static class LookupTerm extends LookupNode
   {

      /** . */
      private final char c;

      public LookupTerm(char c)
      {
         this.c = c;
      }
   }

   private static class LookupNonTerm extends LookupNode
   {
      private final LookupNode[] children = new LookupNode[256];
   }

   /**
    * Returns the hex value of the char c. If the char cannot be converted then -1 is returned.
    *
    * @param c the char to convert
    * @return the converted hex value
    */
   private static int hexValue(char c)
   {
      if (c >= 'A' && c <= 'F')
      {
         return c - 'A' + 10;
      }
      if (c >= 'a' && c <= 'f')
      {
         return c - 'a' + 10;
      }
      if (c >= '0' && c <= '9')
      {
         return c - '0';
      }
      return -1;
   }
}
