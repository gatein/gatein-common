/******************************************************************************
 * JBoss, a division of Red Hat                                               *
 * Copyright 2009, Red Hat Middleware, LLC, and individual                    *
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
package org.gatein.common;

import junit.framework.TestCase;
import org.gatein.common.text.TextTools;

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
      assertEquals("", TextTools.replace("", "abc", "def"));
      assertEquals("defg", TextTools.replace("abc", "abc", "defg"));
      assertEquals("_defg_", TextTools.replace("_abc_", "abc", "defg"));
      assertEquals("_defgdefg_", TextTools.replace("_abcabc_", "abc", "defg"));
      assertEquals("_defg_defg_", TextTools.replace("_abc_abc_", "abc", "defg"));
   }

   public void testReplaceBoundedString()
   {
      assertEquals("", TextTools.replaceAllInstancesOfBoundedString("", "PREFIX", "SUFFIX", "REPLACEMENT"));
      assertEquals("REPLACEMENT", TextTools.replaceAllInstancesOfBoundedString("PREFIXSUFFIX", "PREFIX", "SUFFIX", "REPLACEMENT"));
      assertEquals("PREFIXSUFFIX", TextTools.replaceBoundedString("PREFIXSUFFIX", "PREFIX", "SUFFIX", "REPLACEMENT", false, true));
      assertEquals("PREFIXSUFFIX", TextTools.replaceBoundedString("PREFIXSUFFIX", "PREFIX", "SUFFIX", "REPLACEMENT", false, false));
      assertEquals("aaaaREPLACEMENTccccc", TextTools.replaceAllInstancesOfBoundedString("aaaaPREFIXbbbbbSUFFIXccccc", "PREFIX", "SUFFIX", "REPLACEMENT"));
      assertEquals("aaaPREFIXbbbbSUFF", TextTools.replaceAllInstancesOfBoundedString("aaaPREFIXbbbbSUFF", "PREFIX", "SUFFIX", "REPLACEMENT"));
      assertEquals("aRcccReeeR", TextTools.replaceAllInstancesOfBoundedString("aPbbScccPdSeeePS", "P", "S", "R"));
      assertEquals("PSaPScccReeePS", TextTools.replaceBoundedString("PSaPScccPdSeeePS", "P", "S", "R", false, false));
   }
}
