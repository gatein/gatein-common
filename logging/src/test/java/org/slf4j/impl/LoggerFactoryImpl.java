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

import junit.framework.Assert;
import org.slf4j.ILoggerFactory;
import org.slf4j.Logger;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author <a href="mailto:julien.viet@exoplatform.com">Julien Viet</a>
 * @version $Revision$
 */
public class LoggerFactoryImpl implements ILoggerFactory
{

   /** . */
   private static final LinkedList<Log> logs = new LinkedList<Log>();

   /** . */
   private static final Map<String, LoggerImpl> loggers = Collections.synchronizedMap(new HashMap<String, LoggerImpl>());

   /** For testing concurrency purpose. */
   private static final ReentrantLock lock = new ReentrantLock();

   public LoggerFactoryImpl()
   {
   }

   synchronized static void log(String name, int level, String msg, Throwable t)
   {
      logs.add(new Log(name, level, msg, t));
   }

   public synchronized static void assertEmpty()
   {
      Assert.assertTrue(logs.isEmpty());
   }

   public synchronized static void assertLog(String name, int level, String msg, Throwable t)
   {
      Assert.assertFalse(logs.isEmpty());
      Log log = logs.removeFirst();
      Assert.assertEquals(name, log.getName());
      Assert.assertEquals(level, log.getLevel());
      Assert.assertEquals(msg, log.getMessage());
      Assert.assertEquals(t, log.getThrowable());
   }

   public static LoggerImpl peekLogger(String name)
   {
      return loggers.get(name);
   }

   public static ReentrantLock getLock()
   {
      return lock;
   }

   public LoggerImpl getLogger(String name)
   {
      lock.lock();
      try
      {
         LoggerImpl logger = loggers.get(name);
         if (logger == null)
         {
            logger = new LoggerImpl(name);
            loggers.put(name, logger);
         }
         return logger;
      }
      finally
      {
         lock.unlock();
      }
   }
}
