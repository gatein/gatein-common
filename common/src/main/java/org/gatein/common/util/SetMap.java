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

import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * A map of set. This object does not handle synchronization and use HashMap and HashSet as underlying data structures;
 *
 * @author <a href="mailto:julien@jboss.org">Julien Viet</a>
 * @version $Revision: 7322 $
 */
public class SetMap<K, V> extends CollectionMap<K, V>
{

   /** Version. */
   static final long serialVersionUID = -7239767000556095977L;

   public SetMap()
   {
   }

   public SetMap(SetMap<K, V> other) throws IllegalArgumentException
   {
      super(other);
   }

   public SetMap(SetMap<K, V> other, Comparator<V> comparator) throws IllegalArgumentException
   {
      super(other);

      //
      if (comparator == null)
      {
         throw new IllegalArgumentException("No null comparator allowed");
      }

      //
      this.comparator = comparator;
   }

   public SetMap(Comparator<V> comparator)
   {
      super();

      //
      if (comparator == null)
      {
         throw new IllegalArgumentException("No null comparator allowed");
      }

      //
      this.comparator = comparator;
   }

   /** Return the set specified by the key. */
   public Set<V> get(K key)
   {
      return (Set<V>)super.get(key);
   }

   protected void add(Collection<V> c, V o)
   {
      c.add(o);
   }

   protected void remove(Collection<V> c, Object o)
   {
      c.remove(o);
   }

   protected Collection<V> newCollection()
   {
      if (comparator == null)
      {
         return new HashSet<V>();
      }
      else
      {
         return new TreeSet<V>(comparator);
      }
   }

   protected Collection<V> newCollection(Collection<V> other)
   {
      if (comparator == null)
      {
         return new HashSet<V>(other);
      }
      else
      {
         SortedSet<V> set = new TreeSet<V>(comparator);
         set.addAll(other);
         return set;
      }
   }
}
