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

import org.gatein.common.util.ConversionException;
import org.gatein.common.text.CharWriter;

import java.io.IOException;
import java.util.Locale;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author <a href="mailto:julien@jboss.org">Julien Viet</a>
 * @version $Revision: 7228 $
 */
public class CachingLocaleFormat implements LocaleFormat
{

   /** . */
   private final LocaleFormat delegate;

   /** . */
   private final ConcurrentHashMap<String, Locale> localeCache;

   /** . */
   private final ConcurrentHashMap<Locale, String> stringCache;

   /**
    * @param delegate the delegate when the cache value has not been found
    * @throws IllegalArgumentException if the delegate object provided is null
    */
   public CachingLocaleFormat(LocaleFormat delegate) throws IllegalArgumentException
   {
      if (delegate == null)
      {
         throw new IllegalArgumentException("No null delegate is possible");
      }
      this.delegate = delegate;
      this.localeCache = new ConcurrentHashMap<String, Locale>();
      this.stringCache = new ConcurrentHashMap<Locale, String>();
   }

   public Locale getLocale(String value) throws ConversionException
   {
      Locale locale = localeCache.get(value);

      //
      if (locale != null)
      {
         return locale;
      }
      else
      {
         locale = delegate.getLocale(value);
         localeCache.put(value, locale);
      }

      //
      return locale;
   }

   public String toString(Locale locale) throws ConversionException
   {
      String string = stringCache.get(locale);

      //
      if (string != null)
      {
         return string;
      }
      else
      {
         string = delegate.toString(locale);
         stringCache.put(locale, string);
      }

      //
      return string;
   }

   public void write(Locale locale, CharWriter writer) throws IOException, ConversionException
   {
      delegate.write(locale, writer);
   }
}
