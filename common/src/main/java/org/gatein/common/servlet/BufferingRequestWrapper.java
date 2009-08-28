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
package org.gatein.common.servlet;

import org.gatein.common.util.Tools;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

/**
 * Wrapper around a HTTPServletRequest to add attributes without affecting the actual request.
 *
 * @author <a href="mailto:theute@jboss.org">Thomas Heute</a>
 * @version $Revision: 7228 $
 */
public class BufferingRequestWrapper extends HttpServletRequestWrapper
{

   /** . */
   private static final Object REMOVED_ATTRIBUTE = new Object();

   /** . */
   private Map attributes;

   /** . */
   private Locale[] locales;

   /** . */
   private String contextPath;

   public BufferingRequestWrapper(HttpServletRequest servletRequest, String contextPath, Locale[] locales)
   {
      super(servletRequest);

      //
      this.contextPath = contextPath;
      this.locales = locales;
      this.attributes = new HashMap();
   }

   public String getContextPath()
   {
      return contextPath;
   }

   public Locale getLocale()
   {
      return locales.length > 0 ? locales[0] : null;
   }

   public Enumeration getLocales()
   {
      return Tools.toEnumeration(locales);
   }

   public String getMethod()
   {
      return "GET";
   }

   public void setAttribute(String name, Object value)
   {
      if (value == null)
      {
         value = REMOVED_ATTRIBUTE;
      }

      //
      attributes.put(name, value);
   }

   public Object getAttribute(String name)
   {
      Object value = attributes.get(name);

      //
      if (value == REMOVED_ATTRIBUTE)
      {
         value = null;
      }
      else if (value == null)
      {
         value = getRequest().getAttribute(name);
      }

      //
      return value;
   }

   public void removeAttribute(String name)
   {
      setAttribute(name, null);
   }

   public Enumeration getAttributeNames()
   {
      Set names = new HashSet();

      //
      for (Enumeration e = getRequest().getAttributeNames(); e.hasMoreElements();)
      {
         names.add(e.nextElement());
      }

      //
      for (Iterator i = attributes.entrySet().iterator(); i.hasNext();)
      {
         Map.Entry entry = (Map.Entry)i.next();
         String name = (String)entry.getKey();
         Object value = entry.getValue();
         if (value == REMOVED_ATTRIBUTE)
         {
            names.remove(name);
         }
         else
         {
            names.add(name);
         }
      }

      //
      return Tools.toEnumeration(names.iterator());
   }
}
