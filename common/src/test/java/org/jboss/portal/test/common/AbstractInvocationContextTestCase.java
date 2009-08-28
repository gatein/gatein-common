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
import org.gatein.common.invocation.AbstractInvocationContext;
import org.gatein.common.invocation.Scope;
import org.gatein.common.invocation.AttributeResolver;

import java.util.HashMap;
import java.util.Set;

/**
 * @author <a href="mailto:julien@jboss.org">Julien Viet</a>
 * @version $Revision: 1.1 $
 */
public class AbstractInvocationContextTestCase extends TestCase
{

   /** . */
   private static final Scope TEST_SCOPE = new Scope("test");

   public void testNonExistingScope()
   {
      AbstractInvocationContext ctx = new AbstractInvocationContext();
      try
      {
         ctx.getAttribute(TEST_SCOPE, "foo");
         fail("was expecting an IllegalArgumentException");
      }
      catch (IllegalArgumentException expected)
      {

      }
      try
      {
         ctx.setAttribute(TEST_SCOPE, "foo", "bar");
         fail("was expecting an IllegalArgumentException");
      }
      catch (IllegalArgumentException expected)
      {

      }
      try
      {
         ctx.removeAttribute(TEST_SCOPE, "foo");
         fail("was expecting an IllegalArgumentException");
      }
      catch (IllegalArgumentException expected)
      {

      }
      try
      {
         ctx.getAttributeResolver(TEST_SCOPE);
         fail("was expecting an IllegalArgumentException");
      }
      catch (IllegalArgumentException expected)
      {

      }
   }

   public void testExistingScopeWithResolver()
   {
      final MapResolver resolver = new MapResolver();
      AbstractInvocationContext ctx = new AbstractInvocationContext()
      {
         {
            addResolver(TEST_SCOPE, resolver);
         }
      };
      testExistingScope(ctx, resolver);
   }

   public void testExistingScopeDelegation()
   {
      final MapResolver resolver = new MapResolver();
      final AbstractInvocationContext parent = new AbstractInvocationContext()
      {
         {
            addResolver(TEST_SCOPE, resolver);
         }
      };
      AbstractInvocationContext child = new AbstractInvocationContext()
      {
         {
            addResolver(TEST_SCOPE, parent);
         }
      };
      testExistingScope(child, resolver);
   }


   private void testExistingScope(AbstractInvocationContext ctx, MapResolver resolver)
   {
      // Assert empty does not exist
      assertNull(ctx.getAttribute(TEST_SCOPE, "foo"));
      assertEquals(0, resolver.size());

      // Remove non existing
      ctx.removeAttribute(TEST_SCOPE, "foo");
      assertNull(ctx.getAttribute(TEST_SCOPE, "foo"));
      assertEquals(0, resolver.size());

      // Set non existing
      ctx.setAttribute(TEST_SCOPE, "foo", "bar");
      assertEquals("bar", resolver.getAttribute("foo"));
      assertEquals(1, resolver.size());

      // Overwrite existing
      ctx.setAttribute(TEST_SCOPE, "foo", "bar2");
      assertEquals("bar2", resolver.getAttribute("foo"));
      assertEquals(1, resolver.size());

      // Remove existing
      ctx.removeAttribute(TEST_SCOPE, "foo");
      assertNull(ctx.getAttribute(TEST_SCOPE, "foo"));
      assertEquals(0, resolver.size());

      // Get resolver
      assertEquals(resolver, ctx.getAttributeResolver(TEST_SCOPE));
   }

   public void testAPI()
   {
      AbstractInvocationContext ctx = new AbstractInvocationContext();
      try
      {
         ctx.getAttribute(null, "foo");
         fail("was expecting an IllegalArgumentException");
      }
      catch (IllegalArgumentException expected)
      {
      }
      try
      {
         ctx.getAttribute(TEST_SCOPE, null);
         fail("was expecting an IllegalArgumentException");
      }
      catch (IllegalArgumentException expected)
      {
      }
      try
      {
         ctx.setAttribute(null, "foo", "bar");
         fail("was expecting an IllegalArgumentException");
      }
      catch (IllegalArgumentException expected)
      {
      }
      try
      {
         ctx.setAttribute(TEST_SCOPE, null, "bar");
         fail("was expecting an IllegalArgumentException");
      }
      catch (IllegalArgumentException expected)
      {
      }
      try
      {
         ctx.setAttribute(null, "foo", null);
         fail("was expecting an IllegalArgumentException");
      }
      catch (IllegalArgumentException expected)
      {
      }
      try
      {
         ctx.setAttribute(TEST_SCOPE, null, null);
         fail("was expecting an IllegalArgumentException");
      }
      catch (IllegalArgumentException expected)
      {
      }
      try
      {
         ctx.removeAttribute(null, "foo");
         fail("was expecting an IllegalArgumentException");
      }
      catch (IllegalArgumentException expected)
      {
      }
      try
      {
         ctx.removeAttribute(TEST_SCOPE, null);
         fail("was expecting an IllegalArgumentException");
      }
      catch (IllegalArgumentException expected)
      {
      }
      try
      {
         ctx.getAttributeResolver(null);
         fail("was expecting an IllegalArgumentException");
      }
      catch (IllegalArgumentException expected)
      {
      }
   }

   private static class MapResolver extends HashMap implements AttributeResolver
   {
      public Set getKeys()
      {
         return keySet();
      }
      public Object getAttribute(Object attrKey) throws IllegalArgumentException
      {
         if (attrKey == null)
         {
            throw new IllegalArgumentException();
         }
         return get(attrKey);
      }
      public void setAttribute(Object attrKey, Object attrValue) throws IllegalArgumentException
      {
         if (attrKey == null)
         {
            throw new IllegalArgumentException();
         }
         if (attrValue != null)
         {
            put(attrKey, attrValue);
         }
         else
         {
            remove(attrKey);
         }
      }
   }
}
