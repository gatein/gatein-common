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
package org.gatein.common.http;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Modelize an http header structure.
 *
 * @author <a href="mailto:julien@jboss.org">Julien Viet</a>
 * @version $Revision: 7228 $
 */
public class HttpHeader implements Serializable
{

   /** . */
   private String name;

   /** . */
   private List elements = new ArrayList();

   public HttpHeader(String name)
   {
      if (name == null)
      {
         throw new IllegalArgumentException();
      }
      this.name = name;
   }

   public String getName()
   {
      return name;
   }

   public Element addElement(String name)
   {
      return addElement(new Element(name));
   }

   public Element addElement(String name, String value)
   {
      return addElement(new Element(name, value));
   }

   public Element addElement(Element element)
   {
      if (element == null)
      {
         throw new IllegalArgumentException();
      }
      elements.add(element);
      return element;
   }

   public Element getElement()
   {
      if (elements.size() > 0)
      {
         return (Element)elements.get(0);
      }
      return null;
   }

   public Iterator elements()
   {
      return elements.iterator();
   }

   /** An element of an header */
   public static class Element implements Serializable
   {
      /** The mandatory name. */
      private String name;

      /** The optional value. */
      private String value;

      /** The params. */
      private List params;

      public Element(String name)
      {
         this(name, null);
      }

      public Element(String name, String value)
      {
         if (name == null)
         {
            throw new IllegalArgumentException();
         }
         this.name = name;
         this.value = value;
         this.params = new ArrayList();
      }

      public String getName()
      {
         return name;
      }

      public String getValue()
      {
         return value;
      }

      public Param addParam(String name)
      {
         return addParam(new Param(name));
      }

      public Param addParam(String name, String value)
      {
         return addParam(new Param(name, value));
      }

      public Param addParam(Param param)
      {
         if (param == null)
         {
            throw new IllegalArgumentException();
         }
         params.add(param);
         return param;
      }

      /**
       * Return the first param of this element or null.
       *
       * @return the first param
       */
      public Param getParam()
      {
         if (params.size() > 0)
         {
            return (Param)params.get(0);
         }
         return null;
      }

      /**
       * Returns an iterator over the params.
       *
       * @return a param iterator
       */
      public Iterator params()
      {
         return params.iterator();
      }

      /** A param of an element. */
      public static class Param implements Serializable
      {
         /** The mandatory name. */
         private String name;

         /** The optional value. */
         private String value;

         public Param(String name)
         {
            this(name, null);
         }

         public Param(String name, String value)
         {
            if (name == null)
            {
               throw new IllegalArgumentException();
            }
            this.name = name;
            this.value = value;
         }

         public String getName()
         {
            return name;
         }

         public String getValue()
         {
            return value;
         }
      }
   }

   public String getValue()
   {
      StringBuffer buffer = new StringBuffer();
      for (Iterator j = elements(); j.hasNext();)
      {
         HttpHeader.Element elt = (HttpHeader.Element)j.next();
         buffer.append(elt.getName());
         if (elt.getValue() != null)
         {
            buffer.append("=").append(elt.getValue());
         }
         for (Iterator k = elt.params(); k.hasNext();)
         {
            HttpHeader.Element.Param param = (HttpHeader.Element.Param)k.next();
            buffer.append(";").append(param.getName());
            if (param.getValue() != null)
            {
               buffer.append("=").append(param.getValue());
            }
         }
         if (j.hasNext())
         {
            buffer.append(",");
         }
      }
      return buffer.toString();
   }

   public String toString()
   {
      return name + ": " + getValue();
   }
}
