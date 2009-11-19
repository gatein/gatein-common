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
package org.gatein.common.util;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.gatein.common.logging.Logger;
import org.gatein.common.logging.LoggerFactory;

/**
 * @author <a href="mailto:chris.laprun@jboss.com">Chris Laprun</a>
 * @version $Revision: 5757 $
 * @since 2.4 (May 31, 2009)
 */
public class ParameterValidation
{
   public final static Pattern CSS_DISTANCE = Pattern.compile("\\d+\\W*(em|ex|px|in|cm|mm|pt|pc|%)?");
   public final static Pattern XSS_CHECK = Pattern.compile("([^<>\\(\\)=\\\\](?<!%5C))*", Pattern.CASE_INSENSITIVE);
   public final static Pattern VALID_ASCII_CLASS_NAME = Pattern.compile("([a-z][a-z0-9_]*\\.)*[A-Z][A-Za-z0-9_$]*");

   /**
    * Implements a behavior to be executed in case a value fails to be validated. Uses the Chain of responsibility
    * pattern so that several orthogonal behaviors can be chained if needed.
    */
   public abstract static class ValidationErrorHandler
   {
      private ValidationErrorHandler next;
      private String defaultValue;
      protected static final String CONTINUE = "__JBP__CONTINUE__";

      public ValidationErrorHandler(String defaultValue)
      {
         this.defaultValue = defaultValue;
      }

      public ValidationErrorHandler setNext(ValidationErrorHandler next)
      {
         this.next = next;
         return this;
      }

      public String handleValidationError(String failedValue)
      {
         String result = internalValidationErrorHandling(failedValue);
         if (!CONTINUE.equals(result))
         {
            return result;
         }
         else if (next != null)
         {
            return next.handleValidationError(failedValue);
         }
         else
         {
            return defaultValue;
         }

      }

      /**
       * Decide what to do with the failed value.
       *
       * @param failedValue the value that failed passing validation
       * @return a new value to be used by the client code or {@link #CONTINUE} if this ValidationErrorHandler allows
       *         downstream handlers to further process the value.
       */
      protected abstract String internalValidationErrorHandling(String failedValue);
   }

   public static class LoggingValidationErrorHandler extends ValidationErrorHandler
   {
      private static final Logger log = LoggerFactory.getLogger(ParameterValidation.class);
      private String contextMessage;

      public LoggingValidationErrorHandler(String defaultValue, String contextMessage)
      {
         super(defaultValue);
         this.contextMessage = contextMessage;
      }

      protected String internalValidationErrorHandling(String failedValue)
      {
         log.debug("'" + failedValue + "' failed validation." + (contextMessage != null ? "\n" + contextMessage : ""));
         return CONTINUE;
      }
   }

   public static void throwIllegalArgExceptionIfNullOrEmpty(String valueToCheck, String valueName, String contextName)
   {
      if (isNullOrEmpty(valueToCheck))
      {
         throw new IllegalArgumentException((contextName != null ? contextName + " r" : "R") + "equires a non-null, non-empty " + valueName);
      }
   }

   public static void throwIllegalArgExceptionIfRangeInvalid(char[] chars, int offset, int length)
   {
      ParameterValidation.throwIllegalArgExceptionIfNull(chars, "characters");

      if (offset < 0)
      {
         throw new IllegalArgumentException("Offset must be positive!");
      }
      if (length < 0)
      {
         throw new IllegalArgumentException("Length must be positive!");
      }
      if (offset + length > chars.length)
      {
         throw new IllegalArgumentException("Specified character range is outside of the given char[]!");
      }
   }

   /**
    * Determines whether the given String is <code>null</code> or empty (after all extra whitespaces have been trimmed,
    * if any).
    *
    * @param valueToCheck the String to test
    * @return <code>true</code> if the the given String is <code>null</code> or empty (after being trimmed),
    *         <code>false</code> otherwise.
    */
   public static boolean isNullOrEmpty(String valueToCheck)
   {
      return valueToCheck == null || valueToCheck.trim().length() == 0;
   }

   public static void throwIllegalArgExceptionIfNull(Object objectToTest, String name)
   {
      if (objectToTest == null)
      {
         throw new IllegalArgumentException("Must pass a non null " + name);
      }
   }

   /** @since 2.4.1 */
   public static void throwIllegalArgExceptionIfNullOrEmpty(Object[] array, String name)
   {
      if (array == null)
      {
         throw new IllegalArgumentException("Must pass a non-null " + name);
      }
      if (array.length == 0)
      {
         throw new IllegalArgumentException("Must pass a non-empty " + name);
      }
   }

   public static boolean existsAndIsNotEmpty(Collection collection)
   {
      return collection != null && !collection.isEmpty();
   }

   public static boolean existsAndIsNotEmpty(Map map)
   {
      return map != null && !map.isEmpty();
   }

   public static <T> boolean existsAndIsNotEmpty(T[] array)
   {
      return array != null && array.length > 0;
   }

   /**
    * Checks if a given value matches the given format (as a regular expression). If yes, returns it as is. Otherwise,
    * returns the default value.
    *
    * @param value        value to sanitize if needed, <code>null</code> is considered as not matching
    * @param regex        format the value needs to conform to
    * @param defaultValue default value to use if the specified value does not conform to the specified format
    * @return the specified value if it conforms to the expected format, the given default value otherwise.
    */
   public static String sanitizeFromPattern(String value, Pattern regex, String defaultValue)
   {
      return sanitizeFromPatternWithHandler(value, regex, new LoggingValidationErrorHandler(defaultValue, null));
   }

   /**
    * Checks if a given value matches the given format (as a regular expression) and delegates what to do to the
    * specified ValidationErrorHandler.
    *
    * @param value   value to sanitize if needed, <code>null</code> is considered as not matching
    * @param regex   format the value needs to conform to
    * @param handler ValidationErrorHandler implementing the behavior to apply in case the specified value failed to
    *                validate
    * @return the specified value if it conforms to the expected format, whatever value the specified
    *         ValidationErrorHandler thinks appropriate otherwise.
    */
   public static String sanitizeFromPatternWithHandler(String value, Pattern regex, ValidationErrorHandler handler)
   {
      throwIllegalArgExceptionIfNull(regex, "expected value format");
      throwIllegalArgExceptionIfNull(handler, "ValidationErrorHandler");

      if (value == null || !regex.matcher(value).matches())
      {
         return handler.handleValidationError(value);
      }
      else
      {
         return value;
      }
   }

   /**
    * Checks if a given value matches one of the possible values. If yes, returns it as is. Otherwise, returns the
    * default value.
    *
    * @param value          value to sanitize if needed, <code>null</code> is considered as not matching
    * @param possibleValues possible values
    * @param defaultValue   default value to use if the specified value does not conform to the specified format
    * @return the specified value if it conforms to the expected format, the given default value otherwise.
    */
   public static String sanitizeFromValues(String value, String[] possibleValues, String defaultValue)
   {
      return sanitizeFromValuesWithHandler(value, possibleValues, new LoggingValidationErrorHandler(defaultValue, null));
   }

   /**
    * Checks if a given value matches one of the possible values and delegates what to do to the specified
    * ValidationErrorHandler.
    *
    * @param value          value to sanitize if needed, <code>null</code> is considered as not matching
    * @param possibleValues possible values
    * @param handler        ValidationErrorHandler implementing the behavior to apply in case the specified value failed
    *                       to validate
    * @return the specified value if it conforms to the expected format, whatever value the specified
    *         ValidationErrorHandler thinks appropriate otherwise.
    */
   public static String sanitizeFromValuesWithHandler(String value, String[] possibleValues, ValidationErrorHandler handler)
   {
      throwIllegalArgExceptionIfNullOrEmpty(possibleValues, "possible values");
      throwIllegalArgExceptionIfNull(handler, "ValidationErrorHandler");

      List<String> values = Arrays.asList(possibleValues);
      if (!values.contains(value))
      {
         return handler.handleValidationError(value);
      }
      else
      {
         return value;
      }
   }
}
