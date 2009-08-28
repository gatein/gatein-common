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
 * <p>Encodes and decodes to and from Base64 and URL-safe Base64 encodings. URL-safe Base64 encoding being defined here
 * as standard Base64 encoding with the following modifications:</p>
 * <ul>
 * <li>use '-' and '_' instead of '+' and '/'</li>
 * <li>no line feeds</li>
 * <li>padding character is '*' instead of '='</li>
 * </ul>
 * <p/>
 * <p>Based on version 2.1 of the Base64 class developed by Robert Harder (public domain).
 * Please visit <a href="http://iharder.net/base64">http://iharder.net/base64</a>
 * periodically to check for updates or to contribute improvements.
 * </p>
 *
 * @author <a href="mailto:rob@iharder.net">Robert Harder</a>
 * @author <a href="mailto:chris.laprun@jboss.com">Chris Laprun</a>
 * @since 2.4 (Apr 30, 2006)
 */
public class Base64
{

/* ********  P R I V A T E   F I E L D S  ******** */


   /** Maximum line length (76) of Base64 output. */
   private final static int MAX_LINE_LENGTH = 76;

   /** The equals sign (=) as a byte. */
   private final static byte EQUALS_SIGN = (byte)'=';

   /** '*' as a byte */
   private final static byte STAR = (byte)'*';

   /** The character to be used as a padding character in the encoded Strings. */
   private byte PADDING_CHAR;

   /** The new line character (\n) as a byte. */
   private final static byte NEW_LINE = (byte)'\n';

   /** Preferred encoding. */
   private final static String PREFERRED_ENCODING = "UTF-8";

   public static enum EncodingOption { USEURLSAFEENCODING, NOCARRIAGERETURN, STANDARD};

   /** The 64 valid Base64 values. */
   private byte[] ALPHABET;
   private final static byte[] NATIVE_ALPHABET = /* May be something funny like EBCDIC */
      {
         (byte)'A', (byte)'B', (byte)'C', (byte)'D', (byte)'E', (byte)'F', (byte)'G',
         (byte)'H', (byte)'I', (byte)'J', (byte)'K', (byte)'L', (byte)'M', (byte)'N',
         (byte)'O', (byte)'P', (byte)'Q', (byte)'R', (byte)'S', (byte)'T', (byte)'U',
         (byte)'V', (byte)'W', (byte)'X', (byte)'Y', (byte)'Z',
         (byte)'a', (byte)'b', (byte)'c', (byte)'d', (byte)'e', (byte)'f', (byte)'g',
         (byte)'h', (byte)'i', (byte)'j', (byte)'k', (byte)'l', (byte)'m', (byte)'n',
         (byte)'o', (byte)'p', (byte)'q', (byte)'r', (byte)'s', (byte)'t', (byte)'u',
         (byte)'v', (byte)'w', (byte)'x', (byte)'y', (byte)'z',
         (byte)'0', (byte)'1', (byte)'2', (byte)'3', (byte)'4', (byte)'5',
         (byte)'6', (byte)'7', (byte)'8', (byte)'9', (byte)'+', (byte)'/'
      };
   private final static byte[] NATIVE_URL_SAFE_ALPHABET =
      {
         (byte)'A', (byte)'B', (byte)'C', (byte)'D', (byte)'E', (byte)'F', (byte)'G',
         (byte)'H', (byte)'I', (byte)'J', (byte)'K', (byte)'L', (byte)'M', (byte)'N',
         (byte)'O', (byte)'P', (byte)'Q', (byte)'R', (byte)'S', (byte)'T', (byte)'U',
         (byte)'V', (byte)'W', (byte)'X', (byte)'Y', (byte)'Z',
         (byte)'a', (byte)'b', (byte)'c', (byte)'d', (byte)'e', (byte)'f', (byte)'g',
         (byte)'h', (byte)'i', (byte)'j', (byte)'k', (byte)'l', (byte)'m', (byte)'n',
         (byte)'o', (byte)'p', (byte)'q', (byte)'r', (byte)'s', (byte)'t', (byte)'u',
         (byte)'v', (byte)'w', (byte)'x', (byte)'y', (byte)'z',
         (byte)'0', (byte)'1', (byte)'2', (byte)'3', (byte)'4', (byte)'5',
         (byte)'6', (byte)'7', (byte)'8', (byte)'9', (byte)'-', (byte)'_'
      };

   private static final int IGNORE = -9; // Indicates ignored characters
   private final static byte WHITE_SPACE_ENC = -5; // Indicates white space in encoding
   private final static byte PADDING_CHAR_ENC = -1; // Indicates equals sign in encoding

   /**
    * Translates a value to either its 6-bit reconstruction value
    * or a negative number indicating some other meaning.
    */
   private byte[] DECODABET;

   /**
    * Translates a Base64 value to either its 6-bit reconstruction value
    * or a negative number indicating some other meaning.
    */
   private final static byte[] BASE64_DECODABET =
      {
         IGNORE, IGNORE, IGNORE, IGNORE, IGNORE, IGNORE, IGNORE, IGNORE, IGNORE, // Decimal  0 -  8
         WHITE_SPACE_ENC, WHITE_SPACE_ENC, // Whitespace: Tab and Linefeed
         IGNORE, IGNORE, // Decimal 11 - 12
         WHITE_SPACE_ENC, // Whitespace: Carriage Return
         IGNORE, IGNORE, IGNORE, IGNORE, IGNORE, IGNORE, IGNORE, IGNORE, IGNORE, IGNORE, IGNORE, IGNORE, IGNORE, // Decimal 14 - 26
         IGNORE, IGNORE, IGNORE, IGNORE, IGNORE, // Decimal 27 - 31
         WHITE_SPACE_ENC, // Whitespace: Space
         IGNORE, IGNORE, IGNORE, IGNORE, IGNORE, IGNORE, IGNORE, IGNORE, IGNORE, IGNORE, // Decimal 33 - 42
         62, // Plus sign at decimal 43
         IGNORE, IGNORE, IGNORE, // Decimal 44 - 46
         63, // Slash at decimal 47
         52, 53, 54, 55, 56, 57, 58, 59, 60, 61, // Numbers zero through nine
         IGNORE, IGNORE, IGNORE, // Decimal 58 - 60
         PADDING_CHAR_ENC, // Equals sign at decimal 61 (padding character)
         IGNORE, IGNORE, IGNORE, // Decimal 62 - 64
         0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, // Letters 'A' through 'N'
         14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, // Letters 'O' through 'Z'
         IGNORE, IGNORE, IGNORE, IGNORE, IGNORE, IGNORE, // Decimal 91 - 96
         26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, // Letters 'a' through 'm'
         39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, // Letters 'n' through 'z'
         IGNORE, IGNORE, IGNORE, IGNORE                                 // Decimal 123 - 126
      };

   /**
    * Translates a URL-modified Base64 value to either its 6-bit reconstruction value
    * or a negative number indicating some other meaning.
    */
   private final static byte[] URL_SAFE_DECODABET =
      {
         IGNORE, IGNORE, IGNORE, IGNORE, IGNORE, IGNORE, IGNORE, IGNORE, IGNORE, // Decimal  0 -  8
         WHITE_SPACE_ENC, WHITE_SPACE_ENC, // Whitespace: Tab and Linefeed
         IGNORE, IGNORE, // Decimal 11 - 12
         WHITE_SPACE_ENC, // Whitespace: Carriage Return
         IGNORE, IGNORE, IGNORE, IGNORE, IGNORE, IGNORE, IGNORE, IGNORE, IGNORE, IGNORE, IGNORE, IGNORE, IGNORE, // Decimal 14 - 26
         IGNORE, IGNORE, IGNORE, IGNORE, IGNORE, // Decimal 27 - 31
         WHITE_SPACE_ENC, // Whitespace: Space
         IGNORE, IGNORE, IGNORE, IGNORE, IGNORE, IGNORE, IGNORE, IGNORE, IGNORE, // Decimal 33 - 41
         PADDING_CHAR_ENC, // Star sign at decimal 42 (padding character)
         IGNORE, IGNORE, // Decimal 43 - 44
         62, // Minus sign at decimal 45
         IGNORE, IGNORE, // Decimal 46 - 47
         52, 53, 54, 55, 56, 57, 58, 59, 60, 61, // Numbers zero through nine
         IGNORE, IGNORE, IGNORE, IGNORE, IGNORE, IGNORE, IGNORE, // Decimal 58 - 64
         0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, // Letters 'A' through 'N'
         14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, // Letters 'O' through 'Z'
         IGNORE, IGNORE, IGNORE, IGNORE, // Decimal 91 - 94
         63, // Underscore at decimal 95
         IGNORE, // Decimal 96
         26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, // Letters 'a' through 'm'
         39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, // Letters 'n' through 'z'
         IGNORE, IGNORE, IGNORE, IGNORE                                 // Decimal 123 - 126
      };

   /** Determine which ALPHABET to use. */
   public void initAlphabets(boolean useURLSafeEncoding)
   {
      byte[] __bytes;
      String alphabetString;
      byte[] nativeAlphabet;

      if (useURLSafeEncoding)
      {
         alphabetString = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789-_";
         nativeAlphabet = NATIVE_URL_SAFE_ALPHABET;
         PADDING_CHAR = STAR;
         DECODABET = URL_SAFE_DECODABET;
      }
      else
      {
         alphabetString = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/";
         nativeAlphabet = NATIVE_ALPHABET;
         PADDING_CHAR = EQUALS_SIGN;
         DECODABET = BASE64_DECODABET;
      }

      try
      {
         __bytes = alphabetString.getBytes(PREFERRED_ENCODING);
      }   // end try
      catch (java.io.UnsupportedEncodingException use)
      {
         __bytes = nativeAlphabet; // Fall back to native encoding
      }   // end catch
      ALPHABET = __bytes;
   }   // end initAlphabets

   private static final Base64 STANDARD_BASE64 = new Base64();
   private static final Base64 URL_SAFE_BASE64 = new Base64(true);

   private Base64()
   {
      this(false);
   }

   private Base64(boolean useURLSafeEncoding)
   {
      initAlphabets(useURLSafeEncoding);
   }

   private static Base64 getBase64(EncodingOption option)
   {
      return EncodingOption.USEURLSAFEENCODING.equals(option) ? URL_SAFE_BASE64 : STANDARD_BASE64;
   }

/* ********  E N C O D I N G   M E T H O D S  ******** */


   /**
    * Encodes up to three bytes of the array <var>source</var>
    * and writes the resulting four Base64 bytes to <var>destination</var>.
    * The source and destination arrays can be manipulated
    * anywhere along their length by specifying
    * <var>srcOffset</var> and <var>destOffset</var>.
    * This method does not check to make sure your arrays
    * are large enough to accomodate <var>srcOffset</var> + 3 for
    * the <var>source</var> array or <var>destOffset</var> + 4 for
    * the <var>destination</var> array.
    * The actual number of significant bytes in your array is
    * given by <var>numSigBytes</var>.
    *
    * @param source      the array to convert
    * @param srcOffset   the index where conversion begins
    * @param numSigBytes the number of significant bytes in your array
    * @param destination the array to hold the conversion
    * @param destOffset  the index where output will be put
    * @return the <var>destination</var> array
    */
   private byte[] encode3to4(byte[] source, int srcOffset, int numSigBytes,
                             byte[] destination, int destOffset)
   {
      //           1         2         3
      // 01234567890123456789012345678901 Bit position
      // --------000000001111111122222222 Array position from threeBytes
      // --------|    ||    ||    ||    | Six bit groups to index ALPHABET
      //          >>18  >>12  >> 6  >> 0  Right shift necessary
      //                0x3f  0x3f  0x3f  Additional AND

      // Create buffer with zero-padding if there are only one or two
      // significant bytes passed in the array.
      // We have to shift left 24 in order to flush out the 1's that appear
      // when Java treats a value as negative that is cast from a byte to an int.
      int inBuff = (numSigBytes > 0 ? ((source[srcOffset] << 24) >>> 8) : 0)
         | (numSigBytes > 1 ? ((source[srcOffset + 1] << 24) >>> 16) : 0)
         | (numSigBytes > 2 ? ((source[srcOffset + 2] << 24) >>> 24) : 0);

      switch (numSigBytes)
      {
         case 3:
            destination[destOffset] = ALPHABET[(inBuff >>> 18)];
            destination[destOffset + 1] = ALPHABET[(inBuff >>> 12) & 0x3f];
            destination[destOffset + 2] = ALPHABET[(inBuff >>> 6) & 0x3f];
            destination[destOffset + 3] = ALPHABET[(inBuff) & 0x3f];
            return destination;

         case 2:
            destination[destOffset] = ALPHABET[(inBuff >>> 18)];
            destination[destOffset + 1] = ALPHABET[(inBuff >>> 12) & 0x3f];
            destination[destOffset + 2] = ALPHABET[(inBuff >>> 6) & 0x3f];
            destination[destOffset + 3] = PADDING_CHAR;
            return destination;

         case 1:
            destination[destOffset] = ALPHABET[(inBuff >>> 18)];
            destination[destOffset + 1] = ALPHABET[(inBuff >>> 12) & 0x3f];
            destination[destOffset + 2] = PADDING_CHAR;
            destination[destOffset + 3] = PADDING_CHAR;
            return destination;

         default:
            return destination;
      }   // end switch
   }   // end encode3to4

   /**
    * Encodes a byte array into Base64 notation using the standard Base64 encoding.
    *
    * @param source The data to convert
    */
   public static String encodeBytes(byte[] source)
   {
      return encodeBytes(source, EncodingOption.STANDARD);
   }   // end encodeBytes

   
   /**
    * Encodes a byte array into Base64 notation.
    *
    * @param source             The data to convert
    * @param option             Encoding option
    */
   public static String encodeBytes(byte[] source, EncodingOption option)
   {
      return encodeBytes(source, 0, source.length, option);
   }   // end encodeBytes


   /**
    * Encodes a byte array into Base64 notation.
    *
    * @param source             The data to convert
    * @param off                Offset in array where conversion should begin
    * @param len                Length of data to convert
    * @param option             Encoding option
    */
   public static String encodeBytes(byte[] source, int off, int len, EncodingOption option)
   {
      Base64 b64 = getBase64(option);

      int len43 = len * 4 / 3;
      byte[] outBuff = new byte[(len43)                      // Main 4:3
         + ((len % 3) > 0 ? 4 : 0)      // Account for padding
         + ((EncodingOption.STANDARD.equals(option)) ? (len43 / MAX_LINE_LENGTH) : 0)]; // New lines
      int d = 0;
      int e = 0;
      int len2 = len - 2;
      int lineLength = 0;
      for (; d < len2; d += 3, e += 4)
      {
         b64.encode3to4(source, d + off, 3, outBuff, e);

         lineLength += 4;
         if (EncodingOption.STANDARD.equals(option) && lineLength == MAX_LINE_LENGTH)
         {
            outBuff[e + 4] = NEW_LINE;
            e++;
            lineLength = 0;
         }   // end if: end of line
      }   // en dfor: each piece of array

      if (d < len)
      {
         b64.encode3to4(source, d + off, len - d, outBuff, e);
         e += 4;
      }   // end if: some padding needed

      // Return value according to relevant encoding.
      try
      {
         return new String(outBuff, 0, e, PREFERRED_ENCODING);
      }   // end try
      catch (java.io.UnsupportedEncodingException uue)
      {
         return new String(outBuff, 0, e);
      }   // end catch

   }   // end encodeBytes

/* ********  D E C O D I N G   M E T H O D S  ******** */

   /**
    * Decodes four bytes from array <var>source</var>
    * and writes the resulting bytes (up to three of them)
    * to <var>destination</var>.
    * The source and destination arrays can be manipulated
    * anywhere along their length by specifying
    * <var>srcOffset</var> and <var>destOffset</var>.
    * This method does not check to make sure your arrays
    * are large enough to accomodate <var>srcOffset</var> + 4 for
    * the <var>source</var> array or <var>destOffset</var> + 3 for
    * the <var>destination</var> array.
    * This method returns the actual number of bytes that
    * were converted from the Base64 encoding.
    *
    * @param source      the array to convert
    * @param srcOffset   the index where conversion begins
    * @param destination the array to hold the conversion
    * @param destOffset  the index where output will be put
    * @return the number of decoded bytes converted
    */
   private int decode4to3(byte[] source, int srcOffset, byte[] destination, int destOffset)
   {
      // Example: Dk==
      if (source[srcOffset + 2] == PADDING_CHAR)
      {
         // Two ways to do the same thing. Don't know which way I like best.
         //int outBuff =   ( ( DECODABET[ source[ srcOffset    ] ] << 24 ) >>>  6 )
         //              | ( ( DECODABET[ source[ srcOffset + 1] ] << 24 ) >>> 12 );
         int outBuff = ((DECODABET[source[srcOffset]] & 0xFF) << 18)
            | ((DECODABET[source[srcOffset + 1]] & 0xFF) << 12);

         destination[destOffset] = (byte)(outBuff >>> 16);
         return 1;
      }

      // Example: DkL=
      else if (source[srcOffset + 3] == PADDING_CHAR)
      {
         // Two ways to do the same thing. Don't know which way I like best.
         //int outBuff =   ( ( DECODABET[ source[ srcOffset     ] ] << 24 ) >>>  6 )
         //              | ( ( DECODABET[ source[ srcOffset + 1 ] ] << 24 ) >>> 12 )
         //              | ( ( DECODABET[ source[ srcOffset + 2 ] ] << 24 ) >>> 18 );
         int outBuff = ((DECODABET[source[srcOffset]] & 0xFF) << 18)
            | ((DECODABET[source[srcOffset + 1]] & 0xFF) << 12)
            | ((DECODABET[source[srcOffset + 2]] & 0xFF) << 6);

         destination[destOffset] = (byte)(outBuff >>> 16);
         destination[destOffset + 1] = (byte)(outBuff >>> 8);
         return 2;
      }

      // Example: DkLE
      else
      {
         try
         {
            // Two ways to do the same thing. Don't know which way I like best.
            //int outBuff =   ( ( DECODABET[ source[ srcOffset     ] ] << 24 ) >>>  6 )
            //              | ( ( DECODABET[ source[ srcOffset + 1 ] ] << 24 ) >>> 12 )
            //              | ( ( DECODABET[ source[ srcOffset + 2 ] ] << 24 ) >>> 18 )
            //              | ( ( DECODABET[ source[ srcOffset + 3 ] ] << 24 ) >>> 24 );
            int outBuff = ((DECODABET[source[srcOffset]] & 0xFF) << 18)
               | ((DECODABET[source[srcOffset + 1]] & 0xFF) << 12)
               | ((DECODABET[source[srcOffset + 2]] & 0xFF) << 6)
               | ((DECODABET[source[srcOffset + 3]] & 0xFF));


            destination[destOffset] = (byte)(outBuff >> 16);
            destination[destOffset + 1] = (byte)(outBuff >> 8);
            destination[destOffset + 2] = (byte)(outBuff);

            return 3;
         }
         catch (Exception e)
         {
            System.out.println("" + source[srcOffset] + ": " + (DECODABET[source[srcOffset]]));
            System.out.println("" + source[srcOffset + 1] + ": " + (DECODABET[source[srcOffset + 1]]));
            System.out.println("" + source[srcOffset + 2] + ": " + (DECODABET[source[srcOffset + 2]]));
            System.out.println("" + source[srcOffset + 3] + ": " + (DECODABET[source[srcOffset + 3]]));
            return -1;
         }   //e nd catch
      }
   }   // end decodeToBytes

   /**
    * Very low-level access to decoding ASCII characters in
    * the form of a byte array.
    *
    * @param source The Base64 encoded data
    * @param off    The offset of where to begin decoding
    * @param len    The length of characters to decode
    * @return decoded data
    */
   private byte[] decode(int len, int off, byte[] source)
   {
      int len34 = len * 3 / 4;
      byte[] outBuff = new byte[len34]; // Upper limit on size of output
      int outBuffPosn = 0;

      byte[] b4 = new byte[4];
      int b4Posn = 0;
      int i = 0;
      byte sbiCrop = 0;
      byte sbiDecode = 0;
      for (i = off; i < off + len; i++)
      {
         sbiCrop = (byte)(source[i] & 0x7f); // Only the low seven bits
         sbiDecode = DECODABET[sbiCrop];

         if (sbiDecode >= WHITE_SPACE_ENC) // White space, padding character or better
         {
            if (sbiDecode >= PADDING_CHAR_ENC)
            {
               b4[b4Posn++] = sbiCrop;
               if (b4Posn > 3)
               {
                  outBuffPosn += decode4to3(b4, 0, outBuff, outBuffPosn);
                  b4Posn = 0;

                  // If that was the padding char, break out of 'for' loop
                  if (sbiCrop == PADDING_CHAR)
                  {
                     break;
                  }
               }   // end if: quartet built

            }   // end if: padding character or better

         }   // end if: white space, padding character or better
         else
         {
            System.err.println("Bad Base64 input character at " + i + ": " + source[i] + "(decimal)");
            return null;
         }   // end else:
      }   // each input character

      byte[] out = new byte[outBuffPosn];
      System.arraycopy(outBuff, 0, out, 0, outBuffPosn);
      return out;
   }

   /**
    * Very low-level access to decoding ASCII characters in
    * the form of a byte array.
    *
    * @param source                 The Base64 encoded data
    * @param off                    The offset of where to begin decoding
    * @param len                    The length of characters to decode
    * @param optionThatWasUsed      EncodingOption used during the encoding
    * @return decoded data
    */
   public static byte[] decode(byte[] source, int off, int len, EncodingOption optionThatWasUsed)
   {
      return getBase64(optionThatWasUsed).decode(len, off, source);
   }   // end decode

   /**
    * Decodes data from Base64 notation
    *
    * @param s                      the string to decode
    * @param urlSafeEncodingWasUsed <code>true</code> if the URL-safe was used to encode the data to be decoded
    * @param optionThatWasUsed      EncodingOption used during the encoding
    * @return the decoded data
    */
   public static byte[] decode(String s, EncodingOption optionThatWasUsed)
   {
      byte[] bytes;
      try
      {
         bytes = s.getBytes(PREFERRED_ENCODING);
      }   // end try
      catch (java.io.UnsupportedEncodingException uee)
      {
         bytes = s.getBytes();
      }   // end catch

      // Decode
      bytes = decode(bytes, 0, bytes.length, optionThatWasUsed);

      return bytes;
   }   // end decode

   /**
    * Decodes data from Base64 notation
    *
    * @param s the string to decode
    * @return the decoded data
    */
   public static byte[] decode(String s)
   {
      return decode(s, EncodingOption.STANDARD);
   }   // end decode

   // Deprecated methods
   
   /**
    * Encodes a byte array into Base64 notation using the standard Base64 encoding.
    *
    * @deprecated
    * @param source The data to convert
    * @param useURLSafeEncoding <code>true</code> to use '-', '_' instead of '+', '/' in the alphabet and '*' instead
    *                           of '=' for padding to generate a URL-safe encoding. <i>Note: Technically, this makes
    *                           your encoding non-compliant.</i>
    */
   public static String encodeBytes(byte[] source, boolean useURLSafeEncoding)
   {
      if (useURLSafeEncoding)
      {
         return encodeBytes(source, EncodingOption.USEURLSAFEENCODING);
      }
      else
      {
         return encodeBytes(source, EncodingOption.STANDARD);
      }
   }   // end encodeBytes

   /**
    * Encodes a byte array into Base64 notation.
    *
    * @deprecated
    * @param source             The data to convert
    * @param off                Offset in array where conversion should begin
    * @param len                Length of data to convert
    * @param useURLSafeEncoding <code>true</code> to use '-', '_' instead of '+', '/' in the alphabet and '*' instead
    *                           of '=' for padding to generate a URL-safe encoding. <i>Note: Technically, this makes
    *                           your encoding non-compliant.</i>
    */
   public static String encodeBytes(byte[] source, int off, int len, boolean useURLSafeEncoding)
   {
      return (useURLSafeEncoding) ? encodeBytes(source, off, len, EncodingOption.USEURLSAFEENCODING) : encodeBytes(source, off, len, EncodingOption.STANDARD);
   }
   
   /**
    * Very low-level access to decoding ASCII characters in
    * the form of a byte array.
    *
    * @deprecated
    * @param source                 The Base64 encoded data
    * @param off                    The offset of where to begin decoding
    * @param len                    The length of characters to decode
    * @param urlSafeEncodingWasUsed <code>true</code> if the URL-safe was used to encode the data to be decoded
    * @return decoded data
    */
   public static byte[] decode(byte[] source, int off, int len, boolean urlSafeEncodingWasUsed)
   {
      EncodingOption option = EncodingOption.STANDARD;
      if (urlSafeEncodingWasUsed)
      {
         option = EncodingOption.USEURLSAFEENCODING;
      }
      return decode(source, off, len, option);
   }   // end decode

   /**
    * Decodes data from Base64 notation
    *
    * @deprecated
    * @param s                      the string to decode
    * @param urlSafeEncodingWasUsed <code>true</code> if the URL-safe was used to encode the data to be decoded
    * @return the decoded data
    */
   public static byte[] decode(String s, boolean urlSafeEncodingWasUsed)
   {
      EncodingOption option = EncodingOption.STANDARD;
      if (urlSafeEncodingWasUsed)
      {
         option = EncodingOption.USEURLSAFEENCODING;
      }
      return decode(s, option);
   }   // end decode
   
}   // end class Base64
