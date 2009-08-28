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
package org.jboss.portal.test.common.i18n;

import junit.framework.TestCase;
import org.gatein.common.i18n.BundleName;

/**
 * @author <a href="mailto:julien@jboss.org">Julien Viet</a>
 * @version $Revision: 1.1 $
 */
public class BundleNameParserTestCase extends TestCase
{

   /** . */
   private final BundleName.Parser parser = new BundleName.Parser();


   private static final Test[] tests = {
      new Test("_", null),
      new Test("__", null),
      new Test("___", null),

      //
      new Test("a", new BundleName("a")),
      new Test("a_b", new BundleName("a", "b")),
      new Test("a_b_c", new BundleName("a", "b", "c")),
      new Test("a__b", new BundleName("a", "", "b")),
      new Test("a___b", new BundleName("a", "", "", "b")),
      new Test("a__b_c", new BundleName("a", "", "b", "c")),
      new Test("a_b__c", new BundleName("a", "b", "", "c")),
      new Test("a_b_c_d", new BundleName("a", "b", "c", "d")),

      //
      new Test("", new BundleName("")),
      new Test("_b", new BundleName("", "b")),
      new Test("_b_c", new BundleName("", "b", "c")),
      new Test("__b", new BundleName("", "", "b")),
      new Test("___b", new BundleName("", "", "", "b")),
      new Test("__b_c", new BundleName("", "", "b", "c")),
      new Test("_b__c", new BundleName("", "b", "", "c")),
      new Test("_b_c_d", new BundleName("", "b", "c", "d")),
   };

   public void testA()
   {
      for (int i = 0; i < tests.length; i++)
      {
         Test test = tests[i];
         BundleName name = parser.parse(test.input, 0, test.input.length());
         if (name == null)
         {
            assertNull(test.input, test.expectedName);
         }
         else
         {
            assertNotNull(test.input, test.expectedName);
            assertEquals(test.input, test.expectedName.getBaseName(), name.getBaseName());
            assertEquals(test.input, test.expectedName.getLanguage(), name.getLanguage());
            assertEquals(test.input, test.expectedName.getCountry(), name.getCountry());
            assertEquals(test.input, test.expectedName.getVariant(), name.getVariant());
         }
      }
   }


   private static class Test
   {

      /** . */
      private String input;

      /** . */
      private BundleName expectedName;

      public Test(String input, BundleName expectedName)
      {
         this.input = input;
         this.expectedName = expectedName;
      }
   }
}
