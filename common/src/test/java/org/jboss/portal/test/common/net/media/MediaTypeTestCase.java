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
import org.gatein.common.net.media.MediaType;
import org.gatein.common.net.media.TypeDef;
import org.gatein.common.net.media.SubtypeDef;

/**
 * @author <a href="mailto:julien@jboss-portal.org">Julien Viet</a>
 * @version $Revision: 630 $
 */
public class MediaTypeTestCase extends TestCase
{
   public void testBasic()
   {
      MediaType textHTML = MediaType.create(TypeDef.TEXT, SubtypeDef.HTML);
      assertEquals(TypeDef.TEXT, textHTML.getType());
      assertEquals(SubtypeDef.HTML, textHTML.getSubtype());

      //
      MediaType textHTML2 = MediaType.create("text", "html");
      assertEquals(TypeDef.TEXT, textHTML2.getType());
      assertEquals(SubtypeDef.HTML, textHTML2.getSubtype());

      //
      MediaType textHTML3 = MediaType.create("text/html");
      assertEquals(TypeDef.TEXT, textHTML3.getType());
      assertEquals(SubtypeDef.HTML, textHTML3.getSubtype());

      //
      assertEquals(MediaType.TEXT_HTML, textHTML);
      assertEquals(MediaType.TEXT_HTML, textHTML2);
   }

   public void testIAE()
   {
      try
      {
         MediaType.create(null, SubtypeDef.HTML);
      }
      catch (IllegalArgumentException e)
      {
      }
      try
      {
         MediaType.create(TypeDef.TEXT, null);
      }
      catch (Exception e)
      {
      }
      try
      {
         MediaType.create((TypeDef)null, null);
      }
      catch (Exception e)
      {
      }
      try
      {
         MediaType.create((String)null, null);
      }
      catch (Exception e)
      {
      }
      try
      {
         MediaType.create(null, "html");
      }
      catch (Exception e)
      {
      }
      try
      {
         MediaType.create("test", null);
      }
      catch (Exception e)
      {
      }
      try
      {
         MediaType.create(null);
      }
      catch (Exception e)
      {
      }
      try
      {
         MediaType.create("text");
      }
      catch (Exception e)
      {
      }
      try
      {
         MediaType.create("text/");
      }
      catch (Exception e)
      {
      }
      try
      {
         MediaType.create("/text");
      }
      catch (Exception e)
      {
      }
      try
      {
         MediaType.create("foo/html");
      }
      catch (Exception e)
      {
      }
   }
}
