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

import java.util.HashMap;
import java.util.Map;

/**
 * @author <a href="mailto:julien@jboss.org">Julien Viet</a>
 * @version $Revision: 7228 $
 */
public class AbstractInvocationContext implements InvocationContext
{

   /** . */
   private final Map resolvers;

   public AbstractInvocationContext()
   {
      this.resolvers = new HashMap();
   }

   protected final void addResolver(Scope resolverScope, InvocationContext federatedContext) throws IllegalArgumentException
   {
      if (resolverScope == null)
      {
         throw new IllegalArgumentException();
      }
      if (federatedContext == null)
      {
         throw new IllegalArgumentException();
      }
      resolvers.put(resolverScope, new InvocationContextRegistration(federatedContext));
   }

   protected final void addResolver(Scope resolverScope, AttributeResolver resolver) throws IllegalArgumentException
   {
      if (resolverScope == null)
      {
         throw new IllegalArgumentException();
      }
      if (resolver == null)
      {
         throw new IllegalArgumentException();
      }
      resolvers.put(resolverScope, new AttributeResolverRegistration(resolver));
   }

   public AttributeResolver getAttributeResolver(Scope attrScope) throws IllegalArgumentException
   {
      if (attrScope == null)
      {
         throw new IllegalArgumentException("Attribute name must not be null");
      }
      AttributeResolver resolver = null;
      Registration registration = (Registration)resolvers.get(attrScope);
      if (registration instanceof AttributeResolverRegistration)
      {
         resolver = ((AttributeResolverRegistration)registration).resolver;
      }
      else if (registration instanceof InvocationContextRegistration)
      {
         InvocationContext federaredContext = ((InvocationContextRegistration)registration).context;
         resolver = federaredContext.getAttributeResolver(attrScope);
      }
      else
      {
         throw new IllegalArgumentException("Scope not recognized " + attrScope);
      }
      return resolver;
   }

   public Object getAttribute(Scope attrScope, Object attrKey) throws IllegalArgumentException
   {
      if (attrKey == null)
      {
         throw new IllegalArgumentException("Attribute name must not be null");
      }
      if (attrScope == null)
      {
         throw new IllegalArgumentException("Attribute scope must not be null");
      }
      AttributeResolver resolver = getAttributeResolver(attrScope);
      if (resolver == null)
      {
         throw new IllegalArgumentException("Scope not recognized " + attrScope);
      }
      return resolver.getAttribute(attrKey);
   }

   public void setAttribute(Scope attrScope, Object attrKey, Object attrValue) throws IllegalArgumentException
   {
      if (attrKey == null)
      {
         throw new IllegalArgumentException("Attribute name must not be null");
      }
      if (attrScope == null)
      {
         throw new IllegalArgumentException("Attribute scope must not be null");
      }
      AttributeResolver resolver = getAttributeResolver(attrScope);
      if (resolver == null)
      {
         throw new IllegalArgumentException("Scope not recognized " + attrScope);
      }
      resolver.setAttribute(attrKey, attrValue);
   }

   public void removeAttribute(Scope attrScope, Object attrKey)
   {
      setAttribute(attrScope, attrKey, null);
   }

   /**
    * Typed registration to avoid issues classes that implement both interfaces AttributeResolver and
    * InvocationContext.
    */
   private abstract static class Registration
   {
   }

   private final static class AttributeResolverRegistration extends Registration
   {
      /** . */
      private final AttributeResolver resolver;

      private AttributeResolverRegistration(AttributeResolver resolver)
      {
         this.resolver = resolver;
      }
   }

   private final static class InvocationContextRegistration extends Registration
   {
      /** . */
      private final InvocationContext context;

      private InvocationContextRegistration(InvocationContext context)
      {
         this.context = context;
      }
   }
}
