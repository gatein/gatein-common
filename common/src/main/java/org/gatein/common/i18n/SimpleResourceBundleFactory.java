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
import java.util.ResourceBundle;
import java.util.MissingResourceException;

/**
 * An implementation that delegates bundle loading to <code>ResourceBundle.getBundle(String,Locale,ClassLoader)</code>.
 *
 * @author <a href="mailto:julien@jboss.org">Julien Viet</a>
 * @version $Revision: 7228 $
 */
public class SimpleResourceBundleFactory implements ResourceBundleFactory
{

   /** The base name of the resource bundles. */
   private String baseName;

   /** The classloader to load resource from. */
   private ClassLoader classLoader;

   /**
    *
    * @param baseName the base name for loading
    * @param classLoader the classloader for loading
    * @throws IllegalArgumentException if any argument is null
    */
   public SimpleResourceBundleFactory(String baseName, ClassLoader classLoader) throws IllegalArgumentException
   {
      if (baseName == null)
      {
         throw new IllegalArgumentException("No base name provided");
      }
      if (classLoader == null)
      {
         throw new IllegalArgumentException("No classloader provided");
      }
      this.baseName = baseName;
      this.classLoader = classLoader;
   }

   public ResourceBundle getBundle(Locale locale) throws IllegalArgumentException
   {
      if (locale == null)
      {
         throw new IllegalArgumentException("No null locale accepted");
      }
      try
      {
         return ResourceBundle.getBundle(baseName, locale, classLoader);
      }
      catch (MissingResourceException e)
      {
         return null;
      }
   }
}
