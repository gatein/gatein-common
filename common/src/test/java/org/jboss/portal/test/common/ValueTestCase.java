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

import org.gatein.common.value.Value;
import org.gatein.common.value.IntegerValue;
import org.gatein.common.value.StringValue;
import org.gatein.common.junit.ExtendedAssert;
import org.gatein.common.util.Tools;

import java.util.ArrayList;

/**
 * @author <a href="mailto:julien@jboss.org">Julien Viet</a>
 * @version $Revision: 5451 $
 */
public class ValueTestCase extends TestCase
{
   public ValueTestCase(String s)
   {
      super(s);
   }


   public void testIntegerValueWithNull() throws Exception
   {
      _testNullInteger(new IntegerValue());
      _testNullInteger(new IntegerValue((Integer)null));
      _testNullInteger(new IntegerValue((String)null));
      _testNullInteger(new IntegerValue(new Integer[1]));
      _testNullInteger(new IntegerValue(new String[1]));
   }

   private void _testNullInteger(Value value) throws Exception
   {
      assertFalse(value.isInstanceOf(String.class));
      assertTrue(value.isInstanceOf(Number.class));
      assertTrue(value.isInstanceOf(Integer.class));
      assertTrue(value.isNull());
      assertEquals(1, value.size());
      assertFalse(value.isMultiValued());
      assertFalse(value.isEmpty());
      assertNull(value.asString());
      assertEquals(null, value.asObject());
      assertNotNull(value.asStringArray());
      assertEquals(1, value.asStringArray().length);
      assertNull(value.asStringArray()[0]);
      assertTrue(value.asObjectArray() instanceof Integer[]);
      assertEquals(1, value.asObjectArray().length);
      assertNull(value.asObjectArray()[0]);
   }

   public void testIntegerValueEmptyArray() throws Exception
   {
      _testIntegerValueEmptyArray(new IntegerValue(new Integer[0]));
      _testIntegerValueEmptyArray(new IntegerValue(new int[0]));
      _testIntegerValueEmptyArray(new IntegerValue(new String[0]));
   }

   public void _testIntegerValueEmptyArray(Value value) throws Exception
   {
      assertFalse(value.isInstanceOf(String.class));
      assertTrue(value.isInstanceOf(Number.class));
      assertTrue(value.isInstanceOf(Integer.class));
      assertTrue(value.isNull());
      assertEquals(0, value.size());
      assertFalse(value.isMultiValued());
      assertTrue(value.isEmpty());
      assertNull(value.asString());
      assertNull(value.asObject());
      assertTrue(value.asObjectArray() instanceof Integer[]);
      assertEquals(0, value.asObjectArray().length);
      assertNotNull(value.asStringArray());
      assertEquals(0, value.asStringArray().length);
   }

   public void testIntegerValueNonNullInteger() throws Exception
   {
      _testIntegerValueNonNullInteger(new IntegerValue(1));
      _testIntegerValueNonNullInteger(new IntegerValue(new Integer(1)));
      _testIntegerValueNonNullInteger(new IntegerValue(new int[]{1}));
      _testIntegerValueNonNullInteger(new IntegerValue(new Integer[]{new Integer(1)}));
      _testIntegerValueNonNullInteger(new IntegerValue("1"));
      _testIntegerValueNonNullInteger(new IntegerValue(new String[]{"1"}));
   }

   private void _testIntegerValueNonNullInteger(Value value) throws Exception
   {
      assertFalse(value.isInstanceOf(String.class));
      assertTrue(value.isInstanceOf(Number.class));
      assertTrue(value.isInstanceOf(Integer.class));
      assertFalse(value.isNull());
      assertEquals(1, value.size());
      assertFalse(value.isMultiValued());
      assertFalse(value.isEmpty());
      assertEquals("1", value.asString());
      assertEquals(new Integer(1), value.asObject());
      assertNotNull(value.asStringArray());
      assertEquals(1, value.asStringArray().length);
      assertEquals("1", value.asStringArray()[0]);
      assertTrue(value.asObjectArray() instanceof Integer[]);
      assertEquals(1, value.asObjectArray().length);
      assertEquals(new Integer(1), value.asObjectArray()[0]);
   }

   public void testIntegerValueIntegerArray() throws Exception
   {
      _testIntegerValueIntegerArray(new IntegerValue(new int[]{1,2}));
      _testIntegerValueIntegerArray(new IntegerValue(new Integer[]{new Integer(1), new Integer(2)}));
      _testIntegerValueIntegerArray(new IntegerValue(new String[]{"1","2"}));
   }

   private void _testIntegerValueIntegerArray(Value value) throws Exception
   {
      assertFalse(value.isInstanceOf(String.class));
      assertTrue(value.isInstanceOf(Number.class));
      assertTrue(value.isInstanceOf(Integer.class));
      assertFalse(value.isNull());
      assertEquals(2, value.size());
      assertTrue(value.isMultiValued());
      assertFalse(value.isEmpty());
      assertEquals("1", value.asString());
      assertEquals(new Integer(1), value.asObject());
      assertNotNull(value.asStringArray());
      assertEquals(2, value.asStringArray().length);
      assertEquals("1", value.asStringArray()[0]);
      assertEquals("2", value.asStringArray()[1]);
      assertTrue(value.asObjectArray() instanceof Integer[]);
      assertEquals(2, value.asObjectArray().length);
      assertEquals(new Integer(1), value.asObjectArray()[0]);
      assertEquals(new Integer(2), value.asObjectArray()[1]);
   }

   public void testIntegerValueIntegerArrayWithNull() throws Exception
   {
      _testIntegerValueIntegerArrayWithNull(new IntegerValue(new Integer[]{null, new Integer(1)}));
      _testIntegerValueIntegerArrayWithNull(new IntegerValue(new String[]{null, "1"}));
   }

   private void _testIntegerValueIntegerArrayWithNull(Value value) throws Exception
   {
      assertFalse(value.isInstanceOf(String.class));
      assertTrue(value.isInstanceOf(Number.class));
      assertTrue(value.isInstanceOf(Integer.class));
      assertTrue(value.isNull());
      assertEquals(2, value.size());
      assertTrue(value.isMultiValued());
      assertFalse(value.isEmpty());
      assertEquals(null, value.asString());
      assertEquals(null, value.asObject());
      assertNotNull(value.asStringArray());
      assertEquals(2, value.asStringArray().length);
      assertEquals(null, value.asStringArray()[0]);
      assertEquals("1", value.asStringArray()[1]);
      assertTrue(value.asObjectArray() instanceof Integer[]);
      assertEquals(2, value.asObjectArray().length);
      assertEquals(null, value.asObjectArray()[0]);
      assertEquals(new Integer(1), value.asObjectArray()[1]);
   }

   public void testClone() throws Exception
   {
      StringValue a = new StringValue("abc");
      Object b = a.clone();
      assertTrue(b instanceof StringValue);
      StringValue c = (StringValue)b;
      assertEquals(a, c);

      //
      StringValue d = new StringValue(new String[]{"abc","def"});
      Object e = d.clone();
      assertTrue(e instanceof StringValue);
      StringValue f = (StringValue)e;
      assertEquals(d, f);
   }

   public void testStringValueConstructor()
   {
      ExtendedAssert.assertEquals(new String[0], new StringValue(new ArrayList<String>()).asStringArray());
      ExtendedAssert.assertEquals(new String[]{null}, new StringValue(Tools.toList((String)null)).asStringArray());
      ExtendedAssert.assertEquals(new String[]{"foo"}, new StringValue(Tools.toList("foo")).asStringArray());
      ExtendedAssert.assertEquals(new String[]{"foo","bar"}, new StringValue(Tools.toList("foo","bar")).asStringArray());
      ExtendedAssert.assertEquals(new String[]{"foo",null}, new StringValue(Tools.toList("foo",null)).asStringArray());
      ExtendedAssert.assertEquals(new String[]{null,"bar"}, new StringValue(Tools.toList(null,"bar")).asStringArray());
   }

   public void testEqualsOnStringValues()
   {
      assertEquals(new StringValue(), new StringValue());
      assertEquals(new StringValue(), new StringValue((String)null));
      assertEquals(new StringValue(), new StringValue(new String[0]));
      assertEquals(new StringValue(), new StringValue(new String[]{null}));
      assertEquals(new StringValue((String)null), new StringValue());
      assertEquals(new StringValue((String)null), new StringValue((String)null));
      assertEquals(new StringValue((String)null), new StringValue(new String[0]));
      assertEquals(new StringValue((String)null), new StringValue(new String[]{null}));
      assertEquals(new StringValue(new String[0]), new StringValue());
      assertEquals(new StringValue(new String[0]), new StringValue((String)null));
      assertEquals(new StringValue(new String[0]), new StringValue(new String[0]));
      assertEquals(new StringValue(new String[0]), new StringValue(new String[]{null}));
      assertEquals(new StringValue(new String[]{null}), new StringValue());
      assertEquals(new StringValue(new String[]{null}), new StringValue((String)null));
      assertEquals(new StringValue(new String[]{null}), new StringValue(new String[0]));
      assertEquals(new StringValue(new String[]{null}), new StringValue(new String[]{null}));
   }

   public void testUnmodifiable()
   {
      StringValue a = new StringValue("abc");
      String[] b = (String[])a.asObjectArray();
      b[0] = "def";
      assertEquals("abc", a.asString());
   }
}
