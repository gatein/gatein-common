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
import org.jboss.portal.common.net.media.ContentType;
import org.jboss.portal.common.net.media.Parameter;
import org.jboss.portal.common.net.media.MediaType;

/**
 * @author <a href="mailto:julien@jboss-portal.org">Julien Viet</a>
 * @version $Revision: 630 $
 */
public class ContentTypeTestCase extends TestCase
{

   public void testBlah()
   {
      assertEquals("text/html", MediaType.TEXT_HTML);
      assertEquals("text/html;a=b", MediaType.TEXT_HTML, new Parameter("a", "b"));
      assertEquals("text/html;a=b;c=d", MediaType.TEXT_HTML, new Parameter("a", "b"), new Parameter("c", "d"));
   }

   public void testBlih()
   {
      assertCannotBuild("foo/html");
      assertCannotBuild("text/");
      assertCannotBuild("texthtml");
      assertCannotBuild("text/html;");
      assertCannotBuild("text/html;;");
      assertCannotBuild("text/html;ab");
      assertCannotBuild("text/html;ab;");
      assertCannotBuild("text/html;a=b;");
      assertCannotBuild("text/html;a=;");
      assertCannotBuild("text/html;=b;");
      assertCannotBuild("text/html;=;");
   }

   private void assertCannotBuild(String contentTypeValue)
   {
      try
      {
         ContentType.create(contentTypeValue);
         fail("Was able to build content type with value " + contentTypeValue);
      }
      catch (Exception ignore)
      {
      }
   }

   private void assertEquals(String contentTypeValue, MediaType mediaType)
   {
      assertEquals(contentTypeValue, mediaType, new Parameter[0]);
   }

   private void assertEquals(String contentTypeValue, MediaType mediaType, Parameter parameter)
   {
      assertEquals(contentTypeValue, mediaType, new Parameter[]{parameter});
   }

   private void assertEquals(String contentTypeValue, MediaType mediaType, Parameter... parameters)
   {
      ContentType ct = ContentType.create(contentTypeValue);
      assertNotNull(ct);
      assertEquals(ct.getMediaType(), mediaType);
      assertNotNull(ct.getParameters());
      assertEquals(parameters.length, ct.getParameters().size());
      for (int i = 0;i < ct.getParameters().size();i++)
      {
         Parameter parameter = ct.getParameters().get(i);
         assertNotNull(parameter);
         assertEquals(parameters[i].getName(), parameter.getName());
         assertEquals(parameters[i].getValue(), parameter.getValue());
      }
   }

}
