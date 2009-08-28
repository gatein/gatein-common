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
package org.jboss.portal.test.common.reflect;

import junit.framework.TestCase;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.AbstractList;

import org.gatein.common.reflect.Reflection;

/**
 * @author <a href="mailto:julien@jboss.org">Julien Viet</a>
 * @version $Revision: 1.1 $
 */
public class ReflectionTestCase extends TestCase
{

   public void testClass1()
   {
      assertEquals(Class1.class, "privateMethod", Modifier.PRIVATE, Reflection.findMethod(Class1.class, "privateMethod", new Class[0]));

      //
      assertEquals(Class1.class, "privateMethodOfClass1", Modifier.PRIVATE, Reflection.findMethod(Class1.class, "privateMethodOfClass1", new Class[0]));
      assertEquals(Class1.class, "publicAbstractMethodOfClass1", Modifier.PUBLIC | Modifier.ABSTRACT, Reflection.findMethod(Class1.class, "publicAbstractMethodOfClass1", new Class[0]));
      assertEquals(Class1.class, "protectedAbstractMethodOfClass1", Modifier.PROTECTED | Modifier.ABSTRACT, Reflection.findMethod(Class1.class, "protectedAbstractMethodOfClass1", new Class[0]));
   }

   public void testClass2()
   {
      assertEquals(Class2.class, "privateMethod", Modifier.PRIVATE, Reflection.findMethod(Class2.class, "privateMethod", new Class[0]));

      //
      assertEquals(Class1.class, "privateMethodOfClass1", Modifier.PRIVATE, Reflection.findMethod(Class2.class, "privateMethodOfClass1", new Class[0]));
      assertEquals(Class2.class, "publicAbstractMethodOfClass1", Modifier.PUBLIC, Reflection.findMethod(Class2.class, "publicAbstractMethodOfClass1", new Class[0]));
      assertEquals(Class2.class, "protectedAbstractMethodOfClass1", Modifier.PROTECTED, Reflection.findMethod(Class2.class, "protectedAbstractMethodOfClass1", new Class[0]));

      //
      assertEquals(Class2.class, "privateMethodOfClass2", Modifier.PRIVATE, Reflection.findMethod(Class2.class, "privateMethodOfClass2", new Class[0]));
      assertEquals(Class2.class, "publicMethodOfClass2", Modifier.PUBLIC, Reflection.findMethod(Class2.class, "publicMethodOfClass2", new Class[0]));
      assertEquals(Class2.class, "protectedMethodOfClass2", Modifier.PROTECTED, Reflection.findMethod(Class2.class, "protectedMethodOfClass2", new Class[0]));
   }

   public void testClass3()
   {
      assertEquals(Class3.class, "privateMethod", Modifier.PRIVATE, Reflection.findMethod(Class3.class, "privateMethod", new Class[0]));

      //
      assertEquals(Class1.class, "privateMethodOfClass1", Modifier.PRIVATE, Reflection.findMethod(Class3.class, "privateMethodOfClass1", new Class[0]));
      assertEquals(Class2.class, "publicAbstractMethodOfClass1", Modifier.PUBLIC, Reflection.findMethod(Class3.class, "publicAbstractMethodOfClass1", new Class[0]));
      assertEquals(Class2.class, "protectedAbstractMethodOfClass1", Modifier.PROTECTED, Reflection.findMethod(Class3.class, "protectedAbstractMethodOfClass1", new Class[0]));

      //
      assertEquals(Class2.class, "privateMethodOfClass2", Modifier.PRIVATE, Reflection.findMethod(Class3.class, "privateMethodOfClass2", new Class[0]));
      assertEquals(Class2.class, "publicMethodOfClass2", Modifier.PUBLIC, Reflection.findMethod(Class3.class, "publicMethodOfClass2", new Class[0]));
      assertEquals(Class2.class, "protectedMethodOfClass2", Modifier.PROTECTED, Reflection.findMethod(Class3.class, "protectedMethodOfClass2", new Class[0]));

      //
      assertEquals(Class3.class, "privateMethodOfClass3", Modifier.PRIVATE, Reflection.findMethod(Class3.class, "privateMethodOfClass3", new Class[0]));
   }

   private void assertEquals(Class declaringClass, String methodName, int modifiers, Method effectiveMethod)
   {
      assertNotNull(effectiveMethod);
      assertEquals(declaringClass, effectiveMethod.getDeclaringClass());
      assertEquals(modifiers, effectiveMethod.getModifiers());
      assertEquals(methodName, effectiveMethod.getName());
   }

   public void testSafeCast()
   {
      Integer i = 3;
      assertSame(i, Reflection.safeCast(i, Integer.class));

      //
      assertNull(Reflection.safeCast(3L, Integer.class));

      //
      ArrayList list = new ArrayList();
      assertSame(list, Reflection.safeCast(list, List.class));
      assertSame(list, Reflection.safeCast(list, AbstractList.class));

      //
      try
      {
         Reflection.safeCast(null, null);
         fail();
      }
      catch (IllegalArgumentException ignore)
      {
      }

      //
      try
      {
         Reflection.safeCast("foo", null);
         fail();
      }
      catch (IllegalArgumentException ignore)
      {
      }
   }

}
