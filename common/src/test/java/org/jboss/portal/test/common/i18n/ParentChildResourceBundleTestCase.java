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

import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.Vector;

import junit.framework.TestCase;

import org.gatein.common.i18n.ParentChildResourceBundle;

/**
 * @author <a href="mailto:julien@jboss.org">Julien Viet</a>
 * @version $Revision: 6818 $
 */
public class ParentChildResourceBundleTestCase extends TestCase
{
   public ParentChildResourceBundleTestCase(String name)
   {
      super(name);
   }

   public void testBasic()
   {
      ResourceBundle parent = new ResourceBundle()
      {
         protected Object handleGetObject(String key)
         {
            if ("key-a".equals(key))
            {
               return "parent-a";
            }
            if ("key-b".equals(key))
            {
               return "parent-b";
            }
            return null;
         }
         public Enumeration getKeys()
         {
            Vector keys = new Vector();
            keys.add("key-a");
            keys.add("key-b");
            return keys.elements();
         }
         public Locale getLocale()
         {
            return Locale.CHINA;
         }
      };
      ResourceBundle child = new ResourceBundle()
      {
         protected Object handleGetObject(String key)
         {
            if ("key-a".equals(key))
            {
               return "child-a";
            }
            if ("key-c".equals(key))
            {
               return "child-c";
            }
            return null;
         }
         public Enumeration getKeys()
         {
            Vector keys = new Vector();
            keys.add("key-a");
            keys.add("key-c");
            return keys.elements();
         }
         public Locale getLocale()
         {
            return Locale.GERMAN;
         }
      };
      ResourceBundle bundle = new ParentChildResourceBundle(parent, child);
      assertEquals(Locale.GERMAN, bundle.getLocale());
      assertEquals("child-a", bundle.getString("key-a"));
      assertEquals("parent-b", bundle.getString("key-b"));
      assertEquals("child-c", bundle.getString("key-c"));
      Set keys = new HashSet(Arrays.asList(new Object[]{"key-a","key-b","key-c"}));
      for (Enumeration e = bundle.getKeys();e.hasMoreElements();)
      {
         assertTrue(keys.remove(e.nextElement()));
      }
      assertTrue(keys.isEmpty());
   }

}
