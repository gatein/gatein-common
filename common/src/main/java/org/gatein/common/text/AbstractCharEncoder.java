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
package org.gatein.common.text;

import org.gatein.common.io.WriterCharWriter;
import org.gatein.common.util.ParameterValidation;

import java.io.StringWriter;
import java.io.Writer;

/**
 * @author <a href="mailto:julien@jboss.org">Julien Viet</a>
 * @version $Revision: 1.1 $
 */
public abstract class AbstractCharEncoder implements CharEncoder
{

   public void encode(char[] chars, int off, int len, CharWriter writer) throws EncodingException
   {
      ParameterValidation.throwIllegalArgExceptionIfRangeInvalid(chars, off, len);

      ParameterValidation.throwIllegalArgExceptionIfNull(writer, "CharWriter");

      //
      safeEncode(chars, off, len, writer);
   }

   public void encode(char[] chars, CharWriter writer) throws EncodingException
   {
      ParameterValidation.throwIllegalArgExceptionIfNull(chars, "char[]");
      encode(chars, 0, chars.length, writer);
   }

   public void encode(CharSequence s, CharWriter writer) throws EncodingException
   {
      ParameterValidation.throwIllegalArgExceptionIfNull(s, "CharSequence");

      //
      if (s instanceof String)
      {
         char[] chars = ((String)s).toCharArray();
         encode(chars, 0, chars.length, writer);
      }
      else
      {
         char[] chars = new char[s.length()];
         for (int i = 0; i < s.length(); i++)
         {
            char c = s.charAt(i);
            chars[i] = c;
         }
         encode(chars, 0, chars.length, writer);
      }
   }

   public void encode(char c, CharWriter writer) throws EncodingException
   {
      encode(new char[]{c}, writer);
   }

   public String encode(String string) throws IllegalArgumentException
   {
      ParameterValidation.throwIllegalArgExceptionIfNull(string, "String");

      Writer sw = new StringWriter();
      CharWriter charWriter = new WriterCharWriter(sw);
      safeEncode(string.toCharArray(), 0, string.length(), charWriter);
      return sw.toString();
   }

   protected abstract void safeEncode(char[] chars, int off, int len, CharWriter writer) throws EncodingException;
}
