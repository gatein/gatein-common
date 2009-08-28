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

import java.util.Iterator;
import java.util.Arrays;
import java.util.jar.JarOutputStream;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;
import java.io.IOException;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.io.OutputStream;

import org.gatein.common.jar.JarEntryInfo;
import org.gatein.common.jar.JarInfo;
import org.gatein.common.io.IOTools;

/**
 * @author <a href="mailto:julien@jboss.org">Julien Viet</a>
 * @version $Revision: 7425 $
 */
public class JarTestCase extends TestCase
{

   public JarTestCase(String name)
   {
      super(name);
   }

   public void testJarEntryInfo()
   {
      JarEntryInfo info1 = new JarEntryInfo(new JarEntry("a"));
      assertEquals(Arrays.asList(new String[]{"/", "a"}), info1.getNames());

      JarEntryInfo info2 = new JarEntryInfo(new JarEntry("a/"));
      assertEquals(Arrays.asList(new String[]{"/", "a"}), info2.getNames());

      JarEntryInfo info3 = new JarEntryInfo(new JarEntry("/"));
      assertEquals(Arrays.asList(new String[]{"/"}), info3.getNames());

      JarEntryInfo info4 = new JarEntryInfo(new JarEntry("a/b"));
      assertEquals(Arrays.asList(new String[]{"/", "a","b"}), info4.getNames());

      JarEntryInfo info5 = new JarEntryInfo(new JarEntry("a/b/"));
      assertEquals(Arrays.asList(new String[]{"/", "a","b"}), info5.getNames());

      JarEntryInfo info6 = new JarEntryInfo(new JarEntry("/a"));
      assertEquals(Arrays.asList(new String[]{"/", "a"}), info6.getNames());

      JarEntryInfo info7 = new JarEntryInfo(new JarEntry("/a/"));
      assertEquals(Arrays.asList(new String[]{"/", "a"}), info7.getNames());

      JarEntryInfo info8 = new JarEntryInfo(new JarEntry("/a/b"));
      assertEquals(Arrays.asList(new String[]{"/", "a","b"}), info8.getNames());

      JarEntryInfo info9 = new JarEntryInfo(new JarEntry("/a/b/"));
      assertEquals(Arrays.asList(new String[]{"/", "a","b"}), info9.getNames());
   }

   public void testEntryComparator() throws IOException
   {
      File jarFile = File.createTempFile("test", ".jar");

      OutputStream out = IOTools.safeBufferedWrapper(new FileOutputStream(jarFile));
      JarOutputStream jarOut = new JarOutputStream(out);
      jarOut.putNextEntry(new JarEntry("a/c"));
      jarOut.closeEntry();
      jarOut.putNextEntry(new JarEntry("c/d"));
      jarOut.closeEntry();
      jarOut.putNextEntry(new JarEntry("a"));
      jarOut.closeEntry();
      jarOut.putNextEntry(new JarEntry("b"));
      jarOut.closeEntry();
      jarOut.putNextEntry(new JarEntry("a/b"));
      jarOut.closeEntry();
      jarOut.close();

      jarFile.deleteOnExit();

      JarInputStream jarIn = new JarInputStream(IOTools.safeBufferedWrapper(new FileInputStream(jarFile)));
      Iterator i = new JarInfo(jarIn).entries();
      while (i.hasNext())
      {
         Object o = i.next();
         System.out.println("o = " + o);
      }
      jarIn.close();
   }

   public void testIsChildOf()
   {
      assertFalse(new JarEntryInfo(new JarEntry("a")).isChildOf(new JarEntryInfo(new JarEntry(""))));
      assertFalse(new JarEntryInfo(new JarEntry("/a")).isChildOf(new JarEntryInfo(new JarEntry(""))));
      assertTrue(new JarEntryInfo(new JarEntry("a")).isChildOf(new JarEntryInfo(new JarEntry("/"))));
      assertTrue(new JarEntryInfo(new JarEntry("/a")).isChildOf(new JarEntryInfo(new JarEntry("/"))));
      assertFalse(new JarEntryInfo(new JarEntry("a/b")).isChildOf(new JarEntryInfo(new JarEntry("a"))));
      assertFalse(new JarEntryInfo(new JarEntry("/a/b")).isChildOf(new JarEntryInfo(new JarEntry("a"))));
      assertTrue(new JarEntryInfo(new JarEntry("a/b")).isChildOf(new JarEntryInfo(new JarEntry("a/"))));
      assertTrue(new JarEntryInfo(new JarEntry("/a/b")).isChildOf(new JarEntryInfo(new JarEntry("a/"))));
   }

}
