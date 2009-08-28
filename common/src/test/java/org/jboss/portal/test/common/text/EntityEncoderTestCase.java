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
import org.gatein.common.text.EntityEncoder;

/**
 * @author <a href="mailto:julien@jboss.org">Julien Viet</a>
 * @version $Revision: 630 $
 */
public class EntityEncoderTestCase extends TestCase
{

   public void testChar()
   {
      CharBuffer buffer = new CharBuffer();
      EntityEncoder.FULL.encode('&', buffer);
      assertEquals("&amp;", buffer.asString(true));
      EntityEncoder.FULL.encode('c', buffer);
      assertEquals("c", buffer.asString(true));
   }

   public void testChars()
   {
      CharBuffer buffer = new CharBuffer();
      EntityEncoder.FULL.encode("&bar".toCharArray(), buffer);
      assertEquals("&amp;bar", buffer.asString(true));
   }

   public void testString()
   {
      test("&amp;", "&");
      test("&amp;bar", "&bar");
      test("foo&amp;", "foo&");
      test("foo&amp;bar", "foo&bar");
   }

   private void test(String expected, String actual)
   {
      CharBuffer buffer = new CharBuffer();
      EntityEncoder.FULL.encode(actual, buffer);
      assertEquals(expected, buffer.asString());
   }

}
