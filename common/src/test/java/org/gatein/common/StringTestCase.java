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
   private static final String REPLACEMENT = "REPLACEMENT";
   private static final String PREFIX = "PREFIX";
   private static final String SUFFIX = "SUF";
   private static final TestStringReplacementGenerator DEF_GEN = new TestStringReplacementGenerator(REPLACEMENT);

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

   public void testReplaceAllInstancesOfBoundedString()
   {
      assertEquals("", TextTools.replaceAllInstancesOfBoundedString("", PREFIX, SUFFIX, REPLACEMENT));
      assertEquals("", TextTools.replaceAllInstancesOfBoundedString("", PREFIX, "", REPLACEMENT));
      assertEquals("", TextTools.replaceAllInstancesOfBoundedString("", "", "", REPLACEMENT));

      DEF_GEN.setExpectedMatch("");
      assertEquals(REPLACEMENT, TextTools.replaceAllInstancesOfBoundedString("PREFIXSUF", PREFIX, SUFFIX, DEF_GEN));

      DEF_GEN.setExpectedMatch("bbbbb");
      assertEquals("aaaaREPLACEMENTccccc", TextTools.replaceAllInstancesOfBoundedString("aaaaPREFIXbbbbbSUFccccc", PREFIX, SUFFIX, DEF_GEN));

      DEF_GEN.setExpectedMatch(null);
      assertEquals("aaaPREFIXbbbbSUFF", TextTools.replaceAllInstancesOfBoundedString("aaaPREFIXbbbbSUFF", PREFIX, "SUFFI", DEF_GEN));
      assertEquals("aRcccReeeR", TextTools.replaceAllInstancesOfBoundedString("aPbbScccPdSeeePS", "P", "S", "R"));
   }

   public void testReplaceBoundedString()
   {
      replaceBoundedString(false);
      replaceBoundedString(true);
   }

   private void replaceBoundedString(boolean suffixIsOptional)
   {
      buildTestWithDefaults(PREFIX + SUFFIX, PREFIX + SUFFIX, false, true, suffixIsOptional, "");
      buildTestWithDefaults(PREFIX + SUFFIX, PREFIX + SUFFIX, false, false, suffixIsOptional, "");
      buildTestWithDefaults(REPLACEMENT, PREFIX + SUFFIX, true, false, suffixIsOptional, "");
      buildTestWithDefaults(PREFIX + REPLACEMENT + SUFFIX, PREFIX + SUFFIX, true, true, suffixIsOptional, "");
      buildTest(PREFIX + SUFFIX, PREFIX + SUFFIX, "", PREFIX, SUFFIX, false, false, suffixIsOptional, "");
      buildTest("PSaPScccReeePS", "PSaPScccPdSeeePS", "R", "P", "S", false, false, suffixIsOptional, "d");
   }

   private void buildTestWithDefaults(String expected, String initial, boolean replaceIfBoundedStringEmpty, boolean keepBoundaries, boolean suffixIsOptional, String... expectedMatches)
   {
      buildTest(expected, initial, null, PREFIX, SUFFIX, replaceIfBoundedStringEmpty, keepBoundaries, suffixIsOptional, expectedMatches);
   }

   private void buildTest(String expected, String initial, String replacement, String prefix, String suffix, boolean replaceIfBoundedStringEmpty, boolean keepBoundaries, boolean suffixIsOptional, String... expectedMatches)
   {
      if (replacement == null)
      {
         replacement = REPLACEMENT;
      }
      TestStringReplacementGenerator gen = new TestStringReplacementGenerator(replacement);
      gen.setExpectedMatch(expectedMatches);

      assertEquals(expected, TextTools.replaceBoundedString(initial, prefix, suffix, gen, replaceIfBoundedStringEmpty, keepBoundaries, suffixIsOptional));
   }

   public void testReplaceStringsWithNullSuffix()
   {
      replaceStringsWithOptionalSuffix(false, null);
      replaceStringsWithOptionalSuffix(true, null);
   }

   private void replaceStringsWithOptionalSuffix(boolean suffixIsOptional, String suffix)
   {
      buildTest(REPLACEMENT + "blah", PREFIX + "blah", REPLACEMENT, PREFIX, suffix, false, false, suffixIsOptional, PREFIX);
      buildTest(PREFIX + REPLACEMENT + "blah", PREFIX + "blah", REPLACEMENT, PREFIX, suffix, false, true, suffixIsOptional, PREFIX);
      buildTest(REPLACEMENT + "blah", PREFIX + "blah", REPLACEMENT, PREFIX, suffix, true, false, suffixIsOptional, PREFIX);
      buildTest(REPLACEMENT, PREFIX, REPLACEMENT, PREFIX, suffix, true, false, suffixIsOptional, PREFIX);
   }

   public void testReplaceStringsWithOptionalSuffix()
   {
      replaceStringsWithOptionalSuffix(true, SUFFIX);
      buildTest(REPLACEMENT + "blah" + REPLACEMENT + "foo", PREFIX + "blah" + PREFIX + "replaced" + SUFFIX + "foo", REPLACEMENT, PREFIX, SUFFIX, false, false, true, PREFIX, "replaced");
      buildTest(REPLACEMENT + "blah" + PREFIX + REPLACEMENT + SUFFIX + "foo", PREFIX + "blah" + PREFIX + "replaced" + SUFFIX + "foo", REPLACEMENT, PREFIX, SUFFIX, false, true, true, PREFIX, "replaced");
   }

   static class TestStringReplacementGenerator implements TextTools.StringReplacementGenerator
   {
      private String replacement;
      private String[] expectedMatch;
      private int invocationCount;

      TestStringReplacementGenerator(String replacement)
      {
         this.replacement = replacement;
      }

      public void setExpectedMatch(String... expectedMatch)
      {
         this.expectedMatch = expectedMatch;
         this.invocationCount = 0;
      }

      public String getReplacementFor(String match, String prefix, String suffix)
      {
         if (expectedMatch == null)
         {
            fail("getReplacementFor shouldn't have been called");
         }
         assertEquals("'" + expectedMatch[invocationCount++] + "'", "'" + match + "'");
         return replacement;
      }
   }
}
