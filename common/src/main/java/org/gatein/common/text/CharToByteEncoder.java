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

import java.nio.charset.CharsetEncoder;
import java.nio.charset.Charset;
import java.nio.CharBuffer;
import java.nio.ByteBuffer;

/**
 * Defines an interface for encoding a char to a sequence of bytes.
 *
 * @author <a href="mailto:julien@jboss.org">Julien Viet</a>
 * @version $Revision: 1.1 $
 */
public interface CharToByteEncoder
{

   /**
    * Encode the specified char. The returned byte array can be used safely until a new invocation to the same
    * object is done. If the returned array has a length of zero it means that the char cannot be encoded. 
    *
    * @param c the char to encode
    * @return the encoded char as a byte array
    * @throws EncodingException
    */
   byte[] encode(char c) throws EncodingException;

   /** . */
   final byte[] EMPTY = new byte[0];

   /**
    * Generic implementation that works for any charset, based on NIO.
    */
   public static class Generic implements CharToByteEncoder
   {

      /** . */
      public static final Generic UTF8 = new Generic("UTF8");

      /** . */
      private final CharsetEncoder encoder;

      /** . */
      private final java.nio.CharBuffer in;

      /** . */
      private final ByteBuffer out;

      /** . */
      private final byte[][] arrays = {new byte[0],new byte[1],new byte[2],new byte[3],new byte[4], new byte[5]};

      public Generic(Charset charset)
      {
         encoder = charset.newEncoder();
         in = CharBuffer.allocate(1);
         out = ByteBuffer.allocate(100);
      }

      public Generic(String encoding)
      {
         this(Charset.forName(encoding));
      }

      public byte[] encode(char c) throws EncodingException
      {
         switch(Character.getType(c))
         {
            case Character.SURROGATE:
            case Character.PRIVATE_USE:
               return EMPTY;
            default:
               if (encoder.canEncode(c))
               {
                  in.rewind();
                  out.rewind();
                  in.put(0, c);
                  encoder.reset();
                  encoder.encode(in, out, true);
                  encoder.flush(out);
                  int length = out.position();
                  byte[] bytes = arrays[length];
                  System.arraycopy(out.array(), 0, bytes, 0, length);
                  return bytes;
               }
               else
               {
                  return EMPTY;
               }
         }
      }
   }
}
