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
import org.gatein.common.util.CollectionBuilder;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

/**
 * @author <a href="mailto:julien@jboss.org">Julien Viet</a>
 * @version $Revision: 1.1 $
 */
public class CollectionBuilderTestCase extends TestCase
{

   public void testHashSet1()
   {
      HashSet expected = new HashSet();
      assertEquals(expected, CollectionBuilder.hashSet().get());
   }

   public void testHashSet2()
   {
      HashSet<String> expected = new HashSet<String>();
      expected.add("a");
      assertEquals(expected, CollectionBuilder.hashSet("a").get());
   }

   public void testHashSet3()
   {
      HashSet<String> expected = new HashSet<String>();
      expected.add("a");
      expected.add("b");
      assertEquals(expected, CollectionBuilder.hashSet("a").add("b").get());
   }

   public void testHashSet4()
   {
      HashSet<String> expected = new HashSet<String>();
      expected.add("a");
      expected.add("b");
      assertEquals(expected, CollectionBuilder.hashSet("a").add("b").add("a").get());
   }

   public void testArrayList1()
   {
      ArrayList expected = new ArrayList();
      assertEquals(expected, CollectionBuilder.arrayList().get());
   }

   public void testArrayList2()
   {
      ArrayList<String> expected = new ArrayList<String>();
      expected.add("a");
      assertEquals(expected, CollectionBuilder.arrayList("a").get());
   }

   public void testArrayList3()
   {
      ArrayList<String> expected = new ArrayList<String>();
      expected.add("a");
      expected.add("b");
      assertEquals(expected, CollectionBuilder.arrayList("a").add("b").get());
   }

   public void testArrayList4()
   {
      ArrayList<String> expected = new ArrayList<String>();
      expected.add("a");
      expected.add("b");
      expected.add("a");
      assertEquals(expected, CollectionBuilder.arrayList("a").add("b").add("a").get());
   }

   public void testLinkedList1()
   {
      LinkedList expected = new LinkedList();
      assertEquals(expected, CollectionBuilder.linkedList().get());
   }

   public void testLinkedList2()
   {
      LinkedList<String> expected = new LinkedList<String>();
      expected.add("a");
      assertEquals(expected, CollectionBuilder.linkedList("a").get());
   }

   public void testLinkedList3()
   {
      LinkedList<String> expected = new LinkedList<String>();
      expected.add("a");
      expected.add("b");
      assertEquals(expected, CollectionBuilder.linkedList("a").add("b").get());
   }

   public void testLinkedList4()
   {
      LinkedList<String> expected = new LinkedList<String>();
      expected.add("a");
      expected.add("b");
      expected.add("a");
      assertEquals(expected, CollectionBuilder.linkedList("a").add("b").add("a").get());
   }

   public void testSet1()
   {
      Set<String> expected = new HashSet<String>();
      assertEquals(expected, CollectionBuilder.create(new HashSet<String>()).get());
   }

   public void testSet2()
   {
      Set<String> expected = new HashSet<String>();
      expected.add("a");
      assertEquals(expected, CollectionBuilder.create(new HashSet<String>()).add("a").get());
   }

   public void testSet3()
   {
      Set<String> expected = new HashSet<String>();
      expected.add("a");
      expected.add("b");
      assertEquals(expected, CollectionBuilder.create(new HashSet<String>()).add("a").add("b").get());
   }

   public void testSet4()
   {
      Set<String> expected = new HashSet<String>();
      expected.add("a");
      expected.add("b");
      expected.add("a");
      assertEquals(expected, CollectionBuilder.create(new HashSet<String>()).add("a").add("b").add("a").get());
   }
}
