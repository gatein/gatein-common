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
package org.gatein.common.io;

import java.io.OutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.GZIPOutputStream;
import java.util.zip.GZIPInputStream;

/**
 * Filters a stream for serialize/unserialize operations.
 *
 * @author <a href="mailto:julien@jboss.org">Julien Viet</a>
 * @version $Revision: 630 $
 */
public interface SerializationFilter
{

   SerializationFilter TRIVIAL = new SerializationFilter()
   {
      public <T> void serialize(Serialization<T> serialization, T t, OutputStream out) throws IllegalArgumentException, IOException
      {
         serialization.serialize(t, out);
      }

      public <T> T unserialize(Serialization<T> serialization, InputStream in) throws IllegalArgumentException, IOException
      {
         return serialization.unserialize(in);
      }
   };

   /**
    * Use GZIP streams. 
    */
   SerializationFilter COMPRESSOR = new SerializationFilter()
   {
      public <T> void serialize(Serialization<T> serialization, T t, OutputStream out) throws IllegalArgumentException, IOException
      {
         GZIPOutputStream zos = new GZIPOutputStream(out);
         serialization.serialize(t, zos);
         zos.finish();
      }

      public <T> T unserialize(Serialization<T> serialization, InputStream in) throws IllegalArgumentException, IOException
      {
         GZIPInputStream zis = new GZIPInputStream(in);
         return serialization.unserialize(zis);
      }
   };

   <T> void serialize(Serialization<T> serialization, T t, OutputStream out) throws IllegalArgumentException, IOException;

   <T> T unserialize(Serialization<T> serialization, InputStream in) throws IllegalArgumentException, IOException;

}
