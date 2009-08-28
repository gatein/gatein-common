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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * @author <a href="mailto:julien@jboss.org">Julien Viet</a>
 * @version $Revision: 1.1 $
 */
public class ListMap<K, V> extends CollectionMap<K, V>
{

   public ListMap()
   {
   }

   public ListMap(SetMap<K, V> other) throws IllegalArgumentException
   {
      super(other);
   }

   public ListMap(SetMap<K, V> other, Comparator<V> comparator) throws IllegalArgumentException
   {
      super(other, comparator);
   }

   public ListMap(Comparator<V> comparator)
   {
      super(comparator);
   }

   /** Return the list specified by the key. */
   public List<V> get(K key)
   {
      return (List<V>)super.get(key);
   }

   protected void add(Collection<V> c, V o)
   {
      c.add(o);

      //
      if (comparator != null)
      {
         Collections.sort((List<V>)c, comparator);
      }
   }

   protected void remove(Collection<V> c, Object o)
   {
      c.remove(o);

      //
      if (comparator != null)
      {
         Collections.sort((List<V>)c, comparator);
      }
   }

   protected Collection<V> newCollection()
   {
      return new ArrayList<V>();
   }

   protected Collection<V> newCollection(Collection<V> other)
   {
      return new ArrayList<V>(other);
   }
}
