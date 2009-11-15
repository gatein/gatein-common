/**
 * Copyright (C) 2009 eXo Platform SAS.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.gatein.common.logging;

import org.slf4j.spi.LocationAwareLogger;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * The common way to obtain a logger.
 *
 * @author <a href="mailto:julien.viet@exoplatform.com">Julien Viet</a>
 * @version $Revision$
 */
public class LoggerFactory
{

   /** . */
   private static final ConcurrentMap<String, Logger> loggers = new ConcurrentHashMap<String, Logger>();

   /**
    * Returns a logger by its name.
    *
    * @param name the logger name
    * @return the logger
    * @throws NullPointerException if the logger name is null
    */
   public static Logger getLogger(String name) throws NullPointerException
   {
      if (name == null)
      {
         throw new NullPointerException("No null logger name accepted");
      }
      Logger logger = loggers.get(name);
      if (logger == null)
      {
         logger = createLogger(name);
         Logger phantom = loggers.putIfAbsent(name, logger);
         if (phantom != null)
         {
            logger = phantom;
         }
      }
      return logger;
   }

   /**
    * Returns a logger by its name based on the value returned by {@link Class#getName()}
    *
    * @param type the class type
    * @return the logger
    * @throws NullPointerException if the class type is null
    */
   public static Logger getLogger(Class type)
   {
      if (type == null)
      {
         throw new NullPointerException("No null class type accepted");
      }
      return getLogger(type.getName());
   }

   private static Logger createLogger(String name)
   {
      org.slf4j.Logger slf4jlogger = org.slf4j.LoggerFactory.getLogger(name);
      if (slf4jlogger instanceof LocationAwareLogger)
      {
         return new SLF4JLocationAwareLogger((LocationAwareLogger)slf4jlogger);
      }
      else
      {
         return new SimpleLogger(slf4jlogger);
      }
   }
}
