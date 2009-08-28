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
import java.util.Set;
import java.util.Collection;
import java.util.Iterator;

/**
 * @author <a href="mailto:julien@jboss.org">Julien Viet</a>
 * @version $Revision: 5451 $
 */
public class LazyMap<K, V> implements Map<K, V>
{

   /** . */
   private MapAccessor<K, V> delegate;

   /** . */
   private boolean modified;

   /**
    * @param delegate the delegate
    * @throws IllegalArgumentException if the argument is null
    */
   public LazyMap(MapAccessor<K, V> delegate) throws IllegalArgumentException
   {
      if (delegate == null)
      {
         throw new IllegalArgumentException("No null delegate can be accepted");
      }
      this.delegate = delegate;
   }

   public boolean isModified()
   {
      return modified;
   }

   private Map<K, V> getDelegate(boolean wantWrite)
   {
      return delegate.getMap(wantWrite);
   }

   public int size()
   {
      return getDelegate(false).size();
   }

   public boolean isEmpty()
   {
      return getDelegate(false).isEmpty();
   }

   public boolean containsKey(Object key)
   {
      return getDelegate(false).containsKey(key);
   }

   public boolean containsValue(Object value)
   {
      return getDelegate(false).containsValue(value);
   }

   public V get(Object key)
   {
      return getDelegate(false).get(key);
   }

   public V put(K key, V value)
   {
      modified = true;
      return getDelegate(true).put(key, value);
   }

   public V remove(Object key)
   {
      modified = true;
      return getDelegate(true).remove(key);
   }

   public void putAll(Map<? extends K, ? extends V> t)
   {
      modified = true;
      getDelegate(true).putAll(t);
   }

   public void clear()
   {
      modified = true;
      getDelegate(true).clear();
   }

   public Set<K> keySet()
   {
      return new Set<K>()
      {
         Set<K> keySet = getDelegate(true).keySet();

         public int size()
         {
            return keySet.size();
         }

         public boolean isEmpty()
         {
            return keySet.isEmpty();
         }

         public boolean contains(Object o)
         {
            return keySet.contains(o);
         }

         public Iterator<K> iterator()
         {
            return keySet.iterator();
         }

         public Object[] toArray()
         {
            return keySet.toArray();
         }

         public <T> T[] toArray(T[] a)
         {
            return keySet.toArray(a);
         }

         public boolean add(K o)
         {
            modified = true;
            return keySet.add(o);
         }

         public boolean remove(Object o)
         {
            modified = true;
            return keySet.remove(o);
         }

         public boolean containsAll(Collection<?> c)
         {
            return keySet.containsAll(c);
         }

         public boolean addAll(Collection<? extends K> c)
         {
            modified = true;
            return keySet.addAll(c);
         }

         public boolean retainAll(Collection<?> c)
         {
            return keySet.retainAll(c);
         }

         public boolean removeAll(Collection<?> c)
         {
            modified = true;
            return keySet.removeAll(c);
         }

         public void clear()
         {
            modified = true;
            keySet.clear();
         }

         public boolean equals(Object o)
         {
            return keySet.equals(o);
         }

         public int hashCode()
         {
            return keySet.hashCode();
         }
      };
   }

   public Collection<V> values()
   {
      return new Collection<V>()
      {
         /** . */
         Collection<V> values = getDelegate(true).values();

         public int size()
         {
            return values.size();
         }

         public boolean isEmpty()
         {
            return values.isEmpty();
         }

         public boolean contains(Object o)
         {
            return values.contains(o);
         }

         public Iterator<V> iterator()
         {
            return values.iterator();
         }

         public Object[] toArray()
         {
            return values.toArray();
         }

         public <T> T[] toArray(T[] a)
         {
            return values.toArray(a);
         }

         public boolean add(V o)
         {
            modified = true;
            return values.add(o);
         }

         public boolean remove(Object o)
         {
            modified = true;
            return values.remove(o);
         }

         public boolean containsAll(Collection<?> c)
         {
            return values.containsAll(c);
         }

         public boolean addAll(Collection<? extends V> c)
         {
            modified = true;
            return values.addAll(c);
         }

         public boolean removeAll(Collection<?> c)
         {
            modified = true;
            return values.removeAll(c);
         }

         public boolean retainAll(Collection<?> c)
         {
            return values.retainAll(c);
         }

         public void clear()
         {
            modified = true;
            values.clear();
         }

         public boolean equals(Object o)
         {
            return values.equals(o);
         }

         public int hashCode()
         {
            return values.hashCode();
         }
      };
   }

   public Set<Map.Entry<K, V>> entrySet()
   {
      return new Set<Map.Entry<K, V>>()
      {
         /** . */
         Set<Map.Entry<K, V>> entrySet = getDelegate(true).entrySet();

         public int size()
         {
            return entrySet.size();
         }

         public boolean isEmpty()
         {
            return entrySet.isEmpty();
         }

         public boolean contains(Object o)
         {
            return entrySet.contains(o);
         }

         public Iterator<Map.Entry<K, V>> iterator()
         {
            return entrySet.iterator();
         }

         public Object[] toArray()
         {
            return entrySet.toArray();
         }

         public <T> T[] toArray(T[] a)
         {
            return entrySet.toArray(a);
         }

         public boolean add(Entry<K, V> o)
         {
            modified = true;
            return entrySet.add(o);
         }

         public boolean remove(Object o)
         {
            modified = true;
            return entrySet.remove(o);
         }

         public boolean containsAll(Collection<?> c)
         {
            return entrySet.containsAll(c);
         }

         public boolean addAll(Collection<? extends Entry<K, V>> c)
         {
            modified = true;
            return entrySet.addAll(c);
         }

         public boolean retainAll(Collection<?> c)
         {
            modified = true;
            return entrySet.retainAll(c);
         }

         public boolean removeAll(Collection<?> c)
         {
            modified = true;
            return entrySet.removeAll(c);
         }

         public void clear()
         {
            modified = true;
            entrySet.clear();
         }

         public boolean equals(Object o)
         {
            return entrySet.equals(o);
         }

         public int hashCode()
         {
            return entrySet.hashCode();
         }
      };
   }

   public boolean equals(Object o)
   {
      return getDelegate(false).equals(o);
   }

   public int hashCode()
   {
      return getDelegate(false).hashCode();
   }
}
