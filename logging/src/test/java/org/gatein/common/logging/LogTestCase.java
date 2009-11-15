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
package org.gatein.common.logging;

import junit.framework.TestCase;
import org.slf4j.impl.Log;
import org.slf4j.impl.LoggerFactoryImpl;
import org.slf4j.impl.LoggerImpl;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author <a href="mailto:julien.viet@exoplatform.com">Julien Viet</a>
 * @version $Revision$
 */
public class LogTestCase extends TestCase
{

   public void testGetLogger()
   {
      Logger logger = LoggerFactory.getLogger(LogTestCase.class);
      assertNotNull(logger);
      assertEquals(LogTestCase.class.getName(), logger.getName());
      assertSame(logger, LoggerFactory.getLogger(LogTestCase.class.getName()));
   }

   public void testConcurrentGetRace() throws Exception
   {
      final ReentrantLock lock = LoggerFactoryImpl.getLock();
      lock.lock();
      final AtomicReference<Logger> loggerRef = new AtomicReference<Logger>();
      final AtomicBoolean done = new AtomicBoolean();
      Thread t = new Thread()
      {
         public void run()
         {
            Logger logger = LoggerFactory.getLogger("testConcurrentGetRace");
            loggerRef.set(logger);
            done.set(true);
         }
      };
      t.start();
      while (!lock.hasQueuedThread(t))
      {
         Thread.sleep(1);
      }
      assertEquals(null, LoggerFactoryImpl.peekLogger("testConcurrentGetRace"));
      Logger logger = LoggerFactory.getLogger("testConcurrentGetRace");
      assertNotNull(logger);
      lock.unlock();
      while (!done.get())
      {
         Thread.sleep(1);
      }
      assertSame(logger, loggerRef.get());
   }

   public void testTraceEnabled()
   {
      Logger logger = LoggerFactory.getLogger("testTraceEnabled");
      LoggerImpl loggerImpl = LoggerFactoryImpl.peekLogger("testTraceEnabled");
      assertTrue(logger.isTraceEnabled());
      loggerImpl.traceEnabled = false;
      assertFalse(logger.isTraceEnabled());
   }

   public void testTrace()
   {
      LoggerFactoryImpl.assertEmpty();
      Logger logger = LoggerFactory.getLogger("testTrace");
      logger.trace("testTrace1");
      LoggerFactoryImpl.assertLog("testTrace", Log.TRACE, "testTrace1", null);
      LoggerFactoryImpl.assertEmpty();
      Throwable t = new Throwable();
      logger.trace("testTrace2", t);
      LoggerFactoryImpl.assertLog("testTrace", Log.TRACE, "testTrace2", t);
      LoggerFactoryImpl.assertEmpty();
   }

   public void testDebugEnabled()
   {
      Logger logger = LoggerFactory.getLogger("testDebugEnabled");
      LoggerImpl loggerImpl = LoggerFactoryImpl.peekLogger("testDebugEnabled");
      assertTrue(logger.isDebugEnabled());
      loggerImpl.debugEnabled = false;
      assertFalse(logger.isDebugEnabled());
   }

   public void testDebug()
   {
      LoggerFactoryImpl.assertEmpty();
      Logger logger = LoggerFactory.getLogger("testDebug");
      logger.debug("testDebug1");
      LoggerFactoryImpl.assertLog("testDebug", Log.DEBUG, "testDebug1", null);
      LoggerFactoryImpl.assertEmpty();
      Throwable t = new Throwable();
      logger.debug("testDebug2", t);
      LoggerFactoryImpl.assertLog("testDebug", Log.DEBUG, "testDebug2", t);
      LoggerFactoryImpl.assertEmpty();
   }

   public void testInfoEnabled()
   {
      Logger logger = LoggerFactory.getLogger("testInfoEnabled");
      LoggerImpl loggerImpl = LoggerFactoryImpl.peekLogger("testInfoEnabled");
      assertTrue(logger.isInfoEnabled());
      loggerImpl.infoEnabled = false;
      assertFalse(logger.isInfoEnabled());
   }

   public void testInfo()
   {
      LoggerFactoryImpl.assertEmpty();
      Logger logger = LoggerFactory.getLogger("testInfo");
      logger.info("testInfo1");
      LoggerFactoryImpl.assertLog("testInfo", Log.INFO, "testInfo1", null);
      LoggerFactoryImpl.assertEmpty();
      Throwable t = new Throwable();
      logger.info("testInfo2", t);
      LoggerFactoryImpl.assertLog("testInfo", Log.INFO, "testInfo2", t);
      LoggerFactoryImpl.assertEmpty();
   }

   public void testWarnEnabled()
   {
      Logger logger = LoggerFactory.getLogger("testWarnEnabled");
      LoggerImpl loggerImpl = LoggerFactoryImpl.peekLogger("testWarnEnabled");
      assertTrue(logger.isWarnEnabled());
      loggerImpl.warnEnabled = false;
      assertFalse(logger.isWarnEnabled());
   }

   public void testWarn()
   {
      LoggerFactoryImpl.assertEmpty();
      Logger logger = LoggerFactory.getLogger("testWarn");
      logger.warn("testWarn1");
      LoggerFactoryImpl.assertLog("testWarn", Log.WARN, "testWarn1", null);
      LoggerFactoryImpl.assertEmpty();
      Throwable t = new Throwable();
      logger.warn("testWarn2", t);
      LoggerFactoryImpl.assertLog("testWarn", Log.WARN, "testWarn2", t);
      LoggerFactoryImpl.assertEmpty();
   }

   public void testErrorEnabled()
   {
      Logger logger = LoggerFactory.getLogger("testErrorEnabled");
      LoggerImpl loggerImpl = LoggerFactoryImpl.peekLogger("testErrorEnabled");
      assertTrue(logger.isErrorEnabled());
      loggerImpl.errorEnabled = false;
      assertFalse(logger.isErrorEnabled());
   }

   public void testError()
   {
      LoggerFactoryImpl.assertEmpty();
      Logger logger = LoggerFactory.getLogger("testError");
      logger.error("testError1");
      LoggerFactoryImpl.assertLog("testError", Log.ERROR, "testError1", null);
      LoggerFactoryImpl.assertEmpty();
      Throwable t = new Throwable();
      logger.error("testError2", t);
      LoggerFactoryImpl.assertLog("testError", Log.ERROR, "testError2", t);
      LoggerFactoryImpl.assertEmpty();
   }

   public void testDetypedEnabled()
   {
      Logger logger = LoggerFactory.getLogger("testDetypedEnabled");
      LoggerImpl loggerImpl = LoggerFactoryImpl.peekLogger("testDetypedEnabled");
      LogLevel[] levels = LogLevel.values();
      for (LogLevel level : levels)
      {
         assertTrue(logger.isEnabled(level));
         switch (level)
         {
            case TRACE:
               loggerImpl.traceEnabled = false;
               break;
            case DEBUG:
               loggerImpl.debugEnabled = false;
               break;
            case INFO:
               loggerImpl.infoEnabled = false;
               break;
            case WARN:
               loggerImpl.warnEnabled = false;
               break;
            case ERROR:
               loggerImpl.errorEnabled = false;
               break;
         }
         assertFalse(logger.isEnabled(level));
      }
   }

   public void testDetypedLog()
   {
      LoggerFactoryImpl.assertEmpty();
      Logger logger = LoggerFactory.getLogger("testDetypedLog");
      LogLevel[] levels = LogLevel.values();
      for (int i = 0;i < levels.length;i++)
      {
         LogLevel level = levels[i];
         logger.log(level, "testDetypedLog1_" + i);
         LoggerFactoryImpl.assertLog("testDetypedLog", level.ordinal(), "testDetypedLog1_" + i, null);
         LoggerFactoryImpl.assertEmpty();
         Throwable t = new Throwable();
         logger.log(level, "testDetypedLog2_" + i, t);
         LoggerFactoryImpl.assertLog("testDetypedLog", level.ordinal(), "testDetypedLog2_" + i, t);
         LoggerFactoryImpl.assertEmpty();
      }
   }

   public void testGetLoggerNPE()
   {
      try
      {
         LoggerFactory.getLogger((String)null);
         fail();
      }
      catch (NullPointerException e)
      {
      }
      try
      {
         LoggerFactory.getLogger((Class)null);
         fail();
      }
      catch (NullPointerException e)
      {
      }
   }
}
