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
package org.gatein.common.mc.bootstrap;

import org.jboss.kernel.plugins.deployment.xml.BeanXMLDeployer;
import org.jboss.kernel.spi.deployment.KernelDeployment;
import org.jboss.kernel.spi.registry.KernelRegistryEntry;
import org.jboss.kernel.spi.dependency.KernelControllerContext;
import org.jboss.beans.metadata.spi.BeanMetaData;
import org.apache.log4j.Logger;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextListener;
import javax.servlet.ServletContextEvent;
import java.net.URL;
import java.net.MalformedURLException;
import java.util.List;

/**
 * <p>A kernel bootstrap with a life cycle triggered by the <code>ServletContextListener</code> interface.
 * All beans will be injected as servlet context attributes. The bean xml file must be available as
 * a servlet context resource under the path <i>/WEB-INF/jboss-beans.xml</i>.</p>
 *
 * <p>A special bean with the name <code>ServletContext</code> is injected in the kernel before the deployment
 * of the beans from the xml file are deployed. Thus it is possible to inject the servlet context as a bean
 * into other beans.</p>
 *
 * @todo specify an alternative logger name
 * @todo implement the specification of an alternative xml file path
 * @todo implement kernel validate ?
 *
 * @author <a href="mailto:julien@jboss.org">Julien Viet</a>
 * @author <a href="mailto:theute@jboss.org">Thomas Heute</a>
 * @version $Revision: 1.1 $
 */
public class WebBootstrap implements ServletContextListener
{

   /**
    * The servlet context init parameter name for the bean resource location. For instance for a web application
    * resource it could be <i>/WEB-INF/my-beans.xml</i>, for the web application classloader resource it could be
    * <i>org/jboss/portal/my-beans.xml</i>. If no init parameter is declared then the litteral value
    * <i>/WEB-INF/jboss-beans.xml</i> is used and the lookup is done in the servlet context resourcees.
    */
   public static final String BEANS_RESOURCE_LOCATION_KEY = "jboss.portal.mc.beans_resource_location";

   /**
    * The servlet context init parameter name for the bean resource type which can be the servlet context
    * or the web application classloader. If no value is specified the servlet context will be used for
    * resource lookup. The legal values are <i>context</i> for the servlet context and <i>classloader</i>
    * for the web application classloader.
    */
   public static final String BEANS_RESOURCE_TYPE_KEY = "jboss.portal.mc.beans_resource_type";

   /** Default value for the bean resource location. */
   public static final String DEFAULT_JBOSS_BEANS_RESOURCE_LOCATION = "/WEB-INF/jboss-beans.xml";

   /** Servlet context resource type. */
   public static final String CONTEXT_RESOURCE_TYPE = "context";

   /** Classloader resource type. */
   public static final String CLASSLOADER_RESOURCE_TYPE = "classloader";

   /** Prefix to bean service names in the servlet context */
   public static final String BEAN_PREFIX = "jboss.portal:service=";

   /** . */
   private final static Logger log = Logger.getLogger(WebBootstrap.class);

   /** . */
   private ServletContext servletContext;

   /** . */
   private BeanXMLDeployer deployer;

   /** . */
   private KernelDeployment deployment;

   /** . */
//   private boolean registered;

   /** . */
   private ActualBootstrap bootstrap;

   /** . */
   private KernelControllerContext servletContextControllerContext;


   public void contextInitialized(ServletContextEvent event)
   {
      servletContext = event.getServletContext();

      //
      try
      {
         bootstrap = new ActualBootstrap(this);

         //
         bootstrap.run();

         //
         log.info("Web kernel started");
      }
      catch (Exception e)
      {
         log.info("Web kernel boostrap failed", e);
      }
   }

   public void contextDestroyed(ServletContextEvent event)
   {
      log.info("Web kernel shutdown");

      //
      if (deployment != null)
      {
         deployer.undeploy(deployment);
      }

      //
      if (servletContextControllerContext != null)
      {
         bootstrap.getKernel().getController().uninstall("ServletContext");
      }

      //
      servletContext = null;

      //
      log.info("Web kernel stopped");
   }

   /**
    * Register a MC Bean in the servlet context prefixed with WebBoostrap.BEAN_PREFIX
    */
   private void registerBean(BeanMetaData bean)
   {
      KernelRegistryEntry entry = bootstrap.getKernel().getRegistry().getEntry(bean.getName());
      Object target = entry.getTarget();
      
      servletContext.setAttribute(BEAN_PREFIX  + bean.getName(), target);
   }

   void boostrap() throws Throwable
   {
      //
      deployer = new BeanXMLDeployer(bootstrap.getKernel());
/*
      //
      AbstractBeanMetaData beanMD = new AbstractBeanMetaData("ServletContext", ServletContext.class.getName());
      AbstractConstructorMetaData ctorMD = new AbstractConstructorMetaData();
      ctorMD.setFactory(new AbstractValueMetaData(new ServletContextFactory(servletContext)));
      ctorMD.setFactoryMethod("getInstance");
      beanMD.setConstructor(ctorMD);
      servletContextControllerContext = bootstrap.getKernel().getController().install(beanMD);
*/
      //
      URL url = getBeansURL();

      //
      if (url != null)
      {
         log.debug("About to deploy beans url=" + url);
         deployment = deployer.deploy(url);
         deployer.validate(deployment);
         
         List<BeanMetaData> beans = deployment.getBeans();
         for (BeanMetaData bean: beans)
         {
            registerBean(bean);
         }
      }
      else
      {
         log.error("No valid beans URL was determined");
      }
   }

   protected URL getBeansURL()
   {
      String location = servletContext.getInitParameter(BEANS_RESOURCE_LOCATION_KEY);
      String type = servletContext.getInitParameter(BEANS_RESOURCE_TYPE_KEY);

      //
      if (location == null)
      {
         location = DEFAULT_JBOSS_BEANS_RESOURCE_LOCATION;
         type = CONTEXT_RESOURCE_TYPE;
      }
      else if (type == null)
      {
         type = CONTEXT_RESOURCE_TYPE;
      }

      //
      if (CONTEXT_RESOURCE_TYPE.equals(type))
      {
         try
         {
            return servletContext.getResource(location);
         }
         catch (MalformedURLException e)
         {
            log.error("Cannot obtain beans definition file from servlet context with location=" + location, e);

            //
            return null;
         }
      }
      else if (CLASSLOADER_RESOURCE_TYPE.equals(type))
      {
         URL resource = Thread.currentThread().getContextClassLoader().getResource(location);

         //
         if (resource == null)
         {
            log.error("Cannot obtain bean definition file from thread context classloader with location=" + location);
         }

         //
         return resource;
      }
      else
      {
         log.error("Cannot obtain bean definition file since the context type cannot be determined type=" + type);

         //
         return null;
      }
   }
}
