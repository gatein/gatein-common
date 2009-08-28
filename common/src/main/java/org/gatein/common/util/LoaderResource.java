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
package org.gatein.common.util;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;
import org.apache.log4j.Logger;
import org.gatein.common.io.IOTools;
import org.gatein.common.xml.XMLTools;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Represent a resource that may or not exist.
 *
 * @author <a href="mailto:julien@jboss.org">Julien Viet</a>
 * @version $Revision: 7380 $
 */
public abstract class LoaderResource
{

   protected final String location;

   private final Logger log = Logger.getLogger(getClass());

   public LoaderResource(String location)
   {
      if (location == null)
      {
         throw new IllegalArgumentException();
      }
      this.location = location;
   }

   public abstract boolean exists();

   public abstract InputStream asInputStream() throws IllegalStateException;

   public String getLocation()
   {
      return location;
   }

   public Document asDocument(DocumentBuilder builder) throws IllegalStateException
   {
      if (!exists())
      {
         throw new IllegalStateException("Resource " + location + " does not exist");
      }
      InputStream in = null;
      try
      {
         in = IOTools.safeBufferedWrapper(asInputStream());
         return builder.parse(in);
      }
      catch (SAXException e)
      {
         IllegalStateException ex = new IllegalStateException("Cannot parse stream " + location);
         ex.initCause(e);
         throw ex;
      }
      catch (IOException e)
      {
         IllegalStateException ex = new IllegalStateException("Cannot access stream " + location);
         ex.initCause(e);
         throw ex;
      }
      finally
      {
         IOTools.safeClose(in);
      }
   }

   public Properties asProperties() throws IllegalStateException
   {
      return asProperties(false);
   }

   public Properties asProperties(boolean xml) throws IllegalStateException
   {
      if (!exists())
      {
         throw new IllegalStateException("Resource " + location + " does not exist");
      }
      InputStream in = null;

      log.debug("Loading resource: " + location);

      try
      {
         in = IOTools.safeBufferedWrapper(asInputStream());
         Properties props;
         if (xml)
         {
            DocumentBuilder builder = XMLTools.getDocumentBuilderFactory().newDocumentBuilder();
            Document doc = asDocument(builder);
            props = XMLTools.loadXMLProperties(doc);
         }
         else
         {
            props = new Properties();
            props.load(in);
         }
         log.debug("Finished loading resource: " + location);
         return props;
      }
      catch (ParserConfigurationException e)
      {
         IllegalStateException ex = new IllegalStateException("Cannot parse xml stream " + location);
         ex.initCause(e);
         throw ex;
      }
      catch (IOException e)
      {
         IllegalStateException ex = new IllegalStateException("Cannot access stream " + location);
         ex.initCause(e);
         throw ex;
      }
      finally
      {
         IOTools.safeClose(in);
      }
   }

   public String asString() throws IllegalStateException
   {
      return asString(null);
   }

   public String asString(String enc) throws IllegalStateException
   {
      if (!exists())
      {
         throw new IllegalStateException("Resource " + location + " does not exist");
      }
      InputStream in = null;

      log.debug("Loading resource: " + location);

      try
      {
         in = IOTools.safeBufferedWrapper(asInputStream());
         ByteArrayOutputStream out = new ByteArrayOutputStream(in.available());
         IOTools.copy(in, out);
         String result;
         if (enc == null)
         {
            result = out.toString();
         }
         else
         {
            result = out.toString(enc);
         }

         log.debug("Finished loading resource: " + location);
         return result;
      }
      catch (IOException e)
      {
         IllegalStateException ex = new IllegalStateException("Cannot access stream " + location);
         ex.initCause(e);
         throw ex;
      }
      finally
      {
         IOTools.safeClose(in);
      }
   }
}
