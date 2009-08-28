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
package org.jboss.portal.test.common.text;

import junit.framework.TestCase;
import org.gatein.common.text.CharToByteEncoder;

/**
 * @author <a href="mailto:julien@jboss.org">Julien Viet</a>
 * @version $Revision: 1.1 $
 */
public class CharToByteEncoderTestCase extends TestCase
{

   public void testCorrectness()
   {
      CharToByteEncoder encoder = new CharToByteEncoder.Generic("UTF8");

      //
      for (int i = 0;i <= 0x10FFFD;i++)
      {
         char c = (char)i;
         int type = Character.getType(c);
         byte[] r = encoder.encode(c);
         if (type == Character.SURROGATE || type == Character.PRIVATE_USE)
         {
            if (r.length != 0)
            {
               fail("Char " + i + " has length " + r.length);
            }
         }
         else
         {
            if (r.length < 1)
            {
               fail("Char " + i + " has length " + r.length);
            }
         }
      }
   }
}
