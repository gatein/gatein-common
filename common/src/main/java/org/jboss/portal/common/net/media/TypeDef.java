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

/**
 * A top level media type definition defined by <a href="http://tools.ietf.org/html/rfc2046#section-2">RFC2046 section 2</a>). 
 *
 * @author <a href="mailto:julien@jboss-portal.org">Julien Viet</a>
 * @version $Revision: 630 $
 */
public final class TypeDef
{

   // The five discrete top-level media types

   /** . */
   public static final TypeDef TEXT = new TypeDef("text", "", false);

   /** . */
   public static final TypeDef IMAGE = new TypeDef("image", "", false);

   /** . */
   public static final TypeDef AUDIO = new TypeDef("audio", "", false);

   /** . */
   public static final TypeDef VIDEO = new TypeDef("video", "", false);

   /** . */
   public static final TypeDef APPLICATION = new TypeDef("application", "", false);

   // The two composite top-level media types

   /** . */
   public static final TypeDef MULTIPART = new TypeDef("multipart", "", true);

   /** . */
   public static final TypeDef MESSAGE = new TypeDef("message", "", true);

   /**
    * Returns the corresponding type definition for the given top level type name.
    * If the type name does not correspond to a top level name, then null is returned.
    *
    * @param typeName the name of the type def
    * @return the corresponding type def
    * @throws IllegalArgumentException if the type name is null
    */
   public static TypeDef create(String typeName) throws IllegalArgumentException
   {
      if (typeName == null)
      {
         throw new IllegalArgumentException("no null type name accepted");
      }
      if (TEXT.name.equalsIgnoreCase(typeName))
      {
         return TEXT;
      }
      if (APPLICATION.name.equalsIgnoreCase(typeName))
      {
         return APPLICATION;
      }
      if (MULTIPART.name.equalsIgnoreCase(typeName))
      {
         return MULTIPART;
      }
      if (IMAGE.name.equalsIgnoreCase(typeName))
      {
         return IMAGE;
      }
      if (AUDIO.name.equalsIgnoreCase(typeName))
      {
         return AUDIO;
      }
      if (VIDEO.name.equalsIgnoreCase(typeName))
      {
         return VIDEO;
      }
      if (MESSAGE.name.equalsIgnoreCase(typeName))
      {
         return MESSAGE;
      }
      return null;
   }

   /** . */
   private final String name;

   /** . */
   private final String description;

   /** . */
   private final boolean composite;

   /** . */
   private final String toString;

   private TypeDef(String name, String description, boolean composite)
   {
      this.name = name;
      this.description = description;
      this.composite = composite;
      this.toString = "TypeDef[" + name + "]";
   }

   public String getName()
   {
      return name;
   }

   public String getDescription()
   {
      return description;
   }

   public boolean isComposite()
   {
      return composite;
   }

   public boolean isDiscrete()
   {
      return !composite;
   }

   public String toString()
   {
      return toString;
   }
}
