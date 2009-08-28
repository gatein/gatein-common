/******************************************************************************
 * JBoss, a division of Red Hat                                               *
 * Copyright 2008, Red Hat Middleware, LLC, and individual                    *
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
package org.jboss.portal.test.common.net.media;

import junit.framework.TestCase;
import org.gatein.common.net.media.MediaType;
import org.gatein.common.net.media.TypeDef;
import org.gatein.common.net.media.SubtypeDef;
import org.gatein.common.net.media.MediaTypeMap;
import org.gatein.common.net.media.MediaTypeMapImpl;
import org.gatein.common.util.Tools;

import java.util.Set;

/**
 * @author <a href="mailto:julien@jboss-portal.org">Julien Viet</a>
 * @version $Revision: 630 $
 */
public class MediaTypeMapTestCase extends TestCase
{

   private final String v1 = "v1";
   private final String v2 = "v2";
   private final String v3 = "v3";
   private final String v4 = "v4";
   private final String v5 = "v5";
   private final String v6 = "v6";

   private final MediaType TEXT_HTML = MediaType.TEXT_HTML;
   private final TypeDef TEXT = TypeDef.TEXT;
   private final MediaType TEXT_CSS = MediaType.TEXT_CSS;
   private final MediaType TEXT_JAVASCRIPT = MediaType.TEXT_JAVASCRIPT;
   private final MediaType IMAGE_PNG = MediaType.create(TypeDef.IMAGE, SubtypeDef.create("png"));
   private final TypeDef IMAGE = TypeDef.IMAGE;

   private final String[] values = {v1,v2,v3,v4,v5,v6};

   private void testValidity(MediaTypeMap map, int index)
   {
      assertEquals(all[index], map.getValues());

      //
      assertEquals(get[index][0], map.get(TEXT_HTML));
      assertEquals(get[index][1], map.get(TEXT));
      assertEquals(get[index][2], map.get(TEXT_CSS));
      assertEquals(get[index][3], map.get(TEXT_JAVASCRIPT));
      assertEquals(get[index][4], map.get(IMAGE_PNG));
      assertEquals(get[index][5], map.get(IMAGE));

      //
      assertEquals(resolve[index][0], map.resolve(TEXT_HTML));
      assertEquals(resolve[index][1], map.resolve(TEXT));
      assertEquals(resolve[index][2], map.resolve(TEXT_CSS));
      assertEquals(resolve[index][3], map.resolve(TEXT_JAVASCRIPT));
      assertEquals(resolve[index][4], map.resolve(IMAGE_PNG));
      assertEquals(resolve[index][5], map.resolve(IMAGE));

      for (Object value : values)
      {
         assertEquals("Expected " + value + " to be contained in the map", map.get(TEXT_HTML).contains(value), map.contains(TEXT_HTML, value));
         assertEquals("Expected " + value + " to be contained in the map", map.get(TEXT).contains(value), map.contains(TEXT, value));
         assertEquals("Expected " + value + " to be contained in the map", map.get(TEXT_CSS).contains(value), map.contains(TEXT_CSS, value));
         assertEquals("Expected " + value + " to be contained in the map", map.get(TEXT_JAVASCRIPT).contains(value), map.contains(TEXT_JAVASCRIPT, value));
         assertEquals("Expected " + value + " to be contained in the map", map.get(IMAGE_PNG).contains(value), map.contains(IMAGE_PNG, value));
         assertEquals("Expected " + value + " to be contained in the map", map.get(IMAGE).contains(value), map.contains(IMAGE, value));

         //
         assertEquals("Expected " + value + " to be contained in the map", map.resolve(TEXT_HTML).contains(value), map.isSupported(TEXT_HTML, value));
         assertEquals("Expected " + value + " to be contained in the map", map.resolve(TEXT).contains(value), map.isSupported(TEXT, value));
         assertEquals("Expected " + value + " to be contained in the map", map.resolve(TEXT_CSS).contains(value), map.isSupported(TEXT_CSS, value));
         assertEquals("Expected " + value + " to be contained in the map", map.resolve(TEXT_JAVASCRIPT).contains(value), map.isSupported(TEXT_JAVASCRIPT, value));
         assertEquals("Expected " + value + " to be contained in the map", map.resolve(IMAGE_PNG).contains(value), map.isSupported(IMAGE_PNG, value));
         assertEquals("Expected " + value + " to be contained in the map", map.resolve(IMAGE).contains(value), map.isSupported(IMAGE, value));
      }

      assertEquals(mediaTypes[index], map.getMediaTypes());
      assertEquals(types[index], map.getTypes());
   }

   //

   private final Set all1 = Tools.toSet();
   private final Set mediaTypes1 = Tools.toSet(TEXT_HTML);
   private final Set types1 = Tools.toSet();
   private final Set[] get1 = {
      Tools.toSet(v1),
      Tools.toSet(),
      Tools.toSet(),
      Tools.toSet(),
      Tools.toSet(),
      Tools.toSet(),
   };
   private final Set[] resolve1 = {
      Tools.toSet(v1),
      Tools.toSet(),
      Tools.toSet(),
      Tools.toSet(),
      Tools.toSet(),
      Tools.toSet()
   };

   public void testPut1()
   {
      MediaTypeMapImpl map = new MediaTypeMapImpl();
      map.put(TEXT_HTML, v1);
      testValidity(map, 0);
   }

   //

   private final Set mediaTypes2 = Tools.toSet(TEXT_HTML);
   private final Set types2 = Tools.toSet(TEXT);
   private final Set all2 = Tools.toSet();
   private final Set[] get2 = {
      Tools.toSet(v1, v2),
      Tools.toSet(v2),
      Tools.toSet(),
      Tools.toSet(),
      Tools.toSet(),
      Tools.toSet(),
   };
   private final Set[] resolve2 = {
      Tools.toSet(v1, v2),
      Tools.toSet(v2),
      Tools.toSet(v2),
      Tools.toSet(v2),
      Tools.toSet(),
      Tools.toSet()
   };

   public void testPut2()
   {
      MediaTypeMapImpl map = new MediaTypeMapImpl();
      map.put(TEXT_HTML, v1);
      map.put(TEXT, v2);
      testValidity(map, 1);
   }

   //

   private final Set mediaTypes3 = Tools.toSet(TEXT_HTML);

   private final Set types3 = Tools.toSet(TEXT);

   private final Set all3 = Tools.toSet(v3);

   private final Set[] get3 = {
      Tools.toSet(v1, v2, v3),
      Tools.toSet(v2, v3),
      Tools.toSet(),
      Tools.toSet(),
      Tools.toSet(),
      Tools.toSet(),
   };

   private final Set[] resolve3 = {
      Tools.toSet(v1, v2, v3),
      Tools.toSet(v2, v3),
      Tools.toSet(v2, v3),
      Tools.toSet(v2, v3),
      Tools.toSet(v3),
      Tools.toSet(v3)
   };

   public void testPut3()
   {
      MediaTypeMapImpl map = new MediaTypeMapImpl();
      map.put(TEXT_HTML, v1);
      map.put(TEXT, v2);
      map.put(v3);
      testValidity(map, 2);
   }

   //

   private final Set mediaTypes4 = Tools.toSet(TEXT_HTML, TEXT_CSS);

   private final Set types4 = Tools.toSet(TEXT);

   private final Set all4= Tools.toSet(v3);

   private final Set[] get4 = {
      Tools.toSet(v1, v2, v3),
      Tools.toSet(v2, v3),
      Tools.toSet(v2, v3, v4),
      Tools.toSet(),
      Tools.toSet(),
      Tools.toSet(),
      Tools.toSet(v3)
   };

   private final Set[] resolve4 = {
      Tools.toSet(v1, v2, v3),
      Tools.toSet(v2, v3),
      Tools.toSet(v2, v3, v4),
      Tools.toSet(v2, v3),
      Tools.toSet(v3),
      Tools.toSet(v3)
   };

   public void testPut4()
   {
      MediaTypeMapImpl map = new MediaTypeMapImpl();
      map.put(TEXT_HTML, v1);
      map.put(TEXT, v2);
      map.put(v3);
      map.put(TEXT_CSS, v4);
      testValidity(map, 3);
   }

   //

   private final Set mediaTypes5 = Tools.toSet(TEXT_HTML, TEXT_CSS);

   private final Set types5 = Tools.toSet(TEXT);

   private final Set all5 = Tools.toSet(v3);

   private final Set[] get5 = {
      Tools.toSet(v1, v2, v3, v5),
      Tools.toSet(v2, v3, v5),
      Tools.toSet(v2, v3, v4, v5),
      Tools.toSet(),
      Tools.toSet(),
      Tools.toSet(),
   };

   private final Set[] resolve5 = {
      Tools.toSet(v1, v2, v3, v5),
      Tools.toSet(v2, v3, v5),
      Tools.toSet(v2, v3, v4, v5),
      Tools.toSet(v2, v3, v5),
      Tools.toSet(v3),
      Tools.toSet(v3)
   };

   public void testPut5()
   {
      MediaTypeMapImpl map = new MediaTypeMapImpl();
      map.put(TEXT_HTML, v1);
      map.put(TEXT, v2);
      map.put(v3);
      map.put(TEXT_CSS, v4);
      map.put(TEXT, v5);
      testValidity(map, 4);
   }

   //

   private final Set mediaTypes6 = Tools.toSet(TEXT_HTML, TEXT_CSS);

   private final Set types6 = Tools.toSet(TEXT);

   private final Set all6 = Tools.toSet(v3, v6);

   private final Set[] get6 = {
      Tools.toSet(v1, v2, v3, v5, v6),
      Tools.toSet(v2, v3, v5, v6),
      Tools.toSet(v2, v3, v4, v5, v6),
      Tools.toSet(),
      Tools.toSet(),
      Tools.toSet(),
   };

   private final Set[] resolve6 = {
      Tools.toSet(v1, v2, v3, v5, v6),
      Tools.toSet(v2, v3, v5, v6),
      Tools.toSet(v2, v3, v4, v5, v6),
      Tools.toSet(v2, v3, v5, v6),
      Tools.toSet(v3, v6),
      Tools.toSet(v3, v6)
   };

   public void testPut6()
   {
      MediaTypeMapImpl map = new MediaTypeMapImpl();
      map.put(TEXT_HTML, v1);
      map.put(TEXT, v2);
      map.put(v3);
      map.put(TEXT_CSS, v4);
      map.put(TEXT, v5);
      map.put(v6);
      testValidity(map, 5);
   }

   private Set[] types = {types1,types2,types3,types4,types5,types6};
   private Set[] mediaTypes = {mediaTypes1,mediaTypes2,mediaTypes3,mediaTypes4,mediaTypes5,mediaTypes6};
   private Set[] all = {all1,all2,all3,all4,all5,all6};
   private Set[][] get = {get1,get2,get3,get4,get5,get6};
   private Set[][] resolve = {resolve1,resolve2,resolve3,resolve4,resolve5,resolve6};

   public void interfaceThrowsIAE()
   {
      try
      {
         new MediaTypeMapImpl().get((MediaType)null);
         fail();
      }
      catch (IllegalArgumentException e)
      {
      }
      try
      {
         new MediaTypeMapImpl().get((TypeDef)null);
         fail();
      }
      catch (IllegalArgumentException e)
      {
      }
      try
      {
         new MediaTypeMapImpl().resolve((MediaType)null);
         fail();
      }
      catch (IllegalArgumentException e)
      {
      }
      try
      {
         new MediaTypeMapImpl().resolve((TypeDef)null);
         fail();
      }
      catch (IllegalArgumentException e)
      {
      }
      try
      {
         new MediaTypeMapImpl().isSupported((MediaType)null);
         fail();
      }
      catch (IllegalArgumentException e)
      {
      }
      try
      {
         new MediaTypeMapImpl().isSupported(TEXT, null);
         fail();
      }
      catch (IllegalArgumentException e)
      {
      }
      try
      {
         new MediaTypeMapImpl().isSupported(TEXT_HTML, null);
         fail();
      }
      catch (IllegalArgumentException e)
      {
      }
      try
      {
         new MediaTypeMapImpl().isSupported((TypeDef)null);
         fail();
      }
      catch (IllegalArgumentException e)
      {
      }
      try
      {
         new MediaTypeMapImpl().isSupported((Object)null);
         fail();
      }
      catch (IllegalArgumentException e)
      {
      }
      try
      {
         new MediaTypeMapImpl().contains((MediaType)null);
         fail();
      }
      catch (IllegalArgumentException e)
      {
      }
      try
      {
         new MediaTypeMapImpl().contains(TEXT, null);
         fail();
      }
      catch (IllegalArgumentException e)
      {
      }
      try
      {
         new MediaTypeMapImpl().contains(TEXT_HTML, null);
         fail();
      }
      catch (IllegalArgumentException e)
      {
      }
      try
      {
         new MediaTypeMapImpl().contains((TypeDef)null);
         fail();
      }
      catch (IllegalArgumentException e)
      {
      }
      try
      {
         new MediaTypeMapImpl().contains((Object)null);
         fail();
      }
      catch (IllegalArgumentException e)
      {
      }
   }

   public void testAddUsingPattern1()
   {
      MediaTypeMapImpl map = new MediaTypeMapImpl();
      map.put("text/html", v1);
      assertEquals(Tools.toSet(v1), map.get(MediaType.TEXT_HTML));
   }

   public void testAddUsingPattern2()
   {
      MediaTypeMapImpl map = new MediaTypeMapImpl();
      map.put("text/*", v1);
      assertEquals(Tools.toSet(v1), map.get(TEXT));
   }

   public void testAddUsingPattern3()
   {
      MediaTypeMapImpl map = new MediaTypeMapImpl();
      map.put("*/*", v1);
      assertEquals(Tools.toSet(v1), map.getValues());
   }

   private void testAddUsingPatternThrowsIAE(String pattern, String value)
   {
      MediaTypeMapImpl map = new MediaTypeMapImpl();
      try
      {
         map.put(pattern, value);
         fail() ;
      }
      catch (IllegalArgumentException ignore)
      {
      }
   }

   public void testAddUsingPatternThrowsIAE4()
   {
      testAddUsingPatternThrowsIAE(null, v1);
   }

   public void testAddUsingPattern5()
   {
      testAddUsingPatternThrowsIAE(null, null);
   }

   public void testAddUsingPattern6()
   {
      testAddUsingPatternThrowsIAE("text/html", null);
   }

   public void testAddUsingPattern7()
   {
      testAddUsingPatternThrowsIAE("", v1);
   }

   public void testAddUsingPattern8()
   {
      testAddUsingPatternThrowsIAE("text", v1);
   }

   public void testAddUsingPattern9()
   {
      testAddUsingPatternThrowsIAE("foo/html", v1);
   }

   public void testAddUsingPattern10()
   {
      testAddUsingPatternThrowsIAE("text/", v1);
   }

   public void testAddUsingPattern11()
   {
      testAddUsingPatternThrowsIAE("/html", v1);
   }
}
