/******************************************************************************
 * JBoss, a division of Red Hat                                               *
 * Copyright 2009, Red Hat Middleware, LLC, and individual                    *
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
package org.gatein.common;

import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.NoSuchElementException;

import org.gatein.common.util.IteratorStatus;

/**
 * @author <a href="mailto:julien@jboss.org">Julien Viet</a>
 * @version $Revision: 1.1 $
 */
@SuppressWarnings("unchecked")
public class IteratorStatusTestCase extends TestCase
{

   public void testA()
   {
      ArrayList list = new ArrayList();
      list.add("a");
      list.add("b");
      list.add("c");

      //
      test(new IteratorStatus(list));
      test(new IteratorStatus(list.iterator()));
   }

   private void test(IteratorStatus iterator)
   {
      assertTrue(iterator.hasNext());
      assertEquals(-1, iterator.getIndex());

      //
      assertEquals("a", iterator.next());
      assertTrue(iterator.hasNext());
      assertEquals(0, iterator.getIndex());

      //
      assertEquals("b", iterator.next());
      assertTrue(iterator.hasNext());
      assertEquals(1, iterator.getIndex());

      //
      assertEquals("c", iterator.next());
      assertFalse(iterator.hasNext());
      assertEquals(2, iterator.getIndex());

      //
      try
      {
         iterator.next();
         fail("Was expecting a NoSuchElementException");
      }
      catch (NoSuchElementException expected)
      {
      }
      assertFalse(iterator.hasNext());
      assertEquals(2, iterator.getIndex());
   }
}
