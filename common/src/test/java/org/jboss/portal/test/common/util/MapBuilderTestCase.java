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
import org.gatein.common.util.MapBuilder;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;

/**
 * @author <a href="mailto:julien@jboss.org">Julien Viet</a>
 * @version $Revision: 1.1 $
 */
public class MapBuilderTestCase extends TestCase
{

   public void testHashMap1()
   {
      assertEquals(new HashMap(), MapBuilder.hashMap().get());
   }

   public void testHashMap2()
   {
      HashMap<String, String> expected = new HashMap<String, String>();
      expected.put("a", "b");
      assertEquals(expected, MapBuilder.hashMap("a", "b").get());
   }

   public void testHashMap3()
   {
      HashMap<String, String> expected = new HashMap<String, String>();
      expected.put("a", "b");
      expected.put("c", "d");
      assertEquals(expected, MapBuilder.hashMap("a", "b").put("c", "d").get());
   }

   public void testHashMap4()
   {
      HashMap<String, String> expected = new HashMap<String, String>();
      expected.put("a", "d");
      expected.put("c", "d");
      assertEquals(expected, MapBuilder.hashMap("a", "b").put("c", "d").put("a", "d").get());
   }

   public void testLinkedHashMap1()
   {
      assertEquals(new LinkedHashMap(), MapBuilder.linkedHashMap().get());
   }

   public void testLinkedHashMap2()
   {
      LinkedHashMap<String, String> expected = new LinkedHashMap<String, String>();
      expected.put("a", "b");
      assertEquals(expected, MapBuilder.linkedHashMap("a", "b").get());
   }

   public void testLinkedHashMap3()
   {
      LinkedHashMap<String, String> expected = new LinkedHashMap<String, String>();
      expected.put("a", "b");
      expected.put("c", "d");
      assertEquals(expected, MapBuilder.linkedHashMap("a", "b").put("c", "d").get());
   }

   public void testLinkedHashMap4()
   {
      LinkedHashMap<String, String> expected = new LinkedHashMap<String, String>();
      expected.put("a", "d");
      expected.put("c", "d");
      assertEquals(expected, MapBuilder.linkedHashMap("a", "b").put("c", "d").put("a", "d").get());
   }

   public void testTreeMap1()
   {
      assertEquals(new TreeMap(), MapBuilder.treeMap().get());
   }

   public void testTreeMap2()
   {
      TreeMap<String, String> expected = new TreeMap<String, String>();
      expected.put("a", "b");
      assertEquals(expected, MapBuilder.treeMap("a", "b").get());
   }

   public void testTreeMap3()
   {
      TreeMap<String, String> expected = new TreeMap<String, String>();
      expected.put("a", "b");
      expected.put("c", "d");
      assertEquals(expected, MapBuilder.treeMap("a", "b").put("c", "d").get());
   }

   public void testTreeMap4()
   {
      TreeMap<String, String> expected = new TreeMap<String, String>();
      expected.put("a", "d");
      expected.put("c", "d");
      assertEquals(expected, MapBuilder.treeMap("a", "b").put("c", "d").put("a", "d").get());
   }

   public void testMap1()
   {
      assertEquals(new HashMap(), MapBuilder.create(new HashMap<String, String>()).get());
   }

   public void testMap2()
   {
      Map<String, String> expected = new HashMap<String, String>();
      expected.put("a", "b");
      assertEquals(expected, MapBuilder.create(new HashMap<String, String>()).put("a", "b").get());
   }

   public void testMap3()
   {
      Map<String, String> expected = new HashMap<String, String>();
      expected.put("a", "b");
      expected.put("c", "d");
      assertEquals(expected, MapBuilder.create(new HashMap<String, String>()).put("a", "b").put("c", "d").get());
   }

   public void testMap4()
   {
      Map<String, String> expected = new HashMap<String, String>();
      expected.put("a", "d");
      expected.put("c", "d");
      assertEquals(expected, MapBuilder.create(new HashMap<String, String>()).put("a", "b").put("c", "d").put("a", "d").get());
   }
}
