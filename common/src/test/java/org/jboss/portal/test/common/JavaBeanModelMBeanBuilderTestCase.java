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
package org.jboss.portal.test.common;

import junit.framework.TestCase;
import org.gatein.common.mx.JavaBeanModelMBeanBuilder;

import javax.management.Attribute;
import javax.management.AttributeNotFoundException;
import javax.management.Descriptor;
import javax.management.MBeanAttributeInfo;
import javax.management.MBeanOperationInfo;
import javax.management.MBeanServer;
import javax.management.MBeanServerFactory;
import javax.management.ObjectName;
import javax.management.modelmbean.ModelMBeanInfo;
import javax.management.modelmbean.ModelMBeanOperationInfo;
import javax.management.modelmbean.RequiredModelMBean;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * @author <a href="mailto:julien@jboss.org">Julien Viet</a>
 * @author <a href="mailto:chris.laprun@jboss.com">Chris Laprun</a>
 * @version $Revision: 7452 $
 */
public class JavaBeanModelMBeanBuilderTestCase extends TestCase
{

   public void testEmpty() throws Exception
   {
      JavaBeanModelMBeanBuilder builder = new JavaBeanModelMBeanBuilder(Empty.class, Object.class);
      ModelMBeanInfo info = builder.getInfo();

      //
      MBeanAttributeInfo[] attrs = info.getAttributes();
      assertNotNull(attrs);
      assertEquals(0, attrs.length);

      //
      MBeanOperationInfo[] ops = info.getOperations();
      assertNotNull(ops);
      assertEquals(0, ops.length);
   }

   public void testSimpleClass() throws Exception
   {
      JavaBeanModelMBeanBuilder builder = new JavaBeanModelMBeanBuilder(TestClass.class, Object.class);
      ModelMBeanInfo info = builder.getInfo();

      //
      assertNotNull(info.getAttributes());
      Set attrs = getAttributes(info);

      //
      Set expectedAttrs = new HashSet();
      expectedAttrs.add(new TestAttribute("java.lang.String", "String"));
      expectedAttrs.add(new TestAttribute("boolean", "Boolean"));
      assertEquals(expectedAttrs, attrs);

      //
      assertNotNull(info.getOperations());
      Set ops = getOperations(info);

      //
      Set expectedOps = new HashSet();
      expectedOps.add(TestOperation.newOperation("void", "operation", new String[]{}));
      expectedOps.add(TestOperation.newOperation("void", "operation", new String[]{"java.lang.String"}));
      expectedOps.add(TestOperation.newSetter("setString", "java.lang.String"));
      expectedOps.add(TestOperation.newGetter("getString", "java.lang.String"));
      expectedOps.add(TestOperation.newSetter("setBoolean", "boolean"));
      expectedOps.add(TestOperation.newGetter("isBoolean", "boolean"));
      assertEquals(expectedOps, ops);
   }

   public void testOperationOverride() throws Exception
   {
      JavaBeanModelMBeanBuilder builder = new JavaBeanModelMBeanBuilder(Child.class, Parent.class);
      ModelMBeanInfo info = builder.getInfo();

      //
      assertNotNull(info.getOperations());
      Set ops = getOperations(info);

      //
      Set expectedOps = new HashSet();
      expectedOps.add(TestOperation.newOperation("void", "overridenOperation", new String[]{}));
      assertEquals(expectedOps, ops);
   }

   public void testSetterOverload() throws Exception
   {
      try
      {
         JavaBeanModelMBeanBuilder builder = new JavaBeanModelMBeanBuilder(OverloadedSetter.class, Object.class);
         fail();
      }
      catch (Exception expected)
      {
         // expected
      }
   }

   public void testOverridenGetter() throws Exception
   {
      JavaBeanModelMBeanBuilder builder = new JavaBeanModelMBeanBuilder(TestOverridenExtend.class, Object.class);
      ModelMBeanInfo info = builder.getInfo();

      //
      Set ops = getOperations(info);
      Set expectedOps = new HashSet();
      expectedOps.add(TestOperation.newGetter("getTest", "java.util.Set"));
      expectedOps.add(TestOperation.newGetter("isSet", "boolean"));
      expectedOps.add(TestOperation.newSetter("setSet", "boolean"));
      expectedOps.add(TestOperation.newSetter("setFoo", "java.lang.String"));
      assertEquals(expectedOps, ops);

   }

   public void testMismatchedPropertyOperations()
   {
      try
      {
         JavaBeanModelMBeanBuilder builder = new JavaBeanModelMBeanBuilder(TestMismatchedSetterGetter.class, Object.class);
         fail();
      }
      catch (Exception expected)
      {
         // expected
      }
   }

   public void testAttributesAreNotCached() throws Exception
   {
      AttributesAreNotCached aanc = new AttributesAreNotCached();
      ModelMBeanInfo info = JavaBeanModelMBeanBuilder.build(aanc);

      MBeanServer server = MBeanServerFactory.createMBeanServer();

      ObjectName name = new ObjectName(":a=b");
      RequiredModelMBean mbean = new RequiredModelMBean(info);
      mbean.setManagedResource(aanc, "ObjectReference");
      server.registerMBean(mbean, name);
      assertEquals(new Integer(0), server.getAttribute(name, "Count"));
      assertEquals(new Integer(1), server.getAttribute(name, "Count"));

      MBeanServerFactory.releaseMBeanServer(server);
   }

   public void testStaticAttribute() throws Exception
   {
      StaticAttribute sa = new StaticAttribute();
      ModelMBeanInfo info = JavaBeanModelMBeanBuilder.build(sa);

      MBeanServer server = MBeanServerFactory.createMBeanServer();

      ObjectName name = new ObjectName(":a=b");
      RequiredModelMBean mbean = new RequiredModelMBean(info);
      mbean.setManagedResource(sa, "ObjectReference");
      server.registerMBean(mbean, name);
      try
      {
         server.getAttribute(name, "Read");
         fail();
      }
      catch (AttributeNotFoundException expected)
      {
      }
      try
      {
         server.getAttribute(name, "ReadWrite");
         fail();
      }
      catch (AttributeNotFoundException expected)
      {
      }
      try
      {
         server.setAttribute(name, new Attribute("ReadWrite", new Integer(0)));
         fail();
      }
      catch (AttributeNotFoundException expected)
      {
      }
      try
      {
         server.setAttribute(name, new Attribute("Write", new Integer(0)));
         fail();
      }
      catch (AttributeNotFoundException expected)
      {
      }
      MBeanServerFactory.releaseMBeanServer(server);
   }

   public Set getAttributes(ModelMBeanInfo info)
   {
      Set set = new HashSet();
      MBeanAttributeInfo[] attrs = info.getAttributes();
      for (int i = 0; i < attrs.length; i++)
      {
         MBeanAttributeInfo attr = attrs[i];
         set.add(new TestAttribute(attr));
      }
      return set;
   }

   public Set getOperations(ModelMBeanInfo info)
   {
      MBeanOperationInfo[] ops = info.getOperations();
      Set set = new HashSet();
      for (int i = 0; i < ops.length; i++)
      {
         MBeanOperationInfo op = ops[i];
         set.add(new TestOperation((ModelMBeanOperationInfo)op));
      }
      return set;
   }

   public static class StaticAttribute
   {
      public static int getRead()
      {
         return 0;
      }

      public static void setWrite(int a)
      {
      }

      public static int getReadWrite()
      {
         return 0;
      }

      public static void setReadWrite(int a)
      {
      }
   }

   public static class OverloadedSetter
   {
      public void setA(int a)
      {
      }

      public void setA(String a)
      {
      }

      public boolean getA()
      {
         return false;
      }

      public boolean isA()
      {
         return false;
      }
   }

   public static class Parent
   {
      public void operation()
      {
      }

      public void overridenOperation()
      {
      }
   }

   public static class Child extends Parent
   {
      public void overridenOperation()
      {
      }
   }

   public static class Empty
   {
   }

   public static class TestClass
   {

      public void operation()
      {
         throw new UnsupportedOperationException();
      }

      public void operation(String s)
      {
         throw new UnsupportedOperationException();
      }

      public String getString()
      {
         throw new UnsupportedOperationException();
      }

      public void setString(String s)
      {
         throw new UnsupportedOperationException();
      }

      public boolean isBoolean()
      {
         throw new UnsupportedOperationException();
      }

      public void setBoolean(boolean b)
      {
         throw new UnsupportedOperationException();
      }
   }

   public class AttributesAreNotCached
   {
      private int count;

      public int getCount()
      {
         return count++;
      }
   }

   public class TestOverridenBase
   {
      public Set getTest()
      {
         //nothing
         return new HashSet();
      }

      public boolean isSet()
      {
         return false;
      }

      public void setSet(boolean set)
      {
         // nothing
      }

      public void setFoo(String foo)
      {
         // nothing
      }
   }

   public class TestOverridenExtend extends TestOverridenBase
   {
      public Set getTest()
      {
         //nothing
         return new HashSet();
      }

      public boolean isSet()
      {
         return true;
      }

      public void setFoo(String foo)
      {
         // nothing
      }
   }

   public class TestMismatchedSetterGetter
   {
      public void setFoo(String foo)
      {
         // nothing
      }

      public boolean getFoo()
      {
         return false;
      }
   }

   public static class TestAttribute
   {

      /** . */
      public final String type;

      /** . */
      public final String name;

      public TestAttribute(MBeanAttributeInfo info)
      {
         this.type = info.getType();
         this.name = info.getName();
      }

      public TestAttribute(String type, String name)
      {
         if (type == null)
         {
            throw new IllegalArgumentException();
         }
         if (name == null)
         {
            throw new IllegalArgumentException();
         }
         this.type = type;
         this.name = name;
      }

      public int hashCode()
      {
         return type.hashCode() * 43 + name.hashCode();
      }

      public boolean equals(Object obj)
      {
         TestAttribute that = (TestAttribute)obj;
         return type.equals(that.type) && name.equals(that.name);
      }

      public String toString()
      {
         return "Attribute[" + type + "," + name + "]";
      }
   }

   public static class TestOperation
   {

      /** . */
      public final String returnType;

      /** . */
      public final String name;

      /** . */
      public final String[] argTypes;

      /** . */
      public final String role;

      public TestOperation(ModelMBeanOperationInfo info)
      {
         Descriptor desc = info.getDescriptor();

         this.returnType = info.getReturnType();
         this.name = info.getName();
         this.argTypes = new String[info.getSignature().length];
         this.role = (String)desc.getFieldValue("role");
         for (int i = 0; i < argTypes.length; i++)
         {
            argTypes[i] = info.getSignature()[i].getType();
         }
      }

      public static TestOperation newOperation(String returnType, String name, String[] argTypes)
      {
         return new TestOperation(returnType, name, argTypes, "operation");
      }

      public static TestOperation newGetter(String name, String type)
      {
         return new TestOperation(type, name, new String[0], "getter");
      }

      public static TestOperation newSetter(String name, String type)
      {
         return new TestOperation("void", name, new String[]{type}, "setter");
      }

      public TestOperation(String returnType, String name, String[] argTypes, String role)
      {
         if (returnType == null)
         {
            throw new IllegalArgumentException();
         }
         if (name == null)
         {
            throw new IllegalArgumentException();
         }
         if (argTypes == null)
         {
            throw new IllegalArgumentException();
         }
         for (int i = 0; i < argTypes.length; i++)
         {
            if (argTypes[i] == null)
            {
               throw new IllegalArgumentException();
            }
         }
         this.returnType = returnType;
         this.name = name;
         this.argTypes = argTypes;
         this.role = role;
      }

      public int hashCode()
      {
         int code = (returnType.hashCode() * 43 + name.hashCode()) * 43 + role.hashCode();
         for (int i = 0; i < argTypes.length; i++)
         {
            String argType = argTypes[i];
            code = code * 43 + argType.hashCode();
         }
         return code;
      }

      public boolean equals(Object obj)
      {
         TestOperation that = (TestOperation)obj;
         return returnType.equals(that.returnType) && name.equals(that.name) && role.equals(that.role) && Arrays.equals(argTypes, that.argTypes);
      }

      public String toString()
      {
         StringBuffer tmp = new StringBuffer("Operation[").append(returnType).append(",").append(name).append(",").append(role);
         for (int i = 0; i < argTypes.length; i++)
         {
            tmp.append(",").append(argTypes[i]);
         }
         tmp.append(")");
         return tmp.toString();
      }
   }
}
