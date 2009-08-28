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
package org.gatein.common.io;

import org.gatein.common.text.CharWriter;
import org.gatein.common.util.ParameterValidation;

import java.io.IOException;
import java.io.Writer;

/**
 * @author <a href="mailto:julien@jboss.org">Julien Viet</a>
 * @version $Revision: 1.1 $
 */
public class WriterCharWriter implements CharWriter
{

   /** . */
   private final Writer writer;

   /**
    * @param writer the target writer
    * @throws IllegalArgumentException if the writer is null
    */
   public WriterCharWriter(Writer writer) throws IllegalArgumentException
   {
      ParameterValidation.throwIllegalArgExceptionIfNull(writer, "Writer");
      this.writer = writer;
   }

   public CharWriter append(char c) throws UndeclaredIOException
   {
      try
      {
         writer.write(c);
      }
      catch (IOException e)
      {
         throw new UndeclaredIOException(e);
      }
      return this;
   }

   public CharWriter append(char[] chars, int off, int len) throws UndeclaredIOException
   {
      ParameterValidation.throwIllegalArgExceptionIfRangeInvalid(chars, off, len);

      try
      {
         writer.write(chars, off, len);
      }
      catch (IOException e)
      {
         throw new UndeclaredIOException(e);
      }
      return this;
   }

   public CharWriter append(char[] chars) throws UndeclaredIOException
   {
      ParameterValidation.throwIllegalArgExceptionIfNull(chars, "char[]");
      try
      {
         writer.write(chars, 0, chars.length);
      }
      catch (IOException e)
      {
         throw new UndeclaredIOException(e);
      }
      return this;
   }

   public CharWriter append(CharSequence s) throws UndeclaredIOException
   {
      ParameterValidation.throwIllegalArgExceptionIfNull(s, "CharSequence");
      try
      {
         if (s instanceof String)
         {
            writer.write((String)s);
         }
         else
         {
            for (int i = 0; i < s.length(); i++)
            {
               char c = s.charAt(i);
               writer.write(c);
            }
         }
      }
      catch (IOException e)
      {
         throw new UndeclaredIOException(e);
      }
      return this;
   }

   public void flush() throws UndeclaredIOException
   {
      try
      {
         writer.flush();
      }
      catch (IOException e)
      {
         throw new UndeclaredIOException(e);
      }
   }

   public void close() throws UndeclaredIOException
   {
      try
      {
         writer.flush();
      }
      catch (IOException e)
      {
         throw new UndeclaredIOException(e);
      }
   }
}
