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

import org.slf4j.helpers.MarkerIgnoringBase;

/**
 * @author <a href="mailto:julien.viet@exoplatform.com">Julien Viet</a>
 * @version $Revision$
 */
public class LoggerImpl extends MarkerIgnoringBase
{

   /** . */
   public boolean traceEnabled = true;

   /** . */
   public boolean debugEnabled = true;

   /** . */
   public boolean infoEnabled = true;

   /** . */
   public boolean warnEnabled = true;

   /** . */
   public boolean errorEnabled = true;

   public LoggerImpl(String name)
   {
      this.name = name;
   }

   public String getName()
   {
      return name;
   }

   public boolean isTraceEnabled()
   {
      return traceEnabled;
   }

   public boolean isDebugEnabled()
   {
      return debugEnabled;
   }

   public boolean isInfoEnabled()
   {
      return infoEnabled;
   }

   public boolean isWarnEnabled()
   {
      return warnEnabled;
   }

   public boolean isErrorEnabled()
   {
      return errorEnabled;
   }

   public void trace(String msg)
   {
      trace(msg, (Throwable)null);
   }

   public void trace(String msg, Throwable t)
   {
      LoggerFactoryImpl.log(name, Log.TRACE, msg, t);
   }

   public void debug(String msg)
   {
      debug(msg, (Throwable)null);
   }

   public void debug(String msg, Throwable t)
   {
      LoggerFactoryImpl.log(name, Log.DEBUG, msg, t);
   }

   public void info(String msg)
   {
      info(msg, (Throwable)null);
   }

   public void info(String msg, Throwable t)
   {
      LoggerFactoryImpl.log(name, Log.INFO, msg, t);
   }

   public void warn(String msg)
   {
      warn(msg, (Throwable)null);
   }

   public void warn(String msg, Throwable t)
   {
      LoggerFactoryImpl.log(name, Log.WARN, msg, t);
   }

   public void error(String msg)
   {
      error(msg, (Throwable)null);
   }

   public void error(String msg, Throwable t)
   {
      LoggerFactoryImpl.log(name, Log.ERROR, msg, t);
   }

   public void trace(String format, Object arg)
   {
      throw new Error();
   }

   public void trace(String format, Object arg1, Object arg2)
   {
      throw new Error();
   }

   public void trace(String format, Object[] argArray)
   {
      throw new Error();
   }

   public void debug(String format, Object arg)
   {
      throw new Error();
   }

   public void debug(String format, Object arg1, Object arg2)
   {
      throw new Error();
   }

   public void debug(String format, Object[] argArray)
   {
      throw new Error();
   }

   public void info(String format, Object arg)
   {
      throw new Error();
   }

   public void info(String format, Object arg1, Object arg2)
   {
      throw new Error();
   }

   public void info(String format, Object[] argArray)
   {
      throw new Error();
   }

   public void warn(String format, Object arg)
   {
      throw new Error();
   }

   public void warn(String format, Object[] argArray)
   {
      throw new Error();
   }

   public void warn(String format, Object arg1, Object arg2)
   {
      throw new Error();
   }

   public void error(String format, Object arg)
   {
      throw new Error();
   }

   public void error(String format, Object arg1, Object arg2)
   {
      throw new Error();
   }

   public void error(String format, Object[] argArray)
   {
      throw new Error();
   }
}
