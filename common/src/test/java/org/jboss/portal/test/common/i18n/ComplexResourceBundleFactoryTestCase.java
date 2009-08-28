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
import java.util.ResourceBundle;

import org.gatein.common.i18n.ComplexResourceBundleFactory;

/**
 * @author <a href="mailto:julien@jboss.org">Julien Viet</a>
 * @version $Revision: 1.1 $
 */
public class ComplexResourceBundleFactoryTestCase extends TestCase
{

   public void testExactMatch() throws Exception
   {
      Locale.setDefault(new Locale("ja"));

      //
      ComplexResourceBundleFactory factory = new ComplexResourceBundleFactory(BundleClassLoader.assertExists(), "a");

      //
      ResourceBundle a_de_DE = factory.getBundle(new Locale("de", "DE"));
      assertNotNull(a_de_DE);
      assertEquals("a_de_DE", a_de_DE.getString("value"));

      //
      ResourceBundle a_fr = factory.getBundle(new Locale("fr"));
      assertNotNull(a_fr);
      assertEquals("a_fr", a_fr.getString("value"));

      //
      ResourceBundle a_fr_FR = factory.getBundle(new Locale("fr", "FR"));
      assertNotNull(a_fr_FR);
      assertEquals("a_fr_FR", a_fr_FR.getString("value"));

      //
      ResourceBundle a_it = factory.getBundle(new Locale("it"));
      assertNotNull(a_it);
      assertEquals("a_it", a_it.getString("value"));
   }

   public void testNoMatch() throws Exception
   {
      Locale.setDefault(new Locale("ja"));

      //
      ComplexResourceBundleFactory factory = new ComplexResourceBundleFactory(BundleClassLoader.assertExists(), "a");

      //
      ResourceBundle a_de = factory.getBundle(new Locale("de"));
      assertNull(a_de);

      //
      ResourceBundle a_en = factory.getBundle(new Locale("en"));
      assertNull(a_en);

      //
      ResourceBundle a_en_EN = factory.getBundle(new Locale("en", "EN"));
      assertNull(a_en_EN);
   }

   public void testFallbackOnDefaultLocale1() throws Exception
   {
      ComplexResourceBundleFactory factory = new ComplexResourceBundleFactory(BundleClassLoader.assertExists(), "b");

      //
      Locale.setDefault(new Locale("fr"));
      ResourceBundle de = factory.getBundle(new Locale("de"));
      assertNotNull(de);
      assertEquals("b", de.getString("value"));
      ResourceBundle fr = factory.getBundle(new Locale("fr"));
      assertNotNull(fr);
      assertEquals("b", fr.getString("value"));
      ResourceBundle fr_FR = factory.getBundle(new Locale("fr", "FR"));
      assertNotNull(fr_FR);
      assertEquals("b", fr_FR.getString("value"));

      //
      Locale.setDefault(new Locale("fr", "FR"));
      de = factory.getBundle(new Locale("de"));
      assertNotNull(de);
      assertEquals("b", de.getString("value"));
      fr = factory.getBundle(new Locale("fr"));
      assertNotNull(fr);
      assertEquals("b", fr.getString("value"));
      fr_FR = factory.getBundle(new Locale("fr", "FR"));
      assertNotNull(fr_FR);
      assertEquals("b", fr_FR.getString("value"));
   }

   public void testFallbackOnDefaultLocale2() throws Exception
   {
      ComplexResourceBundleFactory factory = new ComplexResourceBundleFactory(BundleClassLoader.assertExists(), "c");

      //
      Locale.setDefault(new Locale("fr"));
      ResourceBundle de = factory.getBundle(new Locale("de"));
      assertNotNull(de);
      assertEquals("c_fr", de.getString("value"));
      ResourceBundle fr = factory.getBundle(new Locale("fr"));
      assertNotNull(fr);
      assertEquals("c_fr", fr.getString("value"));
      ResourceBundle fr_FR = factory.getBundle(new Locale("fr", "FR"));
      assertNotNull(fr_FR);
      assertEquals("c_fr", fr_FR.getString("value"));

      //
      Locale.setDefault(new Locale("fr", "FR"));
      de = factory.getBundle(new Locale("de"));
      assertNotNull(de);
      assertEquals("c_fr", de.getString("value"));
      fr = factory.getBundle(new Locale("fr"));
      assertNotNull(fr);
      assertEquals("c_fr", fr.getString("value"));
      fr_FR = factory.getBundle(new Locale("fr", "FR"));
      assertNotNull(fr_FR);
      assertEquals("c_fr", fr_FR.getString("value"));
   }

   public void testFallbackOnDefaultLocale3() throws Exception
   {
      ComplexResourceBundleFactory factory = new ComplexResourceBundleFactory(BundleClassLoader.assertExists(), "d");

      //
      Locale.setDefault(new Locale("fr"));
      ResourceBundle de = factory.getBundle(new Locale("de"));
      assertNull(de);
      ResourceBundle fr = factory.getBundle(new Locale("fr"));
      assertNull(fr);
      ResourceBundle fr_FR = factory.getBundle(new Locale("fr", "FR"));
      assertNotNull(fr_FR);
      assertEquals("d_fr_FR", fr_FR.getString("value"));

      //
      Locale.setDefault(new Locale("fr", "FR"));
      de = factory.getBundle(new Locale("de"));
      assertNotNull(de);
      assertEquals("d_fr_FR", de.getString("value"));
      fr = factory.getBundle(new Locale("fr"));
      assertNotNull(fr);
      assertEquals("d_fr_FR", fr.getString("value"));
      fr_FR = factory.getBundle(new Locale("fr", "FR"));
      assertNotNull(fr_FR);
      assertEquals("d_fr_FR", fr_FR.getString("value"));
   }

   public void testFallbackOnDefaultLocale4() throws Exception
   {
      ComplexResourceBundleFactory factory = new ComplexResourceBundleFactory(BundleClassLoader.assertExists(), "e");

      //
      Locale.setDefault(new Locale("fr"));
      ResourceBundle de = factory.getBundle(new Locale("de"));
      assertNotNull(de);
      assertEquals("e_fr", de.getString("value"));
      ResourceBundle fr = factory.getBundle(new Locale("fr"));
      assertNotNull(fr);
      assertEquals("e_fr", fr.getString("value"));
      ResourceBundle fr_FR = factory.getBundle(new Locale("fr", "FR"));
      assertNotNull(fr_FR);
      assertEquals("e_fr", fr_FR.getString("value"));

      //
      Locale.setDefault(new Locale("fr", "FR"));
      de = factory.getBundle(new Locale("de"));
      assertNotNull(de);
      assertEquals("e_fr", de.getString("value"));
      fr = factory.getBundle(new Locale("fr"));
      assertNotNull(fr);
      assertEquals("e_fr", fr.getString("value"));
      fr_FR = factory.getBundle(new Locale("fr", "FR"));
      assertNotNull(fr_FR);
      assertEquals("e_fr", fr_FR.getString("value"));
   }

   public void testFallbackOnDefaultLocale5() throws Exception
   {
      ComplexResourceBundleFactory factory = new ComplexResourceBundleFactory(BundleClassLoader.assertExists(), "f");

      //
      Locale.setDefault(new Locale("fr"));
      ResourceBundle de = factory.getBundle(new Locale("de"));
      assertNotNull(de);
      assertEquals("f", de.getString("value"));
      ResourceBundle fr = factory.getBundle(new Locale("fr"));
      assertNotNull(fr);
      assertEquals("f", fr.getString("value"));
      ResourceBundle fr_FR = factory.getBundle(new Locale("fr", "FR"));
      assertNotNull(fr_FR);
      assertEquals("f_fr_FR", fr_FR.getString("value"));

      //
      Locale.setDefault(new Locale("fr", "FR"));
      de = factory.getBundle(new Locale("de"));
      assertNotNull(de);
      assertEquals("f_fr_FR", de.getString("value"));
      fr = factory.getBundle(new Locale("fr"));
      assertNotNull(fr);
      assertEquals("f_fr_FR", fr.getString("value"));
      fr_FR = factory.getBundle(new Locale("fr", "FR"));
      assertNotNull(fr_FR);
      assertEquals("f_fr_FR", fr_FR.getString("value"));
   }

   public void testFallbackOnDefaultLocale6() throws Exception
   {
      ComplexResourceBundleFactory factory = new ComplexResourceBundleFactory(BundleClassLoader.assertExists(), "g");

      //
      Locale.setDefault(new Locale("fr"));
      ResourceBundle de = factory.getBundle(new Locale("de"));
      assertNotNull(de);
      assertEquals("g_fr", de.getString("value"));
      ResourceBundle fr = factory.getBundle(new Locale("fr"));
      assertNotNull(fr);
      assertEquals("g_fr", fr.getString("value"));
      ResourceBundle fr_FR = factory.getBundle(new Locale("fr", "FR"));
      assertNotNull(fr_FR);
      assertEquals("g_fr_FR", fr_FR.getString("value"));

      //
      Locale.setDefault(new Locale("fr", "FR"));
      de = factory.getBundle(new Locale("de"));
      assertNotNull(de);
      assertEquals("g_fr_FR", de.getString("value"));
      fr = factory.getBundle(new Locale("fr"));
      assertNotNull(fr);
      assertEquals("g_fr", fr.getString("value"));
      fr_FR = factory.getBundle(new Locale("fr", "FR"));
      assertNotNull(fr_FR);
      assertEquals("g_fr_FR", fr_FR.getString("value"));
   }

   public void testFallbackOnDefaultLocale7() throws Exception
   {
      ComplexResourceBundleFactory factory = new ComplexResourceBundleFactory(BundleClassLoader.assertExists(), "h");

      //
      Locale.setDefault(new Locale("fr"));
      ResourceBundle de = factory.getBundle(new Locale("de"));
      assertNotNull(de);
      assertEquals("h_fr", de.getString("value"));
      ResourceBundle fr = factory.getBundle(new Locale("fr"));
      assertNotNull(fr);
      assertEquals("h_fr", fr.getString("value"));
      ResourceBundle fr_FR = factory.getBundle(new Locale("fr", "FR"));
      assertNotNull(fr_FR);
      assertEquals("h_fr_FR", fr_FR.getString("value"));

      //
      Locale.setDefault(new Locale("fr", "FR"));
      de = factory.getBundle(new Locale("de"));
      assertNotNull(de);
      assertEquals("h_fr_FR", de.getString("value"));
      fr = factory.getBundle(new Locale("fr"));
      assertNotNull(fr);
      assertEquals("h_fr", fr.getString("value"));
      fr_FR = factory.getBundle(new Locale("fr", "FR"));
      assertNotNull(fr_FR);
      assertEquals("h_fr_FR", fr_FR.getString("value"));
   }
}
