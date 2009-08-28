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
import org.gatein.common.ant.Implode;

import java.io.File;
import java.net.URL;

/**
 * @author <a href="mailto:julien@jboss.org">Julien Viet</a>
 * @version $Revision: 6289 $
 */
public class ImplodeTestCase extends TestCase
{

   public ImplodeTestCase(String name)
   {
      super(name);
   }

   public void testA() throws Exception
   {
      URL foo_sarURL = Thread.currentThread().getContextClassLoader().getResource("implode/foo.sar");
      assertNotNull(foo_sarURL);
      File foo_sarDir = new File(foo_sarURL.getFile());
      assertTrue(foo_sarDir.exists());
      assertTrue(foo_sarDir.isDirectory());

      File tmp = File.createTempFile("foo", ".sar");
      tmp.deleteOnExit();
      tmp.setLastModified(foo_sarDir.lastModified() - 1);
      assertTrue(foo_sarDir.lastModified() > tmp.lastModified());
      Implode implode = new Implode();
      implode.setDir(foo_sarDir);
      implode.setTofile(tmp);
      implode.execute();

      // Test that the structure is correct
   }

}
