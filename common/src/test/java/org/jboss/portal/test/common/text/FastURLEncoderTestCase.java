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
import org.gatein.common.text.CharBuffer;
import org.gatein.common.text.CharEncoder;
import org.gatein.common.text.FastURLEncoder;

import java.net.URLEncoder;

/**
 * @author <a href="mailto:julien@jboss.org">Julien Viet</a>
 * @version $Revision: 1.1 $
 */
public class FastURLEncoderTestCase extends TestCase
{

   public void testEncodeThrowsIAE() throws Exception
   {
      CharEncoder encoder = FastURLEncoder.getUTF8Instance();
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
      FastURLEncoder encoder = FastURLEncoder.getUTF8Instance();
      StringBuffer tmp = new StringBuffer();
      for (int i = 0; i < 512; i++)
      {
         tmp.append((char)i);
      }
      String s = tmp.toString();
      String u1 = encoder.encode(s);
      String u2 = URLEncoder.encode(s, "UTF8");
      assertEquals(u2, u1);
   }
}
