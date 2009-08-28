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
package org.jboss.portal.test.common.i18n;

import junit.framework.TestCase;

import java.util.Locale;
import java.util.NoSuchElementException;

import org.gatein.common.i18n.BundleName;

/**
 * @author <a href="mailto:julien@jboss.org">Julien Viet</a>
 * @version $Revision: 1.1 $
 */
public class BundleNameIteratorTestCase extends TestCase
{

   private final String A = new Locale("a", "b", "c").getLanguage();
   private final String B = new Locale("a", "b", "c").getCountry();
   private final String C = new Locale("a", "b", "c").getVariant();

   public void testNameLookup1()
   {
      Locale l = new Locale("a", "b", "c");
      BundleName.Iterator iterator = new BundleName.Iterator("base", l);
      assertTrue(iterator.hasNext());
      assertEquals("base_" + A + "_" + B + "_" + C, iterator.next().toString());
      assertTrue(iterator.hasNext());
      assertEquals("base_" + A + "_" + B, iterator.next().toString());
      assertTrue(iterator.hasNext());
      assertEquals("base_" + A, iterator.next().toString());
      assertTrue(iterator.hasNext());
      assertEquals("base", iterator.next().toString());
      assertFalse(iterator.hasNext());
      try
      {
         iterator.next();
         fail("Was expecting NoSuchElementException");
      }
      catch (NoSuchElementException expected)
      {
      }
   }

   public void testNameLookup2()
   {
      Locale l = new Locale("a", "b");
      BundleName.Iterator iterator = new BundleName.Iterator("base", l);
      assertTrue(iterator.hasNext());
      assertEquals("base_" + A + "_" + B, iterator.next().toString());
      assertTrue(iterator.hasNext());
      assertEquals("base_" + A, iterator.next().toString());
      assertTrue(iterator.hasNext());
      assertEquals("base", iterator.next().toString());
      assertFalse(iterator.hasNext());
      try
      {
         iterator.next();
         fail("Was expecting NoSuchElementException");
      }
      catch (NoSuchElementException expected)
      {
      }
   }

   public void testNameLookup4()
   {
      Locale l = new Locale("a", "b", "");
      BundleName.Iterator iterator = new BundleName.Iterator("base", l);
      assertTrue(iterator.hasNext());
      assertEquals("base_" + A + "_" + B, iterator.next().toString());
      assertTrue(iterator.hasNext());
      assertEquals("base_" + A, iterator.next().toString());
      assertTrue(iterator.hasNext());
      assertEquals("base", iterator.next().toString());
      assertFalse(iterator.hasNext());
      try
      {
         iterator.next();
         fail("Was expecting NoSuchElementException");
      }
      catch (NoSuchElementException expected)
      {
      }
   }

   public void testNameLookup3()
   {
      Locale l = new Locale("a");
      BundleName.Iterator iterator = new BundleName.Iterator("base", l);
      assertTrue(iterator.hasNext());
      assertEquals("base_" + A, iterator.next().toString());
      assertTrue(iterator.hasNext());
      assertEquals("base", iterator.next().toString());
      assertFalse(iterator.hasNext());
      try
      {
         iterator.next();
         fail("Was expecting NoSuchElementException");
      }
      catch (NoSuchElementException expected)
      {
      }
   }

   public void testNameLookup5()
   {
      Locale l = new Locale("a", "", "");
      BundleName.Iterator iterator = new BundleName.Iterator("base", l);
      assertTrue(iterator.hasNext());
      assertEquals("base_" + A, iterator.next().toString());
      assertTrue(iterator.hasNext());
      assertEquals("base", iterator.next().toString());
      assertFalse(iterator.hasNext());
      try
      {
         iterator.next();
         fail("Was expecting NoSuchElementException");
      }
      catch (NoSuchElementException expected)
      {
      }
   }

   public void testNameLookup6()
   {
      Locale l = new Locale("a", "", "c");
      BundleName.Iterator iterator = new BundleName.Iterator("base", l);
      assertTrue(iterator.hasNext());
      assertEquals("base_" + A + "__" + C, iterator.next().toString());
      assertTrue(iterator.hasNext());
      assertEquals("base_" + A, iterator.next().toString());
      assertTrue(iterator.hasNext());
      assertEquals("base", iterator.next().toString());
      assertFalse(iterator.hasNext());
      try
      {
         iterator.next();
         fail("Was expecting NoSuchElementException");
      }
      catch (NoSuchElementException expected)
      {
      }
   }

   public void testNameLookup7()
   {
      Locale l = new Locale("", "b", "c");
      BundleName.Iterator iterator = new BundleName.Iterator("base", l);
      assertTrue(iterator.hasNext());
      assertEquals("base__" + B + "_" + C, iterator.next().toString());
      assertTrue(iterator.hasNext());
      assertEquals("base__" + B, iterator.next().toString());
      assertTrue(iterator.hasNext());
      assertEquals("base", iterator.next().toString());
      assertFalse(iterator.hasNext());
      try
      {
         iterator.next();
         fail("Was expecting NoSuchElementException");
      }
      catch (NoSuchElementException expected)
      {
      }
   }

   public void testNameLookup8()
   {
      Locale l = new Locale("", "", "c");
      BundleName.Iterator iterator = new BundleName.Iterator("base", l);
      assertTrue(iterator.hasNext());
      assertEquals("base", iterator.next().toString());
      assertTrue(iterator.hasNext());
      assertEquals("base", iterator.next().toString());
      assertFalse(iterator.hasNext());
      try
      {
         iterator.next();
         fail("Was expecting NoSuchElementException");
      }
      catch (NoSuchElementException expected)
      {
      }
   }
}
