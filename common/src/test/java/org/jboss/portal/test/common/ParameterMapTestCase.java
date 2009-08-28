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
import org.gatein.common.util.ParameterMap;
import org.gatein.common.util.Tools;
import org.gatein.common.junit.ExtendedAssert;
import org.gatein.common.io.IOTools;

import java.util.HashMap;
import java.util.Set;
import java.util.Iterator;
import java.util.Map;
import java.util.Arrays;
import java.util.List;
import java.io.IOException;

/**
 * @author <a href="mailto:julien@jboss.org">Julien Viet</a>
 * @version $Revision: 6643 $
 */
public class ParameterMapTestCase extends TestCase
{

   /** . */
   private ParameterMap param;

   public ParameterMapTestCase()
   {
   }

   public ParameterMapTestCase(String name)
   {
      super(name);
   }

   public void setUp()
   {
      param = new ParameterMap();
   }

   public void tearDown()
   {
      param = null;
   }

   public void testPut()
   {
      ParameterMap pm = new ParameterMap(new HashMap<String, String[]>());
      pm.put("foo", new String[]{"bar"});
      // ExtendedAssert.assertEquals(new String[]{"bar"}, (Object[])pm.get("foo"));
   }

   public void testEntry()
   {
      ParameterMap pm = new ParameterMap(new HashMap<String, String[]>());
      pm.put("foo", new String[]{"bar"});
      Set entries = pm.entrySet();
      assertNotNull(entries);
      Iterator iterator = entries.iterator();
      assertTrue(iterator.hasNext());
      Object tmp = iterator.next();
      assertTrue(tmp instanceof Map.Entry);
      Map.Entry entry = (Map.Entry)tmp;
      assertEquals("foo", entry.getKey());
      // ExtendedAssert.assertEquals(new String[]{"bar"}, (Object[])entry.getValue());
   }

   public void testPutThrowsException()
   {
      ParameterMap pm = new ParameterMap(new HashMap<String, String[]>());
      try
      {
         ((Map)pm).put(new Object(), new String[]{"bar"});
         fail();
      }
      catch (ClassCastException expected)
      {
      }
      try
      {
         ((Map)pm).put("foo", new Object[]{});
         fail();
      }
      catch (ClassCastException expected)
      {
      }
      try
      {
         pm.put("foo", new String[]{});
         fail();
      }
      catch (IllegalArgumentException expected)
      {
      }
      try
      {
         pm.put("foo", new String[]{null});
         fail();
      }
      catch (IllegalArgumentException expected)
      {
      }
   }

   public void testEntrySetValueThrowsException()
   {
      ParameterMap pm = new ParameterMap(new HashMap<String, String[]>());
      pm.put("foo", new String[]{"bar"});
      Set entries = pm.entrySet();
      assertNotNull(entries);
      Iterator iterator = entries.iterator();
      assertTrue(iterator.hasNext());
      Object tmp = iterator.next();
      assertTrue(tmp instanceof Map.Entry);
      Map.Entry entry = (Map.Entry)tmp;
      try
      {
         entry.setValue(null);
         fail();
      }
      catch (NullPointerException expected)
      {
      }
      try
      {
         entry.setValue(new Object[]{});
         fail();
      }
      catch (ClassCastException expected)
      {
      }
      try
      {
         entry.setValue(new String[]{});
         fail();
      }
      catch (IllegalArgumentException expected)
      {
      }
      try
      {
         entry.setValue(new String[]{null});
         fail();
      }
      catch (IllegalArgumentException expected)
      {
      }
   }

   public void testNoCopyOnRead()
   {
      Map<String, String[]> internal = new HashMap<String, String[]>();
      String[] internalValue = {"bar"};
      internal.put("foo", internalValue);

      //
      ParameterMap map = new ParameterMap(internal, ParameterMap.AccessMode.get(false, false));

      //
      String[] externalValue = map.get("foo");
      assertNotNull(externalValue);
      assertEquals(1, externalValue.length);
      assertEquals("bar", externalValue[0]);
      internalValue[0] = null;
      assertEquals(null, externalValue[0]);
   }

   public void testCopyOnRead()
   {
      Map<String, String[]> internal = new HashMap<String, String[]>();
      String[] internalValue = {"bar"};
      internal.put("foo", internalValue);

      //
      ParameterMap map = new ParameterMap(internal, ParameterMap.AccessMode.get(true, false));

      //
      String[] externalValue = map.get("foo");
      assertNotNull(externalValue);
      assertEquals(1, externalValue.length);
      assertEquals("bar", externalValue[0]);
      internalValue[0] = null;
      assertEquals("bar", externalValue[0]);
   }

   public void testNoCopyOnWrite()
   {
      Map<String, String[]> internal = new HashMap<String, String[]>();

      //
      ParameterMap map = new ParameterMap(internal, ParameterMap.AccessMode.get(false, false));

      //
      String[] externalValue = new String[]{"bar"};
      map.put("foo", externalValue);

      //
      String[] internalValue = internal.get("foo");
      assertNotNull(internalValue);
      assertEquals(1, internalValue.length);
      assertEquals("bar", internalValue[0]);
      externalValue[0] = null;
      assertEquals(null, internalValue[0]);
   }

   public void testCopyOnWrite()
   {
      Map<String, String[]> internal = new HashMap<String, String[]>();

      //
      ParameterMap map = new ParameterMap(internal, ParameterMap.AccessMode.get(false, true));

      //
      String[] externalValue = new String[]{"bar"};
      map.put("foo", externalValue);

      //
      String[] internalValue = internal.get("foo");
      assertNotNull(internalValue);
      assertEquals(1, internalValue.length);
      assertEquals("bar", internalValue[0]);
      externalValue[0] = null;
      assertEquals("bar", internalValue[0]);
   }

   public void testGetWithNullName()
   {
      try
      {
         param.getValue(null);
         fail("Expected IllegalArgumentException");
      }
      catch (IllegalArgumentException e)
      {
      }
   }

   public void testSet()
   {
      param.setValue("a", "b");
      assertEquals(param.getValue("a"), "b");
   }

   public void testSetWithNullName()
   {
      try
      {
         param.setValue(null, "b");
         fail("Expected IllegalArgumentException");
      }
      catch (IllegalArgumentException e)
      {
      }
   }

   public void testSetWithNullValue()
   {
      try
      {
         param.setValue("a", null);
         fail("Expected IllegalArgumentException");
      }
      catch (IllegalArgumentException e)
      {
      }
   }

   public void testRemoveWithNullName()
   {
      try
      {
         param.remove(null);
         fail("Expected IllegalArgumentException");
      }
      catch (NullPointerException e)
      {
      }
   }

   public void testRemove()
   {
      param.setValue("a", "b");
      param.remove("a");
      assertEquals(param.getValue("a"), null);
   }

   public void testSetValues()
   {
      param.setValues("a", new String[]{"b", "c"});
      assertTrue(Arrays.equals(param.getValues("a"), new String[]{
         "b", "c"}));
      assertEquals(param.getValue("a"), "b");
   }

   public void testSetValuesWithNullName()
   {
      try
      {
         param.setValues(null, new String[]{"a"});
         fail("Expected IllegalArgumentException");
      }
      catch (IllegalArgumentException e)
      {
      }
   }

   public void testSetValuesWithNullValues()
   {
      try
      {
         param.setValues("a", null);
         fail("Expected IllegalArgumentException");
      }
      catch (IllegalArgumentException e)
      {
      }
   }

   public void testSetValuesWithZeroLengthValues()
   {
      try
      {
         param.setValues("a", new String[0]);
         fail("Expected IllegalArgumentException");
      }
      catch (IllegalArgumentException e)
      {
      }
   }

   public void testSetValuesWithOneNullValue()
   {
      try
      {
         param.setValues("a", new String[]{"a", null});
         fail("Expected IllegalArgumentException");
      }
      catch (IllegalArgumentException e)
      {
      }
   }

   public void testReplaceWithParameters()
   {
      ParameterMap other = new ParameterMap();
      other.setValue("a", "b");
      other.setValues("c", new String[]{"d", "e"});
      param.replace(other);
      assertEquals("b", param.getValue("a"));
      assertTrue(Arrays.equals(param.getValues("c"), new String[]{"d", "e"}));
   }

   public void testCopyConstructorWithNullParameters()
   {
      try
      {
         ParameterMap.clone(null);
         fail("Expected IllegalArgumentException");
      }
      catch (IllegalArgumentException e)
      {
      }
   }

   public void testCopyConstructorWithNullMap()
   {
      try
      {
         ParameterMap.clone(null);
         fail("Expected IllegalArgumentException");
      }
      catch (IllegalArgumentException e)
      {
      }
   }

   public void testReplaceWithNullMap()
   {
      try
      {
         param.replace(null);
         fail("Expected NullPointerException");
      }
      catch (NullPointerException e)
      {
      }
   }

   public void testReplaceWithInvalidMap()
   {
      List<Map<String, String[]>> maps = buildInvalidMaps();
      Class[] exceptionClasses = buildExceptionClasses();
      for (int i = 0;i < maps.size();i++)
      {
         try
         {
            Map<String, String[]> map = maps.get(i);
            param.replace(map);
            fail("Expected IllegalArgumentException with map=" + map);
         }
         catch (Exception e)
         {
            assertTrue(exceptionClasses[i].isAssignableFrom(e.getClass()));
         }
      }
   }

   public void testReplace()
   {
      param.setValue("a", "b");
      param.setValues("c", new String[]{"d", "e"});
      param.setValue("f", "g");
      Map<String, String[]> map = new HashMap<String, String[]>();
      map.put("a", new String[]{"_b"});
      map.put("c", new String[]{"_d", "_e"});
      map.put("h", new String[]{"_i"});
      param.replace(map);
      assertEquals(3, param.size());
      ExtendedAssert.assertEquals(param.getValues("a"), new String[]{"_b"});
      ExtendedAssert.assertEquals(param.getValues("c"), new String[]{"_d", "_e"});
      ExtendedAssert.assertEquals(param.getValues("h"), new String[]{"_i"});
   }

   public void testAppendWithInvalidMap()
   {
      List<Map<String, String[]>> maps = buildInvalidMaps();
      Class[] exceptionClasses = buildExceptionClasses();
      for (int i = 0; i < maps.size();i++)
      {
         try
         {
            Map<String, String[]> map = maps.get(i);
            param.append(map);
            fail("Expected IllegalArgumentException with map=" + map);
         }
         catch (Exception e)
         {
            if (!exceptionClasses[i].isAssignableFrom(e.getClass()))
            {
               fail("Exception class " + exceptionClasses[i].getName() + " (index=" + i + ") should be assignable from caught exception " + e.getClass());
            }
         }
      }
   }

   public void testAppend()
   {
      param.setValue("a", "b");
      param.setValues("c", new String[]{"d", "e"});
      param.setValue("f", "g");
      Map<String, String[]> map = new HashMap<String, String[]>();
      map.put("a", new String[]{"_b"});
      map.put("c", new String[]{"_d", "_e"});
      map.put("h", new String[]{"_i"});
      param.append(map);
      assertEquals(4, param.size());
      ExtendedAssert.assertEquals(param.getValues("a"), new String[]{"b", "_b"});
      ExtendedAssert.assertEquals(param.getValues("c"), new String[]{"d", "e", "_d", "_e"});
      ExtendedAssert.assertEquals(param.getValues("f"), new String[]{"g"});
      ExtendedAssert.assertEquals(param.getValues("h"), new String[]{"_i"});
   }

   public void testClear()
   {
      param.setValue("a", "b");
      param.clear();
      assertNull(param.getValue("a"));
   }

   private void checkSerializable(ParameterMap parameters) throws IOException
   {
      ParameterMap copy = IOTools.clone(parameters);
      assertTrue(copy.equals(parameters));
   }

   public void testSerializable() throws IOException
   {
      ParameterMap p1 = new ParameterMap();
      ParameterMap p2 = new ParameterMap();
      p2.setValues("foo", new String[]{"foo1"});
      ParameterMap p3 = new ParameterMap();
      p3.setValues("foo", new String[]{"foo1"});
      p3.setValues("bar", new String[]{"bar1","bar2"});

      //
      checkSerializable(p1);
      checkSerializable(p2);
      checkSerializable(p3);
   }

   public Class[] buildExceptionClasses()
   {
      return new Class[]
         {
            NullPointerException.class,
            IllegalArgumentException.class,
            IllegalArgumentException.class,
            ClassCastException.class
         };
   }

   public List<Map<String, String[]>> buildInvalidMaps()
   {
      Map<String, String[]> map1 = new HashMap<String, String[]>();
      map1.put("a", null);
      Map<String, String[]> map2 = new HashMap<String, String[]>();
      map2.put("a", new String[0]);
      Map<String, String[]> map3 = new HashMap<String, String[]>();
      map3.put("a", new String[]{null});
      Map map4 = new HashMap();
      map4.put("a", new Object());
      return Tools.toList(map1, map2, map3, (Map<String, String[]>)map4);
   }
}
