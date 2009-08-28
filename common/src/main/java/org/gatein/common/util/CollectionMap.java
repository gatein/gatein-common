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

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * A map of collections.
 *
 * @author <a href="mailto:julien@jboss.org">Julien Viet</a>
 * @version $Revision: 1.1 $
 */
public abstract class CollectionMap<K, V> implements Serializable
{

   /** The underlying map. */
   private final Map<K, Collection<V>> map;
   /** An optional comparator. */
   protected Comparator<V> comparator;

   public CollectionMap()
   {
      map = init(null);
   }

   public CollectionMap(CollectionMap<K, V> other) throws IllegalArgumentException
   {
      if (other == null)
      {
         throw new IllegalArgumentException("Cannot copy null argument");
      }
      map = init(other);
   }

   public CollectionMap(CollectionMap<K, V> other, Comparator<V> comparator) throws IllegalArgumentException
   {
      this(other);
      initComparator(comparator);
   }

   public CollectionMap(Comparator<V> comparator)
   {
      this();

      initComparator(comparator);
   }

   private void initComparator(Comparator<V> comparator)
   {
      //
      if (comparator == null)
      {
         throw new IllegalArgumentException("No null comparator allowed");
      }

      //
      this.comparator = comparator;
   }

   /**
    * Add an object in the set keyed under the specified key.
    *
    * @throws NullPointerException if the key is null
    */
   public final void put(K key, V o) throws NullPointerException
   {
      if (key == null)
      {
         throw new NullPointerException("No null key");
      }

      //
      Collection<V> collection = map.get(key);
      if (collection == null)
      {
         collection = newCollection();
         map.put(key, collection);
      }
      add(collection, o);
   }

   /** Return the set of keys. */
   public final Set<K> keySet()
   {
      return map.keySet();
   }

   /**
    * Remove the entire set of objects specified by the key.
    *
    * @throws NullPointerException if the key is null
    */
   public final void remove(K key) throws NullPointerException
   {
      if (key == null)
      {
         throw new NullPointerException("No null key");
      }

      //
      map.remove(key);
   }

   /**
    * Remove an object in the set keyed under the specified key.
    *
    * @throws NullPointerException if the key is null
    */
   public final void remove(K key, Object o) throws NullPointerException
   {
      if (key == null)
      {
         throw new NullPointerException("No null key");
      }

      //
      Collection<V> collection = map.get(key);

      //
      if (collection != null)
      {
         remove(collection, o);

         //
         if (collection.isEmpty())
         {
            map.remove(key);
         }
      }
   }

   /**
    * Return true if the specified set contains the object o.
    *
    * @throws NullPointerException if the key is null
    */
   public final boolean contains(K key, Object o) throws NullPointerException
   {
      if (key == null)
      {
         throw new NullPointerException("No null key");
      }

      //
      Collection<V> collection = map.get(key);

      //
      if (collection == null)
      {
         return false;
      }
      else
      {
         return collection.contains(o);
      }
   }

   /** Return the collection specified by the key. */
   public Collection<V> get(K key)
   {
      return map.get(key);
   }

   /**
    * Return an iterator over the values in the set specified by the key.
    *
    * @throws NullPointerException if the key is null
    */
   public final Iterator<V> iterator(final K key)
   {
      if (key == null)
      {
         throw new NullPointerException("No null key");
      }

      //
      Collection<V> set = map.get(key);

      //
      if (set == null)
      {
         Set<V> tmp = Collections.emptySet();
         return tmp.iterator();
      }
      else
      {
         final Iterator<V> iterator = set.iterator();
         return new Iterator<V>()
         {
            public boolean hasNext()
            {
               return iterator.hasNext();
            }

            public V next()
            {
               return iterator.next();
            }

            public void remove()
            {
               iterator.remove();
               if (!iterator.hasNext())
               {
                  map.remove(key);
               }
            }
         };
      }
   }

   private Map<K, Collection<V>> init(CollectionMap<K, V> other)
   {
      Map<K, Collection<V>> map = new HashMap<K, Collection<V>>();

      //
      if (other != null)
      {
         for (Map.Entry<K, Collection<V>> entry : other.map.entrySet())
         {
            K key = entry.getKey();
            Collection<V> value = entry.getValue();
            map.put(key, newCollection(value));
         }
      }

      //
      return map;
   }

   protected abstract void add(Collection<V> c, V o);

   /**
    * Removes an object from the collection. The type of the object to remove is intentionnally <code>Object</code> and
    * not the parameterized type <code><V></code> because the Collection<V> interface is designed that way.
    *
    * @param c the collection to remove from
    * @param o the object to remove
    */
   protected abstract void remove(Collection<V> c, Object o);

   protected abstract Collection<V> newCollection();

   protected abstract Collection<V> newCollection(Collection<V> other);
}
