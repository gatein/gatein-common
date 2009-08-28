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
package org.jboss.portal.test.common.io;

import org.gatein.common.io.Serialization;
import org.gatein.common.io.IOTools;
import org.gatein.common.junit.ExtendedAssert;
import org.gatein.common.util.MapBuilder;

import java.util.Map;
import java.util.HashMap;

import junit.framework.TestCase;

/**
 * @author <a href="mailto:julien@jboss.org">Julien Viet</a>
 * @version $Revision: 630 $
 */
public class SerializationTestCase extends TestCase
{

   public void testParameterMapSerialization()
   {
      check(new HashMap<String, String[]>());
      check(MapBuilder.hashMap("foo", new String[]{"foo_value"}).get());
      check(MapBuilder.hashMap("foo", new String[]{"foo_value1","foo_value2"}).get());
      check(MapBuilder.hashMap("foo", new String[]{"foo_value1","foo_value2"}).put("bar", new String[]{"bar_value"}).get());
   }

   private void check(Map<String, String[]> map)
   {
      byte[] bytes = IOTools.serialize(Serialization.PARAMETER_MAP, map);
      Map<String, String[]> copy = IOTools.unserialize(Serialization.PARAMETER_MAP, bytes);
      assertEquals(map.keySet(), copy.keySet());
      for (Map.Entry<String, String[]> entry : map.entrySet())
      {
         String[] values = map.get(entry.getKey());
         String[] valuesCopy = copy.get(entry.getKey());
         ExtendedAssert.assertEquals(values, valuesCopy);
      }
   }
}
