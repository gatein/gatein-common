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
package org.jboss.portal.test.common.http;

import junit.framework.TestCase;
import org.gatein.common.http.QueryStringParser;
import org.gatein.common.util.MapBuilder;
import org.gatein.common.util.ParameterMap;

/**
 * @author <a href="mailto:julien@jboss.org">Julien Viet</a>
 * @version $Revision: 1.1 $
 */
public class QueryStringParserTestCase extends TestCase
{

   /** . */
   private final QueryStringParser parser = new QueryStringParser();

   public void testConstructorThrowsIAE()
   {
      try
      {
         new QueryStringParser(null);
         fail("Was expecting an IAE");
      }
      catch (IllegalArgumentException expected)
      {
      }
   }

   public void testParseThrowsIAE()
   {
      try
      {
         QueryStringParser.getInstance().parseQueryString(null);
         fail("Was expecting an IAE");
      }
      catch (IllegalArgumentException expected)
      {
      }
   }

   public void testEmpty()
   {
      assertEquals(MapBuilder.create(new ParameterMap()).get(), parser.parseQueryString(""));
   }

   public void testOneParam()
   {
      assertEquals(MapBuilder.create(new ParameterMap()).put("f", new String[]{""}).get(), parser.parseQueryString("f"));
      assertEquals(MapBuilder.create(new ParameterMap()).put("f", new String[]{""}).get(), parser.parseQueryString("f="));
      assertEquals(MapBuilder.create(new ParameterMap()).put("f", new String[]{"b"}).get(), parser.parseQueryString("f=b"));
      assertEquals(MapBuilder.create(new ParameterMap()).put("f", new String[]{"bar"}).get(), parser.parseQueryString("f=bar"));
      assertEquals(MapBuilder.create(new ParameterMap()).put("foo", new String[]{""}).get(), parser.parseQueryString("foo"));
      assertEquals(MapBuilder.create(new ParameterMap()).put("foo", new String[]{""}).get(), parser.parseQueryString("foo="));
      assertEquals(MapBuilder.create(new ParameterMap()).put("foo", new String[]{"b"}).get(), parser.parseQueryString("foo=b"));
      assertEquals(MapBuilder.create(new ParameterMap()).put("foo", new String[]{"bar"}).get(), parser.parseQueryString("foo=bar"));
   }

   public void testTwoValues()
   {
      assertEquals(MapBuilder.create(new ParameterMap()).put("f", new String[]{"","bar2"}).get(), parser.parseQueryString("f&f=bar2"));
      assertEquals(MapBuilder.create(new ParameterMap()).put("f", new String[]{"","bar2"}).get(), parser.parseQueryString("f=&f=bar2"));
      assertEquals(MapBuilder.create(new ParameterMap()).put("f", new String[]{"b","bar2"}).get(), parser.parseQueryString("f=b&f=bar2"));
      assertEquals(MapBuilder.create(new ParameterMap()).put("f", new String[]{"bar","bar2"}).get(), parser.parseQueryString("f=bar&f=bar2"));
      assertEquals(MapBuilder.create(new ParameterMap()).put("foo", new String[]{"","bar2"}).get(), parser.parseQueryString("foo&foo=bar2"));
      assertEquals(MapBuilder.create(new ParameterMap()).put("foo", new String[]{"","bar2"}).get(), parser.parseQueryString("foo=&foo=bar2"));
      assertEquals(MapBuilder.create(new ParameterMap()).put("foo", new String[]{"b","bar2"}).get(), parser.parseQueryString("foo=b&foo=bar2"));
      assertEquals(MapBuilder.create(new ParameterMap()).put("foo", new String[]{"bar","bar2"}).get(), parser.parseQueryString("foo=bar&foo=bar2"));

      assertEquals(MapBuilder.create(new ParameterMap()).put("f", new String[]{"bar2",""}).get(), parser.parseQueryString("f=bar2&f"));
      assertEquals(MapBuilder.create(new ParameterMap()).put("f", new String[]{"bar2",""}).get(), parser.parseQueryString("f=bar2&f="));
      assertEquals(MapBuilder.create(new ParameterMap()).put("f", new String[]{"bar2","b"}).get(), parser.parseQueryString("f=bar2&f=b"));
      assertEquals(MapBuilder.create(new ParameterMap()).put("f", new String[]{"bar2","bar"}).get(), parser.parseQueryString("f=bar2&f=bar"));
      assertEquals(MapBuilder.create(new ParameterMap()).put("foo", new String[]{"bar2",""}).get(), parser.parseQueryString("foo=bar2&foo"));
      assertEquals(MapBuilder.create(new ParameterMap()).put("foo", new String[]{"bar2",""}).get(), parser.parseQueryString("foo=bar2&foo="));
      assertEquals(MapBuilder.create(new ParameterMap()).put("foo", new String[]{"bar2","b"}).get(), parser.parseQueryString("foo=bar2&foo=b"));
      assertEquals(MapBuilder.create(new ParameterMap()).put("foo", new String[]{"bar2","bar"}).get(), parser.parseQueryString("foo=bar2&foo=bar"));
   }

   public void testEncodedValue()
   {
      assertEquals(MapBuilder.create(new ParameterMap()).put("foo", new String[]{" "}).get(), parser.parseQueryString("foo=+"));
      assertEquals(MapBuilder.create(new ParameterMap()).put("foo", new String[]{"."}).get(), parser.parseQueryString("foo=."));
      assertEquals(MapBuilder.create(new ParameterMap()).put("foo", new String[]{"-"}).get(), parser.parseQueryString("foo=-"));
      assertEquals(MapBuilder.create(new ParameterMap()).put("foo", new String[]{"*"}).get(), parser.parseQueryString("foo=*"));
      assertEquals(MapBuilder.create(new ParameterMap()).put("foo", new String[]{"_"}).get(), parser.parseQueryString("foo=_"));
      assertEquals(MapBuilder.create(new ParameterMap()).put("foo", new String[]{"/"}).get(), parser.parseQueryString("foo=%2F"));
      assertEquals(MapBuilder.create(new ParameterMap()).put("foo", new String[]{"/"}).get(), parser.parseQueryString("foo=/"));
   }

   public void testMalformedValue()
   {
      assertEquals(MapBuilder.create(new ParameterMap()).get(), parser.parseQueryString("foo=%2"));
      assertEquals(MapBuilder.create(new ParameterMap()).put("foo", new String[]{""}).get(), parser.parseQueryString("foo&foo=%2"));
      assertEquals(MapBuilder.create(new ParameterMap()).put("foo", new String[]{"bar"}).get(), parser.parseQueryString("foo=bar&foo=%2"));
      assertEquals(MapBuilder.create(new ParameterMap()).put("foo", new String[]{""}).get(), parser.parseQueryString("foo=%2&foo"));
   }

   public void testTwoParams()
   {
      assertEquals(MapBuilder.create(new ParameterMap()).put("x", new String[]{"y"}).put("f", new String[]{""}).get(), parser.parseQueryString("x=y&f"));
      assertEquals(MapBuilder.create(new ParameterMap()).put("x", new String[]{"y"}).put("f", new String[]{""}).get(), parser.parseQueryString("x=y&f="));
      assertEquals(MapBuilder.create(new ParameterMap()).put("x", new String[]{"y"}).put("f", new String[]{"b"}).get(), parser.parseQueryString("x=y&f=b"));
      assertEquals(MapBuilder.create(new ParameterMap()).put("x", new String[]{"y"}).put("f", new String[]{"bar"}).get(), parser.parseQueryString("x=y&f=bar"));
      assertEquals(MapBuilder.create(new ParameterMap()).put("x", new String[]{"y"}).put("foo", new String[]{""}).get(), parser.parseQueryString("x=y&foo"));
      assertEquals(MapBuilder.create(new ParameterMap()).put("x", new String[]{"y"}).put("foo", new String[]{""}).get(), parser.parseQueryString("x=y&foo="));
      assertEquals(MapBuilder.create(new ParameterMap()).put("x", new String[]{"y"}).put("foo", new String[]{"b"}).get(), parser.parseQueryString("x=y&foo=b"));
      assertEquals(MapBuilder.create(new ParameterMap()).put("x", new String[]{"y"}).put("foo", new String[]{"bar"}).get(), parser.parseQueryString("x=y&foo=bar"));
   }

   public void testValueContainingEqual()
   {
      assertEquals(MapBuilder.create(new ParameterMap()).put("f", new String[]{"b=j"}).get(), parser.parseQueryString("f=b=j"));
      assertEquals(MapBuilder.create(new ParameterMap()).put("f", new String[]{"bar=j"}).get(), parser.parseQueryString("f=bar=j"));
      assertEquals(MapBuilder.create(new ParameterMap()).put("f", new String[]{"b=juu"}).get(), parser.parseQueryString("f=b=juu"));
      assertEquals(MapBuilder.create(new ParameterMap()).put("f", new String[]{"bar=juu"}).get(), parser.parseQueryString("f=bar=juu"));
      assertEquals(MapBuilder.create(new ParameterMap()).put("foo", new String[]{"b=j"}).get(), parser.parseQueryString("foo=b=j"));
      assertEquals(MapBuilder.create(new ParameterMap()).put("foo", new String[]{"bar=j"}).get(), parser.parseQueryString("foo=bar=j"));
      assertEquals(MapBuilder.create(new ParameterMap()).put("foo", new String[]{"b=juu"}).get(), parser.parseQueryString("foo=b=juu"));
      assertEquals(MapBuilder.create(new ParameterMap()).put("foo", new String[]{"bar=juu"}).get(), parser.parseQueryString("foo=bar=juu"));
   }

   public void testInvalidChunck()
   {
      assertEquals(MapBuilder.create(new ParameterMap()).get(), parser.parseQueryString("="));
      assertEquals(MapBuilder.create(new ParameterMap()).get(), parser.parseQueryString("=x"));
      assertEquals(MapBuilder.create(new ParameterMap()).get(), parser.parseQueryString("=x="));
      assertEquals(MapBuilder.create(new ParameterMap()).get(), parser.parseQueryString("=x=y"));
   }

   public void testInvalidChunckWithAmpersand()
   {
      //
      assertEquals(MapBuilder.create(new ParameterMap()).get(), parser.parseQueryString("=&"));
      assertEquals(MapBuilder.create(new ParameterMap()).get(), parser.parseQueryString("=x&"));
      assertEquals(MapBuilder.create(new ParameterMap()).get(), parser.parseQueryString("=x=&"));
      assertEquals(MapBuilder.create(new ParameterMap()).get(), parser.parseQueryString("=x=y&"));

      //
      assertEquals(MapBuilder.create(new ParameterMap()).put("f", new String[]{""}).get(), parser.parseQueryString("=&f"));
      assertEquals(MapBuilder.create(new ParameterMap()).put("f", new String[]{""}).get(), parser.parseQueryString("=x&f"));
      assertEquals(MapBuilder.create(new ParameterMap()).put("f", new String[]{""}).get(), parser.parseQueryString("=x=&f"));
      assertEquals(MapBuilder.create(new ParameterMap()).put("f", new String[]{""}).get(), parser.parseQueryString("=x=y&f"));

      //
      assertEquals(MapBuilder.create(new ParameterMap()).put("f", new String[]{""}).get(), parser.parseQueryString("=&f="));
      assertEquals(MapBuilder.create(new ParameterMap()).put("f", new String[]{""}).get(), parser.parseQueryString("=x&f="));
      assertEquals(MapBuilder.create(new ParameterMap()).put("f", new String[]{""}).get(), parser.parseQueryString("=x=&f="));
      assertEquals(MapBuilder.create(new ParameterMap()).put("f", new String[]{""}).get(), parser.parseQueryString("=x=y&f="));

      //
      assertEquals(MapBuilder.create(new ParameterMap()).put("f", new String[]{"b"}).get(), parser.parseQueryString("=&f=b"));
      assertEquals(MapBuilder.create(new ParameterMap()).put("f", new String[]{"b"}).get(), parser.parseQueryString("=x&f=b"));
      assertEquals(MapBuilder.create(new ParameterMap()).put("f", new String[]{"b"}).get(), parser.parseQueryString("=x=&f=b"));
      assertEquals(MapBuilder.create(new ParameterMap()).put("f", new String[]{"b"}).get(), parser.parseQueryString("=x=y&f=b"));

      //
      assertEquals(MapBuilder.create(new ParameterMap()).put("foo", new String[]{""}).get(), parser.parseQueryString("=&foo"));
      assertEquals(MapBuilder.create(new ParameterMap()).put("foo", new String[]{""}).get(), parser.parseQueryString("=x&foo"));
      assertEquals(MapBuilder.create(new ParameterMap()).put("foo", new String[]{""}).get(), parser.parseQueryString("=x=&foo"));
      assertEquals(MapBuilder.create(new ParameterMap()).put("foo", new String[]{""}).get(), parser.parseQueryString("=x=y&foo"));

      //
      assertEquals(MapBuilder.create(new ParameterMap()).put("foo", new String[]{""}).get(), parser.parseQueryString("=&foo="));
      assertEquals(MapBuilder.create(new ParameterMap()).put("foo", new String[]{""}).get(), parser.parseQueryString("=x&foo="));
      assertEquals(MapBuilder.create(new ParameterMap()).put("foo", new String[]{""}).get(), parser.parseQueryString("=x=&foo="));
      assertEquals(MapBuilder.create(new ParameterMap()).put("foo", new String[]{""}).get(), parser.parseQueryString("=x=y&foo="));

      //
      assertEquals(MapBuilder.create(new ParameterMap()).put("foo", new String[]{"b"}).get(), parser.parseQueryString("=&foo=b"));
      assertEquals(MapBuilder.create(new ParameterMap()).put("foo", new String[]{"b"}).get(), parser.parseQueryString("=x&foo=b"));
      assertEquals(MapBuilder.create(new ParameterMap()).put("foo", new String[]{"b"}).get(), parser.parseQueryString("=x=&foo=b"));
      assertEquals(MapBuilder.create(new ParameterMap()).put("foo", new String[]{"b"}).get(), parser.parseQueryString("=x=y&foo=b"));

      //
      assertEquals(MapBuilder.create(new ParameterMap()).put("foo", new String[]{"bar"}).get(), parser.parseQueryString("=&foo=bar"));
      assertEquals(MapBuilder.create(new ParameterMap()).put("foo", new String[]{"bar"}).get(), parser.parseQueryString("=x&foo=bar"));
      assertEquals(MapBuilder.create(new ParameterMap()).put("foo", new String[]{"bar"}).get(), parser.parseQueryString("=x=&foo=bar"));
      assertEquals(MapBuilder.create(new ParameterMap()).put("foo", new String[]{"bar"}).get(), parser.parseQueryString("=x=y&foo=bar"));
   }
}
