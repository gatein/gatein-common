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

import java.util.Set;

/**
 * A map containing values associated with media types. The map has the capability to map wildcard subtypes
 * (for instance text / *) or the wildcard type (* / *). In order to make the distinction between what the map
 * declares and what it supports it is possible to query the map with resolution or not.
 *
 * @author <a href="mailto:julien@jboss-portal.org">Julien Viet</a>
 * @version $Revision: 630 $
 */
public interface MediaTypeMap<V>
{

   /**
    * Returns true if the map declares the specified media type.
    *
    * @param mediaType the media type
    * @return true if the map declares the media type
    * @throws IllegalArgumentException if the argument is null
    */
   boolean contains(MediaType mediaType) throws IllegalArgumentException;

   /**
    * Returns true if the map declares the specified type. It is equivalent to check if the map
    * contains a wildard subtype of a type.
    *
    * @param type the type
    * @return true if the map declares the type
    * @throws IllegalArgumentException if the argument is null
    */
   boolean contains(TypeDef type) throws IllegalArgumentException;

   /**
    * Returns true if the map declares the specified media type with a specified value.
    *
    * @param mediaType the media type
    * @param value the value
    * @return true if the map declares the media type and the value
    * @throws IllegalArgumentException if any argument is null
    */
   boolean contains(MediaType mediaType, V value) throws IllegalArgumentException;

   /**
    * Returns true if the map declares the specified type with a specific value. It is equivalent to check if the map
    * contains a wildard subtype of a type with a specific value.
    *
    * @param type the type
    * @param value the value
    * @return true if the map declares the media type and the value
    * @throws IllegalArgumentException if any argument is null
    */
   boolean contains(TypeDef type, V value) throws IllegalArgumentException;

   /**
    * Returns true if the map declares the specified value for any media type or any type. It is equivalent to check
    * if the map contains a wildcard type with a specific value.
    *
    * @param value the value
    * @return true if the map declares the value
    * @throws IllegalArgumentException if any argument is null
    */
   boolean contains(V value) throws IllegalArgumentException;

   /**
    * Returns the set of values declared for a given media type.
    *
    * @param mediaType the media type
    * @return the values declared
    * @throws IllegalArgumentException if any argument is null
    */
   Set<V> get(MediaType mediaType) throws IllegalArgumentException;

   /**
    * Returns the set of values declared for a given type.
    *
    * @param type the type
    * @return the values declared
    * @throws IllegalArgumentException if any argument is null
    */
   Set<V> get(TypeDef type) throws IllegalArgumentException;

   /**
    * Returns true if the map supports the specified media type.
    *
    * @param mediaType the media type
    * @return true if the map supports the media type
    * @throws IllegalArgumentException if the argument is null
    */
   boolean isSupported(MediaType mediaType) throws IllegalArgumentException;

   /**
    * Returns true if the map supports the specified type.
    *
    * @param type the type
    * @return true if the map declares the type
    * @throws IllegalArgumentException if the argument is null
    */
   boolean isSupported(TypeDef type) throws IllegalArgumentException;

   /**
    * Returns true if the map supports the specified media type with a specified value.
    *
    * @param mediaType the media type
    * @param value the value
    * @return true if the map supports the media type and the value
    * @throws IllegalArgumentException if any argument is null
    */
   boolean isSupported(MediaType mediaType, V value) throws IllegalArgumentException;

   /**
    * Returns true if the map supports the specified type with a specific value.
    *
    * @param type the type
    * @param value the value
    * @return true if the map supports the media type and the value
    * @throws IllegalArgumentException if any argument is null
    */
   boolean isSupported(TypeDef type, V value) throws IllegalArgumentException;

   /**
    * Returns true if the map supports the specified value for any media type or any type.
    *
    * @param value the value
    * @return true if the map supports the value
    * @throws IllegalArgumentException if any argument is null
    */
   boolean isSupported(V value) throws IllegalArgumentException;

   /**
    * Returns the set of values supported for a given media type.
    *
    * @param mediaType the media type
    * @return the values supported
    * @throws IllegalArgumentException if any argument is null
    */
   Set<V> resolve(MediaType mediaType) throws IllegalArgumentException;

   /**
    * Returns the set of values supported for a given type.
    *
    * @param type the type
    * @return the values supported
    * @throws IllegalArgumentException if any argument is null
    */
   Set<V> resolve(TypeDef type) throws IllegalArgumentException;

   /**
    * Returns the set of declared media types.
    *
    * @return the media types
    */
   Set<MediaType> getMediaTypes();

   /**
    * Returns the set of declared types.
    *
    * @return the types
    */
   Set<TypeDef> getTypes();

   /**
    * Returns the set of declared values.
    *
    * @return the values
    */
   Set<V> getValues();
}
