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
import java.util.Iterator;
import java.util.Collection;

import org.gatein.common.util.Tools;
import org.gatein.common.util.MapBuilder;

/**
 * @author <a href="mailto:julien@jboss.org">Julien Viet</a>
 * @version $Revision: 630 $
 */
public class TypedMapValuesTestCase extends TestCase
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

   /** . */
   private Collection<String> values;

   /** . */
   private Collection uvalues;

   /** . */
   private Map<Long, Integer> expected;

   public TypedMapValuesTestCase(String name)
   {
      super(name);
   }

   protected void setUp() throws Exception
   {
      delegate = new HashMap<Long, Integer>();
      delegate2 = delegate;
      map = new StringToIntegerMap(delegate);
      map2 = map;

      values = map.values();
      uvalues = values;

      map.put("zero", "0");
      map.put("one", "1");

      expected = new HashMap<Long, Integer>();
      expected.put((long)0, 0);
      expected.put((long)1, 1);
   }

   public void testCannotAdd()
   {
      try
      {
         values.add("1");
         fail();
      }
      catch (UnsupportedOperationException e)
      {
         assertEquals(expected, delegate);
      }

      //
      try
      {
         values.addAll(Tools.toSet("1"));
         fail();
      }
      catch (UnsupportedOperationException e)
      {
         assertEquals(expected, delegate);
      }
   }

   //

   public void testRemove()
   {
      assertEquals(true, values.remove("0"));
      expected.remove((long)0);
      assertEquals(expected, delegate);

      //
//      throw new NotYetImplemented("finish me");
   }

   public void testContains()
   {
      assertEquals(true, values.contains("0"));
      assertEquals(expected, delegate);

      //
      assertEquals(false, values.contains("3"));
      assertEquals(expected, delegate);
   }

   public void testRemoveAll()
   {
      assertEquals(false, values.removeAll(Tools.toSet()));
      assertEquals(expected, delegate);

      //
      assertEquals(false, values.removeAll(Tools.toSet("3")));
      assertEquals(expected, delegate);

      //
      assertEquals(true, values.removeAll(Tools.toSet("0")));
      expected.remove((long)0);
      assertEquals(expected, delegate);
      delegate.put((long)0, 0);
      expected.put((long)0, 0);

      //
      assertEquals(true, values.removeAll(Tools.toSet("0", "3")));
      expected.remove((long)0);
      assertEquals(expected, delegate);
      delegate.put((long)0, 0);
      expected.put((long)0, 0);

      //
      assertEquals(true, values.removeAll(Tools.toSet("0", "1")));
      expected.clear();
      assertEquals(expected, delegate);
   }

   public void testRetainAll()
   {
      assertEquals(false, values.retainAll(Tools.toSet("0", "1")));
      assertEquals(expected, delegate);

      //
      assertEquals(true, values.retainAll(Tools.toSet("0")));
      expected.remove((long)1);
      assertEquals(expected, delegate);
      delegate.put((long)1, 1);
      expected.put((long)1, 1);

      //
      assertEquals(true, values.retainAll(Tools.toSet("3")));
      expected.clear();
      assertEquals(expected, delegate);
      delegate.put((long)0, 0);
      delegate.put((long)1, 1);
      expected.put((long)0, 0);
      expected.put((long)1, 1);

      //
      assertEquals(true, values.retainAll(Tools.toSet("0","3")));
      expected.remove((long)1);
      assertEquals(expected, delegate);
      delegate.put((long)1, 1);
      expected.put((long)1, 1);

      //
      assertEquals(true, values.retainAll(Tools.toSet()));
      expected.clear();
      assertEquals(expected, delegate);
   }

   public void testContainsAll()
   {
      assertEquals(true, values.containsAll(Tools.toSet("0", "1")));
      assertEquals(expected, delegate);

      //
      assertEquals(true, values.containsAll(Tools.toSet()));
      assertEquals(expected, delegate);

      //
      assertEquals(true, values.containsAll(Tools.toSet("0")));
      assertEquals(expected, delegate);

      //
      assertEquals(false, values.containsAll(Tools.toSet("0", "1", "3")));
      assertEquals(expected, delegate);

      //
      assertEquals(false, values.containsAll(Tools.toSet("1", "3")));
      assertEquals(expected, delegate);

      //
      assertEquals(false, values.containsAll(Tools.toSet("3")));
      assertEquals(expected, delegate);
   }

   //

   public void testRemoveNullArgument()
   {
      try
      {
         values.remove(null);
         fail();
      }
      catch (NullPointerException e)
      {
         assertEquals(expected, delegate);
      }

      //
      map.setAcceptNull(true);
      delegate.put(0L, null);
      delegate.put(1L, null);
      assertTrue(values.remove(null));
      assertEquals(MapBuilder.hashMap(delegate.keySet().iterator().next(), (Integer)null).get(), delegate);
   }

   public void testContainsNullArgument()
   {
      try
      {
         values.contains(null);
         fail();
      }
      catch (NullPointerException e)
      {
         assertEquals(expected, delegate);
      }

      //
      map.setAcceptNull(true);
      delegate.put(0L, null);
      assertTrue(values.contains(null));
   }

   public void testRemoveAllNullArgument()
   {
      try
      {
         values.removeAll(null);
         fail();
      }
      catch (NullPointerException e)
      {
         assertEquals(expected, delegate);
      }

      //
      try
      {
         values.removeAll(Tools.toList("0", null));
         fail();
      }
      catch (NullPointerException e)
      {
         assertEquals(expected, delegate);
      }

      //
      map.setAcceptNull(true);
      delegate.put(0L, 0);
      delegate.put(1L, null);
      delegate.put(2L, null);
      delegate.put(3L, 3);
      assertTrue(values.removeAll(Tools.toList("0", null)));
      assertEquals(MapBuilder.hashMap(3L, 3).get(), delegate);
   }

   public void testRetainAllNullArgument()
   {
      try
      {
         values.retainAll(null);
         fail();
      }
      catch (NullPointerException e)
      {
         assertEquals(expected, delegate);
      }

      //
      try
      {
         values.retainAll(Tools.toList("0", null));
         fail();
      }
      catch (NullPointerException e)
      {
         assertEquals(expected, delegate);
      }

      //
      delegate.put(0L, 0);
      delegate.put(1L, null);
      delegate.put(2L, null);
      delegate.put(3L, 3);
      map.setAcceptNull(true);
      assertTrue(values.retainAll(Tools.toList("0", null)));
      assertEquals(MapBuilder.hashMap(0L, 0).put(1L, null).put(2L, null).get(), delegate);
   }

   public void testContainsAllNullArgument()
   {
      try
      {
         values.containsAll(null);
         fail();
      }
      catch (NullPointerException e)
      {
         assertEquals(expected, delegate);
      }

      //
      try
      {
         values.containsAll(Tools.toList("0", null));
         fail();
      }
      catch (NullPointerException e)
      {
         assertEquals(expected, delegate);
      }

      //
      map.setAcceptNull(true);
      delegate.put(0L, 0);
      delegate.put(1L, null);
      assertTrue(values.containsAll(Tools.toList("0", null)));
   }

   //

   public void testRemoveArgumentWithWrongClass()
   {
      try
      {
         uvalues.remove(new Object());
         fail();
      }
      catch (ClassCastException e)
      {
         assertEquals(expected, delegate);
      }
   }

   public void testContainsArgumentWithWrongClass()
   {
      try
      {
         uvalues.contains(new Object());
         fail();
      }
      catch (ClassCastException e)
      {
         assertEquals(expected, delegate);
      }
   }

   public void testRemoveAllNullArgumentWithWrongClass()
   {
      try
      {
         uvalues.removeAll(Tools.toList("0", new Object()));
         fail();
      }
      catch (ClassCastException e)
      {
         assertEquals(expected, delegate);
      }
   }

   public void testRetainAllNullArgumentWithWrongClass()
   {
      try
      {
         uvalues.retainAll(Tools.toList("0", new Object()));
         fail();
      }
      catch (ClassCastException e)
      {
         assertEquals(expected, delegate);
      }
   }

   public void _testContainsAllNullArgumentWithWrongClass()
   {
      try
      {
         uvalues.containsAll(Tools.toList("zero", new Object()));
         fail();
      }
      catch (ClassCastException e)
      {
         assertEquals(expected, delegate);
      }
   }

   //

   public void testRemoveArgumentConversionThrowsCCE()
   {
      try
      {
         values.remove("" + ValueConverter.CCE);
         fail();
      }
      catch (ClassCastException e)
      {
         assertEquals(expected, delegate);
      }
   }

   public void testContainsArgumentConversionThrowsCCE()
   {
      try
      {
         values.contains("" + ValueConverter.CCE);
         fail();
      }
      catch (ClassCastException e)
      {
         assertEquals(expected, delegate);
      }
   }

   public void testRemoveAllArgumentConversionThrowsCCE()
   {
      try
      {
         values.removeAll(Tools.toList("0", "" + ValueConverter.CCE));
         fail();
      }
      catch (ClassCastException e)
      {
         assertEquals(expected, delegate);
      }
   }

   public void testRetainAllArgumentConversionThrowsCCE()
   {
      try
      {
         values.retainAll(Tools.toList("0", "" + ValueConverter.CCE));
         fail();
      }
      catch (ClassCastException e)
      {
         assertEquals(expected, delegate);
      }
   }

   public void testContainsAllArgumentConversionThrowsCCE()
   {
      try
      {
         values.containsAll(Tools.toList("0", "" + ValueConverter.CCE));
         fail();
      }
      catch (ClassCastException e)
      {
         assertEquals(expected, delegate);
      }
   }

   //

   public void testRemoveArgumentConversionThrowsIAE()
   {
      try
      {
         values.remove("" + ValueConverter.IAE);
         fail();
      }
      catch (IllegalArgumentException e)
      {
         assertEquals(expected, delegate);
      }
   }

   public void testContainsArgumentConversionThrowsIAE()
   {
      try
      {
         values.contains("" + ValueConverter.IAE);
         fail();
      }
      catch (IllegalArgumentException e)
      {
         assertEquals(expected, delegate);
      }
   }

   public void testRemoveAllArgumentConversionThrowsIAE()
   {
      try
      {
         values.removeAll(Tools.toList("0", "" + ValueConverter.IAE));
         fail();
      }
      catch (IllegalArgumentException e)
      {
         assertEquals(expected, delegate);
      }
   }

   public void testRetainAllArgumentConversionThrowsIAE()
   {
      try
      {
         values.retainAll(Tools.toList("0", "" + ValueConverter.IAE));
         fail();
      }
      catch (IllegalArgumentException e)
      {
         assertEquals(expected, delegate);
      }
   }

   public void testContainsAllArgumentConversionThrowsIAE()
   {
      try
      {
         values.containsAll(Tools.toList("0", "" + ValueConverter.IAE));
         fail();
      }
      catch (IllegalArgumentException e)
      {
         assertEquals(expected, delegate);
      }
   }

   //

   // blih
/*
   public void testRemoveArgumentConvertedToNull()
   {
      try
      {
         values.remove(ValueConverter.NULL);
         fail();
      }
      catch (NullPointerException e)
      {
         assertEquals(expected, delegate);
      }

      //
      map.setAcceptNull(true);
   }
*/

   // blih
/*
   public void testContainsArgumentConvertedToNull()
   {
      try
      {
         values.contains("" + ValueConverter.NULL);
         fail();
      }
      catch (IllegalArgumentException e)
      {
         assertEquals(expected, delegate);
      }
   }
*/

   public void testRemoveAllArgumentConvertedToNull()
   {
      try
      {
         values.removeAll(Tools.toList("ze0ro", "" + ValueConverter.NULL));
         fail();
      }
      catch (IllegalArgumentException e)
      {
         assertEquals(expected, delegate);
      }
   }

   // blih
//   public void testRetainAllArgumentConvertedToNull()
//   {
//      try
//      {
//         values.retainAll(Tools.toList("0", ""+ ValueConverter.NULL));
//         fail();
//      }
//      catch (IllegalArgumentException e)
//      {
//         assertEquals(expected, delegate);
//      }
//   }

   // blih
//   public void testContainsAllArgumentConvertedToNull()
//   {
//      try
//      {
//         values.containsAll(Tools.toList("0", "" + ValueConverter.NULL));
//         fail();
//      }
//      catch (IllegalArgumentException e)
//      {
//         assertEquals(expected, delegate);
//      }
//   }

   //

   public void testRemoveArgumentConversionThrowsUncheckedException()
   {
      try
      {
         values.remove("" + ValueConverter.UNCHECKED);
         fail();
      }
      catch (CustomRuntimeException e)
      {
         assertEquals(expected, delegate);
      }
   }

   public void testContiansArgumentConversionThrowsUncheckedException()
   {
      try
      {
         values.contains("" + ValueConverter.UNCHECKED);
         fail();
      }
      catch (CustomRuntimeException e)
      {
         assertEquals(expected, delegate);
      }
   }

   public void testRemoveAllArgumentConversionThrowsUncheckedException()
   {
      try
      {
         values.removeAll(Tools.toList("0", "" + ValueConverter.UNCHECKED));
         fail();
      }
      catch (CustomRuntimeException e)
      {
         assertEquals(expected, delegate);
      }
   }

   public void testRetainAllArgumentConversionThrowsUncheckedException()
   {
      try
      {
         values.retainAll(Tools.toList("0", "" + ValueConverter.UNCHECKED));
         fail();
      }
      catch (CustomRuntimeException e)
      {
         assertEquals(expected, delegate);
      }
   }

   public void testContainsAllArgumentConversionThrowsUncheckedException()
   {
      try
      {
         values.containsAll(Tools.toList("0", "" + ValueConverter.UNCHECKED));
         fail();
      }
      catch (CustomRuntimeException e)
      {
         assertEquals(expected, delegate);
      }
   }

   //

   public void testSize()
   {
      assertEquals(2, values.size());
   }

   public void testClear()
   {
      values.clear();
      delegate.clear();
      assertEquals(0, delegate.size());
   }

   public void _testEquals()
   {
      // todo
   }

   public void _testHashCode()
   {
      // todo
   }

   public void testToArray()
   {
      Object[] a = values.toArray();
      assertNotNull(a);
      assertEquals(Object[].class, a.getClass());
      assertEquals(2, a.length);
      assertEquals(Tools.toSet("0","1"), Tools.toSet(a));
      assertEquals(expected, delegate);

      //
      try
      {
         values.toArray(null);
         fail();
      }
      catch (NullPointerException e)
      {
         assertEquals(expected, delegate);
      }

      //
      try
      {
         uvalues.toArray(new Integer[2]);
         fail();
      }
      catch (ArrayStoreException e)
      {
         assertEquals(expected, delegate);
      }

      //
      String[] strings1 = new String[1];
      String[] strings2 = values.toArray(strings1);
      assertNotSame(strings1, strings2);
      assertEquals(Tools.toSet("0","1"), Tools.toSet(strings2));
      assertEquals(expected, delegate);

      //
      strings1 = new String[2];
      strings2 = values.toArray(strings1);
      assertSame(strings1, strings2);
      assertEquals(Tools.toSet("0","1"), Tools.toSet(strings2));
      assertEquals(expected, delegate);

      //
      strings1 = new String[3];
      strings2 = values.toArray(strings1);
      assertSame(strings1, strings2);
      assertEquals(Tools.toSet("0","1", null), Tools.toSet(strings2));
      assertEquals(expected, delegate);
   }

   public void testIterator()
   {
      Iterator<String> i = values.iterator();
      assertEquals(Tools.toSet("0","1"), Tools.toSet(i));
      assertEquals(expected, delegate);

      //
      i = values.iterator();
      for (Iterator<String> j = values.iterator();j.hasNext();)
      {
         if ("0".equals(j.next()))
         {
            j.remove();
         }
      }
      expected.remove((long)0);
      assertEquals(expected, delegate);
   }
}