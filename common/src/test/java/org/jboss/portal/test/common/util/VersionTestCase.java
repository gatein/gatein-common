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
package org.jboss.portal.test.common.util;

import junit.framework.TestCase;
import org.gatein.common.util.Version;

/**
 * @author <a href="mailto:julien@jboss.org">Julien Viet</a>
 * @version $Revision: 1.1 $
 */
public class VersionTestCase extends TestCase
{

   /** . */
   private final Version.Qualifier GAQualifer = new Version.Qualifier(Version.Qualifier.Prefix.GA);

   public void testIllegalNameThrowsIAE()
   {
      try
      {
         new Version(null, 0, 0, 0, GAQualifer, "code");
         fail();
      }
      catch (IllegalArgumentException ignore)
      {
      }
   }

   public void testIllegalMajorThrowsIAE()
   {
      try
      {
         new Version("name", -1, 0, 0, GAQualifer, "code");
         fail();
      }
      catch (IllegalArgumentException ignore)
      {
      }
   }

   public void testIllegalMinorThrowsIAE()
   {
      try
      {
         new Version("name", 0, -1, 0, GAQualifer, "code");
         fail();
      }
      catch (IllegalArgumentException ignore)
      {
      }
   }

   public void testIllegalQualifierThrowsIAE()
   {
      try
      {
         new Version("name", 0, 0, 0, null, "code");
         fail();
      }
      catch (IllegalArgumentException ignore)
      {
      }

      //
      try
      {
         new Version.Qualifier(null);
         fail();
      }
      catch (IllegalArgumentException ignore)
      {
      }

      //
      try
      {
         new Version.Qualifier(null, Version.Qualifier.Suffix.EMPTY);
         fail();
      }
      catch (IllegalArgumentException ignore)
      {
      }

      //
      try
      {
         new Version.Qualifier(Version.Qualifier.Prefix.GA, Version.Qualifier.Suffix.SUFFIX_1);
         fail();
      }
      catch (IllegalArgumentException ignore)
      {
      }
   }

   public void testIllegalCodeNameThrowsIAE()
   {
      try
      {
         new Version("name", 0, 0, 0, GAQualifer, null);
         fail();
      }
      catch (IllegalArgumentException ignore)
      {
      }
   }

   public void testState()
   {
      Version version = new Version("foo", 1, 2, 3, new Version.Qualifier(Version.Qualifier.Prefix.CR, Version.Qualifier.Suffix.SUFFIX_3), "bar");
      assertEquals("foo", version.getName());
      assertEquals(1, version.getMajor());
      assertEquals(2, version.getMinor());
      assertEquals(3, version.getPatch());
      assertEquals(Version.Qualifier.Prefix.CR, version.getQualifier().getPrefix());
      assertEquals(Version.Qualifier.Suffix.SUFFIX_3, version.getQualifier().getSuffix());
      assertEquals("bar", version.getCodeName());
   }

   public void testQualifier()
   {
      
   }

   public void testFormat()
   {
      Version version = new Version("foo", 1, 2, 3, new Version.Qualifier(Version.Qualifier.Prefix.CR, Version.Qualifier.Suffix.SUFFIX_3), "bar");
      assertEquals("foo 1.2.3-CR3", version.toString());
   }
}
