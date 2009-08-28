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

import org.apache.log4j.Logger;
import org.apache.log4j.Level;

import java.io.Writer;
import java.io.IOException;

/**
 * @author <a href="mailto:julien@jboss.org">Julien Viet</a>
 * @version $Revision: 5451 $
 */
public class Log4JWriter extends Writer
{

   /** The open/closed status. */
   private boolean open;

   /** The logger. */
   private Logger log;

   /** The logging level. */
   private Level level;

   /**
    * @throws IllegalArgumentException if log or level is null
    */
   public Log4JWriter(Logger log, Level level) throws IllegalArgumentException
   {
      if (log == null)
      {
         throw new IllegalArgumentException("No logger provided");
      }
      if (level == null)
      {
         throw new IllegalArgumentException("No level provided");
      }
      this.open = false;
      this.log = log;
      this.level = level;
   }

   public void write(char cbuf[], int off, int len) throws IOException
   {
      if (open)
      {
         log.log(level, new String(cbuf, off, len));
      }
      else
      {
         throw new IOException("Stream closed");
      }
   }

   public void flush() throws IOException
   {
   }

   public void close() throws IOException
   {
      open = false;
   }
}
