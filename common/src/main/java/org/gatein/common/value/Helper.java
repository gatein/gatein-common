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
package org.gatein.common.value;

import org.gatein.common.util.FormatConversionException;
import org.gatein.common.util.NullConversionException;


/**
 * @author <a href="mailto:julien@jboss.org">Julien Viet</a>
 * @version $Revision: 7200 $
 */
@Deprecated
public class Helper
{

   public static final int[] EMPTY_INT_ARRAY = new int[0];
   public static final boolean[] EMPTY_BOOLEAN_ARRAY = new boolean[0];
   public static final Object[] EMPTY_OBJECT_ARRAY = new Object[0];
   public static final String[] EMPTY_STRING_ARRAY = new String[0];

   public static final Converter FLOAT_CONVERTER = new Converter()
   {
      public boolean accept(Class clazz)
      {
         return Float.class.equals(clazz);
      }
      public Object toObject(String value) throws NullConversionException, FormatConversionException
      {
         if (value == null)
         {
            return null;
         }
         try
         {
            return Float.valueOf(value);
         }
         catch (NumberFormatException e)
         {
            throw new FormatConversionException();
         }
      }
      public String toString(Object value) throws NullConversionException, FormatConversionException
      {
         if (value == null)
         {
            return null;
         }
         if (!(value instanceof Float))
         {
            throw new FormatConversionException();
         }
         return value.toString();
      }
   };

   public static final Converter INTEGER_CONVERTER = new Converter()
   {
      public boolean accept(Class clazz)
      {
         return Integer.class.equals(clazz);
      }
      public Object toObject(String value) throws NullConversionException, FormatConversionException
      {
         if (value == null)
         {
            return null;
         }
         try
         {
            return Integer.valueOf(value);
         }
         catch (NumberFormatException e)
         {
            throw new FormatConversionException();
         }
      }
      public String toString(Object value) throws NullConversionException, FormatConversionException
      {
         if (value == null)
         {
            return null;
         }
         if (!(value instanceof Integer))
         {
            throw new FormatConversionException();
         }
         return value.toString();
      }
   };

   public static final Converter BOOLEAN_CONVERTER = new Converter()
   {
      public boolean accept(Class clazz)
      {
         return Boolean.class.equals(clazz);
      }
      public Object toObject(String value) throws NullConversionException, FormatConversionException
      {
         if (value == null)
         {
            return null;
         }
         return Boolean.valueOf(Helper.toBoolean(value));
      }
      public String toString(Object value) throws NullConversionException, FormatConversionException
      {
         if (value == null)
         {
            return null;
         }
         if (!(value instanceof Boolean))
         {
            throw new FormatConversionException();
         }
         return value.toString();
      }
   };

   public static int toInt(String value) throws NullConversionException, FormatConversionException
   {
      try
      {
         if (value == null)
         {
            throw new NullConversionException();
         }
         return Integer.parseInt(value);
      }
      catch (NumberFormatException e)
      {
         throw new FormatConversionException();
      }
   }

   public static boolean toBoolean(String value) throws NullConversionException, FormatConversionException
   {
      if ("true".equals(value))
      {
         return true;
      }
      else if ("false".equals(value))
      {
         return false;
      }
      else if (value == null)
      {
         throw new NullConversionException();
      }
      else
      {
         throw new FormatConversionException();
      }
   }

   public static String toString(int value)
   {
      return Integer.toString(value);
   }

   public static String toString(boolean value)
   {
      return Boolean.toString(value);
   }

   public static String toString(Object value, Converter converter) throws NullConversionException, FormatConversionException, IllegalArgumentException
   {
      if (converter == null)
      {
         throw new IllegalArgumentException();
      }
      return converter.toString(value);
   }

   public static String[] toStringArray(int[] values) throws IllegalArgumentException
   {
      if (values == null)
      {
         throw new IllegalArgumentException();
      }
      String[] strings = new String[values.length];
      for (int i = 0;i < strings.length;i++)
      {
         strings[i] = Integer.toString(values[i]);
      }
      return strings;
   }

   public static String[] toStringArray(boolean[] values) throws IllegalArgumentException
   {
      if (values == null)
      {
         throw new IllegalArgumentException();
      }
      String[] strings = new String[values.length];
      for (int i = 0;i < strings.length;i++)
      {
         strings[i] = Boolean.toString(values[i]);
      }
      return strings;
   }

   public static String[] toStringArray(Object[] values, Converter converter) throws NullConversionException, FormatConversionException, IllegalArgumentException
   {
      if (values == null)
      {
         throw new IllegalArgumentException();
      }
      if (converter == null)
      {
         throw new IllegalArgumentException();
      }
      String[] strings = new String[values.length];
      for (int i = 0;i < strings.length;i++)
      {
         Object value = values[i];
         if (value != null)
         {
            strings[i] = converter.toString(values[i]);
         }
      }
      return strings;
   }
}
