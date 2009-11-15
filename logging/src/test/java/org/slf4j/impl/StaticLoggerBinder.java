/*
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
package org.slf4j.impl;

import org.slf4j.ILoggerFactory;
import org.slf4j.spi.LoggerFactoryBinder;

/**
 * @author <a href="mailto:julien.viet@exoplatform.com">Julien Viet</a>
 * @version $Revision$
 */
public class StaticLoggerBinder implements LoggerFactoryBinder
{

   private static final StaticLoggerBinder SINGLETON = new StaticLoggerBinder();

   public static StaticLoggerBinder getSingleton() {
     return SINGLETON;
   }

   public static String REQUESTED_API_VERSION = "1.5.6";

   private static final String loggerFactoryClassStr = LoggerFactoryImpl.class.getName();

   private final ILoggerFactory loggerFactory;

   private StaticLoggerBinder() {
     loggerFactory = new LoggerFactoryImpl();
   }

   public ILoggerFactory getLoggerFactory() {
     return loggerFactory;
   }

   public String getLoggerFactoryClassStr() {
     return loggerFactoryClassStr;
   }
}
