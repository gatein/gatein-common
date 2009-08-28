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
package org.gatein.common.invocation.resolver;

import org.gatein.common.invocation.AttributeResolver;
import org.gatein.common.util.Tools;

import javax.servlet.http.HttpServletRequest;
import java.util.Set;

/**
 * @author <a href="mailto:julien@jboss.org">Julien Viet</a>
 * @version $Revision: 7228 $
 */
public class RequestAttributeResolver implements AttributeResolver
{

   /** . */
   private final HttpServletRequest req;

   public RequestAttributeResolver(HttpServletRequest req)
   {
      if (req == null)
      {
         throw new IllegalArgumentException();
      }
      this.req = req;
   }


   public Set getKeys()
   {
      return Tools.toSet(req.getAttributeNames());
   }

   public Object getAttribute(Object attrKey) throws IllegalArgumentException
   {
      if (attrKey == null)
      {
         throw new IllegalArgumentException("No null attribute key accepted");
      }
      if (attrKey instanceof String == false)
      {
         throw new IllegalArgumentException("Attribute key must be a string");
      }

      //
      return req.getAttribute((String)attrKey);
   }

   public void setAttribute(Object attrKey, Object attrValue) throws IllegalArgumentException
   {
      if (attrKey == null)
      {
         throw new IllegalArgumentException("No null attribute key accepted");
      }
      if (attrKey instanceof String == false)
      {
         throw new IllegalArgumentException("Attribute key must be a string");
      }

      //
      if (attrValue != null)
      {
         req.setAttribute((String)attrKey, attrValue);
      }
      else
      {
         req.removeAttribute((String)attrKey);
      }
   }
}
