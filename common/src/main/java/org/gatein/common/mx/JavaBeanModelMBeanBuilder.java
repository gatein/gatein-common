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
package org.gatein.common.mx;

import javax.management.Descriptor;
import javax.management.modelmbean.ModelMBeanAttributeInfo;
import javax.management.modelmbean.ModelMBeanConstructorInfo;
import javax.management.modelmbean.ModelMBeanInfo;
import javax.management.modelmbean.ModelMBeanInfoSupport;
import javax.management.modelmbean.ModelMBeanNotificationInfo;
import javax.management.modelmbean.ModelMBeanOperationInfo;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;


/**
 * @author <a href="mailto:julien@jboss.org">Julien Viet</a>
 * @author <a href="mailto:chris.laprun@jboss.com">Chris Laprun</a>
 * @version $Revision: 7452 $
 */
public class JavaBeanModelMBeanBuilder
{
   private final static String CURRENCY_TIME_LIMIT = "currencyTimeLimit";
   private final static String GET_METHOD = "getMethod";
   private final static String SET_METHOD = "setMethod";
   private final static String PERSIST_POLICY = "persistPolicy";
   private final static String ROLE = "role";

   private ArrayList mmais;
   private ArrayList mmois;
   private String className;
   private static final String GET = "get";
   private static final String IS = "is";
   private static final String SET = "set";

   public JavaBeanModelMBeanBuilder(Class from, Class to) throws Exception
   {
      if (from == null)
      {
         throw new IllegalArgumentException("The from class must not be null");
      }
      if (from.isInterface())
      {
         throw new IllegalArgumentException("The from class " + from + " must not be an interface");
      }
      if (to != null)
      {
         if (to.isInterface())
         {
            throw new IllegalArgumentException("The to class " + to + " must not be an interface");
         }
         if (!to.isAssignableFrom(from))
         {
            throw new IllegalArgumentException("The from class " + from + " is not a subclass of " + to);
         }
      }

      //
      Map beanGetters = new HashMap();
      Map beanSetters = new HashMap();
      Map beanMethods = new HashMap();

      //
      for (Class c = from;c != null && !c.equals(to);c = c.getSuperclass())
      {
         Map<String,Method> currentClassGetters = new HashMap<String, Method>();
         Map<String, Method> currentClassSetters = new HashMap<String, Method>();

         Method[] methods = c.getDeclaredMethods();
         for (Method method : methods)
         {
            int modifiers = method.getModifiers();
            if (Modifier.isPublic(modifiers) &&
                    !Modifier.isAbstract(modifiers) &&
                    !Modifier.isStatic(modifiers))
            {
               String methodName = method.getName();
               Class returnType = method.getReturnType();
               Class[] parameterTypes = method.getParameterTypes();

               int prefixLength = 0;
               boolean isPotentialGetter = false;
               if(methodName.startsWith(GET))
               {
                  prefixLength = 3;
                  isPotentialGetter = true;
               }
               else if(methodName.startsWith(IS))
               {
                  prefixLength = 2;
                  isPotentialGetter = true;
               } else if (methodName.startsWith(SET))
               {
                  prefixLength = 3;
               }

               if (methodName.length() > prefixLength)
               {
                  if (isPotentialGetter && !void.class.equals(returnType) && parameterTypes.length == 0)
                  {
                     processPropertyOperation(method, currentClassGetters, currentClassSetters, beanSetters,
                             prefixLength, false);
                  }
                  else if (methodName.startsWith(SET) && void.class.equals(returnType) && parameterTypes.length == 1)
                  {
                     processPropertyOperation(method, currentClassSetters, currentClassGetters, beanGetters,
                             prefixLength, true);
                  }
               }

               //
               beanMethods.put(new MethodKey(method), method);
            }
         }
         beanGetters.putAll(currentClassGetters);
         beanSetters.putAll(currentClassSetters);
      }

      // Keep track of property accessors methods
      Map roles = new HashMap();

      // Properties->Attributes
      mmais = new ArrayList();
      Set allPropertyNames = new HashSet();
      allPropertyNames.addAll(beanGetters.keySet());
      allPropertyNames.addAll(beanSetters.keySet());
      for (Iterator i = allPropertyNames.iterator(); i.hasNext();)
      {
         String propertyName = (String)i.next();

         //
         Method getter = (Method)beanGetters.get(propertyName);
         Method setter = (Method)beanSetters.get(propertyName);

         // Create the metadata
         ModelMBeanAttributeInfo mmai = new ModelMBeanAttributeInfo(
            propertyName,
            "Javabean introspected attribute",
            getter,
            setter);

         // Complete the descriptor
         Descriptor desc = mmai.getDescriptor();
         desc.setField(JavaBeanModelMBeanBuilder.CURRENCY_TIME_LIMIT, "-1");
         desc.setField(JavaBeanModelMBeanBuilder.PERSIST_POLICY, "Never");

         //
         if (getter != null)
         {
            roles.put(getter, "getter");
            desc.setField(JavaBeanModelMBeanBuilder.GET_METHOD, getter.getName());
         }

         //
         if (setter != null)
         {
            roles.put(setter, "setter");
            desc.setField(JavaBeanModelMBeanBuilder.SET_METHOD, setter.getName());
         }

         //
         mmai.setDescriptor(desc);

         // Finally add the metadata
         mmais.add(mmai);
      }

      //
      className = from.getName();

      // Methods->Operations
      mmois = new ArrayList();
      for (Iterator i = beanMethods.values().iterator(); i.hasNext();)
      {
         Method method = (Method)i.next();

         // Create the metadata
         ModelMBeanOperationInfo mmoi = new ModelMBeanOperationInfo("Javabean introspected method", method);

         // Complete the descriptor
         Descriptor desc = mmoi.getDescriptor();
         String role = (String)roles.get(method);
         desc.setField(JavaBeanModelMBeanBuilder.ROLE, role != null ? role : "operation");
         mmoi.setDescriptor(desc);

         // Finally add the metadata
         mmois.add(mmoi);
      }
   }

   /**
    * Process a property operation either setter or getter, checking for consistency. "Reverse" operation is defined here
    * as a setter for a getter operation, and a getter for a setter operation.
    * Hence, if we are currently checking a getter, <code>beanReverseOperations</code> will refer to known setters so far,
    * <code>currentClassOperations</code> to the already known getter for this class at this hierachical level,
    * <code>currentClassReverseOperations</code> to the already known setter for this class at this hierachical level.
    *
    * @param operation
    * @param currentClassOperations known property operations for the hierarchy level being currently examined
    * @param currentClassReverseOperations known property "reverse" operations for the hierarchy level being currently examined
    * @param beanReverseOperations known "reverse" property operations for this bean
    * @param prefixLength
    * @param isSetter
    */
   private void processPropertyOperation(Method operation, Map<String, Method> currentClassOperations,
                                         Map<String, Method> currentClassReverseOperations, Map beanReverseOperations,
                                         int prefixLength, boolean isSetter)
   {
      String propertyName = operation.getName().substring(prefixLength);

      // Try to locate an existing setter for the same property
      Method reverseOp = currentClassReverseOperations.get(propertyName);
      if (reverseOp == null)
      {
         reverseOp = (Method)beanReverseOperations.get(propertyName);
      }

      // check that if we know a reverse operation, the types match
      if (reverseOp != null)
      {
         Class opType = isSetter ? operation.getParameterTypes()[0] : operation.getReturnType();
         Class reverseOpType = isSetter ? reverseOp.getReturnType() : reverseOp.getParameterTypes()[0];
         if (!reverseOpType.equals(opType))
         {
            throw new IllegalArgumentException("Property " + propertyName + " has a " + getterOrSetter(isSetter) +
                    " type " + reverseOpType + " different from the corresponding " + getterOrSetter(!isSetter) +
                    " type " + opType);
         }
      }

      // Check that we don't have twice the same operation
      Method op = currentClassOperations.get(propertyName);
      if (op != null)
      {
         throw new IllegalArgumentException("Property " + propertyName + " has two " + getterOrSetter(isSetter) + "s " +
                 op + " and " + operation);
      }

      //
      currentClassOperations.put(propertyName, operation);
   }

   private String getterOrSetter(boolean isSetter)
   {
      return isSetter ? "setter" : "getter";
   }

   /**
    * Remove an interface from the management interface.
    */
   public void remove(Class itf)
   {
      throw new UnsupportedOperationException("To be implemented if useful, just a placeholder now");
   }

   /**
    * Generates and returns the management interface.
    */
   public ModelMBeanInfo getInfo()
   {
      // Assemble the mbean info
      ModelMBeanInfoSupport info = new ModelMBeanInfoSupport(
         className,
         "Javabean model mbean",
         (ModelMBeanAttributeInfo[])mmais.toArray(new ModelMBeanAttributeInfo[mmais.size()]),
         new ModelMBeanConstructorInfo[0],
         (ModelMBeanOperationInfo[])mmois.toArray(new ModelMBeanOperationInfo[mmois.size()]),
         new ModelMBeanNotificationInfo[0]);

      //
      return info;
   }

   public static ModelMBeanInfo build(Class from, Class to) throws Exception
   {
      return new JavaBeanModelMBeanBuilder(from, to).getInfo();
   }

   public static ModelMBeanInfo build(Object o) throws Exception
   {
      return new JavaBeanModelMBeanBuilder(o.getClass(), null).getInfo();
   }

   /**
    * Key a method during the lookup operations
    */
   private static class MethodKey
   {

      /** . */
      private final String name;

      /** . */
      private final Class[] parameterTypes;

      /** . */
      private final int hashCode;

      public MethodKey(Method method)
      {
         this.name = method.getName();
         this.parameterTypes = method.getParameterTypes();

         // Compute hash code
         int hashCode = method.getName().hashCode();
         for (int i = 0;i < parameterTypes.length;i++)
         {
            Class parameterType = parameterTypes[i];
            hashCode = hashCode * 43 + parameterType.hashCode();
         }
         this.hashCode = hashCode;
      }

      public int hashCode()
      {
         return hashCode;
      }

      public boolean equals(Object obj)
      {
         if (obj == this)
         {
            return true;
         }
         if (obj instanceof MethodKey)
         {
            MethodKey that = (MethodKey)obj;

            //
            if (that.name.equals(this.name) && that.parameterTypes.length == this.parameterTypes.length)
            {
               for (int i = 0;i < that.parameterTypes.length;i++)
               {
                  if (!that.parameterTypes[i].equals(this.parameterTypes[i]))
                  {
                     return false;
                  }
               }
               return true;
            }
         }
         return false;
      }
   }

}
