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

import org.gatein.common.text.CharBuffer;
import org.gatein.common.util.ConversionException;

import java.util.Locale;
import java.util.NoSuchElementException;
import java.io.IOException;

/**
 * The immutable name of a bundle.
 *
 * @author <a href="mailto:julien@jboss.org">Julien Viet</a>
 * @version $Revision: 7228 $
 */
public class BundleName
{

   public BundleName(String baseName)
   {
      this(baseName, "", "", "");
   }

   public BundleName(String baseName, String language)
   {
      this(baseName, language, "", "");
   }

   public BundleName(String baseName, String language, String country)
   {
      this(baseName, language, country, "");
   }

   public BundleName(String baseName, String language, String country, String variant)
   {
      if (baseName == null)
      {
         throw new IllegalArgumentException();
      }
      if (language == null)
      {
         throw new IllegalArgumentException();
      }
      if (country == null)
      {
         throw new IllegalArgumentException();
      }
      if (variant == null)
      {
         throw new IllegalArgumentException();
      }
      this.baseName = baseName;
      this.locale = new Locale(language, country, variant);
   }

   /** . */
   private final String baseName;

   /** . */
   private final Locale locale;

   public String getBaseName()
   {
      return baseName;
   }

   public Locale getLocale()
   {
      return locale;
   }

   public String getLanguage()
   {
      return locale.getLanguage();
   }

   public String getCountry()
   {
      return locale.getCountry();
   }

   public String getVariant()
   {
      return locale.getVariant();
   }

   public String toString()
   {
      if (locale.getLanguage().length() == 0 && locale.getCountry().length() == 0)
      {
         return baseName;
      }
      else
      {
         CharBuffer buffer = new CharBuffer(baseName.length() + 15);
         buffer.append(baseName);

         //
         buffer.append('_');

         //
         try
         {
            LocaleFormat.DEFAULT.write(locale, buffer);
         }
         catch (IOException e)
         {
            throw new AssertionError(e);
         }
         catch (ConversionException e)
         {
            throw new AssertionError(e);
         }

         //
         return buffer.asString();
      }
   }

   public static class Iterator implements java.util.Iterator<BundleName>
   {

      /** . */
      private BundleName name;

      /** . */
      private int status;

      public Iterator(String baseName, Locale locale)
      {
         String language = locale.getLanguage();
         String country = locale.getCountry();
         String variant = locale.getVariant();
         status = 8 + (language.length() > 0 ? 4 : 0) + (country.length() > 0 ? 2 : 0) + (variant.length() > 0 ? 1 : 0);
         switch (status & 0x7)
         {
            case 0:
               name = new BundleName(baseName);
               break;
            case 1:
               name = new BundleName(baseName, "", "", variant);
               break;
            case 2:
               name = new BundleName(baseName, "", variant, "");
               break;
            case 3:
               name = new BundleName(baseName, "", country, variant);
               break;
            case 4:
               name = new BundleName(baseName, language, "", "");
               break;
            case 5:
               name = new BundleName(baseName, language, "", variant);
               break;
            case 6:
               name = new BundleName(baseName, language, country, "");
               break;
            case 7:
               name = new BundleName(baseName, language, country, variant);
               break;
            default:
               throw new AssertionError("Should not be here");
         }
      }

      public boolean hasNext()
      {
         return status != 0;
      }

      public BundleName next()
      {
         if (status >= 8)
         {
            status -= 8;
         }
         else
         {
            switch (status)
            {
               case 0:
                  throw new NoSuchElementException();
               case 1:
                  name = new BundleName(name.getBaseName());
                  status = 0;
                  break;
               case 2:
                  name = new BundleName(name.getBaseName());
                  status = 0;
                  break;
               case 3:
                  name = new BundleName(name.getBaseName(), "", name.getCountry());
                  status = 2;
                  break;
               case 4:
                  name = new BundleName(name.getBaseName());
                  status = 0;
                  break;
               case 5:
                  name = new BundleName(name.getBaseName(), name.getLanguage());
                  status = 4;
                  break;
               case 6:
                  name = new BundleName(name.getBaseName(), name.getLanguage());
                  status = 4;
                  break;
               case 7:
                  name = new BundleName(name.getBaseName(), name.getLanguage(), name.getCountry());
                  status = 6;
                  break;
               default:
                  throw new AssertionError("Should not be here");
            }
         }
         return name;
      }

      public void remove()
      {
         throw new UnsupportedOperationException();
      }
   }

   /**
    *
    */
   public static class Parser
   {
      /**
       * @param s
       * @param from inclusive
       * @param to   exclusive
       * @return
       */
      public BundleName parse(String s, int from, int to)
      {
         if (s == null)
         {
            throw new IllegalArgumentException();
         }
         if (from < 0)
         {
            throw new IllegalArgumentException();
         }
         if (to < s.length())
         {
            throw new IllegalArgumentException();
         }
         if (to < from)
         {
            throw new IllegalArgumentException();
         }

         //
         int p1 = s.lastIndexOf('_', to - 1);
         if (p1 < from)
         {
            p1 = -1;
         }

         //
         if (p1 == -1)
         {
            // We have base name
            return new BundleName(s.substring(from, to));
         }
         else if (p1 == to - 1)
         {
            // It ends up with _ or __ or ___
            return null;
         }
         String a = s.substring(p1 + 1, to);

         //
         int p2 = s.lastIndexOf('_', p1 - 1);
         if (p2 < from)
         {
            p2 = -1;
         }

         //
         if (p2 == -1)
         {
            // We have base name + language
            return new BundleName(s.substring(from, p1), a);
         }
         String b = p2 == p1 - 1 ? "" : s.substring(p2 + 1, p1);

         //
         int p3 = s.lastIndexOf('_', p2 - 1);
         if (p3 < from)
         {
            p3 = -1;
         }

         //
         if (p3 == -1)
         {
            // We have (base name + language + country) or (base name + country)
            return new BundleName(s.substring(from, p2), b, a);
         }

         //
         String c = (p3 == p2 - 1) ? "" : s.substring(p3 + 1, p2);

         // We have (base name + variant)
         // or (base name + country + variant)
         // or (base name + language + country + variant)
         // or (base name + language + variant)
         return new BundleName(s.substring(from, p3), c, b, a);
      }
   }
}
