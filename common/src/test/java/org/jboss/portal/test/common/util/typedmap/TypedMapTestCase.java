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
package org.jboss.portal.test.common.util.typedmap;

import junit.framework.TestCase;

import java.util.Map;
import java.util.HashMap;
import java.util.Collections;

import org.gatein.common.util.CollectionBuilder;
import org.gatein.common.util.MapBuilder;

/**
 * @author <a href="mailto:julien@jboss.org">Julien Viet</a>
 * @version $Revision: 630 $
 */
public class TypedMapTestCase extends TestCase
{

   /** . */
   private static final Integer ZERO = 0;

   /** . */
   private static final Integer ONE = 1;

   /** . */
   private static final Integer TWO = 2;

   /** . */
   private Map<Long, Integer> delegate;

   /** . */
   private Map delegate2;

   /** . */
   private StringToIntegerMap map;

   /** . */
   private Map map2;

   public TypedMapTestCase(String name)
   {
      super(name);
   }

   protected void setUp() throws Exception
   {
      delegate = new HashMap<Long, Integer>();
      delegate2 = delegate;
      map = new StringToIntegerMap(delegate);
      map2 = map;
   }

   // Basic operations *************************************************************************************************

   public void testGet()
   {
      assertNull(map.get("zero"));
      delegate.put((long)0, ZERO);
      assertEquals(Collections.singletonMap("zero", "0"), map);
   }

   public void testPut()
   {
      map.put("zero", "0");
      assertEquals(Collections.singletonMap((long)0, ZERO), delegate);
   }

   public void testRemove()
   {
      assertNull(map.remove("zero"));
      delegate.put((long)0, ZERO);
      assertEquals("0", map.remove("zero"));
      assertTrue(delegate.isEmpty());
   }

   //

   public void testGetWithWrongKeyArgumentClass()
   {
      try
      {
         map.get(new Object());
         fail();
      }
      catch (ClassCastException expected)
      {
      }
   }

   public void testPutWithWrongKeyArgumentClass()
   {
      try
      {
         map2.put(new Object(), "0");
         fail();
      }
      catch (ClassCastException expected)
      {
      }
   }

   public void testRemoveWithWrongKeyArgumentClass()
   {
      try
      {
         map.remove(new Object());
         fail();
      }
      catch (ClassCastException expected)
      {
      }
   }

   public void testContainsKeyWithWrongKeyArgumentClass()
   {
      try
      {
         map.containsKey(new Object());
         fail();
      }
      catch (ClassCastException expected)
      {
      }
   }

   //

   public void testGetInternalValueConversionThrowsUncheckedException()
   {
      delegate.put((long)0, ValueConverter.UNCHECKED);
      try
      {
         map.get("zero");
         fail();
      }
      catch (CustomRuntimeException e)
      {
         assertEquals(ValueConverter.UNCHECKED, (int)delegate.get((long)0));
      }
   }

   public void testPutInternalValueConversionThrowsUncheckedException()
   {
      delegate.put((long)0, ValueConverter.UNCHECKED);
      try
      {
         map.put("zero", "0");
         fail();
      }
      catch (CustomRuntimeException e)
      {
         assertEquals(ValueConverter.UNCHECKED, (int)delegate.get((long)0));
      }
   }

   public void testRemoveInternalValueConversionThrowsUncheckedException()
   {
      delegate.put((long)0, ValueConverter.UNCHECKED);
      try
      {
         map.remove("zero");
         fail();
      }
      catch (CustomRuntimeException e)
      {
         assertEquals(ValueConverter.UNCHECKED, (int)delegate.get((long)0));
      }
   }

   //

   public void testGetKeyArgumentConversionThrowsUncheckedException()
   {
      try
      {
         map.get(KeyConverter.UNCHECKED);
         fail();
      }
      catch (CustomRuntimeException e)
      {
         assertEquals(0, delegate.size());
      }
   }

   public void testPutKeyArgumentConversionThrowsUncheckedException()
   {
      try
      {
         map.put("" + KeyConverter.UNCHECKED, "0");
         fail();
      }
      catch (CustomRuntimeException e)
      {
         assertEquals(0, delegate.size());
      }
   }

   public void testRemoveKeyArgumentConversionThrowsUncheckedException()
   {
      try
      {
         map.remove("" + KeyConverter.UNCHECKED);
         fail();
      }
      catch (CustomRuntimeException e)
      {
         assertEquals(0, delegate.size());
      }
   }

   public void testContainsKeyKeyArgumentConversionThrowsUncheckedException()
   {
      try
      {
         map.containsKey("" + KeyConverter.UNCHECKED);
         fail();
      }
      catch (CustomRuntimeException e)
      {
         assertEquals(0, delegate.size());
      }
   }

   //

   public void testPutWithWrongValueArgumentClass()
   {
      delegate.put((long)0, ZERO);
      try
      {
         map2.put("zero", new Object());
         fail();
      }
      catch (ClassCastException expected)
      {
         assertEquals(1, delegate.size());
         assertEquals(ZERO, delegate.get((long)0));
      }
   }

   public void testContainsValueWithWrongValueArgumentClass()
   {
      try
      {
         map.containsValue(new Object());
         fail();
      }
      catch (ClassCastException expected)
      {
      }
   }

   //

   public void testPutValueArgumentConversionThrowsUncheckedException()
   {
      try
      {
         map.put("zero", "" + ValueConverter.UNCHECKED);
         fail();
      }
      catch (CustomRuntimeException e)
      {
         assertEquals(0, delegate.size());
      }
   }

   public void testContainsValueValueArgumentConversionThrowsUncheckedException()
   {
      try
      {
         map.containsValue("" + ValueConverter.UNCHECKED);
         fail();
      }
      catch (CustomRuntimeException e)
      {
         assertEquals(0, delegate.size());
      }
   }

   //

   public void testGetNullKeyArgument()
   {
      try
      {
         map.get(null);
         fail();
      }
      catch (NullPointerException e)
      {
         assertTrue(delegate.isEmpty());
      }
   }

   public void testPutNullKeyArgument()
   {
      try
      {
         map.put(null, "0");
         fail();
      }
      catch (NullPointerException e)
      {
         assertTrue(delegate.isEmpty());
      }
   }

   public void testRemoveNullKeyArgument()
   {
      try
      {
         map.remove(null);
         fail();
      }
      catch (NullPointerException e)
      {
         assertTrue(delegate.isEmpty());
      }
   }

   public void testContainsKeyNullKeyArgument()
   {
      try
      {
         map.remove(null);
         fail();
      }
      catch (NullPointerException e)
      {
         assertTrue(delegate.isEmpty());
      }
   }

   //

   public void testPutNullValueArgument()
   {
      try
      {
         map.put("zero", null);
         fail();
      }
      catch (NullPointerException e)
      {
         assertTrue(delegate.size() == 0);
      }

      //
      map.setAcceptNull(true);
      map.put("zero", null);
      assertEquals(MapBuilder.hashMap(0L, (Integer)null).get(), delegate);
   }

   public void testContainsValueNullValueArgument()
   {
      try
      {
         map.containsValue(null);
         fail();
      }
      catch (NullPointerException e)
      {
         assertTrue(delegate.size() == 0);
      }

      //
      map.setAcceptNull(true);
      assertFalse(map.containsValue(null));
      delegate.put((0L), null);
      assertTrue(map.containsValue(null));
   }

   //

   public void testGetKeyArgumentConvertedToNull()
   {
      try
      {
         map.get(KeyConverter.NULL);
         fail();
      }
      catch (IllegalArgumentException e)
      {
         assertTrue(delegate.isEmpty());
      }
   }

   public void testPutKeyArgumentConvertedToNull()
   {
      try
      {
         map.put(KeyConverter.NULL, "0");
         fail();
      }
      catch (IllegalArgumentException e)
      {
         assertTrue(delegate.isEmpty());
      }
   }

   public void testRemoveKeyArgumentConvertedToNull()
   {
      try
      {
         map.remove(KeyConverter.NULL);
         fail();
      }
      catch (IllegalArgumentException e)
      {
         assertTrue(delegate.isEmpty());
      }
   }

   public void testContainsKeyKeyArgumentConvertedToNull()
   {
      try
      {
         map.remove(KeyConverter.NULL);
         fail();
      }
      catch (IllegalArgumentException e)
      {
         assertTrue(delegate.isEmpty());
      }
   }

   //

   // blih
//   public void testPutValueArgumentConvertedToNull()
//   {
//      try
//      {
//         map.put("zero", "" + ValueConverter.NULL);
//         fail();
//      }
//      catch (IllegalArgumentException e)
//      {
//         assertFalse(delegate.containsKey((long)0));
//      }
//   }

   // blih
//   public void testContainsValueValueArgumentConvertedToNull()
//   {
//      try
//      {
//         map.containsValue("" + ValueConverter.NULL);
//         fail();
//      }
//      catch (IllegalArgumentException e)
//      {
//         assertTrue(delegate.size() == 0);
//      }
//   }

   //

   public void testPutValueArgumentThrowsCCE()
   {
      try
      {
         map.put("zero", "" + ValueConverter.CCE);
         fail();
      }
      catch (ClassCastException e)
      {
         assertTrue(delegate.size() == 0);
      }
   }

   public void testContainsValueValueArgumentThrowsCCE()
   {
      try
      {
         map.containsValue("" + ValueConverter.CCE);
         fail();
      }
      catch (ClassCastException e)
      {
         assertTrue(delegate.size() == 0);
      }
   }

   //

   public void testGetKeyArgumentThrowsCCE()
   {
      try
      {
         map.get(KeyConverter.CCE);
         fail();
      }
      catch (ClassCastException e)
      {
         assertTrue(delegate.isEmpty());
      }
   }

   public void testPutKeyArgumentThrowsCCE()
   {
      try
      {
         map.put(KeyConverter.CCE, "0");
         fail();
      }
      catch (ClassCastException e)
      {
         assertTrue(delegate.isEmpty());
      }
   }

   public void testRemoveKeyArgumentThrowsCCE()
   {
      try
      {
         map.remove(KeyConverter.CCE);
         fail();
      }
      catch (ClassCastException e)
      {
         assertTrue(delegate.isEmpty());
      }
   }

   public void testContainsKeyKeyArgumentThrowsCCE()
   {
      try
      {
         map.containsKey(KeyConverter.CCE);
         fail();
      }
      catch (ClassCastException e)
      {
         assertTrue(delegate.isEmpty());
      }
   }

   //
   public void testGetInternalNullValue()
   {
      delegate.put(0l, null);
      try
      {
         map.get("zero");
         fail();
      }
      catch (IllegalStateException e)
      {
      }
   }

   public void testRemoveInternalNullValue()
   {
      delegate.put(0l, null);
      try
      {
         map.remove("zero");
         fail();
      }
      catch (IllegalStateException e)
      {
         assertEquals(MapBuilder.hashMap(0L, null).get(), delegate);
      }
   }

   public void testPutOldInternalNullValue()
   {
      delegate.put(0l, null);
      try
      {
         map.put("zero", "0");
         fail();
      }
      catch (IllegalStateException e)
      {
         assertEquals(MapBuilder.hashMap(0L, null).get(), delegate);
      }
   }

   // blih
//   public void testGetInternalValueConvertedToNull()
//   {
//      delegate.put((long)0, ValueConverter.NULL);
//      try
//      {
//         map.get("zero");
//         fail();
//      }
//      catch (IllegalStateException e)
//      {
//         assertEquals(ValueConverter.NULL, delegate.get((long)0));
//      }
//   }

   // blih
//   public void testPutInternalValueConvertedToNull()
//   {
//      delegate.put((long)0, ValueConverter.NULL);
//      try
//      {
//         map.put("zero", "0");
//         fail();
//      }
//      catch (IllegalStateException e)
//      {
//         assertEquals(ValueConverter.NULL, delegate.get((long)0));
//      }
//   }

   // blih
//   public void testRemoveInternalValueConvertedToNull()
//   {
//      delegate.put((long)0, ValueConverter.NULL);
//      try
//      {
//         map.remove("zero");
//         fail();
//      }
//      catch (IllegalStateException e)
//      {
//         assertEquals(ValueConverter.NULL, delegate.get((long)0));
//      }
//   }








   public void testEquals()
   {
      Map<Long, Integer> leftDelegate = new HashMap<Long, Integer>();
      StringToIntegerMap left = new StringToIntegerMap(leftDelegate);
      leftDelegate.put((long)0, ZERO);
      Map<? super Object, ? super Object> right = new HashMap<Object, Object>();

      //
      assertFalse(left.equals(right));
      assertFalse(right.equals(left));

      //
      right.put("zero", new Object());
      assertFalse(left.equals(right));
      assertFalse(right.equals(left));

      //
      right.put("abc", "abc");
      assertFalse(left.equals(right));
      assertFalse(right.equals(left));

      //
      right.remove("abc");
      right.put("zero", "0");
      assertTrue(left.equals(right));
      assertTrue(right.equals(left));

      //
      right.put("def", "1");
      assertFalse(left.equals(right));
      assertFalse(right.equals(left));

      //
      right.remove("def");
      right.put(null, "0");
      assertFalse(left.equals(right));
      assertFalse(right.equals(left));

      //
      right.remove(null);
      right.put("def", null);
      assertFalse(left.equals(right));
      assertFalse(right.equals(left));
   }

   public void testEntrySetRetainAll()
   {
      Map right = new HashMap();
      right.put("zero", ZERO);
      right.put("one", ONE);
      right.put("two", TWO);

      //
      Map<Long, Integer> leftTemplate = new HashMap<Long, Integer>();
      leftTemplate.put((long)0, ZERO);
      leftTemplate.put((long)1, ONE);
      leftTemplate.put((long)2, TWO);

      //
      Map<Long, Integer> leftDelegate = new HashMap<Long, Integer>(leftTemplate);

      //
      StringToIntegerMap left = new StringToIntegerMap(leftDelegate);

      try
      {
         left.keySet().retainAll(null);
         fail("Was expecting NPE");
      }
      catch (NullPointerException expected)
      {
      }

      //
      boolean changed = left.keySet().retainAll(right.keySet());
      assertFalse(changed);
      assertEquals(leftTemplate, leftDelegate);

      //
      changed = left.keySet().retainAll(CollectionBuilder.hashSet().add("one").get());
      assertTrue(changed);
      right.remove("zero");
      right.remove("two");
      leftTemplate.remove((long)0);
      leftTemplate.remove((long)2);
      assertEquals(leftTemplate, leftDelegate);
   }
}
