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
package org.gatein.common.junit.ant;

import org.apache.tools.ant.taskdefs.optional.junit.XMLJUnitResultFormatter;
import org.apache.tools.ant.taskdefs.optional.junit.JUnitTest;
import org.w3c.dom.Element;

import java.lang.reflect.Field;
import java.util.Map;

/**
 * @author <a href="mailto:julien@jboss.org">Julien Viet</a>
 * @version $Revision: 1.1 $
 */
public class ConfigurableXMLJUnitResultFormatter extends XMLJUnitResultFormatter
{

   /** . */
   private static final Field f;

   static
   {
      try
      {
         f = XMLJUnitResultFormatter.class.getDeclaredField("rootElement");
         if (!f.isAccessible())
         {
            f.setAccessible(true);
         }
      }
      catch (NoSuchFieldException e)
      {
         throw new Error(e);
      }
   }

   public void startTestSuite(JUnitTest suite)
   {
      super.startTestSuite(suite);

      //
      Map parameters = TestParameter.readExternalParameters();
      if (parameters != null)
      {
         String[] values = (String[])parameters.get(TestParameter.TEST_ID_PARAM);
         if (values != null && values.length > 0)
         {
            try
            {
               String id = values[0];
               Element rootElement = (Element)f.get(this);
               rootElement.setAttribute("name", id);
            }
            catch (IllegalAccessException e)
            {
               throw new Error(e);
            }
         }
      }
   }
}
