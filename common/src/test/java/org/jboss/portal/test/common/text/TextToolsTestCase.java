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
import org.gatein.common.text.TextTools;

/**
 * @author <a href="mailto:julien@jboss.org">Julien Viet</a>
 * @version $Revision: 1.1 $
 */
public class TextToolsTestCase extends TestCase
{

   public void testIsAlphaNumeric()
   {
      assertForChar(false, 0, '0');
      assertForChar(true, '0', '9' + 1);
      assertForChar(false, '9' + 1, 'A');
      assertForChar(true, 'A', 'Z' + 1);
      assertForChar(false, 'Z' + 1, 'a');
      assertForChar(true, 'a', 'z' + 1);
      assertForChar(false, 'z' + 1, 512);
   }

   private void assertForChar(boolean result, int from, int to)
   {
      while (from < to)
      {
         assertEquals(result, TextTools.isAlphaNumeric((char)(from++)));
      }
   }

   public void testToHex()
   {
      assertEquals('0', TextTools.toHex(0));
      assertEquals('1', TextTools.toHex(1));
      assertEquals('2', TextTools.toHex(2));
      assertEquals('3', TextTools.toHex(3));
      assertEquals('4', TextTools.toHex(4));
      assertEquals('5', TextTools.toHex(5));
      assertEquals('6', TextTools.toHex(6));
      assertEquals('7', TextTools.toHex(7));
      assertEquals('8', TextTools.toHex(8));
      assertEquals('9', TextTools.toHex(9));
      assertEquals('A', TextTools.toHex(10));
      assertEquals('B', TextTools.toHex(11));
      assertEquals('C', TextTools.toHex(12));
      assertEquals('D', TextTools.toHex(13));
      assertEquals('E', TextTools.toHex(14));
      assertEquals('F', TextTools.toHex(15));
      for (int i = 16;i < 512;i++)
      {
         try
         {
            TextTools.toHex(i);
            fail("Was expecting an IAE at index=" + i);
         }
         catch (IllegalArgumentException expected)
         {
         }
      }
      for (int i = -512;i < 0;i++)
      {
         try
         {
            TextTools.toHex(i);
            fail("Was expecting an IAE at index=" + i);
         }
         catch (IllegalArgumentException expected)
         {
         }
      }
   }

}
