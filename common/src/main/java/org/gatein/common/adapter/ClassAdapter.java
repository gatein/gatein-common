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
package org.gatein.common.adapter;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;

/**
 * Creates a new class adapter.
 *
 * @author <a href="mailto:julien@jboss.org">Julien Viet</a>
 * @version $Revision: 7228 $
 */
public class ClassAdapter
{

   /** . */
   private final Map dispatchers;

   /** . */
   private final Constructor ctor;

   /** . */
   private final InvocationHandler handler = new InvocationHandler()
   {
      public Object invoke(Object proxy, Method method, Object[] args) throws Throwable
      {
         MethodDispatcher dispatcher = (MethodDispatcher)dispatchers.get(method);

         //
         ClassAdaptable adaptable = (ClassAdaptable)proxy;

         //
         return dispatcher.dispatch(adaptable, args);
      }
   };

   /**
    * Creates a new class adapter. The object adapted implementation used will be an instance of
    * <code>JavaLangObjectAdapted</code>.
    *
    * @param loader   the class loader that will contain the dynamic proxy generated class
    * @param adapteds the array of class adapteds
    * @throws NoSuchMethodException
    * @throws IllegalArgumentException if an argument is null
    */
   public ClassAdapter(ClassLoader loader, ClassAdapted[] adapteds) throws NoSuchMethodException, IllegalArgumentException
   {
      this(loader, adapteds, new DefaultJavaLangObjectAdapted());
   }

   /**
    * Create a new class adapter.
    *
    * @param loader        the class loader that will contain the dynamic proxy generated class
    * @param adapteds      the array of class adapteds
    * @param objectAdapted the implementation of object adapted
    * @throws NoSuchMethodException
    * @throws IllegalArgumentException if an argument is null
    */
   public ClassAdapter(ClassLoader loader, ClassAdapted[] adapteds, JavaLangObjectAdapted objectAdapted) throws NoSuchMethodException, IllegalArgumentException
   {
      if (loader == null)
      {
         throw new IllegalArgumentException("No null class loader accepted");
      }
      if (adapteds == null)
      {
         throw new IllegalArgumentException("No null adapteds accepted");
      }
      if (objectAdapted == null)
      {
         throw new IllegalArgumentException("No null object adapted accepted");
      }

      //
      dispatchers = new HashMap();

      //
      addAdapted(Object.class, new JavaLangObjectMethodDispatcherFactory(objectAdapted));

      //
      Class[] itfs = new Class[adapteds.length + 1];
      itfs[0] = ClassAdaptable.class;

      //
      for (int i = 0; i < adapteds.length; i++)
      {
         ClassAdapted adapted = adapteds[i];

         //
         if (adapted == null)
         {
            throw new IllegalArgumentException("No null adapted accepted");
         }

         //
         itfs[1 + i] = adapted.itf;

         //
         addAdapted(adapted.itf, new AdaptedMethodDispatcherFactory(adapted.object));
      }

      //
      ctor = Proxy.getProxyClass(loader, itfs).getConstructor(new Class[]{InvocationHandler.class});
   }

   private void addAdapted(Class adaptedClass, MethodDispatcherFactory methodDispatcherFactory)
   {
      Method[] methods = adaptedClass.getMethods();
      for (int j = 0; j < methods.length; j++)
      {
         Method method = methods[j];
         int modifiers = method.getModifiers();
         if (Modifier.isPublic(modifiers) && !Modifier.isFinal(modifiers) && !Modifier.isStatic(modifiers))
         {
            if (!dispatchers.containsKey(method))
            {
               dispatchers.put(method, methodDispatcherFactory.createDispatcher(method));
            }
         }
      }
   }

   /** Returns an instance of the the adaptable object. */
   public ClassAdaptable getAdaptable() throws IllegalAccessException, InstantiationException, InvocationTargetException
   {
      return (ClassAdaptable)ctor.newInstance(new Object[]{handler});
   }
}
