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
package org.jboss.portal.test.common.util;

import junit.framework.TestCase;
import org.gatein.common.util.CollectionMap;
import org.gatein.common.util.ListMap;
import org.gatein.common.util.SetMap;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

/**
 * @author <a href="mailto:julien@jboss.org">Julien Viet</a>
 * @version $Revision: 1.1 $
 */
public class CollectionMapTestCase extends TestCase
{

   private Key k1 = new Key();
   private Value v1 = new Value();
   private Value v2 = new Value();
   private ValueExt ve1 = new ValueExt();
   private ValueExt ve2 = new ValueExt();

   public void testNormal()
   {
      testNormal(new SetMap<Key, Value>());
      testNormal(new ListMap<Key, Value>());
   }

   public void testRemoveAbsent()
   {
      testRemoveAbsent(new SetMap<Key, Value>());
      testRemoveAbsent(new ListMap<Key, Value>());
   }

   public void testRemoveNull()
   {
      testRemoveNull(new SetMap<Key, Value>());
      testRemoveNull(new ListMap<Key, Value>());
   }

   public void testWithNullValue()
   {
      testWithNullValue(new SetMap<Key, Value>());
      testWithNullValue(new ListMap<Key, Value>());
   }

   public void testClassCastException()
   {
      testClassCastException(new SetMap<Key, Value>());
      testClassCastException(new ListMap<Key, Value>());
   }

   public void testThrowNPE()
   {
      testThrowNPE(new SetMap<Key, Value>());
      testThrowNPE(new ListMap<Key, Value>());
   }

   public void testSorting()
   {
      Comparator<Value> comp = new Comparator<Value>()
      {
         public int compare(Value o1, Value o2)
         {
            return o1.i - o2.i;
         }
      };

      testComparatorSorting(new ListMap<Key, Value>(comp));
      testComparatorSorting(new SetMap<Key, Value>(comp));
   }

   private void testComparatorSorting(CollectionMap<Key, Value> map)
   {
      Value v1 = new Value(1);
      Value v2 = new Value(2);
      Value v3 = new Value(3);
      Value v4 = new Value(4);

      map.put(k1, v2);
      map.put(k1, v4);
      map.put(k1, v3);
      map.put(k1, v1);

      Iterator<Value> iterator = map.iterator(k1);
      for (int i = 0; iterator.hasNext(); i++)
      {
         Value value = iterator.next();
         switch (i)
         {
            case 0:
               assertEquals(v1, value);
               break;
            case 1:
               assertEquals(v2, value);
               break;
            case 2:
               assertEquals(v3, value);
               break;
            case 3:
               assertEquals(v4, value);
               break;
         }
      }
   }

   private void testNormal(CollectionMap<Key, Value> map)
   {
      assertFalse(map.contains(k1, v1));
      assertFalse(map.contains(k1, ve1));
      map.put(k1, v1);
      assertTrue(map.contains(k1, v1));
      assertFalse(map.contains(k1, ve1));
      map.put(k1, ve1);
      assertTrue(map.contains(k1, v1));
      assertTrue(map.contains(k1, ve1));
      map.remove(k1, v1);
      assertFalse(map.contains(k1, v1));
      assertTrue(map.contains(k1, ve1));
      map.remove(k1, ve1);
      assertFalse(map.contains(k1, v1));
      assertFalse(map.contains(k1, ve1));
   }

   private void testRemoveAbsent(CollectionMap<Key, Value> map)
   {
      map.put(k1, v1);
      assertNotNull(map.get(k1));
      assertEquals(1, map.get(k1).size());
      assertTrue(map.get(k1).contains(v1));
      map.remove(k1, v2);
      assertNotNull(map.get(k1));
      assertEquals(1, map.get(k1).size());
      assertTrue(map.get(k1).contains(v1));
   }

   private void testRemoveNull(CollectionMap<Key, Value> map)
   {
      map.put(k1, v1);
      assertNotNull(map.get(k1));
      assertEquals(1, map.get(k1).size());
      assertTrue(map.get(k1).contains(v1));
      map.remove(k1, null);
      assertNotNull(map.get(k1));
      assertEquals(1, map.get(k1).size());
      assertTrue(map.get(k1).contains(v1));
   }

   private void testWithNullValue(CollectionMap<Key, Value> map)
   {
      assertFalse(map.contains(k1, null));
      map.put(k1, null);
      assertTrue(map.contains(k1, null));
      assertNotNull(map.get(k1));
      assertEquals(1, map.get(k1).size());
      assertTrue(map.get(k1).contains(null));
      map.remove(k1, null);
      assertFalse(map.contains(k1, null));
      assertEquals(null, map.get(k1));
   }

   private void testClassCastException(CollectionMap<Key, Value> map)
   {
      CollectionMap sm2 = map;
      sm2.put(k1, new Object());
      Iterator<Value> i = map.iterator(k1);
      List<Value> lst = get(i);
      try
      {
         Value v = lst.get(0);
         fail();
      }
      catch (ClassCastException expected)
      {
      }
   }

   private void testThrowNPE(CollectionMap<Key, Value> map)
   {
      try
      {
         map.put(null, v1);
         fail();
      }
      catch (NullPointerException expected)
      {
      }
      try
      {
         map.remove(null);
         fail();
      }
      catch (NullPointerException expected)
      {
      }
      try
      {
         map.remove(null, v1);
         fail();
      }
      catch (NullPointerException expected)
      {
      }
      try
      {
         map.contains(null, v1);
         fail();
      }
      catch (NullPointerException expected)
      {
      }
      try
      {
         map.iterator(null);
         fail();
      }
      catch (NullPointerException expected)
      {
      }
   }

   private <V> List<V> get(Iterator<V> i)
   {
      List<V> list = new ArrayList<V>();
      while (i.hasNext())
      {
         V v = i.next();
         list.add(v);
      }
      return list;
   }

   private static final class Key
   {
   }

   private static class Value
   {
      int i;

      private Value()
      {
      }

      private Value(int i)
      {
         this.i = i;
      }

      @Override
      public String toString()
      {
         return "Value " + i;
      }
   }

   private static class ValueExt extends Value
   {
   }
}
