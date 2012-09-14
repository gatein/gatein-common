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

import org.slf4j.Marker;
import org.slf4j.spi.LocationAwareLogger;

import java.lang.reflect.Method;

/**
 * The logger for GateIn.
 *
 * @author <a href="mailto:julien.viet@exoplatform.com">Julien Viet</a>
 * @version $Revision$
 */
public abstract class Logger
{

   /** . */
   public static final int SLF_1_5 = 0;

   /** . */
   public static final int SLF_1_6 = 1;

   /** . */
   static Method log;

   /** . */
   public static final int LOGGER;

   static
   {
      Method m = getLogMethod(Marker.class, String.class, int.class, String.class, Throwable.class);
      if (m != null)
      {
         LOGGER = SLF_1_5;
      }
      else
      {
         m = getLogMethod(Marker.class, String.class, int.class, String.class, Object[].class, Throwable.class);
         if (m != null)
         {
            LOGGER = SLF_1_6;
         }
         else
         {
            throw new AssertionError("Could not use with the found SLF 4J version " +
               LocationAwareLogger.class.getProtectionDomain().getCodeSource().getLocation());
         }
      }
      log = m;
   }

   private static Method getLogMethod(Class... signature)
   {
      try
      {
         return LocationAwareLogger.class.getMethod("log", signature);
      }
      catch (NoSuchMethodException ignore)
      {
         return null;
      }
   }

   /** Helps the compiler to be happy. */
   private static final Object[] NO_PARAMETERS = null;

   protected abstract void doLog(LogLevel level, Object msg, Object[] argArray, Throwable throwable);

   protected abstract org.slf4j.Logger getDelegate();

   private void log(LogLevel level, Object msg, Throwable throwable, Object... parameters)
   {
      if (level == null)
      {
         level = LogLevel.INFO;
      }

      // Take care of interpolation
      if (parameters != null && parameters.length > 0)
      {
         throw new UnsupportedOperationException("log interpolation to do, for now it cannot be called");
      }

      //
      doLog(level, msg, parameters, throwable);
   }

   public final String getName()
   {
      return getDelegate().getName();
   }

   public final boolean isEnabled(LogLevel level)
   {
      if (level == null)
      {
         level = LogLevel.INFO;
      }
      return level.isEnabled(getDelegate());
   }

   public final void log(LogLevel level, Object msg)
   {
      log(level, msg, null, NO_PARAMETERS);
   }

   public final void log(LogLevel level, Object msg, Throwable throwable)
   {
      log(level, msg, throwable, NO_PARAMETERS);
   }

   public final boolean isTraceEnabled()
   {
      return isEnabled(LogLevel.TRACE);
   }

   public final void trace(Object msg)
   {
      log(LogLevel.TRACE, msg, null, NO_PARAMETERS);
   }

   public final void trace(Object msg, Throwable throwable)
   {
      log(LogLevel.TRACE, msg, throwable, NO_PARAMETERS);
   }

   public final boolean isDebugEnabled()
   {
      return isEnabled(LogLevel.DEBUG);
   }

   public final void debug(Object msg)
   {
      log(LogLevel.DEBUG, msg, null, NO_PARAMETERS);
   }

   public final void debug(Object msg, Throwable throwable)
   {
      log(LogLevel.DEBUG, msg, throwable, NO_PARAMETERS);
   }

   public final boolean isInfoEnabled()
   {
      return isEnabled(LogLevel.INFO);
   }

   public final void info(Object msg)
   {
      log(LogLevel.INFO, msg, null, NO_PARAMETERS);
   }

   public final void info(Object msg, Throwable throwable)
   {
      log(LogLevel.INFO, msg, throwable, NO_PARAMETERS);
   }

   public final boolean isWarnEnabled()
   {
      return isEnabled(LogLevel.WARN);
   }

   public final void warn(Object msg)
   {
      log(LogLevel.WARN, msg, null, NO_PARAMETERS);
   }

   public final void warn(Object msg, Throwable throwable)
   {
      log(LogLevel.WARN, msg, throwable, NO_PARAMETERS);
   }

   public final boolean isErrorEnabled()
   {
      return isEnabled(LogLevel.ERROR);
   }

   public final void error(Object msg)
   {
      log(LogLevel.ERROR, msg, null, NO_PARAMETERS);
   }

   public final void error(Object msg, Throwable throwable)
   {
      log(LogLevel.ERROR, msg, throwable, NO_PARAMETERS);
   }

   @Override
   public String toString()
   {
      return "Logger[name=" + getDelegate().getName() + "]";
   }
}
