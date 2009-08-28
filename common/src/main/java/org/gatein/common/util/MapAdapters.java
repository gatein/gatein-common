/******************************************************************************
 * JBoss, a division of Red Hat                                               *
 * Copyright 2008, Red Hat Middleware, LLC, and individual                    *
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

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Map;

/**
 * A collection of various map adapters.
 *
 * @author <a href="mailto:julien@jboss-portal.org">Julien Viet</a>
 * @version $Revision: 630 $
 */
public class MapAdapters
{

   /** . */
   private final static ArrayElementConverter<String> stringArrayElementConverter = new ArrayElementConverter<String>(String.class);

   /** . */
   private final static AbstractTypedMap.Converter<String, String> stringConverter = AbstractTypedMap.Converter.identityConverter();

   public static <K,V> Map<K,V> adapt(Map<K, V[]> adapted, Class<? extends V> type)
   {
      if (adapted == null)
      {
         throw new IllegalArgumentException("Cannot adapt a null map");
      }
      ArrayElementConverter<V> valueConverter = new ArrayElementConverter<V>(type);
      AbstractTypedMap.Converter<K, K> keyConverter = AbstractTypedMap.Converter.identityConverter();
      return new TypedMap<K, V, K, V[]>(adapted, keyConverter, valueConverter);
   }

   /**
    * <p>Adapt a <code>Map<String, String[]></code> map as a <code>Map<String, String></code> map. The adapted map
    * must not contain null string array or zero lenght string array, if the map contains such values then the
    * map state is considered as not valid and {@link IllegalStateException} can be thrown when the adapted map</p>
    * is used.
    *
    * <p>The adapted map is writable but does not accept null values for write operations. On a put operation
    * the adapted map will receive a put operation with a string array of length one containing the provided value.</p>
    *
    * @param adapted the adapted map
    * @return the adapter
    */
   public static Map<String, String> adapt(Map<String, String[]> adapted)
   {
      if (adapted == null)
      {
         throw new IllegalArgumentException("Cannot adapt a null map");
      }
      return new TypedMap<String, String, String, String[]>(adapted, stringConverter, stringArrayElementConverter);
   }
   
   private static class ArrayElementConverter<T> extends AbstractTypedMap.Converter<T, T[]>
   {

      /** . */
      private final Class<? extends T> type;

      private ArrayElementConverter(Class<? extends T> type)
      {
         if (type == null)
         {
            throw new IllegalArgumentException("No null type accepted");
         }
         this.type = type;
      }

      protected T[] getInternal(T external) throws IllegalArgumentException, ClassCastException
      {
         if (external == null)
         {
            throw new NullPointerException();
         }

         //
         T[] array = (T[])Array.newInstance(type, 1);
         array[0] = external;

         //
         return array;
      }

      protected T getExternal(T[] internal) throws IllegalArgumentException, ClassCastException
      {
         if (internal == null)
         {
            throw new IllegalStateException();
         }

         //
         if (internal.length == 0)
         {
            throw new IllegalStateException();
         }

         //
         return internal[0];
      }

      protected boolean equals(T[] left, T[] right)
      {
         return Arrays.equals(left, right);
      }
   }
}
