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
import java.util.Iterator;

/**
 * An java.util.Iterator wrapper which keep additional state associated with the wrapped iterator. The implementation of
 * the Iterator interface delegates all operations to the wrapped iterator.
 *
 * @author <a href="mailto:julien@jboss.org">Julien Viet</a>
 * @version $Revision: 7228 $
 */
public class IteratorStatus implements Iterator
{

   /** The real iterator. */
   private final Iterator delegate;

   /** The iterator index. */
   private int index;

   /**
    * @param delegate the wrapped iterator
    * @throws IllegalArgumentException if the wrapped iterator is null
    */
   public IteratorStatus(Iterator delegate) throws IllegalArgumentException
   {
      if (delegate == null)
      {
         throw new IllegalArgumentException("No iterator is provided");
      }
      this.delegate = delegate;
      this.index = -1;
   }

   /**
    * @param c the collection to iterate
    * @throws IllegalArgumentException if the collection is null
    */
   public IteratorStatus(Collection c) throws IllegalArgumentException
   {
      if (c == null)
      {
         throw new IllegalArgumentException("No collection is provided");
      }
      this.delegate = c.iterator();
      this.index = -1;
   }

   /**
    * Returns the index of the last element obtained or -1 if no element has been returned yet.
    *
    * @return the index of the last element obtained
    */
   public int getIndex()
   {
      return index;
   }

   /**
    * Returns true if one element has been iterated.
    *
    * @return true if one element has been iterated.
    * @throws IllegalStateException if no element has been iterated yet
    */
   public boolean isFirst() throws IllegalStateException
   {
      if (index == -1)
      {
         throw new IllegalStateException("No element has been iterated so far");
      }
      return index == 0;
   }

   public boolean hasNext()
   {
      return delegate.hasNext();
   }

   public Object next()
   {
      Object o = delegate.next();

      // Increment after getting the next object since the call to the next() method
      // can throw a NoSuchElementException
      index++;
      return o;
   }

   public void remove()
   {
      delegate.remove();
   }
}
