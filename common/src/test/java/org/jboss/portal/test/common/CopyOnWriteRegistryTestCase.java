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
import org.gatein.common.util.CopyOnWriteRegistry;

import java.util.Collections;
import java.util.Set;
import java.util.List;
import java.util.ArrayList;

/**
 * @author <a href="mailto:julien@jboss.org">Julien Viet</a>
 * @version $Revision: 1.1 $
 */
public class CopyOnWriteRegistryTestCase extends TestCase
{

   private CopyOnWriteRegistry registry;
   Object key;
   Object registered1;
   Object registered2;


   protected void setUp() throws Exception
   {
      registry = new CopyOnWriteRegistry();
      key = new Object();
      registered1 = new Object();
      registered2 = new Object();
   }

   protected void tearDown() throws Exception
   {
      registry = null;
      key = null;
      registered1 = null;
      registered2 = null;
   }

   public void testRegisterThrowsIAE()
   {
      try
      {
         registry.register(null, registered1);
         fail("Was expecting an IAE");
      }
      catch (IllegalArgumentException expected)
      {
      }
      try
      {
         registry.register(registered1, null);
         fail("Was expecting an IAE");
      }
      catch (IllegalArgumentException expected)
      {
      }
      try
      {
         registry.register(null, null);
         fail("Was expecting an IAE");
      }
      catch (IllegalArgumentException expected)
      {
      }
   }

   public void testUnregisterThrowsIAE()
   {
      try
      {
         registry.unregister(null);
         fail("Was expecting an IAE");
      }
      catch (IllegalArgumentException expected)
      {
      }
   }

   public void testGetThrowsIAE()
   {
      try
      {
         registry.getRegistration(null);
         fail("Was expecting an IAE");
      }
      catch (IllegalArgumentException expected)
      {
      }
   }

   public void testDualRegistration()
   {
      assertTrue(registry.register(key, registered1));
      assertFalse(registry.register(key, registered2));
      assertEquals(registered1, registry.getRegistration(key));
   }

   public void testUnregisterNonRegistered()
   {
      assertNull(registry.unregister(key));
   }

   public void testNormal()
   {
      assertEquals(Collections.EMPTY_SET, registry.getKeys());
      assertEquals(Collections.EMPTY_LIST, new ArrayList(registry.getRegistrations()));
      assertTrue(registry.register(key, registered1));
      assertEquals(Collections.singleton(key), registry.getKeys());
      assertEquals(Collections.singletonList(registered1), new ArrayList(registry.getRegistrations()));
      assertEquals(registered1, registry.getRegistration(key));
      assertEquals(registered1, registry.unregister(key));
      assertEquals(null, registry.getRegistration(key));
      assertEquals(Collections.EMPTY_SET, registry.getKeys());
      assertEquals(Collections.EMPTY_LIST, new ArrayList(registry.getRegistrations()));
   }

   public void testCopyOnWrite()
   {
      Set keys = registry.getKeys();
      List registrations = new ArrayList(registry.getRegistrations());
      assertEquals(Collections.EMPTY_SET, keys);
      assertEquals(Collections.EMPTY_LIST, registrations);
      assertTrue(registry.register(key, registered1));
      assertEquals(Collections.EMPTY_SET, keys);
      assertEquals(Collections.EMPTY_LIST, registrations);
      keys = registry.getKeys();
      registrations = new ArrayList(registry.getRegistrations());
      assertEquals(Collections.singleton(key), keys);
      assertEquals(Collections.singletonList(registered1), registrations);
      assertEquals(registered1, registry.unregister(key));
      assertEquals(Collections.singleton(key), keys);
      assertEquals(Collections.singletonList(registered1), registrations);
      keys = registry.getKeys();
      registrations = new ArrayList(registry.getRegistrations());
      assertEquals(Collections.EMPTY_SET, keys);
      assertEquals(Collections.EMPTY_LIST, registrations);
   }

   public void testKeysAreNotModifiable()
   {
      registry.register(key, registered1);
      try
      {
         registry.getKeys().clear();
         fail("Was expecting UnsupportedOperationException");
      }
      catch (UnsupportedOperationException expected)
      {
      }
   }

   public void testRegistrationsAreNotModifiable()
   {
      registry.register(key, registered1);
      try
      {
         registry.getRegistrations().clear();
         fail("Was expecting UnsupportedOperationException");
      }
      catch (UnsupportedOperationException expected)
      {
      }
   }
}
