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

import java.util.List;
import java.util.Collections;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Represent a content type header value as defined by the section 5 of the
 * <a href="http://tools.ietf.org/html/rfc2045#section-5">RFC2045</a>
 *
 * @author <a href="mailto:julien@jboss-portal.org">Julien Viet</a>
 * @version $Revision: 630 $
 */
public class ContentType
{

   /**
    * Creates a content type header value by parsing a content type value. The content type value format is defined
    * by the section 5.1 of the <a href="http://tools.ietf.org/html/rfc2045#section-5.1">RFC2045</a> but does not
    * take in account the prefix "Content-Type" ":" of the content production rule of the grammar.
    *
    * @param contentTypeValue the content type  value
    * @return
    * @throws IllegalArgumentException if the
    */
   public static ContentType create(String contentTypeValue) throws IllegalArgumentException
   {
      if (contentTypeValue == null)
      {
         throw new IllegalArgumentException("No null content type value accepted");
      }

      //
      int slashIndex = contentTypeValue.indexOf('/');
      if (slashIndex == -1)
      {
         throw new IllegalArgumentException("The content type " + contentTypeValue + " does not contain a /");
      }

      //
      String typeName = contentTypeValue.substring(0, slashIndex);

      //
      int semiColonIndex = contentTypeValue.indexOf(';', slashIndex + 1);
      if (semiColonIndex == -1)
      {
         return new ContentType(MediaType.create(typeName, contentTypeValue.substring(slashIndex + 1)));

      }

      //
      String subtypeName = contentTypeValue.substring(slashIndex + 1, semiColonIndex);

      //
      MediaType mediaType = MediaType.create(typeName, subtypeName);

      //
      List<Parameter> parameters = null;

      //
      for (int i = semiColonIndex + 1;semiColonIndex != -1;i = semiColonIndex + 1)
      {
         semiColonIndex = contentTypeValue.indexOf(';', i);

         //
         if (semiColonIndex == 0)
         {
            // We have 2 following semi colons
            throw new IllegalArgumentException();
         }

         // Compute the to
         int to = semiColonIndex == -1 ? contentTypeValue.length() : semiColonIndex;

         // Get the equals char position
         int equalsIndex = contentTypeValue.indexOf('=', i);

         //
         if (equalsIndex != -1 && equalsIndex < to)
         {
            if (parameters == null)
            {
               parameters = new ArrayList<Parameter>();
            }

            //
            String name = contentTypeValue.substring(i, equalsIndex);
            String value = contentTypeValue.substring(equalsIndex + 1, to);
            parameters.add(new Parameter(name, value));
         }
         else
         {
            throw new IllegalArgumentException("No equals char found in the string " + contentTypeValue.substring(i, to));
         }
      }

      //
      if (parameters == null)
      {
         parameters = Collections.emptyList();
      }

      //
      return new ContentType(mediaType, parameters);
   }

   /** . */
   private MediaType mediaType;

   /** . */
   private List<Parameter> parameters;

   public ContentType(MediaType mediaType, List<Parameter> parameters)
   {
      if (mediaType == null)
      {
         throw new IllegalArgumentException("No null media type accepted");
      }
      if (parameters == null)
      {
         throw new IllegalArgumentException("No null parameter list accepted");
      }

      //
      this.mediaType = mediaType;
      this.parameters = parameters;
   }

   public ContentType(MediaType mediaType)
   {
      if (mediaType == null)
      {
         throw new IllegalArgumentException("No null media type accepted");
      }

      //
      this.mediaType = mediaType;
      this.parameters = Collections.emptyList();
   }

   public MediaType getMediaType()
   {
      return mediaType;
   }

   public List<Parameter> getParameters()
   {
      return parameters;
   }

   /**
    * Returns the value of the content type which is the concatenation of the media type name
    * followed by the parameters.
    *
    * @return the value
    */
   public String getValue()
   {
      StringBuilder builder = new StringBuilder();
      builder.append(mediaType.getValue());
      for (Iterator<Parameter> i = parameters.iterator();;)
      {
         Parameter parameter = i.next();

         //
         builder.append(parameter.getName()).append('=').append(parameter.getValue());

         //
         if (i.hasNext())
         {
            builder.append(';');
         }
         else
         {
            break;
         }
      }
      return builder.toString();
   }
}
