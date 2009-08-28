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
package org.gatein.common.util;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Encapsulate info about a proxy and provide a way to instantiate it.
 *
 * @author <a href="mailto:julien@jboss.org">Julien Viet</a>
 * @version $Revision: 5451 $
 */
public class ProxyInfo
{

   private static final Class[] EMPTY_SIGNATURE = new Class[0];
   private static final Class[] EQUALS_SIGNATURE = new Class[]{Object.class};
   private static final Class[] INVOCATION_HANDLER_SIGNATURE = new Class[]{InvocationHandler.class};

   /** The proxy constructor. */
   private final Constructor ctor;

   /** The proxy toString method. */
   private final Method toString;

   /** The proxy hashCode method. */
   private final Method hashCode;

   /** The proxy equals method. */
   private final Method equals;

   public ProxyInfo(Class clazz) throws Exception
   {
      this.ctor = clazz.getConstructor(INVOCATION_HANDLER_SIGNATURE);

      //
      toString = Object.class.getMethod("toString", EMPTY_SIGNATURE);
      hashCode = Object.class.getMethod("hashCode", EMPTY_SIGNATURE);
      equals = Object.class.getMethod("equals", EQUALS_SIGNATURE);
   }

   /**
    * Instantiate a proxy.
    *
    * @param handler the invocation handler
    * @return the proxy
    */
   public Object instantiate(InvocationHandler handler) throws IllegalAccessException, InvocationTargetException, InstantiationException
   {
      return ctor.newInstance(new Object[]{handler});
   }

   public Method getToString()
   {
      return toString;
   }

   public Method getHashCode()
   {
      return hashCode;
   }

   public Method getEquals()
   {
      return equals;
   }
}
