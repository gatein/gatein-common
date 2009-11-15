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

/**
 * The level of a log message.
 *
 * @author <a href="mailto:julien.viet@exoplatform.com">Julien Viet</a>
 * @version $Revision$
 */
public enum LogLevel
{
   /**
    * Trace log level.
    */
   TRACE(LocationAwareLogger.TRACE_INT)
   {
      @Override
      void log(org.slf4j.Logger delegate, Object msg, Throwable throwable)
      {
         delegate.trace(String.valueOf(msg), throwable);
      }

      @Override
      boolean isEnabled(org.slf4j.Logger delegate)
      {
         return delegate.isTraceEnabled();
      }
   },

   /**
    * Debug log level.
    */
   DEBUG(LocationAwareLogger.DEBUG_INT)
   {
      @Override
      void log(org.slf4j.Logger delegate, Object msg, Throwable throwable)
      {
         delegate.debug(String.valueOf(msg), throwable);
      }

      @Override
      boolean isEnabled(org.slf4j.Logger delegate)
      {
         return delegate.isDebugEnabled();
      }
   },

   /**
    * Info log level.
    */
   INFO(LocationAwareLogger.INFO_INT)
   {
      @Override
      void log(org.slf4j.Logger delegate, Object msg, Throwable throwable)
      {
         delegate.info(String.valueOf(msg), throwable);
      }

      @Override
      boolean isEnabled(org.slf4j.Logger delegate)
      {
         return delegate.isInfoEnabled();
      }
   },

   /**
    * Warn log level.
    */
   WARN(LocationAwareLogger.WARN_INT)
   {
      @Override
      void log(org.slf4j.Logger delegate, Object msg, Throwable throwable)
      {
         delegate.warn(String.valueOf(msg), throwable);
      }

      @Override
      boolean isEnabled(org.slf4j.Logger delegate)
      {
         return delegate.isWarnEnabled();
      }
   },

   /**
    * Warn log level.
    */
   ERROR(LocationAwareLogger.ERROR_INT)
   {
      @Override
      void log(org.slf4j.Logger delegate, Object msg, Throwable throwable)
      {
         delegate.error(String.valueOf(msg), throwable);
      }
      
      @Override
      boolean isEnabled(org.slf4j.Logger delegate)
      {
         return delegate.isErrorEnabled();
      }
   }

   ;

   private final int slf4Jlevel;

   LogLevel(int slf4Jlevel)
   {
      this.slf4Jlevel = slf4Jlevel;
   }

   int getSLF4Jlevel()
   {
      return slf4Jlevel;
   }

   abstract void log(org.slf4j.Logger delegate, Object msg, Throwable throwable);

   abstract boolean isEnabled(org.slf4j.Logger delegate);

}
