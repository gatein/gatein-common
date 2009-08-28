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

import java.util.AbstractCollection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * A map implementations that use a conversion mechanism to provide a type mapping between the external types declared
 * by the map and the internal representations backed by the internal map.
 *
 * @author <a href="mailto:julien@jboss.org">Julien Viet</a>
 * @version $Revision: 1.1 $
 */
public abstract class AbstractTypedMap<EK, EV, IK, IV> implements Map<EK, EV>
{

   /** Transform an input to an output. */
   private abstract static class Transformer<Out, In>
   {
      /**
       * Convert an object to another one.
       *
       * @param in the input
       * @return the output
       */
      protected abstract Out transform(In in);
   }

   /**
    * Defines a converter that converts an internal value to an external value and vice versa. Null values will not
    * passed as arguments to the methods and if a conversion method returns a null value it will be considered as a
    * conversion failure although the implementations should not rely and this behavior and rather rely on a adapted
    * exception.
    */
   public abstract static class Converter<E, I>
   {

      private static final Converter identity = new Converter()
      {
         protected Object getInternal(Object external) throws IllegalArgumentException, ClassCastException
         {
            return external;
         }

         protected Object getExternal(Object internal) throws IllegalArgumentException, ClassCastException
         {
            return internal;
         }

         protected boolean equals(Object left, Object right)
         {
            return left.equals(right);
         }
      };

      public static <T> Converter<T, T> identityConverter()
      {
         return (Converter<T, T>)identity;
      }

      private final Transformer<I, E> keyUnwrapper = new Transformer<I, E>()
      {
         protected I transform(E e)
         {
            if (e == null)
            {
               throw new NullPointerException("No null input accepted");
            }

            //
            I i = getInternal(e);

            //
            if (i == null)
            {
               throw new IllegalArgumentException("The provided input " + e + " was converted to a null output");
            }

            //
            return i;
         }
      };

      private final Transformer<E, I> keyWrapper = new Transformer<E, I>()
      {
         protected E transform(I i)
         {
            if (i == null)
            {
               throw new IllegalStateException("No null input accepted");
            }

            //
            E e = getExternal(i);

            //
            if (e == null)
            {
               throw new IllegalStateException("The provided input " + i + " was converted to a null output");
            }

            //
            return e;
         }
      };

      private final Transformer<I, E> valueUnwrapper = new Transformer<I, E>()
      {
         protected I transform(E e)
         {
            return getInternal(e);
         }
      };

      private final Transformer<E, I> valueWrapper = new Transformer<E, I>()
      {
         protected E transform(I i)
         {
            return getExternal(i);
         }
      };

      /**
       * Converts the external value to the its internal representation that will be stored in the map.
       *
       * @param external the external value
       * @return the the internal value
       * @throws ClassCastException       if the class of the specified argument prevents it from being converter
       * @throws IllegalArgumentException if some aspect of this argument prevents it from being converted
       */
      protected abstract I getInternal(E external) throws IllegalArgumentException, ClassCastException;

      /**
       * Converts the internal value into its external representation.
       *
       * @param internal the internal value
       * @return the external value
       * @throws ClassCastException       if the class of the specified argument prevents it from being converter
       * @throws IllegalArgumentException if some aspect of this argument prevents it from being converted
       */
      protected abstract E getExternal(I internal) throws IllegalArgumentException, ClassCastException;

      /**
       * Compare internal values, the passed argument are never null so the method does not need to check nullity of the
       * arguments.
       *
       * @param left  the left value
       * @param right the right value
       * @return true if the values are considered equals
       */
      protected abstract boolean equals(I left, I right);
   }

   private static <I> boolean safeEquals(I left, I right, Converter<?, I> converter)
   {
      // Check the internal value, it should not be null
      return !(left == null || right == null) && converter.equals(left, right);
   }

   /** . */
   private KeySet keySet;

   /** . */
   private TypedEntrySet entrySet;

   /** . */
   private ValueCollection values;

   public abstract Converter<EK, IK> getKeyConverter();

   public abstract Converter<EV, IV> getValueConverter();

   protected abstract Map<IK, IV> getDelegate();

   public final int size()
   {
      return getDelegate().size();
   }

   public final void clear()
   {
      getDelegate().clear();
   }

   public final boolean isEmpty()
   {
      return getDelegate().isEmpty();
   }

   public final boolean containsKey(Object key)
   {
      EK ek = (EK)key;
      IK ik = getKeyConverter().keyUnwrapper.transform(ek);
      return getDelegate().containsKey(ik);
   }

   public final Set<EK> keySet()
   {
      if (keySet == null)
      {
         keySet = new KeySet();
      }
      return keySet;
   }

   public final EV put(EK ek, EV ev)
   {
      Map<IK, IV> map = getDelegate();
      IK ik = getKeyConverter().keyUnwrapper.transform(ek);
      boolean contains = map.containsKey(ik);

      //
      IV iv = getValueConverter().valueUnwrapper.transform(ev);
      iv = map.put(ik, iv);

      // Do we have a result ?
      if (contains)
      {
         boolean rollback = true;
         try
         {
            ev = getValueConverter().valueWrapper.transform(iv);
            rollback = false;
         }
         finally
         {
            if (rollback)
            {
               map.put(ik, iv);
            }
         }
      }
      else
      {
         ev = null;
      }

      //
      return ev;
   }

   public final EV get(Object key)
   {
      EK ek = (EK)key;
      IK ik = getKeyConverter().keyUnwrapper.transform(ek);
      Map<IK, IV> map = getDelegate();
      IV iv = map.get(ik);
      EV ev = null;
      if (iv != null || map.containsKey(ik))
      {
         ev = getValueConverter().valueWrapper.transform(iv);
      }
      return ev;
   }

   public final EV remove(Object key)
   {
      EK ek = (EK)key;
      IK ik = getKeyConverter().keyUnwrapper.transform(ek);
      Map<IK, IV> map = getDelegate();
      if (getDelegate().containsKey(ik))
      {
         IV iv = map.remove(ik);
         boolean rollback = true;
         try
         {
            EV ev = getValueConverter().valueWrapper.transform(iv);
            rollback = false;
            return ev;
         }
         finally
         {
            if (rollback)
            {
               map.put(ik, iv);
            }
         }
      }
      else
      {
         return null;
      }
   }

   public final boolean containsValue(Object value)
   {
      EV ev = (EV)value;
      IV iv = getValueConverter().valueUnwrapper.transform(ev);
      return getDelegate().containsValue(iv);
   }

   public final Set<Entry<EK, EV>> entrySet()
   {
      if (entrySet == null)
      {
         entrySet = new TypedEntrySet();
      }
      return entrySet;
   }

   public final void putAll(Map<? extends EK, ? extends EV> em)
   {
      Map<IK, IV> im = convert(em);
      getDelegate().putAll(im);
   }

   public final Collection<EV> values()
   {
      if (values == null)
      {
         values = new ValueCollection();
      }
      return values;
   }

   /** Compare to parameters objects. */
   public final boolean equals(Object o)
   {
      if (o == this)
      {
         return true;
      }
      if (o instanceof Map)
      {
         Map<EK, EV> that = (Map<EK, EV>)o;
         Map<IK, IV> delegate = getDelegate();

         // Must have same sizes
         if (that.size() != delegate.size())
         {
            return false;
         }

         //
         for (Entry<EK, EV> thatEntry : that.entrySet())
         {

            EK thatKey = thatEntry.getKey();
            EV thatValue = thatEntry.getValue();

            //
            try
            {
               // Unwrap key, mostly for checking its type is correct
               IK ik = getKeyConverter().keyUnwrapper.transform(thatKey);

               // Unwrap value
               IV iv = getValueConverter().valueUnwrapper.transform(thatValue);

               // Get the internal value
               IV internalValue = delegate.get(ik);

               // Perform value comparison
               if (!safeEquals(internalValue, iv, getValueConverter()))
               {
                  return false;
               }
            }
            catch (IllegalArgumentException e)
            {
               return false;
            }
            catch (ClassCastException e)
            {
               return false;
            }
            catch (NullPointerException e)
            {
               return false;
            }
         }

         //
         return true;
      }

      //
      return false;
   }

   public String toString()
   {
      return getDelegate().toString();
   }

   /** Validates and unwraps the map. */
   protected final Map<IK, IV> convert(Map<? extends EK, ? extends EV> t) throws IllegalArgumentException, NullPointerException, ClassCastException
   {
      if (t == null)
      {
         throw new NullPointerException("No null map can be accepted");
      }
      Map<IK, IV> u = new HashMap<IK, IV>(t.size());
      for (Entry<? extends EK, ? extends EV> entry : t.entrySet())
      {
         IK ik = getKeyConverter().keyUnwrapper.transform(entry.getKey());
         IV iv = getValueConverter().valueUnwrapper.transform(entry.getValue());
         u.put(ik, iv);
      }
      return u;
   }

   /** Replace the content with the new map which is validated before replacement. */
   public final void replace(Map<EK, EV> map) throws ClassCastException, NullPointerException, IllegalArgumentException
   {
      Map<IK, IV> tmp = convert(map);

      //
      Map<IK, IV> delegate = getDelegate();
      delegate.clear();
      delegate.putAll(tmp);
   }

   /** Validate the content. */
   public final void validate() throws ClassCastException, NullPointerException, IllegalArgumentException
   {
      for (Entry<IK, IV> entry : getDelegate().entrySet())
      {
         getKeyConverter().keyWrapper.transform(entry.getKey());
         getValueConverter().valueWrapper.transform(entry.getValue());
      }
   }

   private static class TypedCollection<E, I> extends AbstractCollection<E>
   {

      /** . */
      private final Collection<I> delegate;

      /** . */
      private final Transformer<I, E> unwrapper;

      /** . */
      private final Transformer<E, I> wrapper;

      private TypedCollection(Collection<I> delegate, Transformer<I, E> unwrapper, Transformer<E, I> wrapper)
      {
         this.delegate = delegate;
         this.unwrapper = unwrapper;
         this.wrapper = wrapper;
      }

      public int size()
      {
         return delegate.size();
      }

      public void clear()
      {
         delegate.clear();
      }

      public boolean isEmpty()
      {
         return delegate.isEmpty();
      }

      public boolean contains(Object o)
      {
         E e = (E)o;
         I i = unwrapper.transform(e);
         return delegate.contains(i);
      }

      public boolean addAll(Collection<? extends E> ek)
      {
         throw new UnsupportedOperationException();
      }

      public boolean remove(Object o)
      {
         E e = (E)o;
         I i = unwrapper.transform(e);
         return delegate.remove(i);
      }

      public boolean removeAll(Collection<?> c)
      {
         ArrayList<I> tmp = new ArrayList<I>(c.size());

         //
         for (Object o : c)
         {
            E e = (E)o;
            I i = unwrapper.transform(e);
            tmp.add(i);
         }

         //
         return delegate.removeAll(tmp);
      }

      public boolean retainAll(Collection<?> c)
      {
         ArrayList<I> tmp = new ArrayList<I>(c.size());

         //
         for (Object o : c)
         {
            E e = (E)o;
            I i = unwrapper.transform(e);
            tmp.add(i);
         }

         //
         return delegate.retainAll(tmp);
      }

      public Iterator<E> iterator()
      {
         return new TypedIterator();
      }

      private final class TypedIterator implements Iterator<E>
      {

         /** . */
         private final Iterator<I> delegate;

         public TypedIterator()
         {
            this.delegate = TypedCollection.this.delegate.iterator();
         }

         public void remove()
         {
            delegate.remove();
         }

         public boolean hasNext()
         {
            return delegate.hasNext();
         }

         public E next()
         {
            I i = delegate.next();
            return wrapper.transform(i);
         }
      }
   }

   private static abstract class TypedEntry<EK, EV, IK, IV> implements Entry<EK, EV>
   {

      private final Entry<IK, IV> delegate;

      protected TypedEntry(Entry<IK, IV> delegate)
      {
         this.delegate = delegate;
      }

      protected abstract Transformer<EK, IK> getKeyWrapper();

      protected abstract Transformer<EV, IV> getValueWrapper();

      protected abstract Transformer<IV, EV> getValueUnwrapper();

      public EK getKey()
      {
         return getKeyWrapper().transform(delegate.getKey());
      }

      public EV getValue()
      {
         return getValueWrapper().transform(delegate.getValue());
      }

      public EV setValue(EV value)
      {
         IV iv = getValueUnwrapper().transform(value);
         IV oldIV = delegate.setValue(iv);
         boolean rollback = true;
         try
         {
            EV ev = getValueWrapper().transform(oldIV);
            rollback = false;
            return ev;
         }
         finally
         {
            if (rollback)
            {
               delegate.setValue(oldIV);
            }
         }
      }

      public int hashCode()
      {
         return delegate.getKey().hashCode();
      }
   }

   private class ExternalEntry extends TypedEntry<EK, EV, IK, IV>
   {
      private ExternalEntry(Entry<IK, IV> delegate)
      {
         super(delegate);
      }

      protected Transformer<EK, IK> getKeyWrapper()
      {
         return getKeyConverter().keyWrapper;
      }

      protected Transformer<EV, IV> getValueWrapper()
      {
         return getValueConverter().valueWrapper;
      }

      protected Transformer<IV, EV> getValueUnwrapper()
      {
         return getValueConverter().valueUnwrapper;
      }
   }

   private class InternalEntry extends TypedEntry<IK, IV, EK, EV>
   {
      private InternalEntry(Entry<EK, EV> delegate)
      {
         super(delegate);
      }

      protected Transformer<IK, EK> getKeyWrapper()
      {
         return getKeyConverter().keyUnwrapper;
      }

      protected Transformer<IV, EV> getValueWrapper()
      {
         return getValueConverter().valueUnwrapper;
      }

      protected Transformer<EV, IV> getValueUnwrapper()
      {
         return getValueConverter().valueWrapper;
      }
   }

   private class EntryWrapper extends Transformer<Map.Entry<EK, EV>, Map.Entry<IK, IV>>
   {
      protected Entry<EK, EV> transform(Entry<IK, IV> externalEntry)
      {
         return new ExternalEntry(externalEntry);
      }
   }

   private class EntryUnwrapper extends Transformer<Map.Entry<IK, IV>, Map.Entry<EK, EV>>
   {
      protected Entry<IK, IV> transform(Entry<EK, EV> internalEntry)
      {
         return new InternalEntry(internalEntry);
      }
   }

   private final class KeySet extends TypedCollection<EK, IK> implements Set<EK>
   {
      public KeySet()
      {
         super(getDelegate().keySet(), getKeyConverter().keyUnwrapper, getKeyConverter().keyWrapper);
      }

      public boolean equals(Object obj)
      {
         if (obj == this)
         {
            return true;
         }
         if (obj instanceof Set)
         {
            Set that = (Set)obj;

            //
            if (size() != that.size())
            {
               return false;
            }

            //
            for (Object o : that)
            {
               try
               {
                  if (!contains(o))
                  {
                     return false;
                  }
               }
               catch (IllegalArgumentException e)
               {
                  return false;
               }
               catch (ClassCastException e)
               {
                  return false;
               }
               catch (NullPointerException e)
               {
                  return false;
               }
            }

            //
            return true;
         }
         return false;
      }

      public int hashCode()
      {
         int hashCode = 0;

         //
         for (Object o : this)
         {
            if (o != null)
            {
               hashCode += o.hashCode();
            }
         }

         //
         return hashCode;
      }
   }

   private final class ValueCollection extends TypedCollection<EV, IV>
   {
      public ValueCollection()
      {
         super(getDelegate().values(), getValueConverter().valueUnwrapper, getValueConverter().valueWrapper);
      }
   }

   private class TypedEntrySet extends TypedCollection<Map.Entry<EK, EV>, Map.Entry<IK, IV>> implements Set<Map.Entry<EK, EV>>
   {
      private TypedEntrySet()
      {
         super(getDelegate().entrySet(), new EntryUnwrapper(), new EntryWrapper());
      }

      public boolean remove(Object o)
      {
         throw new UnsupportedOperationException();
      }

      public boolean removeAll(Collection<?> c)
      {
         throw new UnsupportedOperationException();
      }

      public boolean retainAll(Collection<?> c)
      {
         throw new UnsupportedOperationException();
      }
   }
}
