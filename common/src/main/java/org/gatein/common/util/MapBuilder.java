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

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;

/**
 * An helper to build map in a simple manner.
 *
 * @author <a href="mailto:julien@jboss.org">Julien Viet</a>
 * @version $Revision: 7228 $
 */
public class MapBuilder<M extends Map<K, V>, K, V>
{

   /** . */
   private final M map;

   private MapBuilder(M map)
   {
      if (map == null)
      {
         throw new IllegalArgumentException();
      }
      this.map = map;
   }

   /**
    * Creates a new instance.
    *
    * @return a new instance
    */
   public static <K, V> MapBuilder<HashMap<K, V>, K, V> hashMap()
   {
      return new MapBuilder<HashMap<K, V>, K, V>(new HashMap<K, V>());
   }

   /**
    * Creates a new instance.
    *
    * @return a new instance
    */
   public static <K, V> MapBuilder<HashMap<K, V>, K, V> hashMap(K k, V v)
   {
      MapBuilder<HashMap<K, V>, K, V> builder = new MapBuilder<HashMap<K, V>, K, V>(new HashMap<K, V>());
      builder.put(k, v);
      return builder;
   }

   /**
    * Creates a new instance.
    *
    * @return a new instance
    */
   public static <K, V> MapBuilder<TreeMap<K, V>, K, V> treeMap()
   {
      return new MapBuilder<TreeMap<K, V>, K, V>(new TreeMap<K, V>());
   }

   /**
    * Creates a new instance.
    *
    * @return a new instance
    */
   public static <K, V> MapBuilder<TreeMap<K, V>, K, V> treeMap(K k, V v)
   {
      MapBuilder<TreeMap<K, V>, K, V> builder = new MapBuilder<TreeMap<K, V>, K, V>(new TreeMap<K, V>());
      builder.put(k, v);
      return builder;
   }

   /**
    * Creates a new instance.
    *
    * @return a new instance
    */
   public static <K, V> MapBuilder<LinkedHashMap<K, V>, K, V> linkedHashMap()
   {
      return new MapBuilder<LinkedHashMap<K, V>, K, V>(new LinkedHashMap<K, V>());
   }

   /**
    * Creates a new instance.
    *
    * @return a new instance
    */
   public static <K, V> MapBuilder<LinkedHashMap<K, V>, K, V> linkedHashMap(K k, V v)
   {
      MapBuilder<LinkedHashMap<K, V>, K, V> builder = new MapBuilder<LinkedHashMap<K, V>, K, V>(new LinkedHashMap<K, V>());
      builder.put(k, v);
      return builder;
   }

   public static <M extends Map<K, V>, K, V> MapBuilder<M, K, V> create(M m)
   {
      return new MapBuilder<M, K, V>(m);
   }

   /**
    * Add the object to the collection.
    *
    * @param key   the key
    * @param value the value
    * @return the builder
    */
   public MapBuilder<M, K, V> put(K key, V value)
   {
      map.put(key, value);
      return this;
   }

   /**
    * Add all the objects to the collection.
    *
    * @param all the entries to add
    * @return the builder
    */
   public MapBuilder<M, K, V> putAll(M all)
   {
      map.putAll(all);
      return this;
   }

   public M get()
   {
      return map;
   }
}
