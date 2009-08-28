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
package org.jboss.portal.test.common.adapter;

import junit.framework.TestCase;
import org.gatein.common.adapter.ClassAdapter;
import org.gatein.common.adapter.ClassAdapted;
import org.gatein.common.adapter.ClassAdaptable;

/**
 * @author <a href="mailto:julien@jboss.org">Julien Viet</a>
 * @version $Revision: 1.1 $
 */
public class AdapterTestCase extends TestCase
{

   Adapted1 adapted1;
   Adapted2 adapted2;
   ObjectAdapted objectAdapted;
   ClassAdaptable adaptable;

   protected void setUp() throws Exception
   {
      adapted1 = new Adapted1();
      adapted2 = new Adapted2();
      objectAdapted = new ObjectAdapted();
      ClassAdapted[] adapteds = new ClassAdapted[]{new ClassAdapted(Business1.class, adapted1),new ClassAdapted(Business2.class, adapted2)};
      ClassAdapter adapter = new ClassAdapter(Thread.currentThread().getContextClassLoader(), adapteds, objectAdapted);
      adaptable = adapter.getAdaptable();
   }


   protected void tearDown() throws Exception
   {
      adapted1 = null;
      adapted2 = null;
      objectAdapted = null;
      adaptable = null;
   }

   public void testImplementedInterfaces()
   {
      assertTrue(adaptable instanceof Business1);
      assertTrue(adaptable instanceof Business2);
   }

   public void testObjectMethodCall() throws Exception
   {
      assertEquals(0, adapted1.business1MethodCount);
      assertEquals(0, adapted1.commonMethodCount);
      assertEquals(0, adapted2.business2MethodCount);
      assertEquals(0, adapted2.commonMethodCount);
      assertEquals(0, objectAdapted.toStringCount);
      assertEquals(0, objectAdapted.hashCodeCount);
      assertEquals(0, objectAdapted.equalsCount);

      //
      assertEquals("Foo", adaptable.toString());
      assertEquals(0, adapted1.business1MethodCount);
      assertEquals(0, adapted1.commonMethodCount);
      assertEquals(0, adapted2.business2MethodCount);
      assertEquals(0, adapted2.commonMethodCount);
      assertEquals(1, objectAdapted.toStringCount);
      assertEquals(0, objectAdapted.hashCodeCount);
      assertEquals(0, objectAdapted.equalsCount);

      //
      assertEquals(1234, adaptable.hashCode());
      assertEquals(0, adapted1.business1MethodCount);
      assertEquals(0, adapted1.commonMethodCount);
      assertEquals(0, adapted2.business2MethodCount);
      assertEquals(0, adapted2.commonMethodCount);
      assertEquals(1, objectAdapted.toStringCount);
      assertEquals(1, objectAdapted.hashCodeCount);
      assertEquals(0, objectAdapted.equalsCount);

      //
      assertEquals(true, adaptable.equals(Boolean.TRUE));
      assertEquals(0, adapted1.business1MethodCount);
      assertEquals(0, adapted1.commonMethodCount);
      assertEquals(0, adapted2.business2MethodCount);
      assertEquals(0, adapted2.commonMethodCount);
      assertEquals(1, objectAdapted.toStringCount);
      assertEquals(1, objectAdapted.hashCodeCount);
      assertEquals(1, objectAdapted.equalsCount);

      //
      assertEquals(false, adaptable.equals(Boolean.FALSE));
      assertEquals(0, adapted1.business1MethodCount);
      assertEquals(0, adapted1.commonMethodCount);
      assertEquals(0, adapted2.business2MethodCount);
      assertEquals(0, adapted2.commonMethodCount);
      assertEquals(1, objectAdapted.toStringCount);
      assertEquals(1, objectAdapted.hashCodeCount);
      assertEquals(2, objectAdapted.equalsCount);
   }

   public void testBusinessMethodCall() throws Exception
   {
      assertEquals(0, adapted1.business1MethodCount);
      assertEquals(0, adapted1.commonMethodCount);
      assertEquals(0, adapted2.business2MethodCount);
      assertEquals(0, adapted2.commonMethodCount);
      assertEquals(0, objectAdapted.toStringCount);
      assertEquals(0, objectAdapted.hashCodeCount);
      assertEquals(0, objectAdapted.equalsCount);

      //
      Business1 business1Adapter = (Business1)adaptable;
      Business2 business2Adapter = (Business2)adaptable;

      //
      business1Adapter.business1Method();
      assertEquals(1, adapted1.business1MethodCount);
      assertEquals(0, adapted1.commonMethodCount);
      assertEquals(0, adapted2.business2MethodCount);
      assertEquals(0, adapted2.commonMethodCount);
      assertEquals(0, objectAdapted.toStringCount);
      assertEquals(0, objectAdapted.hashCodeCount);
      assertEquals(0, objectAdapted.equalsCount);

      //
      business2Adapter.business2Method();
      assertEquals(1, adapted1.business1MethodCount);
      assertEquals(0, adapted1.commonMethodCount);
      assertEquals(1, adapted2.business2MethodCount);
      assertEquals(0, adapted2.commonMethodCount);
      assertEquals(0, objectAdapted.toStringCount);
      assertEquals(0, objectAdapted.hashCodeCount);
      assertEquals(0, objectAdapted.equalsCount);

      //
      business1Adapter.commonMethod();
      assertEquals(1, adapted1.business1MethodCount);
      assertEquals(1, adapted1.commonMethodCount);
      assertEquals(1, adapted2.business2MethodCount);
      assertEquals(0, adapted2.commonMethodCount);
      assertEquals(0, objectAdapted.toStringCount);
      assertEquals(0, objectAdapted.hashCodeCount);
      assertEquals(0, objectAdapted.equalsCount);
   }
}
