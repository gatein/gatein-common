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

import org.gatein.common.util.Tools;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * This resource bundle takes two resource bundle to make one :
 * - The locale of this resource bundle is given by the child.
 * - For a given key present in the child and the parent, the child
 *   value overrides the parent value.
 *
 * The locale used for the bundle is the child locale.
 *
 * @author <a href="mailto:julien@jboss.org">Julien Viet</a>
 * @author <a href="mailto:theute@jboss.org">Thomas Heute</a>
 * @version $Revision: 6818 $
 */
public class ParentChildResourceBundle extends ResourceBundle
{

   /** The bundle locale. */
   private Locale locale;

   /** The bundle values. */
   private Map values;

   /**
    * Construct a new resource bundle whose content is based on the child
    * and parent content.
    *
    * @param parent the parent eventually null
    * @param child the child
    * @throws IllegalArgumentException if the child is null or its locale is null
    */
   public ParentChildResourceBundle(ResourceBundle parent, ResourceBundle child) throws IllegalArgumentException
   {
      // Arg check
      if (child == null)
      {
         throw new IllegalArgumentException("Child cannot be null");
      }
      if (child.getLocale() == null)
      {
         throw new IllegalArgumentException("Child locale must not be null");
      }
      this.locale = child.getLocale();
      this.values = new HashMap();

      // Set the parent content
      if (parent != null)
      {
         for (Enumeration e = parent.getKeys();e.hasMoreElements();)
         {
            String key = (String)e.nextElement();
            Object value = parent.getObject(key);
            values.put(key, value);
         }
      }

      // Set the child content
      for (Enumeration e = child.getKeys();e.hasMoreElements();)
      {
         String key = (String)e.nextElement();
         Object value = child.getObject(key);
         values.put(key, value);
      }
   }

   public Locale getLocale()
   {
      return locale;
   }

   protected Object handleGetObject(String key)
   {
      return values.get(key);
   }

   public Enumeration getKeys()
   {
      return Tools.toEnumeration(values.keySet().iterator());
   }
}
