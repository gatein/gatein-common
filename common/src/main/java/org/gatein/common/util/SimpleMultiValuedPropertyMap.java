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
package org.gatein.common.util;

import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.Collections;
import java.util.LinkedList;

/**
 * A map of multi valued properties.
 *
 * @author <a href="mailto:julien@jboss.org">Julien Viet</a>
 * @version $Revision: 630 $
 */
public class SimpleMultiValuedPropertyMap<T> implements MultiValuedPropertyMap<T>
{

   /** . */
   private Map<String, LinkedList<T>> content;

   /**
    * @param key the property key
    * @param value the property value
    * @throws IllegalArgumentException if name or value is null
    */
   public void addValue(String key, T value)
   {
      if (key == null)
      {
         throw new IllegalArgumentException("Name cannot be null");
      }
      if (value == null)
      {
         throw new IllegalArgumentException("Value cannot be null");
      }
      if (content == null)
      {
         content = new HashMap<String, LinkedList<T>>();
      }

      //
      LinkedList<T> values = content.get(key);
      if (values == null)
      {
         values = new LinkedList<T>();
         content.put(key, values);
      }

      //
      values.add(value);
   }

   /**
    * @param key  the property key
    * @param value the property value
    * @throws IllegalArgumentException if name or value is null
    */
   public void setValue(String key, T value)
   {
      if (key == null)
      {
         throw new IllegalArgumentException("Name cannot be null");
      }
      if (value == null)
      {
         throw new IllegalArgumentException("Value cannot be null");
      }
      if (content == null)
      {
         content = new HashMap<String, LinkedList<T>>();
      }

      //
      LinkedList<T> values = content.get(key);
      if (values == null)
      {
         values = new LinkedList<T>();
         content.put(key, values);
      }
      else
      {
         values.clear();
      }

      //
      values.add(value);
   }

   /**
    * Clear the properties.
    */
   public void clear()
   {
      if (content != null)
      {
         content.clear();
      }
   }

   /**
    * Returns the first property value or null if it cannot be found.
    *
    * @param key the property key
    * @return the property value
    * @throws IllegalArgumentException if the name argument is null
    */
   public T getValue(String key) throws IllegalArgumentException
   {
      if (key == null)
      {
         throw new IllegalArgumentException("Name cannot be null");
      }
      if (content == null)
      {
         return null;
      }

      //
      LinkedList<T> values = content.get(key);
      if (values == null)
      {
         return null;
      }
      else
      {
         return values.get(0);
      }
   }

   /**
    * Returns the list of values for a specified property.
    *
    * @param key the property key
    * @return the list of properties for the specified name or null if it does not exist
    * @throws IllegalArgumentException if name argument is null
    */
   public List<T> getValues(String key)
   {
      if (key == null)
      {
         throw new IllegalArgumentException("Name cannot be null");
      }
      if (content == null)
      {
         return null;
      }
      return content.get(key);
   }

   /**
    * Returns the set of property keys.
    *
    * @return the set of property keys
    */
   public Set<String> keySet()
   {
      if (content == null)
      {
         return Collections.emptySet();
      }
      return content.keySet();
   }

   public int size()
   {
      if (content == null)
      {
         return 0;
      }
      return content.size();
   }

   /**
    * Append the multi valued property map to this one. Entries from the provided map that do not exist
    * in the current map are created, entries that exist in both maps are concatenated in the existing map
    * with the values of the existing map being before the values of the provided map.
    *
    * @param appended the property map to append
    * @throws IllegalArgumentException if the provided map is null
    */
   public void append(MultiValuedPropertyMap<T> appended) throws IllegalArgumentException
   {
      if (appended == null)
      {
         throw new IllegalArgumentException();
      }

      //
      if (appended instanceof SimpleMultiValuedPropertyMap)
      {
         Map<String, LinkedList<T>> appendedContent = ((SimpleMultiValuedPropertyMap<T>)appended).content;

         //
         if (appendedContent != null)
         {
            for (Map.Entry<String, LinkedList<T>> entry : appendedContent.entrySet())
            {
               String name = entry.getKey();

               //
               if (content == null)
               {
                  content = new HashMap<String, LinkedList<T>>(appendedContent.size());
               }

               //
               LinkedList<T> values = content.get(name);

               //
               if (values != null)
               {
                  values.addAll(entry.getValue());
               }
               else
               {
                  content.put(name, new LinkedList<T>(entry.getValue()));
               }
            }
         }
      }
      else
      {
         for (String key : appended.keySet())
         {
            List<T> values = appended.getValues(key);

            //
            if (content == null)
            {
               content = new HashMap<String, LinkedList<T>>(appended.size());
            }

            //
            if (values != null)
            {
               values.addAll(values);
            }
            else
            {
               content.put(key, new LinkedList<T>(values));
            }
         }
      }
   }
}
