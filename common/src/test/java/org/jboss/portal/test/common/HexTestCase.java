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
package org.jboss.portal.test.common;

import junit.framework.TestCase;
import org.gatein.common.util.Tools;

import java.util.Arrays;

/**
 * @author <a href="mailto:julien@jboss.org">Julien Viet</a>
 * @version $Revision: 5451 $
 */
public class HexTestCase extends TestCase
{
   public HexTestCase(String s)
   {
      super(s);
      System.out.println("BUILD");
      System.out.println("BUILD");
      System.out.println("BUILD");
   }

   public void testBasic()
   {
      byte[] original = new byte[]{0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20};
      String hex = Tools.toHexString(original);
//      assertEquals((String)"000102030405060708090a0b0c0d0e0f1011121314", (String)hex);
      assertEquals("a", "a");
      byte[] copy = Tools.fromHexString(hex);
      assertTrue(Arrays.equals(original, copy));
   }
}
