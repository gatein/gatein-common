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
import org.gatein.common.path.PathMapper;
import org.gatein.common.path.PathMapperContext;
import org.gatein.common.path.PathMapperResult;
import org.gatein.common.path.SimplePathMapper;

import java.util.HashMap;
import java.util.Map;

/**
 * @author <a href="mailto:julien@jboss.org">Julien Viet</a>
 * @version $Revision: 5448 $
 */
public class PathMapperTestCase extends TestCase
{

   public PathMapperTestCase(String s)
   {
      super(s);
   }

   private PathMapper mapper;

   protected void setUp() throws Exception
   {
      mapper = new SimplePathMapper();
   }

   protected void tearDown() throws Exception
   {
      mapper = null;
   }

   public void testEmptyContext()
   {
      PathMapperContextImpl root = new PathMapperContextImpl(null);

      assertEquals(new PathMapperResult(null, null, null), mapper.map(root, null));
      assertEquals(new PathMapperResult(null, null, null), mapper.map(root, ""));
      assertEquals(new PathMapperResult(null, null, "/"), mapper.map(root, "/"));
      assertEquals(new PathMapperResult(null, null, "/"), mapper.map(root, "//"));

      assertEquals(new PathMapperResult(null, null, "/a"), mapper.map(root, "/a"));
      assertEquals(new PathMapperResult(null, null, "/a/"), mapper.map(root, "/a/"));
      assertEquals(new PathMapperResult(null, null, "/a/b"), mapper.map(root, "/a/b"));
      assertEquals(new PathMapperResult(null, null, "/a/b/"), mapper.map(root, "/a/b/"));
      assertEquals(new PathMapperResult(null, null, "/a/b/c"), mapper.map(root, "/a/b/c"));
   }

   public void testOneChild()
   {
      PathMapperContextImpl root = new PathMapperContextImpl(null);
      PathMapperContextImpl child = new PathMapperContextImpl(null);
      root.addChild("a", child);

      assertEquals(new PathMapperResult(null, null, null), mapper.map(root, null));
      assertEquals(new PathMapperResult(null, null, null), mapper.map(root, ""));
      assertEquals(new PathMapperResult(null, null, "/"), mapper.map(root, "/"));
      assertEquals(new PathMapperResult(null, null, "/"), mapper.map(root, "//"));

      assertEquals(new PathMapperResult(child, "/a", null), mapper.map(root, "/a"));
      assertEquals(new PathMapperResult(child, "/a", "/"), mapper.map(root, "/a/"));
      assertEquals(new PathMapperResult(child, "/a", "/b"), mapper.map(root, "/a/b"));
      assertEquals(new PathMapperResult(child, "/a", "/b/"), mapper.map(root, "/a/b/"));
      assertEquals(new PathMapperResult(child, "/a", "/b/c"), mapper.map(root, "/a/b/c"));

      assertEquals(new PathMapperResult(null, null, "/b"), mapper.map(root, "/b"));
      assertEquals(new PathMapperResult(null, null, "/b/"), mapper.map(root, "/b/"));
      assertEquals(new PathMapperResult(null, null, "/b/c"), mapper.map(root, "/b/c"));
      assertEquals(new PathMapperResult(null, null, "/b/c/"), mapper.map(root, "/b/c/"));
      assertEquals(new PathMapperResult(null, null, "/b/c/d"), mapper.map(root, "/b/c/d"));
   }

   public void testOneChildHavingOneChild()
   {
      PathMapperContextImpl root = new PathMapperContextImpl(null);
      PathMapperContextImpl child = new PathMapperContextImpl(null);
      PathMapperContextImpl childOfChild = new PathMapperContextImpl(null);
      root.addChild("a", child);
      child.addChild("b", childOfChild);

      assertEquals(new PathMapperResult(null, null, null), mapper.map(root, null));
      assertEquals(new PathMapperResult(null, null, null), mapper.map(root, ""));
      assertEquals(new PathMapperResult(null, null, "/"), mapper.map(root, "/"));
      assertEquals(new PathMapperResult(null, null, "/"), mapper.map(root, "//"));

      assertEquals(new PathMapperResult(child, "/a", null), mapper.map(root, "/a"));
      assertEquals(new PathMapperResult(child, "/a", "/"), mapper.map(root, "/a/"));
      assertEquals(new PathMapperResult(childOfChild, "/a/b", null), mapper.map(root, "/a/b"));
      assertEquals(new PathMapperResult(child, "/a", "/b/"), mapper.map(root, "/a/b/"));
      assertEquals(new PathMapperResult(child, "/a", "/b/c"), mapper.map(root, "/a/b/c"));

      assertEquals(new PathMapperResult(null, null, "/b"), mapper.map(root, "/b"));
      assertEquals(new PathMapperResult(null, null, "/b/"), mapper.map(root, "/b/"));
      assertEquals(new PathMapperResult(null, null, "/b/c"), mapper.map(root, "/b/c"));
      assertEquals(new PathMapperResult(null, null, "/b/c/"), mapper.map(root, "/b/c/"));
      assertEquals(new PathMapperResult(null, null, "/b/c/d"), mapper.map(root, "/b/c/d"));
   }

   private static class Context
   {
      private final Map children;

      public Context(Object dflt)
      {
         children = new HashMap();
      }

      public void addChild(String name, Object child)
      {
         children.put(name, child);
      }
   }

   private static class PathMapperContextImpl extends Context implements PathMapperContext
   {
      public PathMapperContextImpl(Object dflt)
      {
         super(dflt);
      }

      public Object getRoot()
      {
         return this;
      }

      public Object getChild(Object parent, String name)
      {
         return ((Context)parent).children.get(name);
      }
   }
}
