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
 * @author <a href="mailto:julien@jboss.org">Julien Viet</a>
 * @version $Revision: 7228 $
 */
public class HttpHeaders implements Serializable
{

   /** . */
   private List headers = new ArrayList();

   public HttpHeader addHeader(String name)
   {
      return addHeader(new HttpHeader(name));
   }

   public HttpHeader addHeader(HttpHeader header)
   {
      if (header == null)
      {
         throw new IllegalArgumentException();
      }
      headers.add(header);
      return header;
   }

   public HttpHeader getHeader(String name)
   {
      if (name == null)
      {
         throw new IllegalArgumentException();
      }
      for (Iterator i = headers.iterator(); i.hasNext();)
      {
         HttpHeader header = (HttpHeader)i.next();
         if (header.getName().equals(name))
         {
            return header;
         }
      }
      return null;
   }

   public Iterator headers()
   {
      return headers.iterator();
   }

   public String toString()
   {
      StringBuffer buffer = new StringBuffer();
      for (Iterator i = headers.iterator(); i.hasNext();)
      {
         HttpHeader header = (HttpHeader)i.next();
         buffer.append(header.toString());
         buffer.append("\n");
      }
      return buffer.toString();
   }
}
