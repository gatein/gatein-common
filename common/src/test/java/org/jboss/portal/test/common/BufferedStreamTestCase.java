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
import org.gatein.common.io.IOTools;

import java.io.InputStream;
import java.io.ByteArrayInputStream;
import java.io.BufferedInputStream;
import java.io.OutputStream;
import java.io.ByteArrayOutputStream;
import java.io.BufferedOutputStream;
import java.util.Arrays;

/**
 * @author <a href="mailto:julien@jboss.org">Julien Viet</a>
 * @version $Revision: 1.1 $
 */
public class BufferedStreamTestCase extends TestCase
{

   public BufferedStreamTestCase(String name)
   {
      super(name);
   }

   public void testInputStream() throws Exception
   {
      assertNull(IOTools.safeBufferedWrapper((InputStream)null));
      BufferedInputStream in = IOTools.safeBufferedWrapper(new ByteArrayInputStream("abc".getBytes("UTF8")));
      assertEquals(System.identityHashCode(in), System.identityHashCode(IOTools.safeBufferedWrapper(in)));
      assertNotNull(in);
      byte[] bytes = "abc".getBytes("UTF8");
      Arrays.fill(bytes, (byte)0);
      assertEquals(bytes.length, in.read(bytes));
      assertEquals(-1, in.read());
      assertEquals("abc", new String(bytes, "UTF8"));
   }

   public void testOutputputStream() throws Exception
   {
      assertNull(IOTools.safeBufferedWrapper((OutputStream)null));
      ByteArrayOutputStream out = new ByteArrayOutputStream();
      BufferedOutputStream buffered = IOTools.safeBufferedWrapper(out);
      assertNotNull(buffered);
      assertEquals(System.identityHashCode(buffered), System.identityHashCode(IOTools.safeBufferedWrapper(buffered)));
      buffered.write("abc".getBytes("UTF8"));
      buffered.close();
      assertEquals("abc", out.toString("UTF8"));
   }
}
