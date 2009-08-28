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
import java.util.Map;
import java.util.HashMap;
import java.io.IOException;

import org.gatein.common.i18n.LocaleFormat;
import org.gatein.common.i18n.CachingLocaleFormat;
import org.gatein.common.i18n.AbstractLocaleFormat;
import org.gatein.common.util.FormatConversionException;
import org.gatein.common.util.ConversionException;
import org.gatein.common.text.CharWriter;

/**
 * @author <a href="mailto:julien@jboss.org">Julien Viet</a>
 * @version $Revision: 1.1 $
 */
public class LocaleFormatTestCase extends TestCase
{

   public void testgetLocaleFromDefaultFormat() throws ConversionException
   {
      assertEquals(new Locale("a"), LocaleFormat.DEFAULT_NO_CACHE.getLocale("a"));
      assertEquals(new Locale("a", "b"), LocaleFormat.DEFAULT_NO_CACHE.getLocale("a_b"));
      assertEquals(new Locale("", "b"), LocaleFormat.DEFAULT_NO_CACHE.getLocale("_b"));
      assertEquals(new Locale("a", "b", "c"), LocaleFormat.DEFAULT_NO_CACHE.getLocale("a_b_c"));
      assertEquals(new Locale("a", "", "b"), LocaleFormat.DEFAULT_NO_CACHE.getLocale("a__b"));
      assertEquals(new Locale("", "a", "b"), LocaleFormat.DEFAULT_NO_CACHE.getLocale("_a_b"));
      assertEquals(new Locale(""), LocaleFormat.DEFAULT_NO_CACHE.getLocale(""));

      //
      try
      {
         assertEquals(new Locale("", "", "a"), LocaleFormat.DEFAULT.getLocale("__a"));
         fail();
      }
      catch (ConversionException expected)
      {
      }
      try
      {
         assertEquals(new Locale("", "", ""), LocaleFormat.DEFAULT_NO_CACHE.getLocale("__"));
         fail();
      }
      catch (ConversionException expected)
      {
      }
      try
      {
         LocaleFormat.DEFAULT.getLocale("_");
         fail();
      }
      catch (ConversionException expected)
      {
      }
   }

   public void testToStringFromDefaultFormat() throws ConversionException
   {
      assertEquals("a", LocaleFormat.DEFAULT_NO_CACHE.toString(new Locale("a")));
      assertEquals("", LocaleFormat.DEFAULT_NO_CACHE.toString(new Locale("")));

      //
      assertEquals("", LocaleFormat.DEFAULT_NO_CACHE.toString(new Locale("", "")));
      assertEquals("a_B", LocaleFormat.DEFAULT_NO_CACHE.toString(new Locale("a", "b")));
      assertEquals("a", LocaleFormat.DEFAULT_NO_CACHE.toString(new Locale("a", "")));
      assertEquals("_A", LocaleFormat.DEFAULT_NO_CACHE.toString(new Locale("", "a")));

      //
      assertEquals("a_B_c", LocaleFormat.DEFAULT_NO_CACHE.toString(new Locale("a", "b", "c")));
      assertEquals("_A_b", LocaleFormat.DEFAULT_NO_CACHE.toString(new Locale("", "a", "b")));
      assertEquals("a__b", LocaleFormat.DEFAULT_NO_CACHE.toString(new Locale("a", "", "b")));
      assertEquals("a_B", LocaleFormat.DEFAULT_NO_CACHE.toString(new Locale("a", "b", "")));
      assertEquals("", LocaleFormat.DEFAULT_NO_CACHE.toString(new Locale("", "", "a")));
      assertEquals("a", LocaleFormat.DEFAULT_NO_CACHE.toString(new Locale("a", "", "")));
      assertEquals("_A", LocaleFormat.DEFAULT_NO_CACHE.toString(new Locale("", "a", "")));
      assertEquals("", LocaleFormat.DEFAULT_NO_CACHE.toString(new Locale("", "", "")));
   }

   public void testGetLocaleFromRFC3066LanguageTag() throws ConversionException
   {
      assertEquals(new Locale("en"), LocaleFormat.RFC3066_LANGUAGE_TAG_NO_CACHE.getLocale("en"));
      assertEquals(new Locale("en", "US"), LocaleFormat.RFC3066_LANGUAGE_TAG_NO_CACHE.getLocale("en-US"));

      try
      {
         LocaleFormat.RFC3066_LANGUAGE_TAG_NO_CACHE.getLocale("wrong");
         fail("Should have failed since an invalid String was passed.");
      }
      catch (FormatConversionException e)
      {
         //expected
      }

      try
      {
         LocaleFormat.RFC3066_LANGUAGE_TAG_NO_CACHE.getLocale("zz");
         fail("Should have failed since an invalid language code was passed.");
      }
      catch (FormatConversionException e)
      {
         //expected
      }

      try
      {
         LocaleFormat.RFC3066_LANGUAGE_TAG_NO_CACHE.getLocale("fr-ZZ");
         fail("Should have failed since an invalid country code was passed.");
      }
      catch (FormatConversionException e)
      {
         //expected
      }

      // weird combination should work as well
      LocaleFormat.RFC3066_LANGUAGE_TAG_NO_CACHE.getLocale("fr-US");
   }

   public void testGetRFC3066LanguageTagFromLocale() throws ConversionException
   {
      assertEquals("en", LocaleFormat.RFC3066_LANGUAGE_TAG_NO_CACHE.toString(new Locale("en")));
      assertEquals("en-US", LocaleFormat.RFC3066_LANGUAGE_TAG_NO_CACHE.toString(new Locale("en", "US")));
   }

   public void testCachingLocaleFormat() throws ConversionException
   {
      TestLocaleFormat delegate = new TestLocaleFormat();
      delegate.put(new Locale("abc"), "abc");
      CachingLocaleFormat format = new CachingLocaleFormat(delegate);

      //
      assertEquals(new Locale("abc"), format.getLocale("abc"));
      assertEquals("abc", format.toString(new Locale("abc")));

      //
      try
      {
         format.getLocale("def");
         fail();
      }
      catch (ConversionException e)
      {
      }

      //
      try
      {
         format.toString(new Locale("def"));
         fail();
      }
      catch (ConversionException e)
      {
      }
   }

   private static class TestLocaleFormat extends AbstractLocaleFormat
   {

      /** . */
      private final Map localeToString = new HashMap();

      /** . */
      private final Map stringToLocale = new HashMap();

      private void put(Locale locale, String string)
      {
         localeToString.put(locale, string);
         stringToLocale.put(string, locale);
      }

      protected Locale internalGetLocale(String value) throws ConversionException
      {
         Locale locale = (Locale)stringToLocale.get(value);
         if (locale == null)
         {
            throw new ConversionException();
         }
         return locale;
      }

      protected void internalWrite(Locale locale, CharWriter writer) throws IOException, ConversionException
      {
         String string = (String)localeToString.get(locale);
         if (string == null)
         {
            throw new ConversionException();
         }
         writer.append(string);
      }
   }

}
