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

import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Map;

/**
 * @author <a href="mailto:julien@jboss.org">Julien Viet</a>
 * @version $Revision: 7228 $
 */
public class TestParameter
{

   /** . */
   static final String TEST_ID_PARAM = "test.param.id";

   /** . */
   static final String PARAMETRIZATION_FILE_NAME = "junit.parameters";

   /** . */
   protected String name;

   /** . */
   protected String value;

   /** . */
   protected ArrayList nestedValues = new ArrayList();

   public void setName(String name)
   {
      this.name = name;
   }

   public void setValue(String value)
   {
      this.value = value;
   }

   public void addValue(TestParameterValue value)
   {
      nestedValues.add(value);
   }

   public static Map readExternalParameters()
   {
      try
      {
         File tmp = new File(System.getProperty("java.io.tmpdir"), PARAMETRIZATION_FILE_NAME);
         if (tmp.exists() && tmp.isFile())
         {
            FileInputStream fis = new FileInputStream(tmp);
            ObjectInputStream ois = new ObjectInputStream(fis);
            Map parameters = (Map)ois.readObject();
            ois.close();
            return parameters;
         }
         else
         {
            return null;
         }
      }
      catch (Exception e)
      {
         throw new Error(e);
      }
   }
}
