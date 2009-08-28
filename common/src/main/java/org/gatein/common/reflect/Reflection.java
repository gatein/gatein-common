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
package org.gatein.common.reflect;

import java.lang.reflect.Method;

/**
 * @author <a href="mailto:julien@jboss.org">Julien Viet</a>
 * @version $Revision: 1.1 $
 */
public class Reflection
{

   /**
    * Search a method on the specified class. The search will try find methods with any scope and will
    * start with the provided class. First it will inspect the public method on the class, if one is found
    * then this method is returned. If no public method is found then it will inspect the declared methods
    * of the provided class and its super classes.
    *
    * @param clazz the class to inspect
    * @param methodName the method name
    * @param parameterTypes the parameter types
    * @return the matched method or null
    * @throws IllegalArgumentException if one argument is null
    */
   public static Method findMethod(
      Class clazz,
      String methodName,
      Class[] parameterTypes) throws IllegalArgumentException
   {
      if (clazz == null)
      {
         throw new IllegalArgumentException("No class provided");
      }
      if (methodName == null)
      {
         throw new IllegalArgumentException("No method name provided");
      }
      if (parameterTypes == null)
      {
         throw new IllegalArgumentException("No parameter types provided");
      }
      for (int i = 0; i < parameterTypes.length; i++)
      {
         if (parameterTypes[i] == null)
         {
            throw new IllegalArgumentException("Null parameter type at element=" + i);
         }
      }

      // First try a public method, which could be an implemented from an interface
      try
      {
         return clazz.getMethod(methodName, parameterTypes);
      }
      catch (NoSuchMethodException ignore)
      {
      }

      //
      return findDeclaredMethod(clazz, methodName, parameterTypes);
   }

   /**
    * Search a declared method on the specified class and its super classes.
    *
    * @param clazz the class to inspect
    * @param methodName the method name
    * @param parameterTypes the parameter types
    * @return the matched method or null
    * @throws IllegalArgumentException if one argument is null
    */
   private static Method findDeclaredMethod(
      Class clazz,
      String methodName,
      Class[] parameterTypes) throws IllegalArgumentException
   {
      if (clazz == null)
      {
         throw new IllegalArgumentException("No class provided");
      }
      if (methodName == null)
      {
         throw new IllegalArgumentException("No method name provided");
      }
      if (parameterTypes == null)
      {
         throw new IllegalArgumentException("No parameter types provided");
      }
      for (int i = 0; i < parameterTypes.length; i++)
      {
         if (parameterTypes[i] == null)
         {
            throw new IllegalArgumentException("Null parameter type at element=" + i);
         }
      }

      //
      try
      {
         return clazz.getDeclaredMethod(methodName, parameterTypes);
      }
      catch (NoSuchMethodException ignore)
      {
      }

      //
      Class parentClass = clazz.getSuperclass();
      if (parentClass != null)
      {
         Method method = findDeclaredMethod(parentClass, methodName, parameterTypes);
         if (method != null)
         {
            return method;
         }
      }

      // Nothing found
      return null;
   }

   /**
    * Attempt to cast the value argument to the provided type argument. If the value argument type is assignable
    * to the provided type, the value is returned, otherwise if it is not or the value is null, null is returned.
    *
    * @param value the value to cast
    * @param type the type to downcast
    * @return the casted value or null
    * @throws IllegalArgumentException if the type argument is null
    */
   public static <T> T safeCast(Object value, Class<T> type) throws IllegalArgumentException
   {
      if (type == null)
      {
         throw new IllegalArgumentException("No null type accepted");
      }
      if (value == null)
      {
         return null;
      }
      else
      {
         if (type.isAssignableFrom(value.getClass()))
         {
            return type.cast(value);
         }
         else
         {
            return null;
         }
      }
   }
}
