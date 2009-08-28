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

import org.apache.tools.ant.taskdefs.optional.junit.JUnitTest;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Used by ant to create a representation of the test to run.
 *
 * @author <a href="mailto:julien@jboss.org">Julien Viet</a>
 * @version $Revision: 7228 $
 */
public class ConfigurableJUnitTest extends JUnitTest
{

   /** . */
   private final ArrayList nestedParameters;

   /** . */
   private ConfigurableJUnitTask task;

   /** . */
   private String id;

   public ConfigurableJUnitTest()
   {
      nestedParameters = new ArrayList();
   }

   public void addParameter(TestParameter parameter)
   {
      nestedParameters.add(parameter);
   }

   public void setTask(ConfigurableJUnitTask task)
   {
      this.task = task;
   }

   public String getId()
   {
      return id;
   }

   public void setId(String id)
   {
      this.id = id;

      //
      TestParameter parameter = new TestParameter();
      parameter.setName(TestParameter.TEST_ID_PARAM);
      parameter.setValue(id);
      addParameter(parameter);
   }

   /**
    * Save the state of the junit test on the disk for reuse later in the forked virtual machine.
    * As there is not clear life cycle of the usage of this class, we need to save the state initially
    * and on every update.
    */
   public void saveState()
   {
      try
      {
         Map parameters = new HashMap();
         for (int i = 0; i < nestedParameters.size(); i++)
         {
            TestParameter parameter = (TestParameter)nestedParameters.get(i);
            String[] values = null;
            if (parameter.value != null)
            {
               values = new String[]{parameter.value};
            }
            else if (parameter.nestedValues.size() > 0)
            {
               values = new String[parameter.nestedValues.size()];
               for (int j = 0; j < values.length; j++)
               {
                  values[j] = ((TestParameterValue)parameter.nestedValues.get(j)).text;
               }
            }
            if (values != null)
            {
               parameters.put(parameter.name, values);
            }
         }
         File tmp = new File(System.getProperty("java.io.tmpdir"), TestParameter.PARAMETRIZATION_FILE_NAME);
         tmp.deleteOnExit();
         FileOutputStream fos = new FileOutputStream(tmp);
         ObjectOutputStream oos = new ObjectOutputStream(fos);
         oos.writeObject(parameters);
         oos.close();

      }
      catch (IOException e)
      {
         throw new Error(e);
      }
   }
}
