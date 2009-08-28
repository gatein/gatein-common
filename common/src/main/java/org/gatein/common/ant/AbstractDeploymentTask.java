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
package org.gatein.common.ant;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;
import org.codehaus.cargo.container.ContainerType;
import org.codehaus.cargo.container.RemoteContainer;
import org.codehaus.cargo.container.configuration.Configuration;
import org.codehaus.cargo.container.configuration.ConfigurationType;
import org.codehaus.cargo.container.jboss.JBossJMXDeployer;
import org.codehaus.cargo.container.property.GeneralPropertySet;
import org.codehaus.cargo.container.property.ServletPropertySet;
import org.codehaus.cargo.generic.ContainerFactory;
import org.codehaus.cargo.generic.DefaultContainerFactory;
import org.codehaus.cargo.generic.configuration.ConfigurationFactory;
import org.codehaus.cargo.generic.configuration.DefaultConfigurationFactory;

import java.io.File;

/**
 * A deployment task.
 *
 * @author <a href="mailto:julien@jboss.org">Julien Viet</a>
 * @version $Revision: 7228 $
 */
public abstract class AbstractDeploymentTask extends Task
{

   /** The file. */
   private File file;

   /** The JBoss config. */
   private String config;

   public AbstractDeploymentTask()
   {
      file = null;
      config = "default";
   }

   public File getFile()
   {
      return file;
   }

   public void setFile(File file)
   {
      this.file = file;
   }

   public String getConfig()
   {
      return config;
   }

   public void setConfig(String config)
   {
      this.config = config;
   }

   public void execute() throws BuildException
   {
      //
      ConfigurationFactory cfgFactory = new DefaultConfigurationFactory();
      Configuration cfg = cfgFactory.createConfiguration("jboss4x", ConfigurationType.RUNTIME);

      // Configure the container
      if ("default".equals(config))
      {
         cfg.setProperty(GeneralPropertySet.PROTOCOL, "http");
         cfg.setProperty(GeneralPropertySet.HOSTNAME, "localhost");
         cfg.setProperty(ServletPropertySet.PORT, "8080");
      }
      else
      {
         throw new BuildException("Unknown configuration " + config);
      }

      //
      if (file == null)
      {
         throw new BuildException("No specified file to deploy");
      }

      //
      ContainerFactory containerFactory = new DefaultContainerFactory();
      RemoteContainer container = (RemoteContainer)containerFactory.createContainer("jboss4x", ContainerType.REMOTE, cfg);

      //
      JBossJMXDeployer deployer = new JBossJMXDeployer(container);
      execute(deployer);
   }

   protected abstract void execute(JBossJMXDeployer deployer);
}
