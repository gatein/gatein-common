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
package org.jboss.portal.test.common.i18n;

import junit.framework.TestCase;

import java.util.Map;
import java.util.HashMap;
import java.util.Locale;

import org.gatein.common.i18n.LocalizedString;

/**
 * @author <a href="mailto:julien@jboss.org">Julien Viet</a>
 * @version $Revision: 6818 $
 */
public class DescriptionTestCase extends TestCase
{

   public DescriptionTestCase(String name)
   {
      super(name);
   }

   private Locale deflt = Locale.ENGLISH;
   private Locale abc = new Locale("a", "b", "c");
   private Locale ab = new Locale("a", "b");
   private Locale a = new Locale("a");

   public void testA()
   {
      Map map = new HashMap();
      map.put(abc, "abc");
      map.put(ab, "ab");
      map.put(a, "a");
      map.put(deflt, "deflt");
      LocalizedString desc = new LocalizedString(map, deflt);
      assertEquals("abc", desc.getString(abc, true));
      assertEquals("ab", desc.getString(ab, true));
      assertEquals("a", desc.getString(a, true));
   }

   public void testB()
   {
      Map map = new HashMap();
      map.put(abc, "abc");
      map.put(deflt, "deflt");
      LocalizedString desc = new LocalizedString(map, deflt);
      assertEquals("abc", desc.getString(abc, true));
      assertEquals("deflt", desc.getString(ab, true));
      assertEquals("deflt", desc.getString(a, true));
   }

   public void testC()
   {
      Map map = new HashMap();
      map.put(ab, "ab");
      map.put(deflt, "deflt");
      LocalizedString desc = new LocalizedString(map, deflt);
      assertEquals("ab", desc.getString(abc, true));
      assertEquals("ab", desc.getString(ab, true));
      assertEquals("deflt", desc.getString(a, true));
   }

   public void testD()
   {
      Map map = new HashMap();
      map.put(a, "a");
      map.put(deflt, "deflt");
      LocalizedString desc = new LocalizedString(map, deflt);
      assertEquals("a", desc.getString(abc, true));
      assertEquals("a", desc.getString(ab, true));
      assertEquals("a", desc.getString(a, true));
   }

   public void testE()
   {
      Map map = new HashMap();
      map.put(deflt, "deflt");
      LocalizedString desc = new LocalizedString(map, deflt);
      assertEquals("deflt", desc.getString(abc, true));
      assertEquals("deflt", desc.getString(ab, true));
      assertEquals("deflt", desc.getString(a, true));
   }
}
