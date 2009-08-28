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
import org.gatein.common.text.FastURLEncoder;

/**
 * @author <a href="mailto:julien@jboss.org">Julien Viet</a>
 * @version $Revision: 1.1 $
 */
public class CharBufferTestCase extends TestCase
{

   private FastURLEncoder encoder = FastURLEncoder.getUTF8Instance();

   public void testUTF8EncodedStringAppend()
   {
      CharBuffer buffer = new CharBuffer(0);
      buffer.append("/ a$\u0400", encoder);
      assertEquals("%2F+a%24%D0%80", buffer.asString());
   }

   public void testStringAppend()
   {
      CharBuffer buffer = new CharBuffer(0);
      buffer.append("abc");
      assertEquals("abc", buffer.asString());
   }

   public void testCharArrayAppend()
   {
      CharBuffer buffer = new CharBuffer(0);
      buffer.append("abc".toCharArray());
      assertEquals("abc", buffer.asString());
   }

   public void testCharsAppend()
   {
      CharBuffer buffer = new CharBuffer(0);
      buffer.append('a').append('b').append('c');
      assertEquals("abc", buffer.asString());
   }

   public void testCharArrayAppendWithOffset()
   {
      CharBuffer buffer = new CharBuffer();
      buffer.append("abcdef".toCharArray(), 1, 5);
      assertEquals("bcdef", buffer.asString());

      buffer.append("pqrstu".toCharArray(), 3, 3);
      assertEquals("stu", buffer.asString());

      buffer.append("abc".toCharArray(), 0, 0);
      assertEquals("", buffer.asString());

      buffer.append("abc".toCharArray(), 3, 0);
      assertEquals("", buffer.asString());

      buffer.append("abc".toCharArray(), 2, 1);
      assertEquals("c", buffer.asString());
   }

   public void testAppendCharArrayRobustness()
   {
      CharBuffer buffer = new CharBuffer(0);

      try
      {
         buffer.append("abc".toCharArray(), 3, 1);
         fail();
      }
      catch (IllegalArgumentException expected)
      {
         // expected
      }

      try
      {
         buffer.append("abc".toCharArray(), -1, 1);
         fail();
      }
      catch (IllegalArgumentException expected)
      {
         // expected
      }

      try
      {
         buffer.append("abc".toCharArray(), 0, 4);
         fail();
      }
      catch (IllegalArgumentException expected)
      {
         // expected
      }

   }

   public void testReset()
   {
      CharBuffer buffer = new CharBuffer(0);
      buffer.append("abc".toCharArray());
      assertEquals("abc", buffer.asString());
      buffer.reset();
      buffer.append("def".toCharArray());
      assertEquals("def", buffer.asString());
   }

   public void testCharArrayAppendThrowsIAE()
   {
      CharBuffer buffer = new CharBuffer(0);
      try
      {
         buffer.append((char[])null);
      }
      catch (IllegalArgumentException e)
      {
      }
   }

   public void testStringAppendThrowsIAE()
   {
      CharBuffer buffer = new CharBuffer(0);
      try
      {
         buffer.append((String)null);
      }
      catch (IllegalArgumentException e)
      {
      }
   }

   public void testUTF8EncodedAppendThrowsIAE()
   {
      CharBuffer buffer = new CharBuffer(0);
      try
      {
         buffer.append(null, encoder);
      }
      catch (IllegalArgumentException e)
      {
      }
      try
      {
         buffer.append("abc", null);
      }
      catch (IllegalArgumentException e)
      {
      }
   }
}
