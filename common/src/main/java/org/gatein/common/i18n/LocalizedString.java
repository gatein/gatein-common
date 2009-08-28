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

import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.apache.log4j.Logger;
import org.gatein.common.util.ConversionException;

/**
 * An immutable locale sensitive object that returns strings.
 *
 * @author <a href="mailto:julien@jboss.org">Julien Viet</a>
 * @author <a href="mailto:chris.laprun@jboss.com">Chris Laprun</a>
 * @version $Revision: 7200 $
 */
public final class LocalizedString
{

   /** Shared empty string. */
   private static final String[] EMPTY_STRINGS = new String[0];

   /** The logger. */
   private static final Logger log = Logger.getLogger(LocalizedString.class);

   /** A unmodifiable map. */
   private final Map<Locale, Value> values;

   /** The default locale. */
   private final Locale defaultLocale;

   /** The cached hashCode. */
   private Integer hashCode;

   /** The cached toString. */
   private String cachedToString;

   /**
    * Convenience constructor for simple localized strings with only one value using the <code>Locale.ENGLISH</code> locale.
    *
    * @param defaultValue  the localized value using the specified default locale
    * @throws IllegalArgumentException if no default value or locale is provided
    * @since 2.6
    */
   public LocalizedString(String defaultValue) throws IllegalArgumentException
   {
      this(defaultValue, Locale.ENGLISH);
   }

   /**
    * Convenience constructor for simple localized strings with only one value using the default locale.
    *
    * @param defaultValue  the localized value using the specified default locale
    * @param defaultLocale the default locale
    * @throws IllegalArgumentException if no default value or locale is provided
    * @since 2.4
    */
   public LocalizedString(String defaultValue, Locale defaultLocale) throws IllegalArgumentException
   {
      if (defaultValue == null)
      {
         throw new IllegalArgumentException("No null default value allowed");
      }
      if (defaultLocale == null)
      {
         throw new IllegalArgumentException("No null default locale allowed");
      }

      //
      Map<Locale, Value> values = new HashMap<Locale, Value>(1);
      values.put(defaultLocale, new Value(defaultLocale, defaultValue));

      //
      this.defaultLocale = defaultLocale;
      this.values = Collections.unmodifiableMap(values);
   }

   /**
    * Build an empty localized string.
    *
    * @param defaultLocale the default locale
    * @throws IllegalArgumentException if no default locale is provided
    */
   public LocalizedString(Locale defaultLocale) throws IllegalArgumentException
   {
      if (defaultLocale == null)
      {
         throw new IllegalArgumentException("No null default locale allowed");
      }

      //
      this.defaultLocale = defaultLocale;
      this.values = Collections.emptyMap();
   }

   /**
    * Build a localized string using a <Locale,String>Map object.
    *
    * @param values the <Locale,String>Map
    * @param defaultLocale the default locale
    * @throws IllegalArgumentException if one argument if null or if the map entries are different from <Locale,String>Map.Entry
    */
   public LocalizedString(Map<Locale, String> values, Locale defaultLocale) throws IllegalArgumentException
   {
      if (values == null)
      {
         throw new IllegalArgumentException("No null description map allowed");
      }
      if (defaultLocale == null)
      {
         throw new IllegalArgumentException("No null default locale allowed");
      }

      // Convert strings to value
      Map<Locale, Value> tmp = new HashMap<Locale, Value>(values.size());
      for (Map.Entry<Locale, String> entry : values.entrySet())
      {
         Locale key = entry.getKey();
         String value = entry.getValue();
         tmp.put(key, new Value(key, value));
      }

      //
      this.defaultLocale = defaultLocale;
      this.values = Collections.unmodifiableMap(tmp);
   }

   /**
    * Determines if this LocalizedString contains any values.
    *
    * @return <code>true</code> if this LocalizedString contains localized values, <code>false</code> otherwise.
    * @since 2.4
    */
   public boolean hasValues()
   {
      return !values.isEmpty();
   }

   /**
    * Return the string for the default locale.
    *
    * @return the string for the default locale
    */
   public String getDefaultString()
   {
      return getString(defaultLocale, false);
   }

   /**
    * Returns the value for the default locale.
    *
    * @return the value for the default locale
    */
   public Value getDefaultValue()
   {
      return getValue(defaultLocale, false);
   }

   /**
    * Returns a string value.
    *
    * @param locale the desired locale
    * @param resolve true if the locale must be resolved to the most appropriate
    * @return the description string or null if it is not found 
    */
   public String getString(Locale locale, boolean resolve)
   {
      Value value = getValue(locale, resolve);

      //
      if (value != null)
      {
         return value.getString();
      }
      else
      {
         return null;
      }
   }

   /**
    * Returns a localized value. The lookup operation can be done without resolution which
    * means that the locale is just used as a key during the lookup. If the lookup operation is done with
    * resolution then the different parts of the locale will be used during the operation.
    *
    * @param locale the desired locale
    * @param resolve true if the locale must be resolved to the most appropriate
    * @return the value or null if it is not found
    */
   public Value getValue(Locale locale, boolean resolve)
   {
      if (locale == null)
      {
         throw new IllegalArgumentException("No null locale accepted as argument");
      }

      // Fail fast is there aren't any values
      if (values.isEmpty())
      {
         return null;
      }

      //
      if (resolve)
      {
         Value value = values.get(locale);
         if (value == null && !locale.getVariant().equals(""))
         {
            value = values.get(new Locale(locale.getLanguage(), locale.getCountry()));
         }
         if (value == null && !locale.getCountry().equals(""))
         {
            value = values.get(new Locale(locale.getLanguage()));
         }
         if (value == null)
         {
            value = values.get(defaultLocale);
         }
         return value;
      }
      else
      {
         return values.get(locale);
      }
   }

   public Map<Locale, Value> getValues()
   {
      return values;
   }
   
   /**
    * Return the default locale of this localized string.
    *
    * @return the default locale
    */
   public Locale getDefaultLocale()
   {
      return defaultLocale;
   }

   /**
    * Retrieves the localized value most appropriate based on the given desired locales.
    *
    * @param desiredLocales an array of compound language tags (as defined by <a href="http://www.ietf.org/rfc/rfc3066.txt">IETF
    *                       RFC 3066</a>) ordered according to locale preferences.
    * @return the most appropriate localized value based on locale preferences.
    * @throws IllegalArgumentException if the array is null or one of the array string is null or invalid (see
    *                                  #getLocaleFromRFC3066LanguageTag(String))
    * @since 2.4
    */
   public String getMostAppropriateValueFor(String[] desiredLocales) throws IllegalArgumentException
   {
      Value mapping = getPreferredOrBestLocalizedMappingFor(desiredLocales);
      return mapping == null ? null : mapping.getString();
   }

   /**
    * Retrieves the Locale-String mapping most appropriate based on the given desired locales, which are ordered
    * according to locale preferences.
    *
    * @param desiredLocales an array of compound language tags (as defined by <a href="http://www.ietf.org/rfc/rfc3066.txt">IETF
    *                       RFC 3066</a>) ordered according to locale preferences.
    * @return a Map.Entry representing the most appropriate mapping between Locale and localized value, based on locale
    *         preferences.
    * @throws IllegalArgumentException if the array is null or one of the array string is null or invalid (see {@link
    *                                  LocaleFormat#RFC3066_LANGUAGE_TAG#getLocale(String)}
    * @since 2.4
    */
   public Value getPreferredOrBestLocalizedMappingFor(String[] desiredLocales) throws IllegalArgumentException
   {
      if (desiredLocales == null)
      {
         throw new IllegalArgumentException("No null desired locale array accepted");
      }

      //
      if (values.isEmpty())
      {
         return null;
      }

      //
      Value value = null;
      if (desiredLocales.length > 0)
      {
         for (int i = 0; value == null && i < desiredLocales.length; i++)
         {
            String desiredLocale = desiredLocales[i];

            //
            if (desiredLocale == null)
            {
               throw new IllegalArgumentException("Null desired locale not accepted");
            }

            //
            try
            {
               Locale locale = LocaleFormat.RFC3066_LANGUAGE_TAG.getLocale(desiredLocale);
               value = getValue(locale, true);
            }
            catch (ConversionException e)
            {
               if (log.isDebugEnabled())
               {
                  log.debug("Invalid desired locale " + desiredLocale);
               }
            }
         }

         // todo julien
         // We could have a smarter version of this method but this version requires that desiredLocales
         // are ordered by locale preference. Hence the first found is by definition the best.
      }

      //
      if (value == null)
      {
         value = getValue(defaultLocale, true);
      }

      //
      return value;
   }

   public String toString()
   {
      if (cachedToString == null)
      {
         cachedToString = "LocalizedString[value='" + getMostAppropriateValueFor(EMPTY_STRINGS) + "',defaultLocale=" + getDefaultLocale() + "]";
      }
      return cachedToString;
   }

   public boolean equals(Object o)
   {
      if (this == o)
      {
         return true;
      }
      if (o instanceof LocalizedString)
      {
         LocalizedString that = (LocalizedString)o;
         return defaultLocale.equals(that.defaultLocale) && getMostAppropriateValueFor(EMPTY_STRINGS).equals(that.getMostAppropriateValueFor(EMPTY_STRINGS));
      }
      return false;
   }

   public int hashCode()
   {
      if (hashCode == null)
      {
         hashCode = 31 * getMostAppropriateValueFor(EMPTY_STRINGS).hashCode() + defaultLocale.hashCode();
      }
      return hashCode;
   }

   /**
    * A unmodifiable localized string value.
    */
   public static class Value
   {

      /** The locale that describes the string. */
      private final Locale locale;

      /** the string value. */
      private final String string;

      /**
       * @param locale the locale
       * @param string the string
       * @throws IllegalArgumentException if any argument is null
       */
      public Value(Locale locale, String string) throws IllegalArgumentException
      {
         if (locale == null)
         {
            throw new IllegalArgumentException();
         }
         if (string == null)
         {
            throw new IllegalArgumentException();
         }
         this.locale = locale;
         this.string = string;
      }

      public Locale getLocale()
      {
         return locale;
      }

      public String getString()
      {
         return string;
      }
   }
}
