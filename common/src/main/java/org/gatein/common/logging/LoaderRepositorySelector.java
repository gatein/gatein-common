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
package org.gatein.common.logging;

import org.apache.log4j.Hierarchy;
import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.spi.LoggerRepository;
import org.apache.log4j.spi.RepositorySelector;
import org.apache.log4j.spi.RootCategory;
import org.apache.log4j.xml.DOMConfigurator;
import org.gatein.common.util.ResourceLoader;
import org.gatein.common.util.Tools;
import org.gatein.common.io.IOTools;
import org.w3c.dom.Document;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Stan Silvert
 * @author <a href="mailto:julien@jboss.org">Julien Viet</a>
 * @version $Revision: 7374 $
 */
public class LoaderRepositorySelector implements org.apache.log4j.spi.RepositorySelector
{

   private static boolean initialized = false;

   // This object is used for the guard because it doesn't get
   // recycled when the application is redeployed.
   private static Object guard = LogManager.getRootLogger();
   private static Map repositories = new HashMap();
   private static LoggerRepository defaultRepository;
   private static Logger log = Logger.getLogger(LoaderRepositorySelector.class);

   /**
    * Register with this repository selector.
    */
   public static synchronized void register(ClassLoader classloader, ResourceLoader loader, String pathToLog4jFile)
   {
      if (classloader == null)
      {
         throw new IllegalArgumentException("No classloader provided");
      }
      if (loader == null)
      {
         throw new IllegalArgumentException("No loader provided");
      }
      if (pathToLog4jFile == null)
      {
         throw new IllegalArgumentException("No pathToLog4jFile provided");
      }

      // Set the global RepositorySelector
      if (!initialized)
      {
         // defaultRepository = LogManager.getLoggerRepository();
         RepositorySelector theSelector = new LoaderRepositorySelector();
         LogManager.setRepositorySelector(theSelector, guard);
         initialized = true;
      }

      InputStream log4JConfig = null;
      try
      {
         log4JConfig = loader.load(pathToLog4jFile);
         DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
         DocumentBuilder builder = factory.newDocumentBuilder();
         Document doc = builder.parse(log4JConfig);
         DOMConfigurator conf = new DOMConfigurator();

         Hierarchy hierarchy = new Hierarchy(new RootCategory(Level.DEBUG));
         conf.doConfigure(doc.getDocumentElement(), hierarchy);
         repositories.put(classloader, hierarchy);
      }
      catch (Exception e)
      {
         log.error(e);
         throw new IllegalArgumentException("Cannot load log4j configuration");
      }
      finally
      {
         IOTools.safeClose(log4JConfig);
      }
   }

   public static synchronized void unregister(ClassLoader classloader)
   {
      if (classloader == null)
      {
         throw new IllegalArgumentException("No classloader provided");
      }
      Hierarchy hierarchy = (Hierarchy)repositories.remove(classloader);
      if (hierarchy != null)
      {
         hierarchy.shutdown();
      }
      else
      {
         System.out.print("No hierarchy found for classloader : ");
         Writer writer = new PrintWriter(System.out);
         Tools.dumpClassLoaderHierarchyInfo(writer, classloader);
      }
   }

   private LoaderRepositorySelector()
   {
   }

   public LoggerRepository getLoggerRepository()
   {
      ClassLoader loader = Thread.currentThread().getContextClassLoader();
      LoggerRepository repository = (LoggerRepository)repositories.get(loader);
      if (repository == null)
      {
         return defaultRepository;
      }
      else
      {
         return repository;
      }
   }
}
