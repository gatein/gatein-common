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
package org.gatein.common.ant;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarOutputStream;

/**
 * @author <a href="mailto:julien@jboss.org">Julien Viet</a>
 * @version $Revision: 7228 $
 */
public class Implode extends Task
{

   /** Unzipped extensions. */
   private static final Set extensions = new HashSet(Arrays.asList(new String[]{"ear", "war", "sar", "har"}));

   /** The exploded dir. */
   private File dir;

   /** The target file. */
   private File tofile;

   public void setDir(File dir)
   {
      this.dir = dir;
   }

   public void setTofile(File tofile)
   {
      this.tofile = tofile;
   }

   public void execute() throws BuildException
   {
      if (tofile == null)
      {
         throw new BuildException("target file should not be null");
      }
      if (dir == null)
      {
         throw new BuildException("source dir should not be null");
      }
      if (!dir.exists())
      {
         throw new BuildException("source dir does not exist");
      }
      if (dir.isFile())
      {
         throw new BuildException("source dir is a file");
      }
      if (tofile.exists() && tofile.isDirectory())
      {
         throw new BuildException("target file " + tofile + " designates a directory");
      }
      //if (tofile == null || tofile.lastModified() < dir.lastModified())


      OutputStream out = null;
      try
      {
         byte[] bytes = implode(dir);
         out = new BufferedOutputStream(new FileOutputStream(tofile));
         out.write(bytes);
      }
      catch (IOException e)
      {
         e.printStackTrace();
         throw new BuildException(e.getMessage());
      }
      finally
      {
         if (out != null)
         {
            try
            {
               out.close();
            }
            catch (IOException ignore)
            {
            }
         }
      }

   }

   public byte[] implode(File f) throws IOException
   {
      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      JarOutputStream out = new JarOutputStream(baos);
      implode(f, "", out);
      out.close();
      return baos.toByteArray();
   }

   public void implode(File f, String path, JarOutputStream out) throws IOException
   {
      if (f.isFile())
      {
         InputStream in = null;
         try
         {
            in = new BufferedInputStream(new FileInputStream(f));
            byte[] bytes = new byte[1024];

            //
            String fileName = path.substring(1);
            JarEntry fileEntry = new JarEntry(fileName);
            out.putNextEntry(fileEntry);

            //
            for (int l = in.read(bytes, 0, bytes.length); l > -0; l = in.read(bytes, 0, bytes.length))
            {
               out.write(bytes, 0, l);
            }

            //
            out.closeEntry();
         }
         finally
         {
            if (in != null)
            {
               try
               {
                  in.close();
               }
               catch (IOException ignore)
               {
               }
            }
         }
      }
      else
      {
         if (path.length() > 1)
         {
            String dirName = path.substring(1) + '/';
            JarEntry dirEntry = new JarEntry(dirName);
            out.putNextEntry(dirEntry);
            out.closeEntry();
         }

         //
         File[] children = f.listFiles();
         for (int i = 0; i < children.length; i++)
         {
            File child = children[i];
            int lastDot = child.getName().lastIndexOf(".");
            if (extensions.contains(child.getName().substring(lastDot + 1)))
            {
               byte[] bytes = implode(child);

               //
               String fileName = (path + '/' + child.getName()).substring(1);
               JarEntry fileEntry = new JarEntry(fileName);
               out.putNextEntry(fileEntry);

               //
               out.write(bytes, 0, bytes.length);

               //
               out.closeEntry();
            }
            else
            {
               implode(child, path + '/' + child.getName(), out);
            }
         }
      }
   }
}
