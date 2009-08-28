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
import org.gatein.common.reflect.Modifier;

/**
 * @author <a href="mailto:julien@jboss.org">Julien Viet</a>
 * @version $Revision: 1.1 $
 */
public class ModifierTestCase extends TestCase
{

   public void testIsReadableProperty() throws Exception
   {
      try
      {
         Modifier.isReadableProperty(null);
         fail("Was expecting an IAE");
      }
      catch (IllegalArgumentException expected)
      {
      }

      assertTrue(Modifier.isReadableProperty(Bean.class.getDeclaredField("publicField")));
      assertFalse(Modifier.isReadableProperty(Bean.class.getDeclaredField("privateField")));
      assertFalse(Modifier.isReadableProperty(Bean.class.getDeclaredField("protectedField")));
      assertFalse(Modifier.isReadableProperty(Bean.class.getDeclaredField("packageProtectedField")));

      assertTrue(Modifier.isReadableProperty(Bean.class.getDeclaredField("finalPublicField")));
      assertFalse(Modifier.isReadableProperty(Bean.class.getDeclaredField("finalPrivateField")));
      assertFalse(Modifier.isReadableProperty(Bean.class.getDeclaredField("finalProtectedField")));
      assertFalse(Modifier.isReadableProperty(Bean.class.getDeclaredField("finalPackageProtectedField")));

      assertFalse(Modifier.isReadableProperty(Bean.class.getDeclaredField("staticPublicField")));
      assertFalse(Modifier.isReadableProperty(Bean.class.getDeclaredField("staticPrivateField")));
      assertFalse(Modifier.isReadableProperty(Bean.class.getDeclaredField("staticProtectedField")));
      assertFalse(Modifier.isReadableProperty(Bean.class.getDeclaredField("staticPackageProtectedField")));

      assertFalse(Modifier.isReadableProperty(Bean.class.getDeclaredField("finalStaticPublicField")));
      assertFalse(Modifier.isReadableProperty(Bean.class.getDeclaredField("finalStaticPrivateField")));
      assertFalse(Modifier.isReadableProperty(Bean.class.getDeclaredField("finalStaticProtectedField")));
      assertFalse(Modifier.isReadableProperty(Bean.class.getDeclaredField("finalStaticPackageProtectedField")));
   }

   public void testIsWritableProperty() throws Exception
   {
      try
      {
         Modifier.isWritableProperty(null);
         fail("Was expecting an IAE");
      }
      catch (IllegalArgumentException expected)
      {
      }

      assertTrue(Modifier.isWritableProperty(Bean.class.getDeclaredField("publicField")));
      assertFalse(Modifier.isWritableProperty(Bean.class.getDeclaredField("privateField")));
      assertFalse(Modifier.isWritableProperty(Bean.class.getDeclaredField("protectedField")));
      assertFalse(Modifier.isWritableProperty(Bean.class.getDeclaredField("packageProtectedField")));

      assertFalse(Modifier.isWritableProperty(Bean.class.getDeclaredField("finalPublicField")));
      assertFalse(Modifier.isWritableProperty(Bean.class.getDeclaredField("finalPrivateField")));
      assertFalse(Modifier.isWritableProperty(Bean.class.getDeclaredField("finalProtectedField")));
      assertFalse(Modifier.isWritableProperty(Bean.class.getDeclaredField("finalPackageProtectedField")));

      assertFalse(Modifier.isWritableProperty(Bean.class.getDeclaredField("staticPublicField")));
      assertFalse(Modifier.isWritableProperty(Bean.class.getDeclaredField("staticPrivateField")));
      assertFalse(Modifier.isWritableProperty(Bean.class.getDeclaredField("staticProtectedField")));
      assertFalse(Modifier.isWritableProperty(Bean.class.getDeclaredField("staticPackageProtectedField")));

      assertFalse(Modifier.isWritableProperty(Bean.class.getDeclaredField("finalStaticPublicField")));
      assertFalse(Modifier.isWritableProperty(Bean.class.getDeclaredField("finalStaticPrivateField")));
      assertFalse(Modifier.isWritableProperty(Bean.class.getDeclaredField("finalStaticProtectedField")));
      assertFalse(Modifier.isWritableProperty(Bean.class.getDeclaredField("finalStaticPackageProtectedField")));
   }

   private static class Bean
   {
      public Object publicField;
      private Object privateField;
      protected Object protectedField;
      Object packageProtectedField;

      final public Object finalPublicField = new Object();
      final private Object finalPrivateField = new Object();
      final protected Object finalProtectedField = new Object();
      final Object finalPackageProtectedField = new Object();

      static public Object staticPublicField;
      static private Object staticPrivateField;
      static protected Object staticProtectedField;
      static Object staticPackageProtectedField;

      final static public Object finalStaticPublicField = new Object();
      final static private Object finalStaticPrivateField = new Object();
      final static protected Object finalStaticProtectedField = new Object();
      final static Object finalStaticPackageProtectedField = new Object();
   }
}
