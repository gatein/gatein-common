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
package org.jboss.portal.common.net.media;

import org.jboss.portal.common.net.media.MediaType;
import org.jboss.portal.common.net.media.TypeDef;

import java.util.Map;
import java.util.HashMap;
import java.util.Set;
import java.util.HashSet;
import java.util.Collections;

/**
 * An implementation of the media type map interface.
 *
 * @author <a href="mailto:julien@jboss-portal.org">Julien Viet</a>
 * @version $Revision: 630 $
 */
public class MediaTypeMapImpl<V> implements MediaTypeMap<V>
{

   /** . */
   private static final String ANY = "*";

   /** . */
   private static final String ANY_ANY = "*/*";

   /** . */
   private static final char SEPARATOR = '/';

   /** . */
   private final Map<MediaType, Set<V>> mediaTypeToValues = new HashMap<MediaType,Set<V>>();

   /** . */
   private final Map<TypeDef, Set<V>> typeToValues = new HashMap<TypeDef,Set<V>>();

   /** . */
   private final Map<MediaType, Set<V>> combinedMediaTypeToValues = new HashMap<MediaType,Set<V>>();

   /** . */
   private final Map<TypeDef, Set<V>> combinedTypeToValues = new HashMap<TypeDef,Set<V>>();

   /** . */
   private final Set<V> anyTypeValues = new HashSet<V>();

   /**
    * Adds a value to the map using a media type pattern.
    *
    * @param mediaTypePattern the media type pattern
    * @param value the value
    * @throws IllegalArgumentException if any argument is null or the media type pattern is not valid
    */
   public void put(String mediaTypePattern, V value) throws IllegalArgumentException
   {
      if (mediaTypePattern == null)
      {
         throw new IllegalArgumentException("No null media type pattern accepted");
      }
      if (value == null)
      {
         throw new IllegalArgumentException("No null value accepted");
      }

      if (ANY.equals(mediaTypePattern) || ANY_ANY.equals(mediaTypePattern))
      {
         put(value);
      }
      else
      {
         int index = mediaTypePattern.indexOf(SEPARATOR);

         //
         if (index == -1)
         {
            throw new IllegalArgumentException("Not a valid media type pattern value:" + mediaTypePattern);
         }

         //
         String type = mediaTypePattern.substring(0, index);
         String subtype = mediaTypePattern.substring(index + 1);

         //
         if (ANY.equals(subtype))
         {
            put(TypeDef.create(type), value);
         }
         else
         {
            put(MediaType.create(type, subtype), value);
         }
      }
   }

   /**
    * Adds a value to the map.
    *
    * @param mediaType the media type
    * @param value the value
    * @throws IllegalArgumentException if any argument is null
    */
   public void put(MediaType mediaType, V value) throws IllegalArgumentException
   {
      if (mediaType == null)
      {
         throw new IllegalArgumentException("No null media type accepted");
      }
      if (value == null)
      {
         throw new IllegalArgumentException("No null value accepted");
      }

      //
      Set<V> mediaTypeValues = mediaTypeToValues.get(mediaType);
      if (mediaTypeValues == null)
      {
         mediaTypeValues = new HashSet<V>();
         mediaTypeToValues.put(mediaType, mediaTypeValues);
      }
      mediaTypeValues.add(value);

      //
      Set<V> combinedMediaTypeValues = combinedMediaTypeToValues.get(mediaType);
      if (combinedMediaTypeValues == null)
      {
         combinedMediaTypeValues = new HashSet<V>();
         combinedMediaTypeToValues.put(mediaType, combinedMediaTypeValues);
      }
      combinedMediaTypeValues.add(value);

      //
      Set<V> combinedTypeValues = combinedTypeToValues.get(mediaType.getType());
      if (combinedTypeValues != null)
      {
         combinedMediaTypeValues.addAll(combinedTypeValues);
      }

      //
      combinedMediaTypeValues.addAll(anyTypeValues);
   }

   /**
    * Adds a value to the map.
    *
    * @param type the type
    * @param value the value
    * @throws IllegalArgumentException if any argument is null
    */
   public void put(TypeDef type, V value) throws IllegalArgumentException
   {
      if (type == null)
      {
         throw new IllegalArgumentException("No null type accepted");
      }
      if (value == null)
      {
         throw new IllegalArgumentException("No null value accepted");
      }

      //
      Set<V> typeValues = typeToValues.get(type);
      if (typeValues == null)
      {
         typeValues = new HashSet<V>();
         typeToValues.put(type, typeValues);
      }
      typeValues.add(value);

      //
      Set<V> combinedTypeValues = combinedTypeToValues.get(type);
      if (combinedTypeValues == null)
      {
         combinedTypeValues = new HashSet<V>();
         combinedTypeToValues.put(type, combinedTypeValues);
      }
      combinedTypeValues.add(value);

      //
      for (Map.Entry<MediaType, Set<V>> entry : combinedMediaTypeToValues.entrySet())
      {
         if (entry.getKey().getType().equals(type))
         {
            entry.getValue().add(value);
         }
      }
   }

   /**
    * Adds a value to the map.
    *
    * @param value the value
    * @throws IllegalArgumentException if any argument is null
    */
   public void put(V value)
   {
      if (value == null)
      {
         throw new IllegalArgumentException("No null value accepted");
      }

      //
      anyTypeValues.add(value);

      //
      for (Set<V> combinedTypeValues : combinedTypeToValues.values())
      {
         combinedTypeValues.add(value);
      }

      //
      for (Set<V> combinedMediaTypeValues : combinedMediaTypeToValues.values())
      {
         combinedMediaTypeValues.add(value);
      }
   }

   public Set<V> resolve(MediaType mediaType)
   {
      if (mediaType == null)
      {
         throw new IllegalArgumentException("No null media type accepted");
      }

      //
      Set<V> values = combinedMediaTypeToValues.get(mediaType);

      //
      if (values == null)
      {
         values = resolve(mediaType.getType());
      }

      //
      return values;
   }

   public Set<V> resolve(TypeDef type)
   {
      if (type == null)
      {
         throw new IllegalArgumentException("No null type accepted");
      }

      //
      Set<V> values = combinedTypeToValues.get(type);

      //
      if (values == null)
      {
         values = getValues();
      }

      //
      return values;
   }

   public Set<V> get(MediaType mediaType)
   {
      if (mediaType == null)
      {
         throw new IllegalArgumentException("No null media type accepted");
      }

      //
      Set<V> values = combinedMediaTypeToValues.get(mediaType);

      //
      if (values == null)
      {
         values = Collections.emptySet();
      }

      //
      return values;
   }

   public Set<V> get(TypeDef type)
   {
      if (type == null)
      {
         throw new IllegalArgumentException("No null type accepted");
      }

      //
      Set<V> values = combinedTypeToValues.get(type);

      //
      if (values == null)
      {
         values = Collections.emptySet();
      }

      //
      return values;
   }

   public Set<V> getValues()
   {
      return anyTypeValues;
   }

   public boolean isSupported(MediaType mediaType)
   {
      return resolve(mediaType).size() > 0;
   }

   public boolean isSupported(TypeDef type)
   {
      return resolve(type).size() > 0;
   }

   public boolean isSupported(MediaType mediaType, V value)
   {
      if (value == null)
      {
         throw new IllegalArgumentException("No null value accepted");
      }

      //
      return resolve(mediaType).contains(value);
   }

   public boolean isSupported(TypeDef type, V value)
   {
      if (value == null)
      {
         throw new IllegalArgumentException("No null value accepted");
      }

      //
      return resolve(type).contains(value);
   }

   public boolean isSupported(V value)
   {
      if (value == null)
      {
         throw new IllegalArgumentException("No null value accepted");
      }

      //
      return getValues().contains(value);
   }

   public boolean contains(MediaType mediaType)
   {
      return get(mediaType).size() > 0;
   }

   public boolean contains(TypeDef type)
   {
      return get(type).size() > 0;
   }

   public boolean contains(MediaType mediaType, V value)
   {
      return get(mediaType).contains(value);
   }

   public boolean contains(TypeDef type, V value)
   {
      return get(type).contains(value);
   }

   public boolean contains(V value)
   {
      return getValues().contains(value);
   }

   public Set<MediaType> getMediaTypes()
   {
      return combinedMediaTypeToValues.keySet();
   }

   public Set<TypeDef> getTypes()
   {
      return combinedTypeToValues.keySet();
   }
}
