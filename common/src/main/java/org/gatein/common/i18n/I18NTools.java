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

import java.util.Locale;

/**
 * @author <a href="mailto:julien@jboss.org">Julien Viet</a>
 * @version $Revision: 7228 $
 */
public class I18NTools
{

   /** Separator. */
   private static final String RFC3066_SEPARATOR = "-";

   /**
    * Retrieves the language identification tag associated to the specified Locale as defined by <a
    * href="http://www.ietf.org/rfc/rfc3066.txt">IETF RFC 3066</a> limited to 2-letter language code per ISO standard
    * 639, a "-" (dash) and a 2-letter country code per ISO 3166 alpha-2 country codes. E.g. "en-US" for American
    * English, "en-GB" for British English, etc.
    *
    * @param locale the locale which language tag is wanted
    * @return a <a href="http://www.ietf.org/rfc/rfc3066.txt">IETF RFC 3066</a>-compatible language tag.
    * @throws IllegalArgumentException if the given locale is not valid
    * @since 2.4
    */
   public static String getRFC3066LanguageTagFor(Locale locale)
   {
      String country = locale.getCountry(); // country will be empty if no country was specified in the locale
      return locale.getLanguage() + ((country.length() == 2) ? RFC3066_SEPARATOR + country : country);
   }

   /**
    * Compute the trailing name for a given locale.
    *
    * @param locale the locale
    * @return the trailing name
    * @throws IllegalArgumentException if locale is null
    */
   public static String computeTrailingName(Locale locale) throws IllegalArgumentException
   {
      if (locale == null)
      {
         throw new IllegalArgumentException("locale parameter is null");
      }
      StringBuffer tmp = new StringBuffer();
      if (locale.getLanguage() != null && locale.getLanguage().length() > 0)
      {
         tmp.append('_').append(locale.getLanguage());
         if (locale.getCountry() != null && locale.getCountry().length() > 0)
         {
            tmp.append('_').append(locale.getCountry());
            {
               if (locale.getVariant() != null && locale.getVariant().length() > 0)
               {
                  tmp.append('_').append(locale.getVariant());
               }
            }
         }
      }
      return tmp.toString();
   }
}
