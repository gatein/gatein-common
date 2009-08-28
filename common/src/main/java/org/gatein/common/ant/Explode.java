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
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipInputStream;

/**
 * Ant task that explode an archive.
 *
 * @author <a href="mailto:julien@jboss.org">Julien Viet</a>
 * @version $Revision: 7228 $
 */
public class Explode extends Task
{

   /** Unzipped extensions. */
   private static final Set extensions = new HashSet(Arrays.asList(new String[]{"ear", "war", "sar", "har"}));

   /** The exploded file. */
   private File file;

   /** The target directory. */
   private File todir;

   /** The target optional name. */
   private String name;

   /** filename to exclude from decompression * */
   private String exclude;

   public void setExclude(String exclude)
   {
      this.exclude = exclude;
   }

   public void setFile(File file)
   {
      this.file = file;
   }

   public void setTodir(File todir)
   {
      this.todir = todir;
   }

   public void setName(String name)
   {
      this.name = name;
   }

   public void execute() throws BuildException
   {
      try
      {
         explode(file, todir);
      }
      catch (DirException e)
      {
         throw new BuildException(e.getMessage());
      }
   }

   public void explode(File file, File todir) throws BuildException, DirException
   {
      if (!file.exists())
      {
         throw new BuildException("source file does not exists");
      }
      if (!file.isFile())
      {
         throw new BuildException("source file is not file");
      }
      if (name == null)
      {
         name = file.getName();
      }
      ZipInputStream zip = null;
      try
      {
         zip = new ZipInputStream(new BufferedInputStream(new FileInputStream(file)));
         log("Process archive " + name);
         explode(this, name, zip, todir, this.exclude);
      }
      catch (FileNotFoundException e)
      {
         throw new BuildException("Unexpected error " + e.getMessage());
      }
      finally
      {
         if (zip != null)
         {
            try
            {
               zip.close();
            }
            catch (IOException ignored)
            {
            }
         }
      }
   }

   /**
    * Explode a zip stream into a directory.
    *
    * @param explode used to log
    * @param name    the name of the created directory
    * @param zip     the zip stream will not be closed
    * @param todir   the parent directory
    * @throws BuildException
    * @throws DirException
    */
   public static void explode(Explode explode, String name, ZipInputStream zip, File todir, String exclude) throws BuildException, DirException
   {
      // First ensure the target directory exists
      if (!todir.exists())
      {
         throw new BuildException("target dir does not exists");
      }
      if (!todir.isDirectory())
      {
         throw new BuildException("target dir is not a directory");
      }
      try
      {
         // Buffer
         byte[] buffer = new byte[512];

         // The real target dir
         todir = new File(todir, name);

         // Get the directory
         ensureDirExist(explode, todir);

         // Process each file
         for (ZipEntry entry = zip.getNextEntry(); entry != null; entry = zip.getNextEntry())
         {
            // Next entry
            File fic = new File(todir, entry.getName());
            int lastDot = fic.getName().lastIndexOf(".");

            if (entry.isDirectory())
            {
               // This is a directory that we must create
               try
               {
                  ensureDirExist(explode, fic);
               }
               catch (DirException e)
               {
                  explode.log(e.getMessage());
               }
            }
            else if (lastDot != -1 && extensions.contains(fic.getName().substring(lastDot + 1)))
            {
               // This is a nested archive, we explode it
               try
               {
                  explode.log("Process nested archive " + fic.getName());
                  if (!fic.getName().equalsIgnoreCase(exclude))
                  {
                     explode(explode, fic.getName(), new ZipInputStream(zip), todir, exclude);
                  }
               }
               catch (DirException e)
               {
                  explode.log(e.getMessage());
               }
            }
            else
            {
               // This is a file we write it
               OutputStream out = null;
               try
               {
                  out = new BufferedOutputStream(new FileOutputStream(fic));
                  for (int size = zip.read(buffer); size != -1; size = zip.read(buffer))
                  {
                     out.write(buffer, 0, size);
                  }
               }
               catch (IOException e)
               {
                  explode.log("Problem when writing file " + e.getMessage());
               }
               finally
               {
                  if (out != null)
                  {
                     try
                     {
                        out.close();
                     }
                     catch (IOException ignored)
                     {
                     }
                  }
               }
            }
         }
      }
      catch (ZipException e)
      {
         throw new BuildException(e);
      }
      catch (IOException e)
      {
         throw new BuildException(e);
      }
   }

   /** When it returns the dir exists otherwise it throws a BuildException */
   private static void ensureDirExist(Explode explode, File dir) throws FileIsNotDirException, CannotCreateDirException
   {
      if (dir.exists())
      {
         if (dir.isDirectory())
         {
            // explode.log(dir.getName() + " exists and is used");
         }
         else
         {
            throw new FileIsNotDirException(dir);
         }
      }
      else
      {
         if (dir.mkdirs())
         {
            // explode.log("Created directory " + dir.getName());
         }
         else
         {
            throw new CannotCreateDirException(dir);
         }
      }
   }
}
