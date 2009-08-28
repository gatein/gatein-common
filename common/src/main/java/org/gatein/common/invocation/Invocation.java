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
 * A generic invocation object.
 *
 * @author <a href="mailto:julien@jboss.org">Julien Viet</a>
 * @version $Revision: 5597 $
 */
public abstract class Invocation
{

   /** . */
   public static final InterceptorStack EMPTY_STACK = new InterceptorStack()
   {
      public int getLength()
      {
         return 0;
      }
      public Interceptor getInterceptor(int index) throws ArrayIndexOutOfBoundsException
      {
         throw new ArrayIndexOutOfBoundsException();
      }
   };

   /** The current index in the chain. */
   private int currentIndex = 0;

   /** The current interceptor stack. */
   private InterceptorStack currentStack = Invocation.EMPTY_STACK;

   /** . */
   private InvocationHandler handler;

   /**
    * Return the invocation context or throw IllegalStateException.
    *
    * @return the invocation context
    * @throws IllegalStateException if no context is associated with this invocation
    */
   public abstract InvocationContext getContext() throws IllegalStateException;

   /**
    * @see InvocationContext#getAttribute(Scope,Object)
    */
   public Object getAttribute(Scope attrScope, Object attrKey) throws IllegalArgumentException
   {
      return getContext().getAttribute(attrScope, attrKey);
   }

   /**
    * @see InvocationContext#setAttribute(Scope,Object,Object)
    */
   public void setAttribute(Scope attrScope, Object attrKey, Object attrValue) throws IllegalArgumentException
   {
      getContext().setAttribute(attrScope, attrKey, attrValue);
   }

   /**
    * @see InvocationContext#removeAttribute(Scope,Object)
    */
   public void removeAttribute(Scope attrScope, Object attrKey) throws IllegalArgumentException
   {
      getContext().removeAttribute(attrScope, attrKey);
   }

   public InvocationHandler getHandler()
   {
      return handler;
   }

   public void setHandler(InvocationHandler handler)
   {
      this.handler = handler;
   }

   /**
    * Invoke the next interceptor in the chain. If the end of the chain is reached then the <code>dispatch()</code>
    * is invoked.
    */
   public Object invokeNext() throws Exception, InvocationException
   {
      if (currentIndex < currentStack.getLength())
      {
         try
         {
            Interceptor interceptor = currentStack.getInterceptor(currentIndex++);
            if (interceptor == null)
            {
               throw new InvocationException("Null interceptor at index=" + (currentIndex - 1));
            }
            else
            {
               return interceptor.invoke(this);
            }
         }
         finally
         {
            currentIndex--;
         }
      }
      else
      {
         if (handler == null)
         {
            throw new InvocationException("No handler");
         }
         else
         {
            return handler.invoke(this);
         }
      }
   }

   /**
    * Execute the invocation through the chain of interceptors.
    */
   public Object invoke(InterceptorStack newStack) throws Exception, InvocationException
   {
      if (newStack == null)
      {
         throw new InvocationException("Cannot invoke with a null interceptor[]");
      }

      // Save the previous context
      InterceptorStack previousInterceptors = currentStack;
      int previousIndex = currentIndex;

      try
      {
         // Set the new context
         currentStack = newStack;
         currentIndex = 0;

         // Perform invoke
         return invokeNext();
      }
      finally
      {
         // Restablish the previous context
         currentStack = previousInterceptors;
         currentIndex = previousIndex;
      }
   }
}
