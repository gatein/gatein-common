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
import junit.framework.AssertionFailedError;

/**
 * @author <a href="mailto:julien@jboss.org">Julien Viet</a>
 * @version $Revision: 630 $
 */
public class KeyConverter extends AbstractTypedMap.Converter<String, Long>
{

   public static final String NULL = "null";
   public static final String UNCHECKED = "unchecked";
   public static final String IAE = "iae";
   public static final String CCE = "cce";

   protected Long getInternal(String external) throws IllegalArgumentException, ClassCastException
   {
      Assert.assertNotNull(external);

      //
      if (NULL.equals(external))
      {
         return null;
      }
      if (UNCHECKED.equals(external))
      {
         throw new CustomRuntimeException();
      }
      if (IAE.equals(external))
      {
         throw new IllegalArgumentException();
      }
      if (CCE.equals(external))
      {
         throw new ClassCastException();
      }

      //
      if ("zero".equals(external))
      {
         return (long)0;
      }
      else if ("one".equals(external))
      {
         return (long)1;
      }
      else if ("two".equals(external))
      {
         return (long)2;
      }
      else if ("three".equals(external))
      {
         return (long)3;
      }

      //
      throw new AssertionFailedError();
   }

   protected String getExternal(Long internal)
   {
      Assert.assertNotNull(internal);

      //
      if (NULL.equals(internal))
      {
         return null;
      }
      if (UNCHECKED.equals(internal))
      {
         throw new RuntimeException();
      }

      //
      switch (internal.intValue())
      {
         case 0:
            return "zero";
         case 1:
            return "one";
         case 2:
            return "two";
         case 3:
            return "three";
      }

      //
      throw new AssertionFailedError();
   }

   protected boolean equals(Long left, Long right)
   {
      Assert.assertNotNull(left);
      Assert.assertNotNull(right);
      return left.equals(right);
   }
}
