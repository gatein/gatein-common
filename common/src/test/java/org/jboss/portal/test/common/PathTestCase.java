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
import org.gatein.common.path.RelativePathParser;

/**
 * @author <a href="mailto:julien@jboss.org">Julien Viet</a>
 * @version $Revision: 5451 $
 */
public class PathTestCase extends TestCase
{

   public void testSimpleDown()
   {
      String[] paths = {"abc","abc/","abc/."};
      for (int i = 0; i < paths.length; i++)
      {
         String path = paths[i];
         RelativePathParser cursor = new RelativePathParser(path);
         assertEquals(RelativePathParser.DOWN, cursor.next());
         assertEquals(0, cursor.getOffset());
         assertEquals(3, cursor.getLength());
         assertEquals("abc", path.substring(cursor.getOffset(), cursor.getLength()));
         assertEquals(RelativePathParser.NONE, cursor.next());
         assertEquals(-1, cursor.getOffset());
         assertEquals(-1, cursor.getLength());
      }
   }

   public void testSimpleNone()
   {
      String[] paths = {"",".","./","./."};
      for (int i = 0; i < paths.length; i++)
      {
         String path = paths[i];
         RelativePathParser cursor = new RelativePathParser(path);
         assertEquals(RelativePathParser.NONE, cursor.next());
         assertEquals(-1, cursor.getOffset());
         assertEquals(-1, cursor.getLength());
      }
   }

   public void testSimpleUp()
   {
      String[] paths = {"..","../","../."};
      for (int i = 0; i < paths.length; i++)
      {
         String path = paths[i];
         RelativePathParser cursor = new RelativePathParser(path);
         assertEquals(RelativePathParser.UP, cursor.next());
         assertEquals(-1, cursor.getOffset());
         assertEquals(-1, cursor.getLength());
         assertEquals(RelativePathParser.NONE, cursor.next());
         assertEquals(-1, cursor.getOffset());
         assertEquals(-1, cursor.getLength());
      }
   }
}
