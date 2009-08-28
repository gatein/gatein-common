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
package org.gatein.common.invocation;

/**
 * @author <a href="mailto:julien@jboss.org">Julien Viet</a>
 * @version $Revision: 7228 $
 */
public interface InvocationContext
{
   /**
    * Return the attribute resolver of this context.
    *
    * @param attrScope the  attribute resolver scope
    * @return the attribute resolver or null if not found
    * @throws IllegalArgumentException if the attribute scope is invalid
    */
   AttributeResolver getAttributeResolver(Scope attrScope) throws IllegalArgumentException;

   /**
    * Returns an attribute value.
    *
    * @param attrScope the attribute scope
    * @param attrKey
    * @return the attribute value or null if not found
    * @throws IllegalArgumentException if the attribute key or the attribute scope is not valid
    */
   Object getAttribute(Scope attrScope, Object attrKey) throws IllegalArgumentException;

   /**
    * Update an attribute value.
    *
    * @param attrScope the attribute scope
    * @param attrKey
    * @param attrValue the attribute value
    * @throws IllegalArgumentException if the attribute key or the attribute scope is not valid
    */
   void setAttribute(Scope attrScope, Object attrKey, Object attrValue) throws IllegalArgumentException;

   /**
    * Remove an attribute value. If the attribute value is null the resolver must treat the operation as a removal of
    * the attribute.
    *
    * @param attrKey
    * @throws IllegalArgumentException if the attribute key is null or the attribute scope is not valid
    */
   void removeAttribute(Scope attrScope, Object attrKey);
}
