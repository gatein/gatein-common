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

import org.apache.tools.ant.taskdefs.optional.junit.JUnitTask;
import org.apache.tools.ant.taskdefs.optional.junit.JUnitTest;
import org.apache.tools.ant.BuildException;

import java.io.File;

/**
 * @author <a href="mailto:julien@jboss.org">Julien Viet</a>
 * @version $Revision: 7228 $
 */
public class ConfigurableJUnitTask extends JUnitTask
{

   public ConfigurableJUnitTask() throws Exception
   {
   }

   public void addZest(ConfigurableJUnitTest test)
   {
      test.setTask(this);
      addTest(test);
   }

   protected void execute(JUnitTest test) throws BuildException
   {
      // Delete any existing file
      File tmp = new File(System.getProperty("java.io.tmpdir"), TestParameter.PARAMETRIZATION_FILE_NAME);
      if (tmp.exists() && tmp.isFile())
      {
         if (!tmp.delete())
         {
            throw new Error("Cannot delete previous saved parametrization");
         }
      }

      //
      try
      {
         // Basically we are only sure at this time of the execution that the nested parameter map is fully initialized
         if (test instanceof ConfigurableJUnitTest)
         {
            ((ConfigurableJUnitTest)test).saveState();
         }

         // Let the parent class execute the super class
         super.execute(test);
      }
      finally
      {
         // Cleanup any save state
         if (tmp.exists() && tmp.isFile())
         {
            if (!tmp.delete())
            {
               tmp.deleteOnExit();
            }
         }
      }
   }
}
