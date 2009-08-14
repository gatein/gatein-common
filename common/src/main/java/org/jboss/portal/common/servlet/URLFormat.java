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
package org.jboss.portal.common.servlet;

/**
 * Defines how a URL should be formatted when rendered.
 *
 * @author <a href="mailto:julien@jboss-portal.org">Julien Viet</a>
 * @version $Revision: 630 $
 */
public class URLFormat
{

   /**
    * Factory creation of URLFormat objects.
    *
    * @param secure if the url should be secure
    * @param authenticated if the url should force the user to authenticate
    * @param relative if the url is relative or absolute
    * @param escapeXML if the url should have XML escaped
    * @param servletEncoded if the url should be encoded with the servlet response (session propagation)
    * @return the rendered URL
    */
   public static URLFormat create(
      Boolean secure,
      Boolean authenticated,
      Boolean relative,
      Boolean escapeXML,
      Boolean servletEncoded)
   {
      return new URLFormat(secure, authenticated, relative, escapeXML, servletEncoded);
   }

   /** . */
   private final Boolean secure;

   /** . */
   private final Boolean authenticated;

   /** . */
   private final Boolean relative;

   /** . */
   private final Boolean escapeXML;

   /** . */
   private final Boolean servletEncoded;

   private URLFormat(
      Boolean secure,
      Boolean authenticated,
      Boolean relative,
      Boolean escapeXML,
      Boolean servletEncoded)
   {
      this.secure = secure;
      this.authenticated = authenticated;
      this.relative = relative;
      this.escapeXML = escapeXML;
      this.servletEncoded = servletEncoded;
   }

   public Boolean getSecure()
   {
      return secure;
   }

   public Boolean getAuthenticated()
   {
      return authenticated;
   }

   public Boolean getRelative()
   {
      return relative;
   }

   public Boolean getEscapeXML()
   {
      return escapeXML;
   }

   public Boolean getServletEncoded()
   {
      return servletEncoded;
   }
}
