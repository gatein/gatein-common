/*
* JBoss, a division of Red Hat
* Copyright 2009, Red Hat Middleware, LLC, and individual contributors as indicated
* by the @authors tag. See the copyright.txt in the distribution for a
* full listing of individual contributors.
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

package org.jboss.portal.test.common.util;

import junit.framework.TestCase;
import org.jboss.portal.common.util.ParameterValidation;

import java.util.regex.Pattern;

/**
 * @author <a href="mailto:chris.laprun@jboss.com">Chris Laprun</a>
 * @version $Revision$
 */
public class ParameterValidationTestCase extends TestCase
{
   private String appender;

   @Override
   protected void setUp() throws Exception
   {
      appender = "";
   }

   public void testIsNullOrEmpty()
   {
      assertTrue(ParameterValidation.isNullOrEmpty(null));
      assertTrue(ParameterValidation.isNullOrEmpty(""));
      assertTrue(ParameterValidation.isNullOrEmpty(" \t\n"));
      assertFalse(ParameterValidation.isNullOrEmpty("   test   "));
   }

   public void testSanitizeFromPatternNullPattern()
   {
      try
      {
         ParameterValidation.sanitizeFromValues("foo", null, null);
         fail("Should have thrown an IAE on null Pattern");
      }
      catch (IllegalArgumentException e)
      {
         // expected
      }
   }

   public void testSanitizeFromPatternNullValue()
   {
      String defaultValue = "default";
      assertEquals(defaultValue, ParameterValidation.sanitizeFromPattern(null, Pattern.compile(""), defaultValue));
   }

   public void testSanitizeFromPatternNullDefault()
   {
      assertNull(ParameterValidation.sanitizeFromPattern(null, Pattern.compile(""), null));
   }

   public void testSanitizeFromPatternCSSDistance()
   {
      String defaultValue = "300px";
      assertEquals(defaultValue,
         ParameterValidation.sanitizeFromPattern("0%20;%20background-image:%20url(http://us.i1.yimg.com/us.yimg.com/i/us/we/52//28.gif)",
            ParameterValidation.CSS_DISTANCE, defaultValue));

      assertEquals(defaultValue, ParameterValidation.sanitizeFromPattern(defaultValue, ParameterValidation.CSS_DISTANCE, null));

      String value = "0";
      assertEquals(value, ParameterValidation.sanitizeFromPattern(value, ParameterValidation.CSS_DISTANCE, null));

      value = "10%";
      assertEquals(value, ParameterValidation.sanitizeFromPattern(value, ParameterValidation.CSS_DISTANCE, null));

      value = "10 %";
      assertEquals(value, ParameterValidation.sanitizeFromPattern(value, ParameterValidation.CSS_DISTANCE, null));

      value = "100                        \n\tin";
      assertEquals(value, ParameterValidation.sanitizeFromPattern(value, ParameterValidation.CSS_DISTANCE, null));

      assertEquals(defaultValue, ParameterValidation.sanitizeFromPattern("", ParameterValidation.CSS_DISTANCE, defaultValue));
   }

   public void testSanitizeFromPatternXSSCheck()
   {
      String defaultValue = "default";
      assertEquals("foo", ParameterValidation.sanitizeFromPattern("foo", ParameterValidation.XSS_CHECK, defaultValue));
      assertEquals("/foo/bar", ParameterValidation.sanitizeFromPattern("/foo/bar", ParameterValidation.XSS_CHECK, defaultValue));
      assertEquals("test&eacute;", ParameterValidation.sanitizeFromPattern("test&eacute;", ParameterValidation.XSS_CHECK, defaultValue));
      assertEquals("testé", ParameterValidation.sanitizeFromPattern("testé", ParameterValidation.XSS_CHECK, defaultValue));

      assertEquals(defaultValue, ParameterValidation.sanitizeFromPattern("/foo/bar/\\/baz", ParameterValidation.XSS_CHECK, defaultValue));
      assertEquals(defaultValue, ParameterValidation.sanitizeFromPattern("/foo/bar/%5c/baz", ParameterValidation.XSS_CHECK, defaultValue));
      assertEquals(defaultValue, ParameterValidation.sanitizeFromPattern("/foo/bar/%5C/baz", ParameterValidation.XSS_CHECK, defaultValue));
      assertEquals(defaultValue, ParameterValidation.sanitizeFromPattern("%5C", ParameterValidation.XSS_CHECK, defaultValue));
      assertEquals(defaultValue, ParameterValidation.sanitizeFromPattern("%5C\t   ", ParameterValidation.XSS_CHECK, defaultValue));
      assertEquals(defaultValue, ParameterValidation.sanitizeFromPattern("\t\n%5c", ParameterValidation.XSS_CHECK, defaultValue));
      assertEquals(defaultValue, ParameterValidation.sanitizeFromPattern("/foo/bar/%5C", ParameterValidation.XSS_CHECK, defaultValue));
      assertEquals(defaultValue, ParameterValidation.sanitizeFromPattern("/foo/bar/%5c", ParameterValidation.XSS_CHECK, defaultValue));
      assertEquals(defaultValue, ParameterValidation.sanitizeFromPattern("http://qa.cwcportal.aviation.ge.com:80/portal/auth/portal/main/cwcportal-" +
         "Home/cwcportal-Home-LeftNavigationPortletWindow?action=1&org.apache.myfaces.portlet.MyFacesGenericPortlet.VIEW_ID=/pages/h" +
         "omeleftnavigation.jsp<script>window.open(\"http://3.211.64.16/XSS/ \", \"XSS\",\"width=550,height=290\")</script>",
         ParameterValidation.XSS_CHECK, defaultValue));
      assertEquals(defaultValue, ParameterValidation.sanitizeFromPattern("/foo/bar/</baz", ParameterValidation.XSS_CHECK, defaultValue));

   }

   public void testSanitizeFromPatternClassNameCheck()
   {
      assertNull(ParameterValidation.sanitizeFromPattern("foo", ParameterValidation.VALID_ASCII_CLASS_NAME, null));
      assertNull(ParameterValidation.sanitizeFromPattern("com.example.foo", ParameterValidation.VALID_ASCII_CLASS_NAME, null));
      assertNull(ParameterValidation.sanitizeFromPattern("com.example.foo.1Foo", ParameterValidation.VALID_ASCII_CLASS_NAME, null));
      assertEquals("Foo", ParameterValidation.sanitizeFromPattern("Foo", ParameterValidation.VALID_ASCII_CLASS_NAME, null));
      assertEquals("com.example.Foo", ParameterValidation.sanitizeFromPattern("com.example.Foo", ParameterValidation.VALID_ASCII_CLASS_NAME, null));
      assertEquals("com.example.Foo$_1", ParameterValidation.sanitizeFromPattern("com.example.Foo$_1", ParameterValidation.VALID_ASCII_CLASS_NAME, null));
      assertEquals("com2.example.Foo$_1", ParameterValidation.sanitizeFromPattern("com2.example.Foo$_1", ParameterValidation.VALID_ASCII_CLASS_NAME, null));
   }

   public void testSanitizeFromValuesNullValue()
   {
      String defaultValue = "default";
      assertEquals(defaultValue, ParameterValidation.sanitizeFromValues(null, new String[]{""}, defaultValue));
   }

   public void testSanitizeFromValuesNullValues()
   {
      try
      {
         ParameterValidation.sanitizeFromValues("foo", null, null);
         fail("Should have thrown an IAE on null values");
      }
      catch (Exception e)
      {
         // expected
      }
   }

   public void testSanitizeFromValuesNullDefault()
   {
      assertNull(ParameterValidation.sanitizeFromValues("foo", new String[]{"bar"}, null));
   }

   public void testSanitizeFromValues()
   {
      String defaultValue = "foo";
      String[] possible = new String[]{"windowremove", "windowmove"};

      assertEquals(defaultValue, ParameterValidation.sanitizeFromValues("%3Cblink%3EH4XOR3D%3C/blink%3E", possible, defaultValue));

      assertEquals("windowmove", ParameterValidation.sanitizeFromValues("windowmove", possible, defaultValue));
      assertEquals("windowremove", ParameterValidation.sanitizeFromValues("windowremove", possible, defaultValue));
   }

   public void testSanitizeNullHandler()
   {
      try
      {
         ParameterValidation.sanitizeFromPatternWithHandler("foo", Pattern.compile(""), null);
         fail("Should have thrown an IAE on null handler");
      }
      catch (Exception e)
      {
         // expected
      }

      try
      {
         ParameterValidation.sanitizeFromValuesWithHandler("foo", new String[]{""}, null);
         fail("Should have thrown an IAE on null handler");
      }
      catch (Exception e)
      {
         // expected
      }
   }

   public void testSanitizeHandlerChain()
   {
      ParameterValidation.ValidationErrorHandler handler = new TestValidationErrorHandler("default", "1");
      handler.setNext(new TestValidationErrorHandler("default", "2").setNext(new TestValidationErrorHandler("default", "3")));

      assertEquals("default", ParameterValidation.sanitizeFromPatternWithHandler("foo", Pattern.compile("bar"), handler));
      assertEquals("1/2/3/", appender);

      appender = "";
      assertEquals("default", ParameterValidation.sanitizeFromValuesWithHandler("foo", new String[]{"bar"}, handler));
      assertEquals("1/2/3/", appender);
   }

   public void testSanitizeInterruptedHandlerChain()
   {
      ParameterValidation.ValidationErrorHandler interruptor = new InterruptingValidationErrorHandler("default");
      ParameterValidation.ValidationErrorHandler handler = new TestValidationErrorHandler("default", "1");
      handler.setNext(interruptor.setNext(new TestValidationErrorHandler("default", "3")));

      assertNull(ParameterValidation.sanitizeFromPatternWithHandler("foo", Pattern.compile("bar"), handler));
      assertEquals("1/", appender);

      appender = "";
      assertNull(ParameterValidation.sanitizeFromValuesWithHandler("foo", new String[]{"bar"}, handler));
      assertEquals("1/", appender);
   }

   private class InterruptingValidationErrorHandler extends ParameterValidation.ValidationErrorHandler
   {
      private InterruptingValidationErrorHandler(String defaultValue)
      {
         super(defaultValue);
      }

      @Override
      protected String internalValidationErrorHandling(String failedValue)
      {
         return null;
      }
   }

   private class TestValidationErrorHandler extends ParameterValidation.ValidationErrorHandler
   {
      String order;

      private TestValidationErrorHandler(String defaultValue, String order)
      {
         super(defaultValue);
         this.order = order;
      }

      @Override
      protected String internalValidationErrorHandling(String failedValue)
      {
         appender += order + "/";
         return ParameterValidation.ValidationErrorHandler.CONTINUE;
      }
   }
}
