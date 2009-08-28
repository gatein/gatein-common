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
package org.jboss.portal.test.common.io;

import org.gatein.common.io.WriterCharWriter;
import org.gatein.common.io.UndeclaredIOException;

import java.io.StringWriter;
import java.io.IOException;
import java.io.Writer;

import junit.framework.TestCase;

/**
 * @author <a href="mailto:julien@jboss.org">Julien Viet</a>
 * @version $Revision: 1.1 $
 */
public class WriterCharWriterTestCase extends TestCase
{

   public void testAppend1() throws IOException
   {
      StringWriter s = new StringWriter();
      WriterCharWriter writer = new WriterCharWriter(s);
      writer.append('a');
      s.close();
      assertEquals("a", s.toString());
   }

   public void testAppend2() throws IOException
   {
      StringWriter s = new StringWriter();
      WriterCharWriter writer = new WriterCharWriter(s);
      writer.append("abc".toCharArray());
      s.close();
      assertEquals("abc", s.toString());
   }

   public void testAppend3() throws IOException
   {
      StringWriter s = new StringWriter();
      WriterCharWriter writer = new WriterCharWriter(s);
      writer.append("abcdef".toCharArray(), 1, 3);
      s.close();
      assertEquals("bcd", s.toString());
   }

   public void testAppend4() throws IOException
   {
      StringWriter s = new StringWriter();
      WriterCharWriter writer = new WriterCharWriter(s);
      writer.append("abc");
      s.close();
      assertEquals("abc", s.toString());
   }

   private Writer failingWriter = new Writer()
   {
      public void write(char[] chars, int i, int i1) throws IOException
      {
         throw new IOException();
      }

      public void flush() throws IOException
      {
         throw new IOException();
      }

      public void close() throws IOException
      {
         throw new IOException();
      }
   };

   public void testWrappedIOException()
   {
      WriterCharWriter writer = new WriterCharWriter(failingWriter);
      try
      {
         writer.append('c');
         fail("Was expecting an UndeclaredIOException");
      }
      catch (UndeclaredIOException expected)
      {
      }
      try
      {
         writer.append("abc".toCharArray());
         fail("Was expecting an UndeclaredIOException");
      }
      catch (UndeclaredIOException expected)
      {
      }
      try
      {
         writer.append("abcdef".toCharArray(), 1, 3);
         fail("Was expecting an UndeclaredIOException");
      }
      catch (UndeclaredIOException expected)
      {
      }
      try
      {
         writer.append("abc");
         fail("Was expecting an UndeclaredIOException");
      }
      catch (UndeclaredIOException expected)
      {
      }
   }

   public void testAppendThrowsIAE()
   {
      try
      {
         new WriterCharWriter(null);
         fail();
      }
      catch (IllegalArgumentException expected)
      {
      }

      //
      WriterCharWriter writer = new WriterCharWriter(new StringWriter());
      try
      {
         writer.append(new char[10], -1, 0);
         fail();
      }
      catch (IllegalArgumentException expected)
      {
      }
      try
      {
         writer.append(new char[10], 5, -1);
         fail();
      }
      catch (IllegalArgumentException expected)
      {
      }
      try
      {
         writer.append(new char[10], 15, 0);
         fail();
      }
      catch (IllegalArgumentException expected)
      {
      }
      try
      {
         writer.append(new char[10], 5, 6);
         fail();
      }
      catch (IllegalArgumentException expected)
      {
      }
      try
      {
         writer.append(null, 0, 5);
         fail();
      }
      catch (IllegalArgumentException expected)
      {
      }
      try
      {
         writer.append((char[])null);
         fail();
      }
      catch (IllegalArgumentException expected)
      {
      }
      try
      {
         writer.append((CharSequence)null);
         fail();
      }
      catch (IllegalArgumentException expected)
      {
      }
   }

}
