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
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Collection;

/**
 * An helper to build collection of object in a simple manner.
 *
 * @author <a href="mailto:julien@jboss.org">Julien Viet</a>
 * @version $Revision: 7234 $
 */
public class CollectionBuilder<C extends Collection<V>, V>
{

   /** . */
   private C collection;

   private CollectionBuilder(C collection)
   {
      this.collection = collection;
   }

   public static <C extends Collection<V>, V> CollectionBuilder<C, V> create(C collection)
   {
      return new CollectionBuilder<C, V>(collection);
   }

   public static <V> CollectionBuilder<ArrayList<V>, V> arrayList()
   {
      return new CollectionBuilder<ArrayList<V>, V>(new ArrayList<V>());
   }

   public static <V> CollectionBuilder<ArrayList<V>, V> arrayList(V v)
   {
      CollectionBuilder<ArrayList<V>, V> builder = new CollectionBuilder<ArrayList<V>, V>(new ArrayList<V>());
      builder.add(v);
      return builder;
   }

   public static <V> CollectionBuilder<LinkedList<V>, V> linkedList()
   {
      return new CollectionBuilder<LinkedList<V>, V>(new LinkedList<V>());
   }

   public static <V> CollectionBuilder<LinkedList<V>, V> linkedList(V v)
   {
      CollectionBuilder<LinkedList<V>, V> builder = new CollectionBuilder<LinkedList<V>, V>(new LinkedList<V>());
      builder.add(v);
      return builder;
   }

   public static <V> CollectionBuilder<HashSet<V>, V> hashSet()
   {
      return new CollectionBuilder<HashSet<V>, V>(new HashSet<V>());
   }

   public static <V> CollectionBuilder<HashSet<V>, V> hashSet(V v)
   {
      CollectionBuilder<HashSet<V>, V> builder = new CollectionBuilder<HashSet<V>, V>(new HashSet<V>());
      builder.add(v);
      return builder;
   }

   /**
    * Add the object to the collection.
    *
    * @param o the object to add
    * @return the builder
    */
   public CollectionBuilder<C, V> add(V o)
   {
      collection.add(o);
      return this;
   }

   /**
    * Add all the objects to the collection.
    *
    * @param all the objects to add
    * @return the builder
    */
   public CollectionBuilder<C, V> addAll(Collection<V> all)
   {
      collection.addAll(all);
      return this;
   }

   public C get()
   {
      return collection;
   }
}
