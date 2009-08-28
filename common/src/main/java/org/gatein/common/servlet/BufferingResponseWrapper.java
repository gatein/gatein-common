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
package org.gatein.common.servlet;

import org.gatein.common.io.UndeclaredIOException;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Locale;

/**
 * Redirection of the Writer
 *
 * @author <a href="mailto:theute@jboss.org">Thomas Heute</a>
 * @version $Revision: 7228 $
 */
public class BufferingResponseWrapper extends HttpServletResponseWrapper
{

   /** . */
   private ServletOutputStreamBuffer outputStream;

   /** . */
   private PrintWriter writer;

   /** . */
   private StringWriter chars;

   /** . */
   private String characterEncoding;

   /** Not really used but we need it to memorize what the client set optionally. */
   protected int bufferSize;

   public BufferingResponseWrapper(HttpServletResponse response)
   {
      super(response);

      // By default inherit the character encoding of the wrapped response
      this.characterEncoding = response.getCharacterEncoding();

      // 0 means no buffering - we say no buffering
      this.bufferSize = 0;
   }

   public String getContent()
   {
      if (outputStream != null)
      {
         try
         {
            outputStream.flush();
            return outputStream.getContent(characterEncoding);
         }
         catch (IOException e)
         {
            throw new UndeclaredIOException(e);
         }
      }
      else if (chars != null)
      {
         writer.flush();
         return chars.toString();
      }
      else
      {
         return null;
      }
   }

   public void addCookie(Cookie arg0)
   {
   }

   public void addDateHeader(String arg0, long arg1)
   {
   }

   public void addHeader(String arg0, String arg1)
   {
   }

   public void addIntHeader(String arg0, int arg1)
   {
   }

   public void sendError(int arg0) throws IOException
   {
   }

   public void sendError(int arg0, String arg1) throws IOException
   {
   }

   public void sendRedirect(String arg0) throws IOException
   {
   }

   public void setDateHeader(String arg0, long arg1)
   {
   }

   public void setHeader(String arg0, String arg1)
   {
   }

   public void setIntHeader(String arg0, int arg1)
   {
   }

   public void setStatus(int arg0)
   {
   }

   public void setStatus(int arg0, String arg1)
   {
   }

   public int getBufferSize()
   {
      return bufferSize;
   }

   public String getCharacterEncoding()
   {
      return characterEncoding;
   }

   public ServletOutputStream getOutputStream() throws IOException
   {
      if (writer != null)
      {
         throw new IllegalStateException("Already obtained a PrintWriter");
      }
      if (outputStream == null)
      {
         outputStream = new ServletOutputStreamBuffer(500);
      }
      return outputStream;
   }

   public PrintWriter getWriter() throws IOException
   {
      if (outputStream != null)
      {
         throw new IllegalStateException("Already obtained a ServletOutputStream");
      }
      if (writer == null)
      {
         chars = new StringWriter();
         writer = new PrintWriter(chars, false);
      }
      return writer;
   }

   public boolean isCommitted()
   {
      return false;
   }

   public void reset()
   {
      resetBuffer();
   }

   public void resetBuffer()
   {
      if (outputStream != null)
      {
         outputStream.reset();
      }
      else if (chars != null)
      {
         writer.flush();
         chars.getBuffer().setLength(0);
      }
   }

   public void setBufferSize(int bufferSize)
   {
      this.bufferSize = bufferSize;
   }

   public void setCharacterEncoding(String characterEncoding)
   {
      this.characterEncoding = characterEncoding;
   }

   public void setContentLength(int arg0)
   {
   }

   public void setContentType(String arg0)
   {
   }

   public void setLocale(Locale arg0)
   {
   }
}
