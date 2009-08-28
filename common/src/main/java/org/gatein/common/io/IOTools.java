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

import org.gatein.common.util.Tools;
import org.apache.log4j.Logger;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.InvocationTargetException;
import java.io.OutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.io.ByteArrayOutputStream;
import java.io.Writer;
import java.io.Serializable;
import java.io.ObjectOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ObjectStreamClass;
import java.io.Closeable;

/**
 * IO tools.
 *
 * @author <a href="mailto:julien@jboss.org">Julien Viet</a>
 * @version $Revision: 1.1 $
 */
public class IOTools
{

   /** The logger. */
   public static final Logger log = Logger.getLogger(IOTools.class);

   /** . */
   private static final Object[] EMPTY_ARGS = new Object[0];

   /** . */
   private static final Class[] EMPTY_PARAMETER_TYPES = new Class[0];

   /**
    * <p>Attempt to close an object. Null argument value is authorized and no operation will be performed in that
    * use case.</p>
    *
    * <p>It will try to obtain a <code>close()</code> method by reflection and it
    * will be invoked only if the method is public and not static. If the method is called, any <code>Error</code>
    * or <code>RuntimeException</code> will be rethrown, any other kind of throwable will not be rethrown in any form.</p>
    *
    * @param closable the object to close
    */
   public static void safeClose(Object closable)
   {
      if (closable != null)
      {
         try
         {
            Method m = closable.getClass().getMethod("close", EMPTY_PARAMETER_TYPES);
            if (Modifier.isStatic(m.getModifiers()))
            {
               log.warn("close() method on closable object is static");
               return;
            }
            m.invoke(closable, EMPTY_ARGS);
         }
         catch (NoSuchMethodException e)
         {
            log.warn("The closable object does not have a close() method", e);
         }
         catch (IllegalAccessException e)
         {
            log.warn("Cannot access close() method on closable object", e);
         }
         catch (InvocationTargetException e)
         {
            Throwable t = e.getCause();

            //
            if (t instanceof RuntimeException)
            {
               log.error("The close() method threw a runtime exception", t);
               throw (RuntimeException)t;
            }
            else if (t instanceof Error)
            {
               log.error("The close() method threw an error", t);
               throw (Error)t;
            }
            else if (t instanceof Exception)
            {
               log.error("The close() method threw an exception", t);
            }
            else
            {
               log.error("The close() method threw an unexpected throwable", t);
            }
         }
      }
   }

   /**
    * <p>Attempt to close an {@link Closeable} object. Null argument value is authorized and no operation will be performed in that
    * use case. {@link IOException} thrown are logged using the <code>error</code> level but not propagated.</p>
    *
    * @param out the stream to close
    */
   public static void safeClose(Closeable out)
   {
      if (out != null)
      {
         try
         {
            out.close();
         }
         catch (IOException e)
         {
            log.error("Error while closing closeable " + out, e);
         }
      }
   }

   /**
    * @see #getBytes(java.io.InputStream, int)
    * @param in the input stream
    * @return the bytes read from the stream
    * @throws java.io.IOException
    * @throws IllegalArgumentException if the input stream is null
    */
   public static byte[] getBytes(InputStream in) throws IOException, IllegalArgumentException
   {
      return getBytes(in, Tools.DEFAULT_BUFFER_SIZE);
   }

   /**
    * Get the bytes from the provided input stream. No attempt will be made to close the stream.
    *
    * @param in the input stream
    * @param bufferSize the buffer size used to copy the bytes
    * @return the bytes read from the stream
    * @throws java.io.IOException
    * @throws IllegalArgumentException if the input stream is null or the buffer size < 1
    */
   public static byte[] getBytes(InputStream in, int bufferSize) throws IOException, IllegalArgumentException
   {
      ByteArrayOutputStream out = new ByteArrayOutputStream();
      copy(in, out, bufferSize);
      return out.toByteArray();
   }

   /**
    * @see #copy(java.io.InputStream, java.io.OutputStream, int)
    * @param in  the incoming stream
    * @param out the outcoming stream
    * @throws IllegalArgumentException if an argument is null
    */
   public static void copy(InputStream in, OutputStream out) throws IOException
   {
      copy(in, out, Tools.DEFAULT_BUFFER_SIZE);
   }

   /**
    * Pipe an incoming stream in an outcoming stream until no bytes is available from the input stream.
    * No attempts will be made to close the streams.
    *
    * @param in         the incoming stream
    * @param out        the outcoming stream
    * @param bufferSize the buffer size
    * @throws IllegalArgumentException if bufferSize < 1 or an argument is null
    */
   public static void copy(InputStream in, OutputStream out, int bufferSize) throws IOException
   {
      // arguments check
      if (in == null)
      {
         throw new IllegalArgumentException("null in");
      }
      if (out == null)
      {
         throw new IllegalArgumentException("null out");
      }
      if (bufferSize < 1)
      {
         throw new IllegalArgumentException("Buffer size is too small");
      }

      // do the job
      byte[] buffer = new byte[bufferSize];
      while (true)
      {
         int i = in.read(buffer);
         if (i == 0)
         {
            continue;
         }
         if (i == -1)
         {
            break;
         }
         out.write(buffer, 0, i);
      }
   }

   /**
    * Pipe an input stream in an output stream.
    *
    * @param reader the incoming reader
    * @param writer the outcoming writer
    * @throws IllegalArgumentException if an argument is null
    */
   public static void copy(Reader reader, Writer writer) throws IOException
   {
      copy(reader, writer, Tools.DEFAULT_BUFFER_SIZE);
   }

   /**
    * Pipe an incoming stream in an outcoming stream.
    *
    * @param reader     the incoming reader
    * @param writer     the outcoming writer
    * @param bufferSize the buffer size
    * @throws IllegalArgumentException if bufferSize < 1 or an argument is null
    */
   public static void copy(Reader reader, Writer writer, int bufferSize) throws IOException
   {
      // arguments check
      if (reader == null)
      {
         throw new IllegalArgumentException("null in");
      }
      if (writer == null)
      {
         throw new IllegalArgumentException("null out");
      }
      if (bufferSize < 1)
      {
         throw new IllegalArgumentException("Buffer size is too small");
      }

      // do the job
      char[] buffer = new char[bufferSize];
      while (true)
      {
         int i = reader.read(buffer);
         if (i == 0)
         {
            continue;
         }
         if (i == -1)
         {
            break;
         }
         writer.write(buffer, 0, i);
      }
   }

   /**
    * Clone an object implementing the <code>Serializable</code> interface.
    *
    * @param serializable the serializable object to clone
    * @return a clone
    * @throws IllegalArgumentException if the serializable object is null
    * @throws IOException any IOException
    */
   public static <S extends Serializable> S clone(S serializable) throws IllegalArgumentException, IOException
   {
      try
      {
         return clone(serializable, null);
      }
      catch (ClassNotFoundException e)
      {
         throw new Error("Got a class not found exception for a class that was used", e);
      }
   }

   /**
    * Clone an object implementing the <code>Serializable</code> interface. The specified classloader will be used
    * to perform the unserialization. If no classloader is specified and the object is not null then the classloader
    * used is the one returned by <code>serializable.getClass().getClassLoader()</code>.
    *
    * @param serializable the serializable object to clone
    * @return a clone
    * @throws IllegalArgumentException if the serializable object is null
    * @throws IOException any IOException
    */
   public static <S extends Serializable> S clone(S serializable, ClassLoader classLoader) throws IllegalArgumentException, IOException, ClassNotFoundException
   {
      if (serializable == null)
      {
         throw new IllegalArgumentException("Cannot clone null");
      }

      //
      if (classLoader == null && serializable != null)
      {
         classLoader = serializable.getClass().getClassLoader();
      }

      //
      return (S)unserialize(serialize(serializable), classLoader);
   }

   public static byte[] serialize(Serializable serializable) throws IllegalArgumentException, IOException
   {
      if (serializable == null)
      {
         throw new IllegalArgumentException("No null serializable accepted");
      }
      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      ObjectOutputStream oos = new ObjectOutputStream(baos);
      oos.writeObject(serializable);
      oos.close();
      return baos.toByteArray();
   }

   /**
    * Unserialize the bytes into an object. The thread context classloader is used to perform unserialization.
    *
    * @param bytes the bytes to unserialize
    * @return the unserialized object
    * @throws IllegalArgumentException if the byte array is null
    * @throws IOException any IOException
    * @throws ClassNotFoundException any ClassNotFoundException
    */
   public static Serializable unserialize(byte[] bytes) throws IllegalArgumentException, IOException, ClassNotFoundException
   {
      return unserialize(bytes, null);
   }

   /**
    * Unserialize the bytes into an object. If the provided classloader is not null, this classloader is used to perform
    * the unserialization otherwise the thread current context classloader is used.
    *
    * @param bytes the bytes to unserialize
    * @param classLoader the classloader
    * @return the unserialized object
    * @throws IllegalArgumentException if the byte array is null
    * @throws IOException any IOException
    * @throws ClassNotFoundException any ClassNotFoundException
    */
   public static Serializable unserialize(byte[] bytes, final ClassLoader classLoader) throws IllegalArgumentException, IOException, ClassNotFoundException
   {
      if (bytes == null)
      {
         throw new IllegalArgumentException("No null serializable accepted");
      }

      //
      ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
      ObjectInputStream ois = new ObjectInputStream(bais)
      {
         protected Class<?> resolveClass(ObjectStreamClass desc) throws IOException, ClassNotFoundException
         {
            if (classLoader == null)
            {
               return super.resolveClass(desc);
            }
            else
            {
               String className = desc.getName();
               
               // JDK 6, by default, only supports array types (ex. [[B)  using Class.forName()
               return Class.forName(className, false, classLoader);
            }
         }

         protected Class resolveProxyClass(String[] interfaces) throws IOException, ClassNotFoundException
         {
            if (classLoader == null)
            {
               return super.resolveProxyClass(interfaces);
            }
            else
            {
               // Load the interfaces from the specified class loader
               Class[] ifaceClasses = new Class[interfaces.length];
               for (int i = 0; i < interfaces.length; i++)
               {
                  ifaceClasses[i] = classLoader.loadClass(interfaces[i]);
               }
      
               return java.lang.reflect.Proxy.getProxyClass(classLoader, ifaceClasses);
            }
         }
      };
      return (Serializable)ois.readObject();
   }

   public static <T> byte[] serialize(Serialization<T> serialization, T t)
   {
      return serialize(serialization, SerializationFilter.TRIVIAL, t);
   }

   public static <T> byte[] serialize(Serialization<T> serialization, SerializationFilter filter, T t)
   {
      if (serialization == null)
      {
         throw new IllegalArgumentException();
      }
      if (t == null)
      {
         throw new IllegalArgumentException("No null object to serialize");
      }
      try
      {
         ByteArrayOutputStream baos = new ByteArrayOutputStream();
         filter.serialize(serialization, t, baos);
         return baos.toByteArray();
      }
      catch (IOException e)
      {
         throw new UndeclaredIOException(e);
      }
   }

   public static <T> T unserialize(Serialization<T> serialization, byte[] bytes)
   {
      return unserialize(serialization, SerializationFilter.TRIVIAL, bytes);
   }

   public static <T> T unserialize(Serialization<T> serialization, SerializationFilter filter, byte[] bytes)
   {
      if (serialization == null)
      {
         throw new IllegalArgumentException();
      }
      if (bytes == null)
      {
         throw new IllegalArgumentException("No null bytes to unserialize");
      }
      try
      {
         ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
         return filter.unserialize(serialization, bais);
      }
      catch (IOException e)
      {
         throw new UndeclaredIOException(e);
      }
   }

   /**
    * Check that the provided input stream is buffered. If the argument is already an instance of <code>BufferedInputStream</code>
    * no operation will be performed, otherwise a instance of <code>BufferedInputStream</code> will be created and returned.
    *
    * If the provided argument is null, the null value is returned.
    *
    * @param in the stream
    * @return a buffered wrapper
    */
   public static BufferedInputStream safeBufferedWrapper(InputStream in)
   {
      if (in != null)
      {
         if (in instanceof BufferedInputStream)
         {
            return (BufferedInputStream)in;
         }
         else
         {
            return new BufferedInputStream(in);
         }
      }
      else
      {
         return null;
      }
   }

   /**
    * Check that the provided output stream is buffered. If the argument is already an instance of <code>BufferedOutputStream</code>
    * no operation will be performed, otherwise a instance of <code>BufferedOutputStream</code> will be created and returned.
    *
    * If the provided argument is null, the null value is returned.
    *
    * @param out the stream
    * @return a buffered wrapper
    */
   public static BufferedOutputStream safeBufferedWrapper(OutputStream out)
   {
      if (out != null)
      {
         if (out instanceof BufferedOutputStream)
         {
            return (BufferedOutputStream)out;
         }
         else
         {
            return new BufferedOutputStream(out);
         }
      }
      else
      {
         return null;
      }
   }
}
