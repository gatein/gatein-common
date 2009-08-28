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

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.AbstractList;
import java.util.RandomAccess;

/**
 * A read only interface used to retrieve data. It represents both for
 * mono value and multi values. It must implement equals and hashCode.
 * <p/>
 * The semantics :
 * <p/>
 * <table border="1">
 * <tr>
 * <th>storage</th><th>mono</th><th>multi</th>
 * </tr>
 * <tr>
 * <td>xxx</td><td>xxx</td><td>[xxx]</td>
 * </tr>
 * <tr>
 * <td>null</td><td>null</td><td>[null]</td>
 * </tr>
 * <tr>
 * <td colspan="3"></td>
 * </tr>
 * <tr>
 * <td>[]</td><td>null</td><td>[]</td>
 * </tr>
 * <tr>
 * <td>[null,..]</td><td>null</td><td>[null,..]</td>
 * </tr>
 * <tr>
 * <td>["1",..]</td><td>"1"</td><td>["1",..]</td>
 * </tr>
 * </table>
 *
 * @author <a href="mailto:julien@jboss.org">Julien Viet</a>
 * @version $Revision: 5451 $
 */
@Deprecated
public abstract class Value implements Serializable, Cloneable
{

   /**
    * The cached hashCode.
    */
   private transient int hashCode = 0;

   /**
    * The object list representation.
    */
   private transient List objectList;

   /**
    * The string list representation.
    */
   private transient List stringList;

   // ******************************

   /**
    *
    */
   public abstract boolean isInstanceOf(Class clazz);

   public final int hashCode()
   {
      if (isNull())
      {
         return 0;
      }
      else
      {
         if (hashCode == 0)
         {
            Object[] objects = getObjectArray();
            for (int i = 0; i < objects.length; i++)
            {
               Object o = objects[i];
               if (o != null)
               {
                  hashCode *= 43 + o.hashCode();
               }
            }
         }
         else
         {
            return hashCode;
         }
      }
      return hashCode;
   }

   public final boolean equals(Object obj)
   {
      if (obj == this)
      {
         return true;
      }
      if (obj.getClass().equals(getClass()))
      {
         Value other = (Value)obj;
         switch ((other.isNull() ? 2 : 0) + (isNull() ? 1 : 0))
         {
            case 1:
            case 2:
               return false;
            case 3:
               return true;
         }
         return Arrays.equals(getObjectArray(), other.getObjectArray());
      }
      return false;
   }

   public final String toString()
   {
      StringBuffer buffer = new StringBuffer("[");
      String[] strings = asStringArray();
      for (int i = 0; i < strings.length; i++)
      {
         String s = strings[i];
         buffer.append(i > 0 ? "," : "").
            append(String.valueOf(s));
      }
      buffer.append("]");
      return buffer.toString();
   }

   /**
    * Clone this object.
    *
    * @return a clone of this object
    */
   public final Value clone()
   {
      try
      {
         return (Value)super.clone();
      }
      catch (CloneNotSupportedException e)
      {
         throw new Error(e);
      }
   }

   // ***********************
   // * Mono valued methods *
   // ***********************

   /**
    * Return true if the value is null.
    */
   public final boolean isNull()
   {
      return getObjectArray().length == 0 || getObjectArray()[0] == null;
   }

   /**
    * Return the value as a string or null if the value is null.
    */
   public final String asString()
   {
      String string = null;
      Object object = asObject();
      if (object != null)
      {
         string = object.toString();
      }
      return string;
   }

   /**
    * Return the value.
    */
   public final Object asObject()
   {
      if (isNull())
      {
         return null;
      }
      return getObjectArray()[0];
   }

   // ************************
   // * Multi valued methods *
   // ************************

   /**
    * Return true if it contains more than one value.
    */
   public final boolean isMultiValued()
   {
      return size() > 1;
   }

   /**
    * Return true if it contains zero value.
    */
   public final boolean isEmpty()
   {
      return size() == 0;
   }

   /**
    * Return the size.
    */
   public final int size()
   {
      return getObjectArray().length;
   }

   /**
    * Creates a new array and fill it with the string values.
    */
   public final String[] asStringArray()
   {
      Object[] objects = getObjectArray();
      String[] strings = new String[objects.length];
      for (int i = 0; i < objects.length; i++)
      {
         Object object = objects[i];
         if (object != null)
         {
            strings[i] = object.toString();
         }
      }
      return strings;
   }

   /**
    * Create a new array and fill it with the values.
    */
   public final Object[] asObjectArray()
   {
      return (Object[])getObjectArray().clone();
   }

   /**
    * Return an immutable list of strings.
    */
   public final List asStringList()
   {
      if (stringList == null)
      {
         stringList = new ValueList(asStringArray());
      }
      return stringList;
   }

   /**
    * Return an immutable list of objects.
    */
   public final List asObjectList()
   {
      if (objectList == null)
      {
         // Avoid to make a copy as the list is not mutable
         objectList = new ValueList(getObjectArray());
      }
      return objectList;
   }

   /**
    * Return the values as an array of converted object. It must always return a non null array.
    */
   protected abstract Object[] getObjectArray();

   private static class ValueList extends AbstractList implements List, RandomAccess
   {

      /** . */
      private final Object[] array;

      private ValueList(Object[] array)
      {
         this.array = array;
      }

      public int size()
      {
         return array.length;
      }

      public Object get(int index)
      {
         return array[index];
      }
   }

}
