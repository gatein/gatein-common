/******************************************************************************
 * JBoss, a division of Red Hat                                               *
 * Copyright 2008, Red Hat Middleware, LLC, and individual                    *
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
package org.jboss.portal.test.common.util;

import junit.framework.TestCase;
import org.gatein.common.util.MapAdapters;

import java.util.Map;
import java.util.HashMap;

/**
 * @author <a href="mailto:julien@jboss-portal.org">Julien Viet</a>
 * @version $Revision: 630 $
 */
public class MapAdaptersTestCase extends TestCase
{

   /** . */
   Map<String, String[]> adapted;

   /** . */
   Map<String, String> adapter;

   protected void setUp() throws Exception
   {
      adapted = new HashMap<String, String[]>();
      adapter = MapAdapters.adapt(adapted);
   }

   public void testGet()
   {
      adapted.put("daa", null);
      adapted.put("foo", new String[]{});
      adapted.put("bar", new String[]{"bar_1"});
      adapted.put("juu", new String[]{"juu_1","juu_2"});

      //
      try
      {
         adapter.get("foo");
         fail();
      }
      catch (IllegalStateException ignore)
      {
      }
      try
      {
         adapter.get("daa");
         fail();
      }
      catch (IllegalStateException ignore)
      {
      }

      //
      assertEquals("bar_1", adapter.get("bar"));
      assertEquals("juu_1", adapter.get("juu"));
   }

   public void testPut1()
   {
      try
      {
         adapter.put("foo", null);
         fail();
      }
      catch (NullPointerException e)
      {
      }
   }

   public void testPut2()
   {
      adapted.put("foo", new String[]{"bar_1","bar_2"});
      assertEquals("bar_1", adapter.put("foo", "foo_1"));
      assertEquals(1, adapted.size());
      String[] value = adapted.get("foo");
      assertNotNull(value);
      assertEquals(1, value.length);
      assertEquals("foo_1", value[0]);
   }

   public void testPut3()
   {
      adapted.put("foo", new String[]{"bar_1","bar_2"});
      try
      {
         adapter.put("foo", null);
         fail();
      }
      catch (NullPointerException e)
      {
         assertEquals(1, adapted.size());
         String[] value = adapted.get("foo");
         assertNotNull(value);
         assertEquals(2, value.length);
         assertEquals("bar_1", value[0]);
         assertEquals("bar_2", value[1]);
      }
   }

}
