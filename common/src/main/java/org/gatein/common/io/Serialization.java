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

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.DataInputStream;
import java.io.OutputStream;
import java.io.InputStream;
import java.util.Map;
import java.util.HashMap;

/**
 * @author <a href="mailto:julien@jboss.org">Julien Viet</a>
 * @version $Revision: 630 $
 */
public interface Serialization<T>
{

   Serialization<Map<String, String[]>> PARAMETER_MAP = new Serialization<Map<String, String[]>>()
   {
      public void serialize(Map<String, String[]> map, OutputStream out) throws IOException, IllegalArgumentException
      {
         if (map == null)
         {
            throw new IllegalArgumentException("No null map allowed");
         }

         //
         DataOutputStream data = out instanceof DataOutputStream ? (DataOutputStream)out : new DataOutputStream(out);

         //
         data.writeInt(map.size());

         //
         for (Map.Entry entry : map.entrySet())
         {
            String name = (String)entry.getKey();
            data.writeUTF(name);

            //
            String[] values = (String[])entry.getValue();
            if (values == null)
            {
               throw new IllegalArgumentException("No null values are allowed in the map");
            }

            //
            int length = values.length;
            data.writeInt(length);

            //
            for (String value : values)
            {
               if (value == null)
               {
                  throw new IllegalArgumentException("No null value in the String[] are allowed in the map");
               }
               data.writeUTF(value);
            }
         }

         //
         data.flush();
      }

      public Map<String, String[]> unserialize(InputStream in) throws IOException
      {
         if (in == null)
         {
            throw new IllegalArgumentException("No null input stream");
         }

         //
         DataInputStream data = in instanceof DataInputStream ? (DataInputStream)in : new DataInputStream(in);

         //
         int size = data.readInt();
         Map<String, String[]> tmp = new HashMap<String, String[]>(size);
         while (size-- > 0)
         {
            String name = data.readUTF();
            int length = data.readInt();
            String[] values = new String[length];
            for (int i = 0;i < length;i++)
            {
               values[i] = data.readUTF();
            }
            tmp.put(name, values);
         }

         //
         return tmp;
      }
   };

   public abstract void serialize(T t, OutputStream out) throws IOException, IllegalArgumentException;

   public abstract T unserialize(InputStream in) throws IOException, IllegalArgumentException;

}
