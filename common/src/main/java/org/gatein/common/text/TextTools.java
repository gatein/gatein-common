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
package org.gatein.common.text;

import org.gatein.common.util.ParameterValidation;

/**
 * @author <a href="mailto:julien@jboss.org">Julien Viet</a>
 * @version $Revision: 1.1 $
 */
public class TextTools
{

   /**
    * Returns true if the char c is alpha numeric i.e it belongs to one of the following ranges [0,9], [A,Z] or [a,z]
    *
    * @param c the char to test
    * @return true if c is alpha numeric
    */
   public static boolean isAlphaNumeric(char c)
   {
      return (c >= '0' && c <= '9') || (c >= 'A' && c <= 'Z') || (c >= 'a' && c <= 'z');
   }

   /**
    * Returns the hexadecimal value of the provided numeric value.
    *
    * @param z the numeric value to convert
    * @return the hexadecimal char
    * @throws IllegalArgumentException if the value is not between 0 and 15
    */
   public static char toHex(int z) throws IllegalArgumentException
   {
      if (z >= 0 && z < 10)
      {
         return (char)('0' + z);
      }
      else if (z >= 10 && z < 16)
      {
         return (char)('A' - 10 + z);
      }
      else
      {
         throw new IllegalArgumentException("Wrong character");
      }
   }

   /**
    * Replace occurence in a string.
    *
    * @param string      the source string
    * @param pattern     the replaced pattern
    * @param replacement the replacement text
    * @return the new string
    */
   public static String replace(String string, String pattern, String replacement)
   {
      StringBuffer buffer = new StringBuffer(string.length());
      int previous = 0;
      int current = string.indexOf(pattern);
      while (current != -1)
      {
         buffer.append(string.substring(previous, current));
         buffer.append(replacement);
         previous = current + pattern.length();
         current = string.indexOf(pattern, previous);
      }
      buffer.append(string.substring(previous));
      return buffer.toString();
   }

   /**
    * Same as replaceBoundedString(initial, prefix, suffix, replacement, true, false).
    *
    * @param initial
    * @param prefix
    * @param suffix
    * @param replacement
    * @return
    */
   public static String replaceAllInstancesOfBoundedString(String initial, String prefix, String suffix, String replacement)
   {
      return replaceBoundedString(initial, prefix, suffix, replacement, true, false);
   }

   public static String replaceBoundedString(String initial, String prefix, String suffix, String replacement,
                                             boolean replaceIfBoundedStringEmpty, boolean keepBoundaries)
   {
      ParameterValidation.throwIllegalArgExceptionIfNull(replacement, "replacement");
      return replaceBoundedString(initial, prefix, suffix, new ConstantStringReplacementGenerator(replacement),
         replaceIfBoundedStringEmpty, keepBoundaries);
   }

   /**
    * Replace instances of Strings delimited by the given prefix and suffix (hence, bounded) by a replacement String
    * computed by the specified StringReplacementGenerator based on the current bounded String match. It is possible to
    * specify whether the substitution will happen only if the delimited String is non-empty by setting
    * <code>replaceIfBoundedStringEmpty</code> to <code>false</code>. It is also possible to keep the boundaries (prefix
    * and suffix) after the substitution by setting <code>keepBoundaries</code> to <code>true</code>.
    * <p/>
    * See org.gatein.common.StringTestCase.testReplaceBoundedString() for usage details.
    *
    * @param initial                     the String in which we want to replace bounded Strings
    * @param prefix                      the prefix used identify the beginning of the String targeted for replacement
    * @param suffix                      the suffix used to identify the end of the String targeted for replacement
    * @param generator                   the StringReplacementGeneraot generating replacements based on the current
    *                                    bounded String match
    * @param replaceIfBoundedStringEmpty <code>true</code> to allow replacement of empty Strings (in essence, insertion
    *                                    of the replacement String between the prefix and suffix)
    * @param keepBoundaries              <code>true</code> to keep the prefix and suffix markers in the resulting
    *                                    String
    * @return a String where the Strings marked by prefix and suffix have been replaced by replacement
    */
   public static String replaceBoundedString(String initial, String prefix, String suffix, StringReplacementGenerator generator,
                                             boolean replaceIfBoundedStringEmpty, boolean keepBoundaries)
   {
      if (ParameterValidation.isNullOrEmpty(initial))
      {
         return initial;
      }

      ParameterValidation.throwIllegalArgExceptionIfNull(generator, "StringReplacementGenerator");

      ParameterValidation.throwIllegalArgExceptionIfNullOrEmpty(prefix, "prefix", "Tools.replaceBoundedString");
      ParameterValidation.throwIllegalArgExceptionIfNullOrEmpty(suffix, "suffix", "Tools.replaceBoundedString");

      StringBuffer tmp = new StringBuffer(initial);
      int prefixIndex = tmp.indexOf(prefix);
      int suffixLength = suffix.length();
      int prefixLength = prefix.length();

      while (prefixIndex != -1)
      {
         int suffixIndex = tmp.indexOf(suffix, prefixIndex);

         if (suffixIndex != -1)
         {
            // we don't care about empty bounded strings or prefix and suffix don't delimit an empty String => replace!
            if (replaceIfBoundedStringEmpty || suffixIndex != prefixIndex + prefixLength)
            {
               String match;
               if (keepBoundaries)
               {
                  match = tmp.substring(prefixIndex + prefixLength, suffixIndex);
                  tmp.delete(prefixIndex + prefixLength, suffixIndex);
                  tmp.insert(prefixIndex + prefixLength, generator.getReplacementFor(match));
               }
               else
               {
                  match = tmp.substring(prefixIndex, suffixIndex + suffixLength);
                  tmp.delete(prefixIndex, suffixIndex + suffixLength);
                  tmp.insert(prefixIndex, generator.getReplacementFor(match));
               }
            }
         }

         prefixIndex = tmp.indexOf(prefix, prefixIndex + prefixLength);
      }

      return tmp.toString();
   }

   public static interface StringReplacementGenerator
   {
      String getReplacementFor(String match);
   }

   public static class ConstantStringReplacementGenerator implements StringReplacementGenerator
   {
      private String replacement;

      public ConstantStringReplacementGenerator(String replacement)
      {
         this.replacement = replacement;
      }

      public String getReplacementFor(String match)
      {
         return replacement;
      }
   }
}
