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
package org.gatein.common.net.media;

/**
 * An immutable media type class.
 *
 * @author <a href="mailto:julien@jboss-portal.org">Julien Viet</a>
 * @version $Revision: 630 $
 */
public final class MediaType
{

   /** . */
   public static final MediaType TEXT_HTML = new MediaType(TypeDef.TEXT, SubtypeDef.HTML);

   /** . */
   public static final MediaType TEXT_JAVASCRIPT = new MediaType(TypeDef.TEXT, SubtypeDef.JAVASCRIPT);

   /** . */
   public static final MediaType TEXT_CSS = new MediaType(TypeDef.TEXT, SubtypeDef.CSS);

   /** . */
   public static final MediaType APPLICATION_X_WWW_FORM_URLENCODED = new MediaType(TypeDef.APPLICATION, SubtypeDef.X_WWW_FORM_URLENCODED);

   /** . */
   public static final MediaType MULTIPART_FORM_DATA_MEDIA_TYPE = new MediaType(TypeDef.MULTIPART, SubtypeDef.FORM_DATA);

   /**
    * Create a media type object by parsing a media type name. The media type name format is defined by the
    * section 5.1 of the <a href="http://tools.ietf.org/html/rfc2045#section-5.1">RFC2045</a> but is limited
    * to a subset of the grammar, the starting production rule being:
    *
    * media-type: type "/" subtype
    *
    * @param mediaTypeName the media type name value
    * @return the media type object
    * @throws IllegalArgumentException if the argument is null or is not valid
    */
   public static MediaType create(String mediaTypeName) throws IllegalArgumentException
   {
      if (mediaTypeName == null)
      {
         throw new IllegalArgumentException("No null media type value accepted");
      }

      //
      int slashIndex = mediaTypeName.indexOf('/');
      if (slashIndex == -1)
      {
         throw new IllegalArgumentException("The media type " + mediaTypeName + " does not contain a /");
      }

      //
      String typeName = mediaTypeName.substring(0, slashIndex);
      String subtypeName = mediaTypeName.substring(slashIndex + 1);

      //
      return create(typeName, subtypeName);
   }

   /**
    * Create a media type object by using the provided type name and subtype name.
    *
    * @param typeName the type name
    * @param subtypeName the subtype name
    * @return the media type object
    * @throws IllegalArgumentException if any argument is null or not valid
    */
   public static MediaType create(String typeName, String subtypeName) throws IllegalArgumentException
   {
      if (typeName == null)
      {
         throw new IllegalArgumentException("No null type name accepted");
      }
      if (subtypeName == null)
      {
         throw new IllegalArgumentException("No null subtype name accepted");
      }

      //
      TypeDef type = TypeDef.create(typeName);
      if (type == null)
      {
         throw new IllegalArgumentException("Type not recognized in content type " + typeName + "/" + subtypeName);
      }

      //
      SubtypeDef subtype = SubtypeDef.create(subtypeName);

      //
      return new MediaType(type, subtype);
   }

   /**
    * Create a media type object using the provided type and subtype objects.
    *
    * @param type the type
    * @param subtype the subtype
    * @return the media type object
    * @throws IllegalArgumentException if any argument is null
    */
   public static MediaType create(TypeDef type, SubtypeDef subtype) throws IllegalArgumentException
   {
      if (type == null)
      {
         throw new IllegalArgumentException("No null type accepted");
      }
      if (subtype == null)
      {
         throw new IllegalArgumentException("No null subtype accepted");
      }

      //
      return new MediaType(type, subtype);
   }

   /** The type identifier. */
   private final TypeDef type;

   /** The sub type identifier. */
   private final SubtypeDef subtype;

   /** The cached hashCode. */
   private volatile Integer hashCode;

   /** the cached toString. */
   private volatile String toString;

   /** the cached value. */
   private volatile String value;

   MediaType(TypeDef type, SubtypeDef subtype)
   {
      this.type = type;
      this.subtype = subtype;
   }

   public TypeDef getType()
   {
      return type;
   }

   public SubtypeDef getSubtype()
   {
      return subtype;
   }

   /**
    * Returns the value which is a concatenation ofthe type name, a slash char and the subtype name.
    *
    * @return the value
    */
   public String getValue()
   {
      if (value == null)
      {
         value = type.getName() + "/" + subtype.getName();
      }
      return value;
   }
   public int hashCode()
   {
      if (hashCode == null)
      {
         hashCode = type.hashCode() + subtype.hashCode();
      }
      return hashCode;
   }

   public boolean equals(Object obj)
   {
      if (obj == this)
      {
         return true;
      }
      if (obj instanceof MediaType)
      {
         MediaType that = (MediaType)obj;
         return type.equals(that.type) && subtype.equals(that.subtype);
      }
      return false;
   }

   public String toString()
   {
      if (toString == null)
      {
         toString = "MediaType[name=" + type + ",subtype=" + subtype + "]";
      }
      return toString;
   }
}
