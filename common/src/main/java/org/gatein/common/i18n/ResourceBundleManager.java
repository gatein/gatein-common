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

import org.apache.log4j.Logger;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ConcurrentHashMap;

/**
 * <p>Manage a set of resource bundles. Obtention of bundles is delegated to a ResourceBundleFactory. A bundle
 * obtained successfully is cached in order to avoid the potential expensive cost of the bundle retrieval.</p>
 * <p/>
 * <p>The manager can also be used to build LocalizedString object from the loaded bundles.</p>
 *
 * @author <a href="mailto:julien@jboss.org">Julien Viet</a>
 * @author <a href="mailto:theute@jboss.org">Thomas Heute</a>
 * @version $Revision: 7228 $
 */
public class ResourceBundleManager
{

   /** . */
   private final Logger log = Logger.getLogger(getClass());

   /** . */
   private final ConcurrentMap<Locale, BundleRef> localeBundles;

   /** . */
   private final ResourceBundle defaultBundle;

   /** . */
   private final ResourceBundleFactory resourceBundleFactory;

   /**
    * @param defaultBundle         the default bundle returned when no bundle has been obtained for the locale
    * @param resourceBundleFactory the resource bundle factory
    * @throws IllegalArgumentException IllegalArgumentException if the resource bundle factory is null
    */
   public ResourceBundleManager(ResourceBundle defaultBundle, ResourceBundleFactory resourceBundleFactory) throws IllegalArgumentException
   {
      if (resourceBundleFactory == null)
      {
         throw new IllegalArgumentException("Need a resource bundle factory");
      }
      this.localeBundles = new ConcurrentHashMap<Locale, BundleRef>();
      this.defaultBundle = defaultBundle;
      this.resourceBundleFactory = resourceBundleFactory;
   }

   /**
    * Return a localized value constructed from the various resource bundles. The supported locales of the manager are
    * used in combination with the specified key. The default value is used if no value is found for the
    * <code>Locale.ENGLISH</code>. Two successive calls to this method may not return identical results since the
    * returned <code>LocalizedString</code> is built using the bundles known by the manager.
    *
    * @param key          the key to lookup in the bundles
    * @param defaultValue the default value
    * @return the localized string
    * @throws IllegalArgumentException if the key of the default value is null
    */
   public LocalizedString getLocalizedValue(String key, String defaultValue) throws IllegalArgumentException
   {
      if (key == null)
      {
         throw new IllegalArgumentException("No null key accepted");
      }
      if (defaultValue == null)
      {
         throw new IllegalArgumentException("No null default value accepted");
      }

      //
      Map<Locale, String> m = new HashMap<Locale, String>();
      for (Map.Entry<Locale, BundleRef> entry : localeBundles.entrySet())
      {
         try
         {
            Locale locale = entry.getKey();
            ResourceBundle container = entry.getValue().bundle;

            //
            if (container != null)
            {
               String localizedDisplayName = container.getString(key);
               m.put(locale, localizedDisplayName);
            }
         }
         catch (MissingResourceException ignore)
         {
         }
      }

      // Always need default value
      if (!m.containsKey(Locale.ENGLISH))
      {
         m.put(Locale.ENGLISH, defaultValue);
      }

      //
      return new LocalizedString(m, Locale.ENGLISH);
   }

   

   /**
    * Return a bundle for the given locale. If the complete locale (language + country + variant) does not exist then it
    * falls back to (language + country) or (language) or the default file.
    * <p/>
    * When the resource bundle object is found and was not in the global map, it put it in that map with a copy on
    * write.
    *
    * @param locale the locale we want a bundle for
    * @return a bundle for the locale or null if not suitable bundle is found.
    * @throws IllegalArgumentException if the locale is null
    */
   public ResourceBundle getResourceBundle(Locale locale) throws IllegalArgumentException
   {
      // Arg check
      if (locale == null)
      {
         throw new IllegalArgumentException("Locale cannot be null");
      }

      // Try to get the bundle if the map
      BundleRef ref = localeBundles.get(locale);
      if (ref != null)
      {
         return ref.bundle;
      }

      //
      log.debug("Want to load bundle for locale " + locale);
      ResourceBundle bundle = resourceBundleFactory.getBundle(locale);

      //
      if (bundle != null)
      {
         log.debug("Obtained bundle " + bundle + " with locale " + bundle.getLocale() + " for locale " + locale);
      }
      else
      {
         log.debug("No bundle obtained for locale " + locale + " will use default bundle " + defaultBundle + " instead");
      }

      // Cache the bundle
      localeBundles.put(locale, new BundleRef(bundle));

      //
      return bundle;
   }

   /**
    * Keeps track of what we loaded, even null.
    */
   private static class BundleRef
   {

      /** . */
      final ResourceBundle bundle;

      private BundleRef(ResourceBundle bundle)
      {
         this.bundle = bundle;
      }
   }
}
