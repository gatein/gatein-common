/******************************************************************************
 * JBoss, a division of Red Hat                                               *
 * Copyright 2007, Red Hat Middleware, LLC, and individual                    *
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
import org.gatein.common.junit.ExtendedAssert;
import org.gatein.common.util.Tools;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.Vector;

/**
 * @author <a href="mailto:chris.laprun@jboss.com">Chris Laprun</a>
 * @version $Revision: 6384 $
 * @since 2.6
 */
public class ToolsTestCase extends TestCase
{
   public void testIsContainedIn()
   {
      String value = "value";
      String[] array = new String[]{"foo", "bar", value, "baz"};

      assertTrue(Tools.isContainedIn(value, array));
      assertFalse(Tools.isContainedIn(null, array));
      assertFalse(Tools.isContainedIn(value, null));
      assertFalse(Tools.isContainedIn(null, null));
      assertFalse(Tools.isContainedIn("bat", array));
   }

   public void testIteratorToEnumerationThrowsIAE()
   {
      try
      {
         Tools.toEnumeration((Iterator)null);
         fail();
      }
      catch (IllegalArgumentException ignore)
      {
      }
   }

   public void testIteratorToEnumeration1()
   {
      assertEnumeration(Tools.toEnumeration(new ArrayList<String>().iterator()));
   }

   public void testIteratorToEnumeration2()
   {
      List<String> tmp = new ArrayList<String>();
      tmp.add("a");
      assertEnumeration(Tools.toEnumeration(tmp.iterator()), "a");
   }

   public void testIteratorToEnumeration3()
   {
      List<String> tmp = new ArrayList<String>();
      tmp.add("a");
      tmp.add("b");
      assertEnumeration(Tools.toEnumeration(tmp.iterator()), "a", "b");
   }

   public void testArrayToEnumerationThrowsIAE()
   {
      try
      {
         Tools.toEnumeration((Object[])null);
         fail();
      }
      catch (IllegalArgumentException ignore)
      {
      }
   }

   public void testArrayToEnumeration1()
   {
      assertEnumeration(Tools.toEnumeration(new String[]{}));
   }

   public void testArrayToEnumeration2()
   {
      assertEnumeration(Tools.toEnumeration(new String[]{"a"}), "a");
   }

   public void testArrayToEnumeration3()
   {
      assertEnumeration(Tools.toEnumeration(new String[]{"a","b"}), "a", "b");
   }

   public void testElementToEnumeration()
   {
      assertEnumeration(Tools.toEnumeration("a"), "a");
   }

   public void testEnumerationToSetThrowsIAE()
   {
      try
      {
         Tools.toSet((Enumeration)null);
         fail();
      }
      catch (IllegalArgumentException ignore)
      {
      }
   }

   public void testEnumerationToSet()
   {
      Vector<String> v = new Vector<String>();
      v.add("a");
      v.add("b");
      v.add("a");
      Set<String> expected = new HashSet<String>();
      expected.add("a");
      expected.add("b");
      assertEquals(expected, Tools.toSet(v.elements()));
   }

   public void testArrayToSetThrowsIAE()
   {
      try
      {
         Tools.toSet((Object[])null);
         fail();
      }
      catch (IllegalArgumentException ignore)
      {
      }
   }

   public void testArrayToSet()
   {
      Set<String> expected = new HashSet<String>();
      expected.add("a");
      expected.add("b");
      assertEquals(expected, Tools.toSet(new String[]{"a","b","a"}));
   }

   public void testIteratorToSetThrowsIAE()
   {
      try
      {
         Tools.toSet((Iterator)null);
         fail();
      }
      catch (IllegalArgumentException ignore)
      {
      }
   }

   public void testIteratorToSet()
   {
      List<String> list = new ArrayList<String>();
      list.add("a");
      list.add("b");
      list.add("a");
      Set<String> expected = new HashSet<String>();
      expected.add("a");
      expected.add("b");
      assertEquals(expected, Tools.toSet(list.iterator()));
   }

   public void testEnumerationToListThrowsIAE()
   {
      try
      {
         Tools.toList((Enumeration)null);
         fail();
      }
      catch (IllegalArgumentException ignore)
      {
      }
   }

   public void testEnumerationToList()
   {
      Vector<String> v = new Vector<String>();
      v.add("a");
      v.add("b");
      v.add("a");
      List<String> expected = new ArrayList<String>();
      expected.add("a");
      expected.add("b");
      expected.add("a");
      assertEquals(expected, Tools.toList(v.elements()));
   }

   public void testArrayToListThrowsIAE()
   {
      try
      {
         Tools.toList((Object[])null);
         fail();
      }
      catch (IllegalArgumentException ignore)
      {
      }
   }

   public void testArrayToList()
   {
      List<String> expected = new ArrayList<String>();
      expected.add("a");
      expected.add("b");
      expected.add("a");
      assertEquals(expected, Tools.toList(new String[]{"a","b","a"}));
   }

   public void testIteratorToListThrowsIAE()
   {
      try
      {
         Tools.toList((Iterator)null);
         fail();
      }
      catch (IllegalArgumentException ignore)
      {
      }
   }

   public void testIteratorToList()
   {
      List<String> list = new ArrayList<String>();
      list.add("a");
      list.add("b");
      list.add("a");
      List<String> expected = new ArrayList<String>();
      expected.add("a");
      expected.add("b");
      expected.add("a");
      assertEquals(expected, Tools.toList(list.iterator()));
   }


   public void testArrayIteratorThrowsIAE()
   {
      try
      {
         Tools.iterator((Object[])null);
         fail();
      }
      catch (IllegalArgumentException ignore)
      {
      }
   }

   public void testArrayIterator1()
   {
      assertIterator(Tools.iterator(new String[]{}));
   }

   public void testArrayIterator2()
   {
      assertIterator(Tools.iterator(new String[]{"a"}), "a");
   }

   public void testArrayIterator3()
   {
      assertIterator(Tools.iterator(new String[]{"a","b"}), "a", "b");
   }

   public void testElementIterator()
   {
      assertIterator(Tools.iterator("a"), "a");
   }

   public void testArrayRangeIteratorThrowsIAE()
   {
      try
      {
         Tools.iterator(null, 0, 0);
         fail();
      }
      catch (IllegalArgumentException ignore)
      {
      }
   }

   public void testArrayRangeIterator1()
   {
      assertIteratorMethodThrowIAE(new String[]{}, -1, 0);
      assertIteratorMethodThrowIAE(new String[]{}, 0, 1);
      assertIteratorMethodThrowIAE(new String[]{}, 1, 0);
      assertIterator(Tools.iterator(new String[]{}, 0, 0));
   }

   public void testArrayRangeIterator2()
   {
      assertIteratorMethodThrowIAE(new String[]{"a"}, -1, 0);
      assertIteratorMethodThrowIAE(new String[]{"a"}, 1, 0);
      assertIteratorMethodThrowIAE(new String[]{"a"}, 1, 2);
      assertIterator(Tools.iterator(new String[]{"a"}, 0, 0));
      assertIterator(Tools.iterator(new String[]{"a"}, 1, 1));
      assertIterator(Tools.iterator(new String[]{"a"}, 0, 1), "a");
   }

   public void testArrayRangeIterator3()
   {
      assertIteratorMethodThrowIAE(new String[]{"a", "b"}, -1, 0);
      assertIteratorMethodThrowIAE(new String[]{"a", "b"}, 1, 0);
      assertIteratorMethodThrowIAE(new String[]{"a", "b"}, 2, 0);
      assertIteratorMethodThrowIAE(new String[]{"a", "b"}, 2, 1);
      assertIteratorMethodThrowIAE(new String[]{"a", "b"}, 2, 3);
      assertIterator(Tools.iterator(new String[]{"a","b"}, 0, 0));
      assertIterator(Tools.iterator(new String[]{"a","b"}, 1, 1));
      assertIterator(Tools.iterator(new String[]{"a","b"}, 2, 2));
      assertIterator(Tools.iterator(new String[]{"a","b"}, 0, 1), "a");
      assertIterator(Tools.iterator(new String[]{"a","b"}, 1, 2), "b");
      assertIterator(Tools.iterator(new String[]{"a","b"}, 0, 2), "a", "b");
   }

   public void testArrayAppendToThrowsIAE()
   {
      try
      {
         Tools.appendTo(null, "a");
         fail();
      }
      catch (IllegalArgumentException e)
      {
      }
   }

   public void testEmptyEnumeration()
   {
      assertEnumeration(Tools.EMPTY_ENUMERATION);
   }

   public void testEmptyIterator()
   {
      assertIterator(Tools.EMPTY_ITERATOR);
   }

   public void testArrayAppendTo()
   {
      ExtendedAssert.assertEquals(new String[]{"a"}, Tools.appendTo(new String[0], "a"));
      ExtendedAssert.assertEquals(new String[]{null}, Tools.appendTo(new String[0], null));
      ExtendedAssert.assertEquals(new String[]{"a", "b"}, Tools.appendTo(new String[]{"a"}, "b"));
      ExtendedAssert.assertEquals(new String[]{null, "b"}, Tools.appendTo(new String[]{null}, "b"));
      ExtendedAssert.assertEquals(new String[]{"a", null}, Tools.appendTo(new String[]{"a"}, null));
   }

   private <E> void assertEnumeration(Enumeration<E> elements, E... expectedElements)
   {
      assertNotNull(elements);
      for (E expectedElement : expectedElements)
      {
         assertTrue(elements.hasMoreElements());
         E element = elements.nextElement();
         assertEquals(expectedElement, element);
      }
      assertFalse(elements.hasMoreElements());
      try
      {
         elements.nextElement();
         fail();
      }
      catch (NoSuchElementException expected)
      {
      }
   }

   private void assertIteratorMethodThrowIAE(Object[] array, int from, int to)
   {
      try
      {
         Tools.iterator(array, from, to);
         fail();
      }
      catch (IllegalArgumentException ignore)
      {
      }
   }

   private <E> void assertIterator(Iterator<E> elements, E... expectedElements)
   {
      assertNotNull(elements);
      for (E expectedElement : expectedElements)
      {
         assertTrue(elements.hasNext());
         E element = elements.next();
         assertEquals(expectedElement, element);
      }
      assertFalse(elements.hasNext());
      try
      {
         elements.next();
         fail();
      }
      catch (NoSuchElementException expected)
      {
      }
   }
}
