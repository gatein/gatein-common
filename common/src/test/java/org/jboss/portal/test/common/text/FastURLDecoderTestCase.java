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
package org.jboss.portal.test.common.text;

import junit.framework.TestCase;
import org.gatein.common.text.FastURLDecoder;
import org.gatein.common.text.CharEncoder;
import org.gatein.common.text.CharBuffer;
import org.gatein.common.text.MalformedInputException;

import java.net.URLEncoder;

/**
 * @author <a href="mailto:julien@jboss.org">Julien Viet</a>
 * @version $Revision: 1.1 $
 */
public class FastURLDecoderTestCase extends TestCase
{

   public void testEncodeThrowsIAE() throws Exception
   {
      CharEncoder encoder = FastURLDecoder.getUTF8Instance();
      try
      {
         encoder.encode(new char[10], -1, 0, new CharBuffer());
         fail();
      }
      catch (IllegalArgumentException expected)
      {
      }
      try
      {
         encoder.encode(new char[10], 5, -1, new CharBuffer());
         fail();
      }
      catch (IllegalArgumentException expected)
      {
      }
      try
      {
         encoder.encode(new char[10], 15, 0, new CharBuffer());
         fail();
      }
      catch (IllegalArgumentException expected)
      {
      }
      try
      {
         encoder.encode(new char[10], 5, 6, new CharBuffer());
         fail();
      }
      catch (IllegalArgumentException expected)
      {
      }
      try
      {
         encoder.encode(null, 0, 5, new CharBuffer());
         fail();
      }
      catch (IllegalArgumentException expected)
      {
      }
      try
      {
         encoder.encode((char[])null, new CharBuffer());
         fail();
      }
      catch (IllegalArgumentException expected)
      {
      }
      try
      {
         encoder.encode((CharSequence)null, new CharBuffer());
         fail();
      }
      catch (IllegalArgumentException expected)
      {
      }

      //
      try
      {
         encoder.encode(new char[10], 0, 10, null);
         fail();
      }
      catch (IllegalArgumentException expected)
      {
      }
      try
      {
         encoder.encode(new char[10], null);
         fail();
      }
      catch (IllegalArgumentException expected)
      {
      }
      try
      {
         encoder.encode('A', null);
         fail();
      }
      catch (IllegalArgumentException expected)
      {
      }
      try
      {
         encoder.encode("abc", null);
         fail();
      }
      catch (IllegalArgumentException expected)
      {
      }
   }

   public void testEncode() throws Exception
   {
      FastURLDecoder encoder = FastURLDecoder.getUTF8Instance();
      StringBuffer tmp = new StringBuffer();
      for (int i = 0;i < 512;i++)
      {
         tmp.append((char)i);
      }
      CharBuffer out = new CharBuffer();
      String s = tmp.toString();
      String t = URLEncoder.encode(s, "UTF8");
      encoder.encode(t, out);
      assertEquals(s, out.asString());
   }

   public void testEncodeMalformedInput()
   {
      testEncodeMalformedInput(FastURLDecoder.getUTF8StrictInstance(), true);
      testEncodeMalformedInput(FastURLDecoder.getUTF8Instance(), false);
   }

   private void testEncodeMalformedInput(FastURLDecoder encoder, boolean strict)
   {
      try
      {
         encoder.encode("%0T", new CharBuffer());
         fail("Was expecting a malformed input exception");
      }
      catch (MalformedInputException expected)
      {
      }
      try
      {
         encoder.encode("%T0", new CharBuffer());
         fail("Was expecting a malformed input exception");
      }
      catch (MalformedInputException expected)
      {
      }
      try
      {
         encoder.encode("%0\u0100", new CharBuffer());
         fail("Was expecting a malformed input exception");
      }
      catch (MalformedInputException expected)
      {
      }
      try
      {
         encoder.encode("%\u01000", new CharBuffer());
         fail("Was expecting a malformed input exception");
      }
      catch (MalformedInputException expected)
      {
      }
      try
      {
         encoder.encode("%0", new CharBuffer());
         fail("Was expecting a malformed input exception");
      }
      catch (MalformedInputException expected)
      {
      }
      try
      {
         encoder.encode("%", new CharBuffer());
         fail("Was expecting a malformed input exception");
      }
      catch (MalformedInputException expected)
      {
      }
      try
      {
         encoder.encode("%FC", new CharBuffer());
         fail();
      }
      catch (MalformedInputException expected)
      {
      }

      //
      if (strict)
      {
         try
         {
            encoder.encode(";", new CharBuffer());
            fail("Was expecting a malformed input exception");
         }
         catch (MalformedInputException expected)
         {
         }
         try
         {
            encoder.encode("\u0100", new CharBuffer());
            fail("Was expecting a malformed input exception");
         }
         catch (MalformedInputException expected)
         {
         }
      }
      else
      {
         CharBuffer tmp = new CharBuffer();

         //
         encoder.encode(";", tmp);
         assertEquals(";", tmp.asString());

         //
         tmp.reset();
         encoder.encode("\u0100", tmp);
         assertEquals("\u0100", tmp.asString());
      }
   }
}
