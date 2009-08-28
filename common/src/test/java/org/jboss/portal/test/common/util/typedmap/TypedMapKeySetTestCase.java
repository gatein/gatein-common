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
import java.util.HashSet;

import org.gatein.common.util.Tools;

/**
 * @author <a href="mailto:julien@jboss.org">Julien Viet</a>
 * @version $Revision: 630 $
 */
public class TypedMapKeySetTestCase extends TestCase
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
   private Set<String> keys;

   /** . */
   private Set ukeys;

   /** . */
   private Map<Long, Integer> expected;

   public TypedMapKeySetTestCase(String name)
   {
      super(name);
   }

   protected void setUp() throws Exception
   {
      delegate = new HashMap<Long, Integer>();
      delegate2 = delegate;
      map = new StringToIntegerMap(delegate);
      map2 = map;

      keys = map.keySet();
      ukeys = keys;

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
         keys.add("abc");
         fail();
      }
      catch (UnsupportedOperationException e)
      {
         assertEquals(expected, delegate);
      }

      //
      try
      {
         keys.addAll(Tools.toSet("abc"));
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
      assertEquals(true, keys.remove("zero"));
      expected.remove((long)0);
      assertEquals(expected, delegate);
   }

   public void testContains()
   {
      assertEquals(true, keys.contains("zero"));
      assertEquals(expected, delegate);

      //
      assertEquals(false, keys.contains("three"));
      assertEquals(expected, delegate);
   }

   public void testRemoveAll()
   {
      assertEquals(false, keys.removeAll(Tools.toSet()));
      assertEquals(expected, delegate);

      //
      assertEquals(false, keys.removeAll(Tools.toSet("three")));
      assertEquals(expected, delegate);

      //
      assertEquals(true, keys.removeAll(Tools.toSet("zero")));
      expected.remove((long)0);
      assertEquals(expected, delegate);
      delegate.put((long)0, 0);
      expected.put((long)0, 0);

      //
      assertEquals(true, keys.removeAll(Tools.toSet("zero", "three")));
      expected.remove((long)0);
      assertEquals(expected, delegate);
      delegate.put((long)0, 0);
      expected.put((long)0, 0);

      //
      assertEquals(true, keys.removeAll(Tools.toSet("zero", "one")));
      expected.clear();
      assertEquals(expected, delegate);
   }

   public void testRetainAll()
   {
      assertEquals(false, keys.retainAll(Tools.toSet("zero", "one")));
      assertEquals(expected, delegate);

      //
      assertEquals(true, keys.retainAll(Tools.toSet("zero")));
      expected.remove((long)1);
      assertEquals(expected, delegate);
      delegate.put((long)1, 1);
      expected.put((long)1, 1);

      //
      assertEquals(true, keys.retainAll(Tools.toSet("three")));
      expected.clear();
      assertEquals(expected, delegate);
      delegate.put((long)0, 0);
      delegate.put((long)1, 1);
      expected.put((long)0, 0);
      expected.put((long)1, 1);

      //
      assertEquals(true, keys.retainAll(Tools.toSet("zero","three")));
      expected.remove((long)1);
      assertEquals(expected, delegate);
      delegate.put((long)1, 1);
      expected.put((long)1, 1);

      //
      assertEquals(true, keys.retainAll(Tools.toSet()));
      expected.clear();
      assertEquals(expected, delegate);
   }

   public void testContainsAll()
   {
      assertEquals(true, keys.containsAll(Tools.toSet("zero", "one")));
      assertEquals(expected, delegate);

      //
      assertEquals(true, keys.containsAll(Tools.toSet()));
      assertEquals(expected, delegate);

      //
      assertEquals(true, keys.containsAll(Tools.toSet("zero")));
      assertEquals(expected, delegate);

      //
      assertEquals(false, keys.containsAll(Tools.toSet("zero", "one", "three")));
      assertEquals(expected, delegate);

      //
      assertEquals(false, keys.containsAll(Tools.toSet("one", "three")));
      assertEquals(expected, delegate);

      //
      assertEquals(false, keys.containsAll(Tools.toSet("three")));
      assertEquals(expected, delegate);
   }

   //

   public void testRemoveNullArgument()
   {
      try
      {
         keys.remove(null);
         fail();
      }
      catch (NullPointerException e)
      {
         assertEquals(expected, delegate);
      }
   }

   public void testContainsNullArgument()
   {
      try
      {
         keys.contains(null);
         fail();
      }
      catch (NullPointerException e)
      {
         assertEquals(expected, delegate);
      }
   }

   public void testRemoveAllNullArgument()
   {
      try
      {
         keys.removeAll(null);
         fail();
      }
      catch (NullPointerException e)
      {
         assertEquals(expected, delegate);
      }
      try
      {
         keys.removeAll(Tools.toList("zero", null));
         fail();
      }
      catch (NullPointerException e)
      {
         assertEquals(expected, delegate);
      }
   }

   public void testRetainAllNullArgument()
   {
      try
      {
         keys.retainAll(null);
         fail();
      }
      catch (NullPointerException e)
      {
         assertEquals(expected, delegate);
      }
      try
      {
         keys.retainAll(Tools.toList("zero", null));
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
         keys.containsAll(null);
         fail();
      }
      catch (NullPointerException e)
      {
         assertEquals(expected, delegate);
      }
      try
      {
         keys.containsAll(Tools.toList("zero", null));
         fail();
      }
      catch (NullPointerException e)
      {
         assertEquals(expected, delegate);
      }
   }

   //

   public void testRemoveArgumentWithWrongClass()
   {
      try
      {
         ukeys.remove(new Object());
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
         ukeys.contains(new Object());
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
         ukeys.removeAll(Tools.toList("zero", new Object()));
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
         ukeys.retainAll(Tools.toList("zero", new Object()));
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
         ukeys.containsAll(Tools.toList("zero", new Object()));
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
         keys.remove(KeyConverter.CCE);
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
         keys.contains(KeyConverter.CCE);
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
         keys.removeAll(Tools.toList("zero", KeyConverter.CCE));
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
         keys.retainAll(Tools.toList("zero", KeyConverter.CCE));
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
         keys.containsAll(Tools.toList("zero", KeyConverter.CCE));
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
         keys.remove(KeyConverter.IAE);
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
         keys.contains(KeyConverter.IAE);
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
         keys.removeAll(Tools.toList("zero", KeyConverter.IAE));
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
         keys.retainAll(Tools.toList("zero", KeyConverter.IAE));
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
         keys.containsAll(Tools.toList("zero", KeyConverter.IAE));
         fail();
      }
      catch (IllegalArgumentException e)
      {
         assertEquals(expected, delegate);
      }
   }

   //

   public void testRemoveArgumentConvertedToNull()
   {
      try
      {
         keys.remove(KeyConverter.NULL);
         fail();
      }
      catch (IllegalArgumentException e)
      {
         assertEquals(expected, delegate);
      }
   }

   public void testContainsArgumentConvertedToNull()
   {
      try
      {
         keys.contains(KeyConverter.NULL);
         fail();
      }
      catch (IllegalArgumentException e)
      {
         assertEquals(expected, delegate);
      }
   }

   public void testRemoveAllArgumentConvertedToNull()
   {
      try
      {
         keys.removeAll(Tools.toList("zero", KeyConverter.NULL));
         fail();
      }
      catch (IllegalArgumentException e)
      {
         assertEquals(expected, delegate);
      }
   }

   public void testRetainAllArgumentConvertedToNull()
   {
      try
      {
         keys.retainAll(Tools.toList("zero", KeyConverter.NULL));
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
         keys.containsAll(Tools.toList("zero", KeyConverter.NULL));
         fail();
      }
      catch (IllegalArgumentException e)
      {
         assertEquals(expected, delegate);
      }
   }

   //

   public void testRemoveArgumentConversionThrowsUncheckedException()
   {
      try
      {
         keys.remove(KeyConverter.UNCHECKED);
         fail();
      }
      catch (CustomRuntimeException e)
      {
         assertEquals(expected, delegate);
      }
   }

   public void testContainsArgumentConversionThrowsUncheckedException()
   {
      try
      {
         keys.contains(KeyConverter.UNCHECKED);
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
         keys.removeAll(Tools.toList("zero", KeyConverter.UNCHECKED));
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
         keys.retainAll(Tools.toList("zero", KeyConverter.UNCHECKED));
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
         keys.containsAll(Tools.toList("zero", KeyConverter.UNCHECKED));
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
      assertEquals(2, keys.size());
   }

   public void testClear()
   {
      keys.clear();
      delegate.clear();
      assertEquals(0, delegate.size());
   }

   public void testEquals()
   {
      Set<String> same = new HashSet<String>();
      same.add("zero");
      same.add("one");

      assertTrue(keys.equals(same));



   }

   public void testHashCode()
   {
      // todo
   }

   public void testToArray()
   {
      Object[] a = keys.toArray();
      assertNotNull(a);
      assertEquals(Object[].class, a.getClass());
      assertEquals(2, a.length);
      assertEquals(Tools.toSet("zero","one"), Tools.toSet(a));
      assertEquals(expected, delegate);

      //
      try
      {
         keys.toArray(null);
         fail();
      }
      catch (NullPointerException e)
      {
         assertEquals(expected, delegate);
      }

      //
      try
      {
         ukeys.toArray(new Integer[2]);
         fail();
      }
      catch (ArrayStoreException e)
      {
         assertEquals(expected, delegate);
      }

      //
      String[] strings1 = new String[1];
      String[] strings2 = keys.toArray(strings1);
      assertNotSame(strings1, strings2);
      assertEquals(Tools.toSet("zero","one"), Tools.toSet(strings2));
      assertEquals(expected, delegate);

      //
      strings1 = new String[2];
      strings2 = keys.toArray(strings1);
      assertSame(strings1, strings2);
      assertEquals(Tools.toSet("zero","one"), Tools.toSet(strings2));
      assertEquals(expected, delegate);

      //
      strings1 = new String[3];
      strings2 = keys.toArray(strings1);
      assertSame(strings1, strings2);
      assertEquals(Tools.toSet("zero","one", null), Tools.toSet(strings2));
      assertEquals(expected, delegate);
   }

   public void testIterator()
   {
      Iterator<String> i = keys.iterator();
      assertEquals(Tools.toSet("zero","one"), Tools.toSet(i));
      assertEquals(expected, delegate);

      //
      i = keys.iterator();
      for (Iterator<String> j = keys.iterator();j.hasNext();)
      {
         if ("zero".equals(j.next()))
         {
            j.remove();
         }
      }
      expected.remove((long)0);
      assertEquals(expected, delegate);
   }

   

}
