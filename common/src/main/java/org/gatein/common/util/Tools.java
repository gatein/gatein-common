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
package org.gatein.common.util;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.gatein.common.logging.Log4JWriter;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.Array;
import java.math.BigInteger;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.UnknownHostException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.ResourceBundle;
import java.util.Set;

/**
 * @author <a href="mailto:julien@jboss.org">Julien Viet</a>
 * @author <a href="mailto:theute@jboss.org">Thomas Heute</a>
 * @author <a href="mailto:boleslaw dot dawidowicz at jboss.com">Boleslaw Dawidowicz</a>
 * @author <a href="mailto:chris.laprun@jboss.com">Chris Laprun</a>
 * @version $Revision: 7377 $
 */
public class Tools
{

   public static final int DEFAULT_BUFFER_SIZE = 512;

   public static final Logger log = Logger.getLogger(Tools.class);

   /** 16 chars long VMID. */
   public static final String VMID = VMID();

   private static String VMID()
   {
      try
      {
         BigInteger bi = BigInteger.valueOf(0);
         byte[] address = java.net.InetAddress.getLocalHost().getAddress();
         for (int i = 0; i < 4; i++)
         {
            bi = bi.shiftLeft(8);
            bi = bi.add(BigInteger.valueOf(address[i]));
         }
         bi = bi.shiftLeft(32);
         int code = System.identityHashCode(new Object());
         bi = bi.add(BigInteger.valueOf(code));
         byte[] bytes = bi.toByteArray();
         StringBuffer buffer = new StringBuffer();
         char[] chars = "0123456789ABCDEF".toCharArray();
         for (int i = 0; i < bytes.length; i++)
         {
            buffer.append(chars[(bytes[i] & 0xF0) >> 4]).append(chars[bytes[i] & 0xF]);
         }
         return buffer.toString();
      }
      catch (UnknownHostException e)
      {
         e.printStackTrace(System.err);
         throw new Error("Cannot create VMID");
      }
   }

   public static final Enumeration EMPTY_ENUMERATION = new Enumeration()
   {
      public boolean hasMoreElements()
      {
         return false;
      }

      public Object nextElement()
      {
         throw new NoSuchElementException();
      }
   };

   public static final Iterator EMPTY_ITERATOR = new Iterator()
   {
      public boolean hasNext()
      {
         return false;
      }

      public Object next()
      {
         throw new NoSuchElementException();
      }

      public void remove()
      {
         throw new UnsupportedOperationException();
      }
   };

   public static final ResourceBundle EMPTY_BUNDLE = new ResourceBundle()
   {
      protected Object handleGetObject(String key)
      {
         return null;
      }

      public Enumeration getKeys()
      {
         return EMPTY_ENUMERATION;
      }
   };

   public static <E> Enumeration<E> emptyEnumeration()
   {
      return (Enumeration<E>)EMPTY_ENUMERATION;
   }

   public static <E> Iterator<E> emptyIterator()
   {
      return (Iterator<E>)EMPTY_ITERATOR;
   }

   public static <E> Enumeration<E> toEnumeration(final Iterator<E> iterator)
   {
      if (iterator == null)
      {
         throw new IllegalArgumentException();
      }
      return new Enumeration<E>()
      {
         public boolean hasMoreElements()
         {
            return iterator.hasNext();
         }

         public E nextElement()
         {
            return iterator.next();
         }
      };
   }

   public static <E> Enumeration<E> toEnumeration(final E[] objects)
   {
      if (objects == null)
      {
         throw new IllegalArgumentException();
      }
      return new Enumeration<E>()
      {
         int index = 0;

         public boolean hasMoreElements()
         {
            return index < objects.length;
         }

         public E nextElement()
         {
            if (index < objects.length)
            {
               return objects[index++];
            }
            else
            {
               throw new NoSuchElementException();
            }
         }
      };
   }

   public static <E> Enumeration<E> toEnumeration(final E o)
   {
      return new Enumeration<E>()
      {
         boolean hasMore = true;

         public boolean hasMoreElements()
         {
            return hasMore;
         }

         public E nextElement()
         {
            if (hasMore)
            {
               hasMore = false;
            }
            else
            {
               throw new NoSuchElementException();
            }
            return o;
         }
      };
   }

   public static <E> Set<E> toSet(Enumeration<E> e)
   {
      if (e == null)
      {
         throw new IllegalArgumentException();
      }
      HashSet<E> set = new HashSet<E>();
      while (e.hasMoreElements())
      {
         set.add(e.nextElement());
      }
      return set;
   }

   public static <E> Set<E> toSet(E... objects)
   {
      if (objects == null)
      {
         throw new IllegalArgumentException();
      }
      HashSet<E> set = new HashSet<E>();
      for (E object : objects)
      {
         set.add(object);
      }
      return set;
   }

   /**
    * Transforms an iterator into an unordered Set
    *
    * @param iterator The iterator to transform
    * @return A HashSet
    */
   public static <E> Set<E> toSet(Iterator<E> iterator)
   {
      if (iterator == null)
      {
         throw new IllegalArgumentException();
      }
      return toSet(iterator, false);
   }

   /**
    * Transforms an iterator into a Set
    *
    * @param iterator      The iterator to transform
    * @param preserveOrder true if the set must respect the ordering of the iterator
    * @return a LinkedHashSet if ordered is true, a HashSet otherwise
    */
   public static <E> Set<E> toSet(Iterator<E> iterator, boolean preserveOrder)
   {
      if (iterator == null)
      {
         throw new IllegalArgumentException();
      }
      Set<E> set;
      if (preserveOrder)
      {
         set = new LinkedHashSet<E>();
      }
      else
      {
         set = new HashSet<E>();
      }
      while (iterator.hasNext())
      {
         set.add(iterator.next());
      }
      return set;
   }

   public static <E> List<E> toList(Enumeration<E> e)
   {
      if (e == null)
      {
         throw new IllegalArgumentException();
      }
      List<E> list = new ArrayList<E>();
      while (e.hasMoreElements())
      {
         list.add(e.nextElement());
      }
      return list;
   }

   public static <E> List<E> toList(Iterator<E> iterator)
   {
      if (iterator == null)
      {
         throw new IllegalArgumentException();
      }
      List<E> list = new ArrayList<E>();
      while (iterator.hasNext())
      {
         list.add(iterator.next());
      }
      return list;
   }

   public static <E> List<E> toList(E... objects)
   {
      if (objects == null)
      {
         throw new IllegalArgumentException();
      }
      List<E> list = new ArrayList<E>(objects.length);
      for (E object : objects)
      {
         list.add(object);
      }
      return list;
   }

   /**
    * Consider remove this method as it cannot be generified.
    *
    * @param i
    * @return
    */
   @Deprecated
   public static Object[] toArray(Iterator i)
   {
      // This method cannot be generified.
      return toList(i).toArray();
   }

   /**
    * Returns a singleton iterator.
    *
    * @param o the singleton object
    * @return the iterator
    */
   public static <E> Iterator<E> iterator(final E o)
   {
      return new Iterator<E>()
      {
         /** The status of the iterator. */
         boolean done = false;

         public boolean hasNext()
         {
            return !done;
         }

         public E next()
         {
            if (done)
            {
               throw new NoSuchElementException("Already iterated");
            }
            done = true;
            return o;
         }

         public void remove()
         {
            throw new UnsupportedOperationException("read only");
         }
      };
   }

   /**
    * Returns an iterator over the array elements.
    *
    * @param objects the array containing the objects to iterate on
    * @return the iterator
    * @throws IllegalArgumentException if the object array is null or the specified range is not valid
    */
   public static <E> Iterator<E> iterator(final E... objects) throws IllegalArgumentException
   {
      if (objects == null)
      {
         throw new IllegalArgumentException("No null object array");
      }
      return iterator(objects, 0, objects.length);
   }

   /**
    * Returns an iterator over the array elements within the specified range. The range is considered as valid if the
    * from argument is greater or equals than zero, the to argument is lesser or equals than array size and the from
    * argument is lesser or equals to the to argument.
    *
    * @param objects the array containing the objects to iterate on
    * @param from    the inclusive start index
    * @param to      the exclusive stop index
    * @return the iterator
    * @throws IllegalArgumentException if the object array is null or the specified range is not valid or if the range
    *                                  is not valid
    */
   public static <E> Iterator<E> iterator(final E[] objects, final int from, final int to) throws IllegalArgumentException
   {
      if (objects == null)
      {
         throw new IllegalArgumentException("No null object array");
      }
      if (from > to || from < 0 || to > objects.length)
      {
         throw new IllegalArgumentException("Invalid range [" + from + "," + to + "] for array of length " + objects.length);
      }
      return new Iterator<E>()
      {
         /** . */
         int index = from;

         public boolean hasNext()
         {
            return index < to;
         }

         public E next()
         {
            if (index >= to)
            {
               throw new NoSuchElementException("Index is greater than the array length");
            }
            return objects[index++];
         }

         public void remove()
         {
            throw new UnsupportedOperationException("read only");
         }
      };
   }

   public static int computeStringHash(int hash, String s)
   {
      char[] chars = s.toCharArray();
      int length = chars.length;
      for (int i = 0; i < length; i++)
      {
         char c = chars[i];
         hash = 31 * hash + c;
      }
      return hash;
   }

   /**
    * Computes an md5 hash of a string.
    *
    * @param text the hashed string
    * @return the string hash
    * @throws NullPointerException if text is null
    */
   public static byte[] md5(String text)
   {
      // arguments check
      if (text == null)
      {
         throw new NullPointerException("null text");
      }

      try
      {
         MessageDigest md = MessageDigest.getInstance("MD5");
         md.update(text.getBytes());
         return md.digest();
      }
      catch (NoSuchAlgorithmException e)
      {
         log.error("Cannot find MD5 algorithm", e);
         throw new RuntimeException("Cannot find MD5 algorithm");
      }
   }

   /**
    * Computes an md5 hash and returns the result as a string in hexadecimal format.
    *
    * @param text the hashed string
    * @return the string hash
    * @throws NullPointerException if text is null
    */
   public static String md5AsHexString(String text)
   {
      return toHexString(md5(text));
   }

   /**
    * Computes a hash with specified algorighm and returns the result as a string in hexadecimal format
    *
    * @param text
    * @param algorithm
    * @param encoding
    * @return
    * @throws NoSuchAlgorithmException
    */
   public static String hashAndEncodeString(String text, String algorithm, String encoding) throws NoSuchAlgorithmException
   {
      // arguments check
      if (text == null)
      {
         throw new NullPointerException("null text");
      }
      if (algorithm == null)
      {
         throw new NullPointerException("null algorithm");
      }
      if (encoding == null)
      {
         throw new NullPointerException("null encoding");
      }

      MessageDigest md = MessageDigest.getInstance(algorithm);
      md.update(text.getBytes());
      byte[] encoded = md.digest();

      if ("HEX".equalsIgnoreCase(encoding))
      {
         return toHexString(encoded);
      }
      //TODO: add base64 support here
      else
      {
         throw new IllegalArgumentException("Not supported encoding: " + encoding);
      }

   }


   /**
    * Returns a string in the hexadecimal format.
    *
    * @param bytes the converted bytes
    * @return the hexadecimal string representing the bytes data
    * @throws IllegalArgumentException if the byte array is null
    */
   public static String toHexString(byte[] bytes)
   {
      if (bytes == null)
      {
         throw new IllegalArgumentException("byte array must not be null");
      }
      StringBuffer hex = new StringBuffer(bytes.length * 2);
      for (int i = 0; i < bytes.length; i++)
      {
         hex.append(Character.forDigit((bytes[i] & 0XF0) >> 4, 16));
         hex.append(Character.forDigit((bytes[i] & 0X0F), 16));
      }
      return hex.toString();
   }

   /**
    * Returns a byte array converted from the hexadecimal format.
    *
    * @param hex the string to convert
    * @return the byte array corresponding
    * @throws IllegalArgumentException if the string is null or does not have the good format
    */
   public static byte[] fromHexString(String hex)
   {
      if (hex == null)
      {
         throw new IllegalArgumentException("Hex string must not be null");
      }
      if (hex.length() % 2 == 1)
      {
         throw new IllegalArgumentException("Hex string length is not even : " + hex.length());
      }
      int index = 0;
      byte[] bytes = new byte[hex.length() / 2];
      for (int i = 0; i < bytes.length; i++)
      {
         char chigh = hex.charAt(index++);
         int high = Character.digit(chigh, 16);
         if (high == -1)
         {
            throw new IllegalArgumentException("Hex string contains a bad char : " + chigh);
         }
         char clow = hex.charAt(index++);
         int low = Character.digit(clow, 16);
         if (low == -1)
         {
            throw new IllegalArgumentException("Hex string contains a bad char : " + clow);
         }
         byte value = (byte)((high << 4) + low);
         bytes[i] = value;
      }
      return bytes;
   }

   /**
    *
    */
   public static String generateTemporaryHash(String value, long time)
   {
      if (value == null)
      {
         throw new IllegalArgumentException("id must not be null");
      }

      Calendar calendar = Calendar.getInstance();
      calendar.setTimeInMillis(time);
      calendar.set(Calendar.MINUTE, 0);
      calendar.set(Calendar.SECOND, 0);
      calendar.set(Calendar.MILLISECOND, 0);
      return md5AsHexString(value + calendar.getTimeInMillis());
   }

   /**
    *
    */
   public static boolean confirmTemporaryHash(String hash, String value, long time)
   {
      if (hash == null)
      {
         return false;
      }
      if (value == null)
      {
         throw new IllegalArgumentException("value must not be null");
      }

      Calendar calendar = Calendar.getInstance();
      calendar.setTimeInMillis(time);
      calendar.set(Calendar.MINUTE, 0);
      calendar.set(Calendar.SECOND, 0);
      calendar.set(Calendar.MILLISECOND, 0);
      String expected = md5AsHexString(value + calendar.getTimeInMillis());
      if (expected.equals(hash))
      {
         return true;
      }
      calendar.add(Calendar.HOUR_OF_DAY, -1);
      expected = md5AsHexString(value + calendar.getTimeInMillis());
      return expected.equals(hash);
   }

   public static String getShortNameOf(Class clazz)
   {
      return clazz.getSimpleName();
   }

   public static String getPackageOf(Class clazz)
   {
      String name = clazz.getName();
      int index = name.lastIndexOf('.');
      if (index != -1)
      {
         name = name.substring(0, index);
      }
      else
      {
         name = "";
      }
      return name;
   }

   public static String buildClassLoaderInfo(ClassLoader loader)
   {
      if (loader == null)
      {
         throw new IllegalArgumentException("no loader");
      }
      StringBuffer buffer = new StringBuffer();
      buffer.append("ClassLoader[Name=").append(loader.getClass().getName());
      buffer.append(",HashCode=").append(loader.hashCode());
      buffer.append(",IdentityHashCode=").append(System.identityHashCode(loader));
      if (loader instanceof URLClassLoader)
      {
         URLClassLoader urlLoader = (URLClassLoader)loader;
         URL[] urls = urlLoader.getURLs();
         for (int i = 0; i < urls.length; i++)
         {
            URL url = urls[i];
            buffer.append(",URL(").append(i).append(")=").append(url);
         }
      }
      try
      {
         Class uclClass = Thread.currentThread().getContextClassLoader().loadClass("org.jboss.mx.loading.UnifiedClassLoader");
         Class loaderClass = loader.getClass();
         if (uclClass.isAssignableFrom(loaderClass))
         {
            URL url = (URL)loaderClass.getMethod("getURL", new Class[0]).invoke(loader, new Object[0]);
            buffer.append(",GetURL=").append(url);
         }
      }
      catch (Exception e)
      {
         log.error("Cannot get UCL infos", e);
      }
      buffer.append("]");
      return buffer.toString();
   }

   public static String dumpClassLoaderHierarchyInfo(ClassLoader loader)
   {
      StringWriter writer = new StringWriter();
      dumpClassLoaderHierarchyInfo(writer, loader);
      return writer.toString();
   }

   public static void dumpClassLoaderHierarchyInfo(Writer writer, ClassLoader loader)
   {
      if (writer == null)
      {
         throw new IllegalArgumentException("no writer");
      }
      if (loader == null)
      {
         throw new IllegalArgumentException("no loader");
      }

      //
      PrintWriter pw = null;
      if (writer instanceof PrintWriter)
      {
         pw = (PrintWriter)writer;
      }
      else
      {
         pw = new PrintWriter(writer);
      }

      pw.println("<classloader-dump>");
      while (loader != null)
      {
         pw.println(buildClassLoaderInfo(loader));
         loader = loader.getParent();
      }
      pw.print("</classloader-dump>");
      pw.flush();
   }

   public static void dumpClassLoaderHierarchyInfo(Logger log, ClassLoader loader)
   {
      Writer writer = new Log4JWriter(log, Level.DEBUG);
      dumpClassLoaderHierarchyInfo(writer, loader);
   }

   public static void dumpClassLoaderHierarchyInfo(Logger log, Level level, ClassLoader loader)
   {
      Writer writer = new Log4JWriter(log, level);
      dumpClassLoaderHierarchyInfo(writer, loader);
   }

   /**
    * Replace occurence in a string.
    *
    * @param string      the source string
    * @param pattern     the replaced pattern
    * @param replacement the replacement text
    * @return the new string
    */
   public static String replace(String string, String pattern, String replacement)
   {
      StringBuffer buffer = new StringBuffer(string.length());
      int previous = 0;
      int current = string.indexOf(pattern);
      while (current != -1)
      {
         buffer.append(string.substring(previous, current));
         buffer.append(replacement);
         previous = current + pattern.length();
         current = string.indexOf(pattern, previous);
      }
      buffer.append(string.substring(previous));
      return buffer.toString();
   }

   /**
    * Append an object to an array of objects. The original array is not modified. The returned array will be of the
    * same component type of the provided array and its first n elements where n is the size of the provided array will
    * be the elements of the provided array. The last element of the array will be the provided object to append.
    *
    * @param array the array to augment
    * @param o     the object to append
    * @return a new array
    * @throws IllegalArgumentException if the array is null
    * @throws ClassCastException       if the appended object class prevents it from being added to the array
    */
   public static <E> E[] appendTo(E[] array, E o) throws IllegalArgumentException, ClassCastException
   {
      if (array == null)
      {
         throw new IllegalArgumentException();
      }

      //
      Class componentType = array.getClass().getComponentType();
      if (o != null && !componentType.isAssignableFrom(o.getClass()))
      {
         throw new ClassCastException("Object with class " + o.getClass().getName() + " cannot be casted to class " + componentType.getName());
      }

      //
      E[] copy = (E[])Array.newInstance(componentType, array.length + 1);
      System.arraycopy(array, 0, copy, 0, array.length);
      copy[array.length] = o;

      //
      return copy;
   }

   /**
    * Return true if
    * <ul>
    *    <li>o1 is null and o2 is null</li>
    *    <li>o1 is not null and the invocation of <code>equals(Object o)</code> on o1 wit o2 as argument returns true</li>
    * </ul>
    *
    * @param o1 the first argument
    * @param o2 the second argument
    * @return if arguments are equals according to the semantic defined by the method contract
    */
   public static boolean safeEquals(Object o1, Object o2)
   {
      if (o1 == null)
      {
         return o2 == null;
      }
      else
      {
         return o1.equals(o2);
      }
   }

   /**
    * Same as replaceBoundedString(initial, prefix, suffix, replacement, true, false).
    *
    * @param initial
    * @param prefix
    * @param suffix
    * @param replacement
    * @return
    */
   public static String replaceAllInstancesOfBoundedString(String initial, String prefix, String suffix, String replacement)
   {
      return replaceBoundedString(initial, prefix, suffix, replacement, true, false);
   }

   /**
    * Replace instances of Strings delimited by the given prefix and suffix (hence, bounded) by the specified
    * replacement String. It is possible to specify whether the substitution will happen only if the delimited String is
    * non-empty by setting <code>replaceIfBoundedStringEmpty</code> to <code>false</code>. It is also possible to keep
    * the boundaries (prefix and suffix) after the substitution by setting <code>keepBoundaries</code> to
    * <code>true</code>.
    * <p/>
    * See org.jboss.portal.test.common.StringTestCase.testReplaceBoundedString() for usage details.
    *
    * @param initial                     the String in which we want to replace bounded Strings
    * @param prefix                      the prefix used identify the beginning of the String targeted for replacement
    * @param suffix                      the suffix used to identify the end of the String targeted for replacement
    * @param replacement                 the String to replace the bounded String with
    * @param replaceIfBoundedStringEmpty <code>true</code> to allow replacement of empty Strings (in essence, insertion
    *                                    of the replacement String between the prefix and suffix)
    * @param keepBoundaries              <code>true</code> to keep the prefix and suffix markers in the resulting
    *                                    String
    * @return a String where the Strings marked by prefix and suffix have been replaced by replacement
    */
   public static String replaceBoundedString(String initial, String prefix, String suffix, String replacement,
                                             boolean replaceIfBoundedStringEmpty, boolean keepBoundaries)
   {
      if (initial == null || initial.length() == 0)
      {
         return initial;
      }

      ParameterValidation.throwIllegalArgExceptionIfNullOrEmpty(prefix, "prefix", "Tools.replaceBoundedString");
      ParameterValidation.throwIllegalArgExceptionIfNullOrEmpty(suffix, "suffix", "Tools.replaceBoundedString");
      ParameterValidation.throwIllegalArgExceptionIfNull(replacement, "replacement");

      StringBuffer tmp = new StringBuffer(initial);
      int prefixIndex = tmp.indexOf(prefix);
      int suffixLength = suffix.length();
      int prefixLength = prefix.length();

      while (prefixIndex != -1)
      {
         int suffixIndex = tmp.indexOf(suffix, prefixIndex);

         if (suffixIndex != -1)
         {
            // we don't care about empty bounded strings or prefix and suffix don't delimit an empty String => replace!
            if (replaceIfBoundedStringEmpty || suffixIndex != prefixIndex + prefixLength)
            {
               if (keepBoundaries)
               {
                  tmp.delete(prefixIndex + prefixLength, suffixIndex);
                  tmp.insert(prefixIndex + prefixLength, replacement);
               }
               else
               {
                  tmp.delete(prefixIndex, suffixIndex + suffixLength);
                  tmp.insert(prefixIndex, replacement);
               }
            }
         }

         prefixIndex = tmp.indexOf(prefix, prefixIndex + prefixLength);
      }

      return tmp.toString();
   }

   /**
    * Determines if value is contained in array.
    * <p/>
    * todo: correct this method contract in order to make it more reusable, it looks like for now like a method which
    * has a contract convenient only for some kind of callers.
    * <p/>
    * <ol>
    * <li>null value should be accepted (or the method should be called isContainedInButNotForNullValue ?)</li>
    * <li>null array should not be accepted (or the method should be called isContainedInExceptIfThePassedArrayIsNull
    * ?)</li>
    * </ol>
    *
    * @param value
    * @param array
    * @return
    * @since 2.4.2
    */
   public static boolean isContainedIn(Object value, Object[] array)
   {
      if (value == null)
      {
         return false;
      }

      //
      if (array != null)
      {
         for (Object anArray : array)
         {
            if (value.equals(anArray))
            {
               return true;
            }
         }
      }
      return false;
   }

   /**
    * Attempt to cast the value argument to the provided type argument. If the value argument type is assignable to the
    * provided type, the value is returned, otherwise if it is not or the value is null, null is returned.
    * <p/>
    * todo: Move that to common package.
    *
    * @param value the value to cast
    * @param type  the type to downcast
    * @return the casted value or null
    */
   public static <T> T safeCast(Object value, Class<T> type)
   {
      if (value == null)
      {
         return null;
      }
      else
      {
         if (type.isAssignableFrom(value.getClass()))
         {
            return type.cast(value);
         }
         else
         {
            return null;
         }
      }
   }

   public static <T> MultiValuedPropertyMap<T> emptyMultiValuedPropertyMap()
   {
      return new MultiValuedPropertyMap<T>()
      {
         public T getValue(String key) throws IllegalArgumentException
         {
            if (key == null)
            {
               throw new IllegalArgumentException("Key cannot be null");
            }
            return null;
         }

         public List<T> getValues(String key) throws IllegalArgumentException
         {
            if (key == null)
            {
               throw new IllegalArgumentException("Key cannot be null");
            }
            return null;
         }

         public void addValue(String key, T value) throws IllegalArgumentException
         {
            throw new UnsupportedOperationException();
         }

         public void setValue(String key, T value) throws IllegalArgumentException
         {
            throw new UnsupportedOperationException();
         }

         public Set<String> keySet()
         {
            return Collections.emptySet();
         }

         public int size()
         {
            return 0;
         }

         public void clear()
         {
            throw new UnsupportedOperationException();
         }

         public void append(MultiValuedPropertyMap<T> appended) throws IllegalArgumentException
         {
            throw new UnsupportedOperationException();
         }
      };
   }

}