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
package org.jboss.portal.test.common.util;

import junit.framework.TestCase;
import org.gatein.common.util.SimpleMultiValuedPropertyMap;
import org.gatein.common.util.Tools;

/**
 * @author <a href="mailto:julien@jboss.org">Julien Viet</a>
 * @version $Revision: 630 $
 */
public class MultiValuedPropertyMapTestCase extends TestCase
{

   public MultiValuedPropertyMapTestCase()
   {
   }

   public MultiValuedPropertyMapTestCase(String name)
   {
      super(name);
   }

   public void testA()
   {
      SimpleMultiValuedPropertyMap<String> props = new SimpleMultiValuedPropertyMap<String>();

      props.setValue("name1", "value1");

      props.addValue("name2", "value2");

      props.setValue("name3", "value3-1");
      props.addValue("name3", "value3-2");

      props.addValue("name4", "value4-1");
      props.addValue("name4", "value4-2");

      props.setValue("name5", "value5-1");
      props.setValue("name5", "value5-2");

      props.addValue("name6", "value6-1");
      props.setValue("name6", "value6-2");

      assertEquals("value1", props.getValue("name1"));
      assertEquals(1, props.getValues("name1").size());
      assertEquals("value1", props.getValues("name1").get(0));

      assertEquals("value2", props.getValue("name2"));
      assertEquals(1, props.getValues("name2").size());
      assertEquals("value2", props.getValues("name2").get(0));

      assertEquals("value3-1", props.getValue("name3"));
      assertEquals(2, props.getValues("name3").size());
      assertEquals("value3-1", props.getValues("name3").get(0));
      assertEquals("value3-2", props.getValues("name3").get(1));

      assertEquals("value4-1", props.getValue("name4"));
      assertEquals(2, props.getValues("name4").size());
      assertEquals("value4-1", props.getValues("name4").get(0));
      assertEquals("value4-2", props.getValues("name4").get(1));

      assertEquals(1, props.getValues("name5").size());
      assertEquals("value5-2", props.getValues("name5").get(0));

      assertEquals("value6-2", props.getValue("name6"));
      assertEquals(1, props.getValues("name6").size());
      assertEquals("value6-2", props.getValues("name6").get(0));

      assertEquals(null, props.getValues("not here"));
      assertNull(props.getValue("not here"));
      assertEquals(Tools.toSet("name1", "name2", "name3", "name4", "name5", "name6"), props.keySet());

      try
      {
         props.setValue(null, "not null");
         fail("Expected IllegalArgumentException");
      }
      catch (IllegalArgumentException e)
      {
      }

      try
      {
         props.setValue("not null", null);
         fail("Expected IllegalArgumentException");
      }
      catch (IllegalArgumentException e)
      {
      }

      try
      {
         props.setValue(null, null);
         fail("Expected IllegalArgumentException");
      }
      catch (IllegalArgumentException e)
      {
      }
   }

   public void testAppend1()
   {
      SimpleMultiValuedPropertyMap<String> props = new SimpleMultiValuedPropertyMap<String>();
      SimpleMultiValuedPropertyMap<String> appended = new SimpleMultiValuedPropertyMap<String>();
      appended.setValue("foo", "bar");
      props.append(appended);

      //
      assertEquals(Tools.toSet("foo"), props.keySet());
      assertEquals(Tools.toList("bar"), props.getValues("foo"));
   }

   public void testAppend2()
   {
      SimpleMultiValuedPropertyMap<String> props = new SimpleMultiValuedPropertyMap<String>();
      props.setValue("foo", "bar");

      //
      SimpleMultiValuedPropertyMap<String> appended = new SimpleMultiValuedPropertyMap<String>();
      props.append(appended);

      //
      assertEquals(Tools.toSet("foo"), props.keySet());
      assertEquals(Tools.toList("bar"), props.getValues("foo"));
   }

   public void testAppend3()
   {
      SimpleMultiValuedPropertyMap<String> props = new SimpleMultiValuedPropertyMap<String>();
      props.setValue("foo", "foo1");
      props.setValue("bar", "bar1");
      props.addValue("bar", "bar2");
      props.setValue("juu", "juu1");
      props.setValue("daa", "daa1");
      props.addValue("daa", "daa2");

      //
      SimpleMultiValuedPropertyMap<String> appended = new SimpleMultiValuedPropertyMap<String>();
      appended.setValue("juu", "juu2");
      appended.addValue("juu", "juu3");
      appended.setValue("daa", "daa3");
      appended.setValue("zoo", "zoo1");
      appended.setValue("tee", "tee1");
      appended.addValue("tee", "tee2");


      //
      props.append(appended);

      //
      assertEquals(Tools.toSet("foo","bar","juu","daa","zoo","tee"), props.keySet());
      assertEquals(Tools.toList("foo1"), props.getValues("foo"));
      assertEquals(Tools.toList("bar1", "bar2"), props.getValues("bar"));
      assertEquals(Tools.toList("juu1", "juu2","juu3"), props.getValues("juu"));
      assertEquals(Tools.toList("daa1", "daa2","daa3"), props.getValues("daa"));
      assertEquals(Tools.toList("zoo1"), props.getValues("zoo"));
      assertEquals(Tools.toList("tee1","tee2"), props.getValues("tee"));
   }

}
