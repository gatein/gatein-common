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

import org.gatein.common.logging.LogLevel;

/**
 * @author <a href="mailto:julien.viet@exoplatform.com">Julien Viet</a>
 * @version $Revision$
 */
public class Log
{

   /** . */
   public static final int TRACE = LogLevel.TRACE.ordinal();

   /** . */
   public static final int DEBUG = LogLevel.DEBUG.ordinal();

   /** . */
   public static final int INFO = LogLevel.INFO.ordinal();

   /** . */
   public static final int WARN = LogLevel.WARN.ordinal();

   /** . */
   public static final int ERROR = LogLevel.ERROR.ordinal();

   /** . */
   private final String name;

   /** . */
   private final int level;

   /** . */
   private final String msg;

   /** . */
   private final Throwable throwable;

   public Log(String name, int level, String msg, Throwable throwable)
   {
      this.name = name;
      this.level = level;
      this.msg = msg;
      this.throwable = throwable;
   }

   public String getName()
   {
      return name;
   }

   public int getLevel()
   {
      return level;
   }

   public String getMessage()
   {
      return msg;
   }

   public Throwable getThrowable()
   {
      return throwable;
   }
}
