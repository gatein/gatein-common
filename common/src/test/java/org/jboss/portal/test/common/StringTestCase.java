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
package org.jboss.portal.test.common;

import junit.framework.TestCase;
import org.gatein.common.util.Tools;

/**
 * @author <a href="mailto:julien@jboss.org">Julien Viet</a>
 * @version $Revision: 6305 $
 */
public class StringTestCase extends TestCase
{

   public StringTestCase(String name)
   {
      super(name);
   }

   public void testReplace()
   {
      assertEquals("", Tools.replace("", "abc", "def"));
      assertEquals("defg", Tools.replace("abc", "abc", "defg"));
      assertEquals("_defg_", Tools.replace("_abc_", "abc", "defg"));
      assertEquals("_defgdefg_", Tools.replace("_abcabc_", "abc", "defg"));
      assertEquals("_defg_defg_", Tools.replace("_abc_abc_", "abc", "defg"));
   }

   public void testReplaceBoundedString()
   {
      assertEquals("", Tools.replaceAllInstancesOfBoundedString("", "PREFIX", "SUFFIX", "REPLACEMENT"));
      assertEquals("REPLACEMENT", Tools.replaceAllInstancesOfBoundedString("PREFIXSUFFIX", "PREFIX", "SUFFIX", "REPLACEMENT"));
      assertEquals("PREFIXSUFFIX", Tools.replaceBoundedString("PREFIXSUFFIX", "PREFIX", "SUFFIX", "REPLACEMENT", false, true));
      assertEquals("PREFIXSUFFIX", Tools.replaceBoundedString("PREFIXSUFFIX", "PREFIX", "SUFFIX", "REPLACEMENT", false, false));
      assertEquals("aaaaREPLACEMENTccccc", Tools.replaceAllInstancesOfBoundedString("aaaaPREFIXbbbbbSUFFIXccccc", "PREFIX", "SUFFIX", "REPLACEMENT"));
      assertEquals("aaaPREFIXbbbbSUFF", Tools.replaceAllInstancesOfBoundedString("aaaPREFIXbbbbSUFF", "PREFIX", "SUFFIX", "REPLACEMENT"));
      assertEquals("aRcccReeeR", Tools.replaceAllInstancesOfBoundedString("aPbbScccPdSeeePS", "P", "S", "R"));
      assertEquals("PSaPScccReeePS", Tools.replaceBoundedString("PSaPScccPdSeeePS", "P", "S", "R", false, false));
   }
}
