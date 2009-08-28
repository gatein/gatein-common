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
package org.jboss.portal.test.common;

import junit.framework.TestCase;

import java.util.Calendar;

import org.gatein.common.util.Tools;

/**
 * @author <a href="mailto:julien@jboss.org">Julien Viet</a>
 * @version $Revision: 5451 $
 */
public class TemporaryHashTestCase extends TestCase
{

   public TemporaryHashTestCase(String key)
   {
      super(key);
   }

   public void testBasic()
   {
      Calendar calendar = Calendar.getInstance();
      calendar.set(2004, 1, 1, 1, 30, 0);
      String hash = Tools.generateTemporaryHash("blah", calendar.getTimeInMillis());

      calendar.set(2004, 1, 1, 1, 0, 0);
      assertTrue(Tools.confirmTemporaryHash(hash, "blah", calendar.getTimeInMillis()));

      calendar.set(2004, 1, 1, 0, 59, 59);
      assertFalse(Tools.confirmTemporaryHash(hash, "blah", calendar.getTimeInMillis()));

      calendar.set(2004, 1, 1, 2, 0, 0);
      assertTrue(Tools.confirmTemporaryHash(hash, "blah", calendar.getTimeInMillis()));

      calendar.set(2004, 1, 1, 2, 59, 59);
      assertTrue(Tools.confirmTemporaryHash(hash, "blah", calendar.getTimeInMillis()));

      calendar.set(2004, 1, 1, 3, 0, 0);
      assertFalse(Tools.confirmTemporaryHash(hash, "blah", calendar.getTimeInMillis()));
   }
}
