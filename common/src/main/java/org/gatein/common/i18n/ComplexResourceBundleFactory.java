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

import org.gatein.common.io.IOTools;

import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

/**
 * @author <a href="mailto:julien@jboss.org">Julien Viet</a>
 * @version $Revision: 7374 $
 */
public class ComplexResourceBundleFactory implements ResourceBundleFactory
{

   /** . */
   private final ClassLoader resourceLoader;

   /** . */
   private final String baseName;

   public ComplexResourceBundleFactory(ClassLoader resourceLoader, String baseName)
   {
      this.resourceLoader = resourceLoader;
      this.baseName = baseName;
   }

   public ResourceBundle getBundle(Locale locale) throws IllegalArgumentException
   {
      if (locale == null)
      {
         throw new IllegalArgumentException();
      }

      //
      for (BundleName.Iterator iterator = new BundleName.Iterator(baseName, locale); iterator.hasNext();)
      {
         BundleName name = iterator.next();

         // We don't want to process the base name only with the specified locale
         // in order to respect the sequence of candidate bundle names
         if (!iterator.hasNext())
         {
            break;
         }

         //
         ResourceBundle bundle = lookup(name.toString());
         if (bundle != null)
         {
            return bundle;
         }
      }

      // Try default locale
      for (BundleName.Iterator iterator = new BundleName.Iterator(baseName, Locale.getDefault()); iterator.hasNext();)
      {
         BundleName name = iterator.next();

         //
         ResourceBundle bundle = lookup(name.toString());
         if (bundle != null)
         {
            return bundle;
         }
      }

      // Nothing was found
      return null;
   }

   protected ResourceBundle lookup(String s)
   {
      // Try to load class first
      // we don't do that for now !!!

      // Try to load bundle then
      String propertyName = s.replace('.', '/') + ".properties";
      InputStream in = resourceLoader.getResourceAsStream(propertyName);
      if (in != null)
      {
         try
         {
            in = IOTools.safeBufferedWrapper(in);
            return new PropertyResourceBundle(in);
         }
         catch (IOException e)
         {
         }
         finally
         {
            IOTools.safeClose(in);
         }
      }

      //
      return null;
   }
}
