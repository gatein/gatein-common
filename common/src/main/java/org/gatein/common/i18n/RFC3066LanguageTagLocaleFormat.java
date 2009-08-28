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
package org.gatein.common.i18n;

import org.gatein.common.util.FormatConversionException;
import org.gatein.common.text.CharWriter;

import java.io.IOException;
import java.util.Arrays;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author <a href="mailto:julien@jboss.org">Julien Viet</a>
 * @version $Revision: 7228 $
 */
class RFC3066LanguageTagLocaleFormat extends AbstractLocaleFormat
{

   /** Separator. */
   private static final String RFC3066_SEPARATOR = "-";

   /** Valid language tag matcher (see <a href="http://www.ietf.org/rfc/rfc3066.txt">IETF RFC 3066</a>). */
   private final Pattern RFC3066_COMPOUND_LANG_PATTERN = Pattern.compile("(\\p{Lower}{2})(-(\\p{Upper}{2}))?");

   /** Sorted valid ISO country codes (needed for Arrays.binarySearch). */
   private final String[] SORTED_ISO_COUNTRIES = Locale.getISOCountries();

   /** Sorted valid ISO language codes (needed for Arrays.binarySearch). */
   private final String[] SORTED_ISO_LANGUAGES = Locale.getISOLanguages();

   protected Locale internalGetLocale(String value) throws FormatConversionException
   {
      Matcher matcher = RFC3066_COMPOUND_LANG_PATTERN.matcher(value);
      if (matcher.matches())
      {
         String language = matcher.group(1);
         if (Arrays.binarySearch(SORTED_ISO_LANGUAGES, language) < 0)
         {
            throw new FormatConversionException("Invalid ISO language code: " + language);
         }
         String country = matcher.group(3);
         if (country == null)
         {
            return new Locale(language);
         }
         else if (Arrays.binarySearch(SORTED_ISO_COUNTRIES, country) < 0)
         {
            throw new FormatConversionException("Invalid ISO country code: " + country);
         }
         return new Locale(language, country);
      }
      throw new FormatConversionException(value + " is not a valid compound language : accepted " +
         "format is xx-YY where xx is a valid ISO language code and YY is a valid country code. See " +
         "java.util.Locale javadoc for more info.");
   }

   /**
    * Retrieves the language identification tag associated to the specified Locale as defined by <a
    * href="http://www.ietf.org/rfc/rfc3066.txt">IETF RFC 3066</a> limited to 2-letter language code per ISO standard
    * 639, a "-" (dash) and a 2-letter country code per ISO 3166 alpha-2 country codes. E.g. "en-US" for American
    * English, "en-GB" for British English, etc.
    *
    * @param locale the locale which language tag is wanted
    * @param writer
    * @return a <a href="http://www.ietf.org/rfc/rfc3066.txt">IETF RFC 3066</a>-compatible language tag.
    * @throws IllegalArgumentException if the given locale is not valid
    * @since 2.4
    */
   protected void internalWrite(Locale locale, CharWriter writer) throws IOException
   {
      String country = locale.getCountry(); // country will be empty if no country was specified in the locale
      writer.append(locale.getLanguage());
      if (country.length() == 2)
      {
         writer.append(RFC3066_SEPARATOR);
         writer.append(country);
      }
      else
      {
         writer.append(country);
      }
   }
}
