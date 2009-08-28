/******************************************************************************
 * JBoss, a division of Red Hat                                               *
 * Copyright 2008, Red Hat Middleware, LLC, and individual                    *
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
package org.jboss.portal.test.common.io;

import junit.framework.TestCase;
import org.gatein.common.io.IOTools;

import java.io.Serializable;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author <a href="mailto:julien@jboss.org">Julien Viet</a>
 * @version $Revision: 630 $
 */
public class SerializableCloneTestCase extends TestCase
{

   public static class Foo implements Serializable
   {
   }

   public void testA() throws IOException, ClassNotFoundException
   {
      Foo foo = new Foo();
      ClassLoader fooCL = new FooClassLoader();
      Class fooClass = fooCL.loadClass(Foo.class.getName());
      assertNotSame(fooClass, Foo.class);

      //
      Object clone = IOTools.clone(foo, fooCL);
      assertSame(fooClass, clone.getClass());
   }

   private static class FooClassLoader extends ClassLoader
   {

      /** . */
      private final byte[] bytes;

      /** . */
      private Class fooClass;

      private FooClassLoader() throws IOException
      {
         InputStream in = Foo.class.getClassLoader().getResourceAsStream(Foo.class.getName().replace('.', '/') + ".class");
         bytes = IOTools.getBytes(in);
      }

      public Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException
      {
         if (Foo.class.getName().equals(name))
         {
            Class<?> fooClass = findClass(name);

            //
            if (resolve)
            {
                resolveClass(fooClass);
            }

            //
            return fooClass;
         }
         else
         {
            return super.loadClass(name, resolve);
         }
      }

      protected Class<?> findClass(String name) throws ClassNotFoundException
      {
         if (Foo.class.getName().equals(name))
         {
            if (fooClass == null)
            {
               fooClass = defineClass(name, bytes, 0, bytes.length);
            }

            //
            return fooClass;
         }
         else
         {
            return super.findClass(name);
         }
      }
   }

}
