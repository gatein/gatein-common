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
package org.jboss.portal.test.common.util.typedmap;

import org.gatein.common.util.AbstractTypedMap;
import junit.framework.Assert;

/**
 * @author <a href="mailto:julien@jboss.org">Julien Viet</a>
 * @version $Revision: 630 $
 */
public class ValueConverter extends AbstractTypedMap.Converter<String, Integer>
{

   public static final Integer NULL = null;
   public static final int UNCHECKED = 11;
   public static final int IAE = 12;
   public static final int CCE = 13;

   /** . */
   boolean acceptNull;

   public ValueConverter()
   {
      this.acceptNull = false;
   }

   protected Integer getInternal(String external) throws IllegalArgumentException, ClassCastException
   {
      if (external == null)
      {
         if (acceptNull)
         {
            return null;
         }
         else
         {
            throw new NullPointerException();
         }
      }

      //
      Integer i;
      try
      {
         i = new Integer(external);
      }
      catch (NumberFormatException e)
      {
         IllegalArgumentException iae = new IllegalArgumentException();
         iae.initCause(e);
         throw iae;
      }

      //
      if (i == null)
      {
         return null;
      }

      //
      if (UNCHECKED == i)
      {
         throw new CustomRuntimeException();
      }
      if (IAE == i)
      {
         throw new IllegalArgumentException();
      }
      if (CCE == i)
      {
         throw new ClassCastException();
      }

      //
      return i;
   }

   protected String getExternal(Integer internal)
   {
      if (internal == null)
      {
         if (acceptNull)
         {
            return null;
         }
         else
         {
            throw new IllegalStateException();
         }
      }

      //
      if (UNCHECKED == internal)
      {
         throw new CustomRuntimeException();
      }
      if (IAE == internal)
      {
         throw new IllegalArgumentException();
      }
      if (CCE == internal)
      {
         throw new ClassCastException();
      }

      //
      return internal.toString();
   }

   protected boolean equals(Integer left, Integer right)
   {
      Assert.assertNotNull(left);
      Assert.assertNotNull(right);
      return left.intValue() == right.intValue();
   }
}
