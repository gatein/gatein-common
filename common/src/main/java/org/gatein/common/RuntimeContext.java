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

/**
 * @author <a href="mailto:chris.laprun@jboss.com">Chris Laprun</a>
 * @version $Revision$
 */
public class RuntimeContext
{
   public static final String GATEIN_RUNTIME_CONTEXT_PROP_NAME = "gatein.runtime.context";

   private final RunningEnvironment ENV;

   /**
    * Only here for testing purposes... :(
    *
    * @param runtimeContextValue
    */
   RuntimeContext(String runtimeContextValue)
   {
      RunningEnvironment runningEnvironment = RunningEnvironment.UNKNOWN;
      try
      {
         runningEnvironment = RunningEnvironment.valueOf(runtimeContextValue);
      }
      catch (IllegalArgumentException e)
      {
         // nothing to do
      }

      ENV = runningEnvironment;
   }

   private RuntimeContext()
   {
      this(System.getProperty(GATEIN_RUNTIME_CONTEXT_PROP_NAME, RunningEnvironment.gtn.getName()));
   }

   // On-demand class holder Singleton pattern (multi-thread safe)

   private static final class InstanceHolder
   {
      public static final RuntimeContext instance = new RuntimeContext();
   }

   public static RuntimeContext getInstance()
   {
      return InstanceHolder.instance;
   }

   public enum RunningEnvironment
   {
      epp("epp"), gtn("gtn"), plf("plf"), UNKNOWN("__unknown__");

      RunningEnvironment(String name)
      {
         this.name = name;
      }

      public String getName()
      {
         return name;
      }

      private final String name;
   }

   public boolean isRunningIn(RunningEnvironment env)
   {
      return ENV.equals(env);
   }

   public boolean isRunningInUnknownEnvironment()
   {
      return RunningEnvironment.UNKNOWN.equals(ENV);
   }
}
