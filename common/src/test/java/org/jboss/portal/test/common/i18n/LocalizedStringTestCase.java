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
import org.gatein.common.i18n.LocalizedString;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * @author <a href="mailto:chris.laprun@jboss.com">Chris Laprun</a>
 * @version $Revision: 6818 $
 */
public class LocalizedStringTestCase extends TestCase
{
   private Map<Locale, String> values = new HashMap<Locale, String>();
   private LocalizedString localizedString;

   protected void setUp() throws Exception
   {
      values.put(Locale.ENGLISH, "labour");
      values.put(Locale.FRENCH, "travail");
      values.put(Locale.US, "labor");
      localizedString = new LocalizedString(values, Locale.US);
   }

   public void testLocalizedStringValuesAreNotModifiable()
   {
      try
      {
         localizedString.getValues().clear();
      }
      catch (Exception expected)
      {
         assertTrue(localizedString.hasValues());
      }
   }

   public void testPreferredOrBestLocalizedMappingFor()
   {
      try
      {
         localizedString.getPreferredOrBestLocalizedMappingFor(null);
         fail("LocalizedString get most appropriate value for should have thrown an IllegalArgumentException with a null argument");
      }
      catch (IllegalArgumentException expected)
      {
      }

      // no desired locales: should return default
      checkValueAndLocale(Locale.US, new String[]{});

      checkValueAndLocale(Locale.ENGLISH, new String[]{"en", "en-US", "aa"});
      checkValueAndLocale(Locale.US, new String[]{"en-US", "en", "aa"});
      checkValueAndLocale(Locale.FRENCH, new String[]{"fr-CA"});
   }

   private void checkValueAndLocale(Locale locale, String[] desiredLocales)
   {
      LocalizedString.Value mapping = localizedString.getPreferredOrBestLocalizedMappingFor(desiredLocales);

      Locale foundLocale = mapping.getLocale();
      assertEquals(locale, foundLocale);
      assertEquals(values.get(foundLocale), mapping.getString());
      assertEquals(mapping.getString(), localizedString.getMostAppropriateValueFor(desiredLocales));
   }
}
