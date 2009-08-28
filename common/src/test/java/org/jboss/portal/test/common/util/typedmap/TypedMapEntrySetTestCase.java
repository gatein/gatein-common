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
import java.util.Set;
import java.util.Iterator;

import org.gatein.common.util.Tools;
import org.gatein.common.util.MapBuilder;

/**
 * @author <a href="mailto:julien@jboss.org">Julien Viet</a>
 * @version $Revision: 630 $
 */
public class TypedMapEntrySetTestCase extends TestCase
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
   private Set<Map.Entry<String,String>> entries;

   /** . */
   private Set<Map.Entry<String,String>> uentries;

   /** . */
   private Map<Long, Integer> expected;

   public TypedMapEntrySetTestCase(String name)
   {
      super(name);
   }

   protected void setUp() throws Exception
   {
      delegate = new HashMap<Long, Integer>();
      delegate2 = delegate;
      map = new StringToIntegerMap(delegate);
      map2 = map;

      entries = map.entrySet();
      uentries = entries;

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
         entries.add(new ExternalEntry("zero", "0"));
         fail();
      }
      catch (UnsupportedOperationException e)
      {
         assertEquals(expected, delegate);
      }

      //
      try
      {
         entries.addAll(Tools.toSet(new ExternalEntry("zero", "0")));
         fail();
      }
      catch (UnsupportedOperationException e)
      {
         assertEquals(expected, delegate);
      }
   }

   public void testContains()
   {
      assertEquals(true, entries.contains(new ExternalEntry("zero", "0")));
      assertEquals(expected, delegate);

      //
      assertEquals(false, entries.contains(new ExternalEntry("zero", "3")));
      assertEquals(expected, delegate);

      //
      assertEquals(false, entries.contains(new ExternalEntry("three", "3")));
      assertEquals(expected, delegate);
   }

   public void testContainsAll()
   {
      assertEquals(true, entries.containsAll(Tools.toSet(new ExternalEntry("zero", "0"), new ExternalEntry("one", "1"))));
      assertEquals(expected, delegate);

      //
      assertEquals(false, entries.containsAll(Tools.toSet(new ExternalEntry("zero", "3"), new ExternalEntry("one", "1"))));
      assertEquals(expected, delegate);

      //
      assertEquals(true, entries.containsAll(Tools.toSet()));
      assertEquals(expected, delegate);

      //
      assertEquals(true, entries.containsAll(Tools.toSet(new ExternalEntry("zero", "0"))));
      assertEquals(expected, delegate);

      //
      assertEquals(false, entries.containsAll(Tools.toSet(new ExternalEntry("zero", "0"), new ExternalEntry("one", "1"), new ExternalEntry("three", "3"))));
      assertEquals(expected, delegate);

      //
      assertEquals(false, entries.containsAll(Tools.toSet(new ExternalEntry("zero", "0"), new ExternalEntry("one", "1"), new ExternalEntry("zero", "3"))));
      assertEquals(expected, delegate);

      //
      assertEquals(false, entries.containsAll(Tools.toSet(new ExternalEntry("zero", "3"))));
      assertEquals(expected, delegate);

      //
      assertEquals(false, entries.containsAll(Tools.toSet(new ExternalEntry("one", "1"), new ExternalEntry("three", "3"))));
      assertEquals(expected, delegate);

      //
      assertEquals(false, entries.containsAll(Tools.toSet(new ExternalEntry("three", "3"))));
      assertEquals(expected, delegate);
   }

   //

   public void testContainsNullArgument()
   {
      try
      {
         entries.contains(null);
         fail();
      }
      catch (NullPointerException e)
      {
         assertEquals(expected, delegate);
      }
   }
   
   public void testContainsAllNullArgument()
   {
      try
      {
         entries.containsAll(null);
         fail();
      }
      catch (NullPointerException e)
      {
         assertEquals(expected, delegate);
      }
      try
      {
         entries.containsAll(Tools.toList((ExternalEntry)null));
         fail();
      }
      catch (NullPointerException e)
      {
         assertEquals(expected, delegate);
      }
      try
      {
         entries.containsAll(Tools.toList(new ExternalEntry(null, "0")));
         fail();
      }
      catch (NullPointerException e)
      {
         assertEquals(expected, delegate);
      }
      try
      {
         entries.containsAll(Tools.toList(new ExternalEntry("zero", null)));
         fail();
      }
      catch (NullPointerException e)
      {
         assertEquals(expected, delegate);
      }

      //
      map.setAcceptNull(true);
      delegate.put(0L, null);
      assertTrue(entries.containsAll(Tools.toList(new ExternalEntry("zero", null))));
   }

   //

   public void testContainsArgumentWithWrongClass()
   {
      try
      {
         uentries.contains(new Object());
         fail();
      }
      catch (ClassCastException e)
      {
         assertEquals(expected, delegate);
      }
   }

   public void testContainsAllNullArgumentWithWrongClass()
   {
      try
      {
         uentries.containsAll(Tools.toList("zero", new Object()));
         fail();
      }
      catch (ClassCastException e)
      {
         assertEquals(expected, delegate);
      }
   }

   //

   public void testContainsArgumentConversionThrowsCCE()
   {
      try
      {
         entries.contains(new ExternalEntry("one", "" + ValueConverter.CCE));
         fail();
      }
      catch (ClassCastException e)
      {
         assertEquals(expected, delegate);
      }
      try
      {
         entries.contains(new ExternalEntry(KeyConverter.CCE, "1"));
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
         entries.containsAll(Tools.toList(new ExternalEntry("zero", "0"), new ExternalEntry(KeyConverter.CCE, "1")));
         fail();
      }
      catch (ClassCastException e)
      {
         assertEquals(expected, delegate);
      }
      try
      {
         entries.containsAll(Tools.toList(new ExternalEntry("zero", "0"), new ExternalEntry("one", "" + ValueConverter.CCE)));
         fail();
      }
      catch (ClassCastException e)
      {
         assertEquals(expected, delegate);
      }
   }

   //

   public void testContainsArgumentConversionThrowsIAE()
   {
      try
      {
         entries.contains(new ExternalEntry(KeyConverter.IAE, "1"));
         fail();
      }
      catch (IllegalArgumentException e)
      {
         assertEquals(expected, delegate);
      }
      try
      {
         entries.contains(new ExternalEntry("one", "" + ValueConverter.IAE));
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
         entries.containsAll(Tools.toList(new ExternalEntry("zero", "0"), new ExternalEntry(KeyConverter.IAE, "1")));
         fail();
      }
      catch (IllegalArgumentException e)
      {
         assertEquals(expected, delegate);
      }
      try
      {
         entries.containsAll(Tools.toList(new ExternalEntry("zero", "0"), new ExternalEntry("one", "" + ValueConverter.IAE)));
         fail();
      }
      catch (IllegalArgumentException e)
      {
         assertEquals(expected, delegate);
      }
   }

   //

   public void testContainsArgumentConvertedToNull()
   {
      // blih
//      try
//      {
//         entries.contains(new ExternalEntry("one", "" + ValueConverter.NULL));
//         fail();
//      }
//      catch (IllegalArgumentException e)
//      {
//         assertEquals(expected, delegate);
//      }
      try
      {
         entries.contains(new ExternalEntry(KeyConverter.NULL, "1"));
         fail();
      }
      catch (IllegalArgumentException e)
      {
         assertEquals(expected, delegate);
      }
   }

   public void testContainsAllArgumentConvertedToNull()
   {
      try
      {
         entries.containsAll(Tools.toList(new ExternalEntry("zero", "0"), new ExternalEntry(KeyConverter.NULL, "1")));
         fail();
      }
      catch (IllegalArgumentException e)
      {
         assertEquals(expected, delegate);
      }
      // blih
//      try
//      {
//         entries.containsAll(Tools.toList(new ExternalEntry("zero", "0"), new ExternalEntry("one", "" + ValueConverter.NULL)));
//         fail();
//      }
//      catch (IllegalArgumentException e)
//      {
//         assertEquals(expected, delegate);
//      }
   }

   //

   public void testContainsArgumentConversionThrowsUncheckedException()
   {
      try
      {
         entries.contains(new ExternalEntry(KeyConverter.UNCHECKED , "1"));
         fail();
      }
      catch (CustomRuntimeException e)
      {
         assertEquals(expected, delegate);
      }
      try
      {
         entries.contains(new ExternalEntry("one", "" + ValueConverter.UNCHECKED));
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
         entries.containsAll(Tools.toList(new ExternalEntry("zero", "0"), new ExternalEntry(KeyConverter.UNCHECKED, "1")));
         fail();
      }
      catch (CustomRuntimeException e)
      {
         assertEquals(expected, delegate);
      }
      try
      {
         entries.containsAll(Tools.toList(new ExternalEntry("zero", "0"), new ExternalEntry("one", "" + ValueConverter.UNCHECKED)));
         fail();
      }
      catch (CustomRuntimeException e)
      {
         assertEquals(expected, delegate);
      }
   }

   public void testGetValueWithNullValue()
   {
      delegate.put(0L, null);
      delegate.remove(1L);
      Map.Entry<String, String> entry =  entries.iterator().next();
      try
      {
         entry.getValue();
         fail();
      }
      catch (IllegalStateException e)
      {
      }
   }

   public void testSetValueWithPreviousNullValue()
   {
      delegate.put(0L, null);
      delegate.remove(1L);
      Map.Entry<String, String> entry =  entries.iterator().next();
      try
      {
         entry.setValue("1");
         fail();
      }
      catch (IllegalStateException e)
      {
         assertEquals(MapBuilder.hashMap(0L, (Integer)null).get(), delegate);
      }
   }

   //

   public void testSize()
   {
      assertEquals(2, entries.size());
   }

   public void testClear()
   {
      entries.clear();
      delegate.clear();
      assertEquals(0, delegate.size());
   }

   public void testEquals()
   {
      // todo
   }

   public void testHashCode()
   {
      // todo
   }

   public void testToArray()
   {
      Object[] a = entries.toArray();
      assertNotNull(a);
      assertEquals(Object[].class, a.getClass());
      assertEquals(2, a.length);
      Map.Entry<String, String>[] tmp = new Map.Entry[a.length];
      System.arraycopy(a, 0, tmp, 0, a.length);
      assertTrue(map.equals(map(tmp)));
      assertEquals(expected, delegate);

      //
      try
      {
         entries.toArray(null);
         fail();
      }
      catch (NullPointerException e)
      {
         assertEquals(expected, delegate);
      }

      //
      try
      {
         uentries.toArray(new Integer[2]);
         fail();
      }
      catch (ArrayStoreException e)
      {
         assertEquals(expected, delegate);
      }

      //
      Map.Entry[] strings1 = new Map.Entry[1];
      Map.Entry[] strings2 = entries.toArray(strings1);
      assertNotSame(strings1, strings2);
      assertEquals(map, map(strings2));
      assertEquals(expected, delegate);

      //
      strings1 = new Map.Entry[2];
      strings2 = entries.toArray(strings1);
      assertSame(strings1, strings2);
      assertEquals(map, map(strings2));
      assertEquals(expected, delegate);

      //
      strings1 = new Map.Entry[3];
      strings2 = entries.toArray(strings1);
      assertSame(strings1, strings2);
      assertEquals(map, map(strings2));
      assertEquals(expected, delegate);
   }

   private <K, V> Map<K, V> map(Map.Entry<K, V> entries[])
   {
      Map<K, V> tmp = new HashMap<K, V>();
      for (Map.Entry<K, V> entry : entries)
      {
         if (entry != null)
         {
            tmp.put(entry.getKey(), entry.getValue());
         }
      }
      return tmp;
   }

   private <K, V> Map<K, V> map(Iterator<Map.Entry<K, V>> iterator)
   {
      Map<K, V> tmp = new HashMap<K, V>();
      while (iterator.hasNext())
      {
         Map.Entry<K, V> entry = iterator.next();
         if (entry != null)
         {
            tmp.put(entry.getKey(), entry.getValue());
         }
      }
      return tmp;
   }

   public void testIterator()
   {
      Iterator<Map.Entry<String, String>> i = entries.iterator();
      assertEquals(map, map(i));
      assertEquals(expected, delegate);

      //
      i = entries.iterator();
      for (Iterator<Map.Entry<String, String>> j = entries.iterator();j.hasNext();)
      {
         if ("zero".equals(j.next().getKey()))
         {
            j.remove();
         }
      }
      expected.remove((long)0);
      assertEquals(expected, delegate);
   }

   public void testBlah()
   {

      Map.Entry<String, String> entry = getEntry("zero");

      assertEquals("zero", entry.getKey());
      assertEquals("0", entry.getValue());

      assertEquals("0", entry.setValue("1"));

      expected.put((long)0, 1);
      assertEquals(expected, delegate);

      //
      try
      {
         entry.setValue("" + ValueConverter.CCE);
         fail();
      }
      catch (ClassCastException e)
      {
         assertEquals(expected, delegate);
      }

      //
      try
      {
         entry.setValue("" + ValueConverter.IAE);
         fail();
      }
      catch (IllegalArgumentException e)
      {
         assertEquals(expected, delegate);
      }

      //
      try
      {
         entry.setValue("" + ValueConverter.UNCHECKED);
         fail();
      }
      catch (CustomRuntimeException e)
      {
         assertEquals(expected, delegate);
      }

      // blih
//      try
//      {
//         entry.setValue("" + ValueConverter.NULL);
//         fail();
//      }
//      catch (IllegalArgumentException e)
//      {
//         assertEquals(expected, delegate);
//      }

      //
      try
      {
         entry.setValue(null);
         fail();
      }
      catch (NullPointerException e)
      {
         assertEquals(expected, delegate);
      }

      //
      map.setAcceptNull(true);
      assertEquals("1", entry.setValue(null));
      Map<Long, Integer> copy = new HashMap<Long, Integer>(expected);
      copy.put(0L, null);
      assertEquals(copy, delegate);
      assertEquals(null, entry.setValue("1"));

      // blih
//      delegate.put((long)0, ValueConverter.NULL);
//      try
//      {
//         entry.getValue();
//         fail();
//      }
//      catch (IllegalStateException e)
//      {
//      }

      //
      delegate.put((long)0, ValueConverter.IAE);
      try
      {
         entry.getValue();
         fail();
      }
      catch (IllegalArgumentException e)
      {
      }
   }

   private Map.Entry<String, String> getEntry(String key)
   {
       for (Map.Entry<String, String> entry : entries)
       {
          if (key.equals(entry.getKey()))
          {
             return entry;
          }
       }
      return null;
   }

   private static class ExternalEntry implements Map.Entry<String, String>
   {

      /** . */
      private final String key;

      /** . */
      private String value;

      private ExternalEntry(String key, String value)
      {
         this.key = key;
         this.value = value;
      }

      public String getKey()
      {
         return key;
      }

      public String getValue()
      {
         return value;
      }

      public String setValue(String value)
      {
         String oldValue = this.value;
         this.value = value;
         return oldValue;
      }
   }
}