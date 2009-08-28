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

import org.gatein.common.io.IOTools;
import org.gatein.common.junit.ExtendedAssert;
import junit.framework.TestCase;

import java.io.OutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.io.Writer;
import java.io.ByteArrayOutputStream;
import java.io.ByteArrayInputStream;
import java.io.CharArrayWriter;
import java.io.CharArrayReader;
import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * todo:
 * <ul>
 * <li>test safeBufferedWrapper</li>
 * <li>test serialize</li>
 * <li>test unserialize</li>
 * <li>test clone</li>
 * </ul>
 *
 * @author <a href="mailto:julien@jboss.org">Julien Viet</a>
 * @version $Revision: 1.1 $
 */
public class IOToolsTestCase extends TestCase
{

   public void testGenericSafeClose()
   {
      IOTools.safeClose((Object)null);
      IOTools.safeClose(new Object());

      //
      assertTrue(new GenericPublicSafeClose().apply().called);
      assertFalse(new GenericProtectedSafeClose().apply().called);
      assertFalse(new GenericPackageProtectedSafeClose().apply().called);
      assertFalse(new GenericPrivateSafeClose().apply().called);

      //
      IOTools.safeClose(new GenericPublicStaticSafeClose());
      assertFalse(GenericPublicStaticSafeClose.called);
      IOTools.safeClose(new GenericProtectedStaticSafeClose());
      assertFalse(GenericProtectedStaticSafeClose.called);
      IOTools.safeClose(new GenericPackageProtectedStaticSafeClose());
      assertFalse(GenericPackageProtectedStaticSafeClose.called);
      IOTools.safeClose(new GenericPrivateStaticSafeClose());
      assertFalse(GenericPrivateStaticSafeClose.called);
   }

   public static class GenericSafeClose
   {
      boolean called = false;
      public GenericSafeClose apply()
      {
         IOTools.safeClose(this);
         return this;
      }
   }

   public static class GenericPublicSafeClose extends GenericSafeClose
   {
      public void close()
      {
         called = true;
      }
   }

   public static class GenericProtectedSafeClose extends GenericSafeClose
   {
      protected void close()
      {
         called = true;
      }
   }

   public static class GenericPackageProtectedSafeClose extends GenericSafeClose
   {
      protected void close()
      {
         called = true;
      }
   }

   public static class GenericPrivateSafeClose extends GenericSafeClose
   {
      private void close()
      {
         called = true;
      }
   }

   public static class GenericPublicStaticSafeClose
   {
      static boolean called = false;
      public static void close()
      {
         called = true;
      }
   }

   public static class GenericProtectedStaticSafeClose
   {
      static boolean called = false;
      protected static void close()
      {
         called = true;
      }
   }

   public static class GenericPackageProtectedStaticSafeClose
   {
      static boolean called = false;
      static void close()
      {
         called = true;
      }
   }

   public static class GenericPrivateStaticSafeClose
   {
      static boolean called = false;
      private static void close()
      {
         called = true;
      }
   }


   public void testGenericSafeCloseWithThrowable()
   {
      new FailingGenericSafeClose()
      {
         protected void internalClose() throws Throwable
         {
            throw new Exception();
         }
      }.apply(null);
      new FailingGenericSafeClose()
      {
         protected void internalClose() throws Throwable
         {
            throw new Throwable();
         }
      }.apply(null);
      final RuntimeException runtimeException = new RuntimeException();
      new FailingGenericSafeClose()
      {
         protected void internalClose() throws Throwable
         {
            throw runtimeException;
         }
      }.apply(runtimeException);
      final Error error = new Error();
      new FailingGenericSafeClose()
      {
         protected void internalClose() throws Throwable
         {
            throw error;
         }
      }.apply(error);
   }

   public abstract static class FailingGenericSafeClose
   {
      public void apply(Throwable expectedThrowable)
      {
         try
         {
            IOTools.safeClose(this);
            assertNull(expectedThrowable);
         }
         catch (Throwable t)
         {
            assertEquals(expectedThrowable, t);
         }
      }

      public void close() throws Throwable
      {
         internalClose();
      }

      protected abstract void internalClose() throws Throwable;
   }

   public void testOutputStreamSafeClose()
   {
      IOTools.safeClose((OutputStream)null);
      final AtomicBoolean called = new AtomicBoolean(false);
      IOTools.safeClose(new AbstractOutputStream()
      {
         public void close() throws IOException
         {
            called.set(true);
         }
      });
      assertTrue(called.get());
      IOTools.safeClose(new AbstractOutputStream()
      {
         public void close() throws IOException
         {
            throw new IOException();
         }
      });
      final Error error = new Error();
      try
      {
         IOTools.safeClose(new AbstractOutputStream()
         {
            public void close() throws IOException
            {
               throw error;
            }
         });
         fail("Was expecting an error exception");
      }
      catch (Error expected)
      {
         assertEquals(error, expected);
      }
      final RuntimeException runtimeException = new RuntimeException();
      try
      {
         IOTools.safeClose(new AbstractOutputStream()
         {
            public void close() throws IOException
            {
               throw runtimeException;
            }
         });
         fail("Was expecting a runtime exception");
      }
      catch (RuntimeException expected)
      {
         assertEquals(runtimeException, expected);
      }
   }

   public abstract static class AbstractOutputStream extends OutputStream
   {
      public void write(int b) throws IOException
      {
      }
   }

   public void testInputStreamSafeClose()
   {
      IOTools.safeClose((InputStream)null);
      final AtomicBoolean called = new AtomicBoolean(false);
      IOTools.safeClose(new AbstractInputStream()
      {
         public void close() throws IOException
         {
            called.set(true);
         }
      });
      assertTrue(called.get());
      IOTools.safeClose(new AbstractInputStream()
      {
         public void close() throws IOException
         {
            throw new IOException();
         }
      });
      final Error error = new Error();
      try
      {
         IOTools.safeClose(new AbstractInputStream()
         {
            public void close() throws IOException
            {
               throw error;
            }
         });
         fail("Was expecting an error exception");
      }
      catch (Error expected)
      {
         assertEquals(error, expected);
      }
      final RuntimeException runtimeException = new RuntimeException();
      try
      {
         IOTools.safeClose(new AbstractInputStream()
         {
            public void close() throws IOException
            {
               throw runtimeException;
            }
         });
         fail("Was expecting a runtime exception");
      }
      catch (RuntimeException expected)
      {
         assertEquals(runtimeException, expected);
      }
   }

   public abstract static class AbstractInputStream extends InputStream
   {
      public int read() throws IOException
      {
         return 0;
      }
   }

   public void testReaderSafeClose()
   {
      IOTools.safeClose((Reader)null);
      final AtomicBoolean called = new AtomicBoolean(false);
      IOTools.safeClose(new AbstractReader()
      {
         public void close() throws IOException
         {
            called.set(true);
         }
      });
      assertTrue(called.get());
      IOTools.safeClose(new AbstractReader()
      {
         public void close() throws IOException
         {
            throw new IOException();
         }
      });
      final Error error = new Error();
      try
      {
         IOTools.safeClose(new AbstractReader()
         {
            public void close() throws IOException
            {
               throw error;
            }
         });
         fail("Was expecting an error exception");
      }
      catch (Error expected)
      {
         assertEquals(error, expected);
      }
      final RuntimeException runtimeException = new RuntimeException();
      try
      {
         IOTools.safeClose(new AbstractReader()
         {
            public void close() throws IOException
            {
               throw runtimeException;
            }
         });
         fail("Was expecting a runtime exception");
      }
      catch (RuntimeException expected)
      {
         assertEquals(runtimeException, expected);
      }
   }

   public abstract static class AbstractReader extends Reader
   {
      public int read(char cbuf[], int off, int len) throws IOException
      {
         return 0;
      }
   }

   public void testWriterSafeClose()
   {
      IOTools.safeClose((Writer)null);
      final AtomicBoolean called = new AtomicBoolean(false);
      IOTools.safeClose(new AbstractWriter()
      {
         public void close() throws IOException
         {
            called.set(true);
         }
      });
      assertTrue(called.get());
      IOTools.safeClose(new AbstractWriter()
      {
         public void close() throws IOException
         {
            throw new IOException();
         }
      });
      final Error error = new Error();
      try
      {
         IOTools.safeClose(new AbstractWriter()
         {
            public void close() throws IOException
            {
               throw error;
            }
         });
         fail("Was expecting an error exception");
      }
      catch (Error expected)
      {
         assertEquals(error, expected);
      }
      final RuntimeException runtimeException = new RuntimeException();
      try
      {
         IOTools.safeClose(new AbstractWriter()
         {
            public void close() throws IOException
            {
               throw runtimeException;
            }
         });
         fail("Was expecting a runtime exception");
      }
      catch (RuntimeException expected)
      {
         assertEquals(runtimeException, expected);
      }
   }

   public abstract static class AbstractWriter extends Writer
   {
      public void write(char cbuf[], int off, int len) throws IOException
      {
      }
      public void flush() throws IOException
      {
      }
   }

   public void testCopyInputStreamToOutputStream() throws IOException
   {
      try
      {
         IOTools.copy(null, new ByteArrayOutputStream());
         fail("Was expecting an IAE");
      }
      catch (IllegalArgumentException expected)
      {
      }

      //
      try
      {
         IOTools.copy(new ByteArrayInputStream(new byte[0]), null);
         fail("Was expecting an IOException");
      }
      catch (IllegalArgumentException expected)
      {
      }

      //
      final AtomicBoolean closeCalled1 = new AtomicBoolean(false);
      final IOException ioException1 = new IOException();
      try
      {
         IOTools.copy(new AbstractInputStream()
         {
            public int read() throws IOException
            {
               throw ioException1;
            }
            public void close() throws IOException
            {
               closeCalled1.set(true);
            }
         }, new ByteArrayOutputStream());
         fail("Was expecting an IOException");
      }
      catch (IOException expected)
      {
         assertEquals(ioException1, expected);
         assertFalse(closeCalled1.get());
      }

      //
      final AtomicBoolean closeCalled2 = new AtomicBoolean(false);
      IOTools.copy(new AbstractInputStream()
      {
         public int read() throws IOException
         {
            return -1;
         }
         public void close() throws IOException
         {
            closeCalled2.set(true);
         }
      }, new ByteArrayOutputStream());
      assertFalse(closeCalled2.get());

      //
      final IOException ioException2 = new IOException();
      final AtomicBoolean closeCalled3 = new AtomicBoolean(false);
      try
      {
         IOTools.copy(new ByteArrayInputStream(new byte[1]), new AbstractOutputStream()
         {
            public void write(int b) throws IOException
            {
               throw ioException2;
            }
            public void close() throws IOException
            {
               closeCalled3.set(true);
            }
         });
         fail("Was expecting an IOException");
      }
      catch (IOException expected)
      {
         assertEquals(ioException2, expected);
      }

      //
      final AtomicBoolean closeCalled4 = new AtomicBoolean(false);
      IOTools.copy(new ByteArrayInputStream(new byte[1]), new AbstractOutputStream()
      {
         public void close() throws IOException
         {
            closeCalled4.set(true);
         }
      });

      //
      Random random = new Random();
      byte[] bytes = new byte[2000];
      random.nextBytes(bytes);
      ByteArrayOutputStream baos = new ByteArrayOutputStream(bytes.length);
      IOTools.copy(new ByteArrayInputStream((byte[])bytes.clone()), baos);
      ExtendedAssert.assertEquals(bytes, baos.toByteArray());
   }
   
   public void testCopyReaderToWriter() throws IOException
   {
      try
      {
         IOTools.copy(null, new CharArrayWriter());
         fail("Was expecting an IAE");
      }
      catch (IllegalArgumentException expected)
      {
      }

      //
      try
      {
         IOTools.copy(new CharArrayReader(new char[0]), null);
         fail("Was expecting an IOException");
      }
      catch (IllegalArgumentException expected)
      {
      }

      //
      final AtomicBoolean closeCalled1 = new AtomicBoolean(false);
      final IOException ioException1 = new IOException();
      try
      {
         IOTools.copy(new AbstractReader()
         {
            public int read(char cbuf[], int off, int len) throws IOException
            {
               throw ioException1;
            }
            public void close() throws IOException
            {
               closeCalled1.set(true);
            }
         }, new CharArrayWriter());
         fail("Was expecting an IOException");
      }
      catch (IOException expected)
      {
         assertEquals(ioException1, expected);
         assertFalse(closeCalled1.get());
      }

      //
      final AtomicBoolean closeCalled2 = new AtomicBoolean(false);
      IOTools.copy(new AbstractReader()
      {
         public int read(char cbuf[], int off, int len) throws IOException
         {
            return -1;
         }
         public void close() throws IOException
         {
            closeCalled2.set(true);
         }
      }, new CharArrayWriter());
      assertFalse(closeCalled2.get());

      //
      final IOException ioException2 = new IOException();
      final AtomicBoolean closeCalled3 = new AtomicBoolean(false);
      try
      {
         IOTools.copy(new CharArrayReader(new char[1]), new AbstractWriter()
         {
            public void write(char cbuf[], int off, int len) throws IOException
            {
               throw ioException2;
            }
            public void close() throws IOException
            {
               closeCalled3.set(true);
            }
         });
         fail("Was expecting an IOException");
      }
      catch (IOException expected)
      {
         assertEquals(ioException2, expected);
      }

      //
      final AtomicBoolean closeCalled4 = new AtomicBoolean(false);
      IOTools.copy(new CharArrayReader(new char[1]), new AbstractWriter()
      {
         public void close() throws IOException
         {
            closeCalled4.set(true);
         }
      });

      //
      Random random = new Random();
      char[] chars = new char[2000];
      for (int i = 0; i < chars.length; i++)
      {
         chars[i] = (char)random.nextInt();

      }
      CharArrayWriter caw = new CharArrayWriter(chars.length);
      IOTools.copy(new CharArrayReader((char[])chars.clone()), caw);
      ExtendedAssert.assertEquals(chars, caw.toCharArray());
   }
}
