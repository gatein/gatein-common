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
package org.gatein.common.jar;

import java.io.IOException;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.JarInputStream;

/**
 * @author <a href="mailto:julien@jboss.org">Julien Viet</a>
 * @version $Revision: 7228 $
 */
public class JarInfo
{

   /** . */
   private SortedSet entries;

   public JarInfo(JarFile file)
   {
      if (file == null)
      {
         throw new IllegalArgumentException();
      }
      entries = new TreeSet();
      for (Enumeration e = file.entries(); e.hasMoreElements();)
      {
         JarEntry entry = (JarEntry)e.nextElement();
         JarEntryInfo info = new JarEntryInfo(entry);
         entries.add(info);
      }
   }

   public JarInfo(JarInputStream in) throws IOException
   {
      if (in == null)
      {
         throw new IllegalArgumentException();
      }
      entries = new TreeSet();
      for (JarEntry entry = in.getNextJarEntry(); entry != null; entry = in.getNextJarEntry())
      {
         JarEntryInfo info = new JarEntryInfo(entry);
         entries.add(info);
      }
   }

   public Iterator entries()
   {
      return entries.iterator();
   }
}
