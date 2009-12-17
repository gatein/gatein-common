/******************************************************************************
 * JBoss, a division of Red Hat                                               *
 * Copyright 2009, Red Hat Middleware, LLC, and individual                    *
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
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Implementation for the programmatic name of a locale.
 *
 * @author <a href="mailto:julien@jboss.org">Julien Viet</a>
 * @version $Revision: 7228 $
 */
class DefaultLocaleFormat extends AbstractLocaleFormat
{

   /** A copy on write cache (yeah not volatile, so what?). */
   private Map<String, Locale> CACHE = new HashMap<String, Locale>();

   /** . */
   private LocaleFactory factory;

   public DefaultLocaleFormat(LocaleFactory factory)
   {
      this.factory = factory;
   }

   public DefaultLocaleFormat()
   {
      this(LocaleFactory.DEFAULT_FACTORY);
   }

   protected Locale internalGetLocale(String value) throws FormatConversionException
   {
      Locale locale = CACHE.get(value);
      if (locale != null)
      {
         return locale;
      }

      //
      int p1 = value.lastIndexOf('_');
      if (p1 < 0)
      {
         locale = factory.createLocale(value);
      }
      else
      {
         String a = (p1 == (value.length() - 1)) ? "" : value.substring(p1 + 1, value.length());
         int p2 = value.lastIndexOf('_', p1 - 1);
         if (p2 < 0)
         {
            if (a.length() == 0)
            {
               throw new FormatConversionException();
            }
            else
            {
               locale = factory.createLocale(value.substring(0, p1), a);
            }
         }
         else
         {
            boolean emptyLanguage = p2 == p1 - 1;
            if (p2 == 0 && emptyLanguage)
            {
               throw new FormatConversionException();
            }

            //
            String b = emptyLanguage ? "" : value.substring(p2 + 1, p1);
            locale = factory.createLocale(value.substring(0, p2), b, a);
         }
      }

      //
      Map<String, Locale> copy = new HashMap<String, Locale>(CACHE);
      copy.put(locale.toString(), locale);
      CACHE = copy;

      //
      return locale;
   }

   protected void internalWrite(Locale locale, CharWriter writer) throws IOException
   {
      writer.append(locale.toString());
   }
}
