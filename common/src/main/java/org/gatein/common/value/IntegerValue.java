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


/**
 * @author <a href="mailto:julien@jboss.org">Julien Viet</a>
 * @version $Revision: 5451 $
 */
@Deprecated
public class IntegerValue extends Value
{

   /** The serialVersionUID */
   private static final long serialVersionUID = -3396982952223096067L;
   
   /** The Integer array. */
   private Integer[] values;

   public IntegerValue()
   {
      this((Integer)null);
   }

   public IntegerValue(Integer integer)
   {
      this(new Integer[]{integer});
   }

   public IntegerValue(int _value)
   {
      this(new int[]{_value});
   }

   public IntegerValue(String value)
   {
      this(new String[]{value});
   }

   public IntegerValue(Integer[] values)
   {
      if (values == null)
      {
         throw new IllegalArgumentException();
      }
      this.values = values;
   }

   public IntegerValue(int[] _values)
   {
      if (_values == null)
      {
         throw new IllegalArgumentException();
      }
      values = new Integer[_values.length];
      for (int i = 0;i < _values.length;i++)
      {
         values[i] = new Integer(_values[i]);
      }
   }

   public IntegerValue(String[] values) throws IllegalArgumentException
   {
      if (values == null)
      {
         throw new IllegalArgumentException();
      }
      try
      {
         this.values = new Integer[values.length];
         for (int i = 0;i < values.length;i++)
         {
            String value = values[i];
            if (value != null)
            {
               this.values[i] = new Integer(value);
            }
         }
      }
      catch (NumberFormatException e)
      {
         throw new IllegalArgumentException();
      }
   }

   public boolean isInstanceOf(Class clazz)
   {
      return clazz != null && clazz.isAssignableFrom(Integer.class);
   }

   protected Object[] getObjectArray()
   {
      return values;
   }
}
