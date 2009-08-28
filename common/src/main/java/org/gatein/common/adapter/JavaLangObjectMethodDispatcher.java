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
package org.gatein.common.adapter;

/**
 * Implementation of the <code>MethodDispatcher</code> interface for the methods of <code>java.lang.Object</code> which
 * are delegated by a dynamic proxy to the <code>InvocationHandler</code>.
 *
 * @author <a href="mailto:julien@jboss.org">Julien Viet</a>
 * @version $Revision: 7228 $
 */
abstract class JavaLangObjectMethodDispatcher implements MethodDispatcher
{

   /** . */
   protected final JavaLangObjectAdapted target;

   public JavaLangObjectMethodDispatcher(JavaLangObjectAdapted target)
   {
      this.target = target;
   }

   static class ToString extends JavaLangObjectMethodDispatcher
   {

      public ToString(JavaLangObjectAdapted target)
      {
         super(target);
      }

      public Object dispatch(ClassAdaptable adaptable, Object[] args) throws Throwable
      {
         return target.toString(adaptable);
      }
   }

   static class Equals extends JavaLangObjectMethodDispatcher
   {

      public Equals(JavaLangObjectAdapted target)
      {
         super(target);
      }

      public Object dispatch(ClassAdaptable adaptable, Object[] args) throws Throwable
      {
         return Boolean.valueOf(target.equals(adaptable, args[0]));
      }
   }

   static class HashCode extends JavaLangObjectMethodDispatcher
   {

      public HashCode(JavaLangObjectAdapted target)
      {
         super(target);
      }

      public Object dispatch(ClassAdaptable adaptable, Object[] args) throws Throwable
      {
         return new Integer(target.hashCode(adaptable));
      }
   }
}
