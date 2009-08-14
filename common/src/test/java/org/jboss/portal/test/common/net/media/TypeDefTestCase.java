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
package org.jboss.portal.test.common.net.media;

import junit.framework.TestCase;
import org.jboss.portal.common.net.media.TypeDef;

/**
 * @author <a href="mailto:julien@jboss-portal.org">Julien Viet</a>
 * @version $Revision: 630 $
 */
public class TypeDefTestCase extends TestCase
{

   public void testIAE()
   {
      try
      {
         TypeDef.create(null);
      }
      catch (IllegalArgumentException ignore)
      {
      }
   }

   public void testNotRecognized()
   {
      assertNull(TypeDef.create(""));
      assertNull(TypeDef.create("foo"));
   }

   public void testRecognized()
   {
      assertEquals(TypeDef.TEXT, TypeDef.create("text"));
      assertEquals(TypeDef.AUDIO, TypeDef.create("audio"));
      assertEquals(TypeDef.VIDEO, TypeDef.create("video"));
      assertEquals(TypeDef.IMAGE, TypeDef.create("image"));
      assertEquals(TypeDef.APPLICATION, TypeDef.create("application"));
      assertEquals(TypeDef.MESSAGE, TypeDef.create("message"));
      assertEquals(TypeDef.MULTIPART, TypeDef.create("multipart"));

      //
      assertEquals(TypeDef.TEXT, TypeDef.create("TEXT"));
      assertEquals(TypeDef.AUDIO, TypeDef.create("AUDIO"));
      assertEquals(TypeDef.VIDEO, TypeDef.create("VIDEO"));
      assertEquals(TypeDef.IMAGE, TypeDef.create("IMAGE"));
      assertEquals(TypeDef.APPLICATION, TypeDef.create("APPLICATION"));
      assertEquals(TypeDef.MESSAGE, TypeDef.create("MESSAGE"));
      assertEquals(TypeDef.MULTIPART, TypeDef.create("MULTIPART"));

      //
      assertEquals(TypeDef.TEXT, TypeDef.create("tExt"));
      assertEquals(TypeDef.AUDIO, TypeDef.create("aUdio"));
      assertEquals(TypeDef.VIDEO, TypeDef.create("vIdeo"));
      assertEquals(TypeDef.IMAGE, TypeDef.create("iMage"));
      assertEquals(TypeDef.APPLICATION, TypeDef.create("aPplication"));
      assertEquals(TypeDef.MESSAGE, TypeDef.create("mEssage"));
      assertEquals(TypeDef.MULTIPART, TypeDef.create("mUltipart"));
   }
}
