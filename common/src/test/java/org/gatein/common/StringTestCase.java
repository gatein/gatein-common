/*
 * JBoss, a division of Red Hat
 * Copyright 2010, Red Hat Middleware, LLC, and individual
 * contributors as indicated by the @authors tag. See the
 * copyright.txt in the distribution for a full listing of
 * individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
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

      DEF_GEN.setExpectedMatch(new Pair("", false));
      assertEquals(REPLACEMENT, TextTools.replaceAllInstancesOfBoundedString("PREFIXSUF", PREFIX, SUFFIX, DEF_GEN));

      DEF_GEN.setExpectedMatch(new Pair("bbbbb", false));
      assertEquals("aaaaREPLACEMENTccccc", TextTools.replaceAllInstancesOfBoundedString("aaaaPREFIXbbbbbSUFccccc", PREFIX, SUFFIX, DEF_GEN));

      DEF_GEN.setExpectedMatch((Pair)null);
      assertEquals("aaaPREFIXbbbbSUFF", TextTools.replaceAllInstancesOfBoundedString("aaaPREFIXbbbbSUFF", PREFIX, "SUFFI", DEF_GEN));
      assertEquals("aRcccReeeR", TextTools.replaceAllInstancesOfBoundedString("aPbbScccPdSeeePS", "P", "S", "R"));
   }

   public void testReplaceBoundedString()
   {
      replaceBoundedString(false);
      replaceBoundedString(true);
   }

   public void testReplaceStringsWithNullSuffix()
   {
      replaceStringsWithOptionalSuffix(false, null);
      replaceStringsWithOptionalSuffix(true, null);
   }

   public void testReplaceStringsWithOptionalSuffix()
   {
      replaceStringsWithOptionalSuffix(true, SUFFIX);
      buildTest(REPLACEMENT + "blah" + REPLACEMENT + "foo", PREFIX + "blah" + PREFIX + "replaced" + SUFFIX + "foo", REPLACEMENT, PREFIX, SUFFIX, false, false, true, new Pair("blah", true), new Pair("replaced", false));
      buildTest(REPLACEMENT + "blah" + PREFIX + REPLACEMENT + SUFFIX + "foo", PREFIX + "blah" + PREFIX + "replaced" + SUFFIX + "foo", REPLACEMENT, PREFIX, SUFFIX, false, true, true, new Pair("blah", true), new Pair("replaced", false));
   }

   /*public void testReplaceWSRPTokens()
   {
      String original = "<form method='post' action='wsrp_rewrite?wsrp-urlType=blockingAction&wsrp" +
         "-interactionState=JBPNS_/wsrp_rewrite' id='wsrp_rewrite_portfolioManager'><table><tr><td>Stock symbol</t" +
         "d><td><input name='symbol'/></td></tr><tr><td><input type='submit' value='Submit'></td></tr></table></form>";

      String expected = "<form method='post' action='foo' id='namespaceportfolioManager'><table><tr><td>Stock symbol</t" +
         "d><td><input name='symbol'/></td></tr><tr><td><input type='submit' value='Submit'></td></tr></table></form>";

      String replaced = TextTools.replaceBoundedString(original, "wsrp_rewrite", "/wsrp_rewrite", new TextTools.StringReplacementGenerator()
      {
         public String getReplacementFor(String match, String prefix, String suffix, boolean matchedPrefixOnly)
         {
            if(matchedPrefixOnly)
            {
               return "namespace" + match.substring(1);
            }
            else
            {
               return "foo";
            }
         }
      }, true, false, true);

      assertEquals(expected, replaced);
   }*/

   private void replaceBoundedString(boolean suffixIsOptional)
   {
      buildTestWithDefaults(PREFIX + SUFFIX, PREFIX + SUFFIX, false, true, suffixIsOptional, "");
      buildTestWithDefaults(PREFIX + SUFFIX, PREFIX + SUFFIX, false, false, suffixIsOptional, "");
      buildTestWithDefaults(REPLACEMENT, PREFIX + SUFFIX, true, false, suffixIsOptional, "");
      buildTestWithDefaults(PREFIX + REPLACEMENT + SUFFIX, PREFIX + SUFFIX, true, true, suffixIsOptional, "");
      buildTest(PREFIX + SUFFIX, PREFIX + SUFFIX, "", PREFIX, SUFFIX, false, false, suffixIsOptional, new Pair("", false));
      buildTest("PSaPScccReeePS", "PSaPScccPdSeeePS", "R", "P", "S", false, false, suffixIsOptional, new Pair("d", false));
   }

   private void replaceStringsWithOptionalSuffix(boolean suffixIsOptional, String suffix)
   {
      buildTest(REPLACEMENT + "blah", PREFIX + "blah", REPLACEMENT, PREFIX, suffix, false, false, suffixIsOptional, new Pair("blah", true));
      buildTest(PREFIX + REPLACEMENT + "blah", PREFIX + "blah", REPLACEMENT, PREFIX, suffix, false, true, suffixIsOptional, new Pair("blah", true));
      buildTest(REPLACEMENT + "blah", PREFIX + "blah", REPLACEMENT, PREFIX, suffix, true, false, suffixIsOptional, new Pair("blah", true));
      buildTest(REPLACEMENT, PREFIX, REPLACEMENT, PREFIX, suffix, true, false, suffixIsOptional, new Pair("", true));
   }

   private void buildTestWithDefaults(String expected, String initial, boolean replaceIfBoundedStringEmpty, boolean keepBoundaries, boolean suffixIsOptional, String... expectedMatches)
   {
      Pair[] pairs = new Pair[expectedMatches.length];
      for (int i = 0; i < expectedMatches.length; i++)
      {
         pairs[i] = new Pair(expectedMatches[i], false);
      }
      buildTest(expected, initial, null, PREFIX, SUFFIX, replaceIfBoundedStringEmpty, keepBoundaries, suffixIsOptional, pairs);
   }

   private void buildTest(String expected, String initial, String replacement, String prefix, String suffix, boolean replaceIfBoundedStringEmpty, boolean keepBoundaries, boolean suffixIsOptional, Pair... expectedMatches)
   {
      if (replacement == null)
      {
         replacement = REPLACEMENT;
      }
      TestStringReplacementGenerator gen = new TestStringReplacementGenerator(replacement);
      gen.setExpectedMatch(expectedMatches);

      assertEquals(expected, TextTools.replaceBoundedString(initial, prefix, suffix, gen, replaceIfBoundedStringEmpty, keepBoundaries, suffixIsOptional));
   }

   static class TestStringReplacementGenerator implements TextTools.StringReplacementGenerator
   {
      private String replacement;
      private Pair[] expectedMatch;
      private int invocationCount;

      TestStringReplacementGenerator(String replacement)
      {
         this.replacement = replacement;
      }

      public void setExpectedMatch(Pair... expectedMatch)
      {
         this.expectedMatch = expectedMatch;
         this.invocationCount = 0;
      }

      public String getReplacementFor(String match, String prefix, String suffix, boolean matchedPrefixOnly)
      {
         if (expectedMatch == null)
         {
            fail("getReplacementFor shouldn't have been called");
         }
         Pair pair = expectedMatch[invocationCount++];
         assertEquals("'" + pair.match + "'", "'" + match + "'");
         assertEquals(matchedPrefixOnly, pair.matchedPrefix);
         return replacement;
      }
   }

   static class Pair
   {
      String match;
      boolean matchedPrefix;

      public Pair(String match, boolean matchedPrefix)
      {
         this.match = match;
         this.matchedPrefix = matchedPrefix;
      }
   }
}
