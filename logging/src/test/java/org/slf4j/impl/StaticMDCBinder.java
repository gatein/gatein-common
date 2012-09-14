/*
 * Copyright (C) 2009 eXo Platform SAS.
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
package org.slf4j.impl;

import junit.framework.AssertionFailedError;
import org.gatein.common.logging.Logger;
import org.slf4j.spi.MDCAdapter;

/**
 * @author <a href="mailto:julien.viet@exoplatform.com">Julien Viet</a>
 * @version $Revision$
 */
public class StaticMDCBinder
{

   /** . */
   public static final StaticMDCBinder SINGLETON = new StaticMDCBinder();

   /** . */
   private static final Class<? extends MDCAdapter> MDCAdapter_CLASS;

   static
   {
      try
      {
         switch (Logger.LOGGER)
         {
            case Logger.SLF_1_5:
               MDCAdapter_CLASS = (Class<MDCAdapter>)MDCAdapter.class.getClassLoader().loadClass("org.slf4j.helpers.NOPMakerAdapter");
               break;
            case Logger.SLF_1_6:
               MDCAdapter_CLASS = (Class<MDCAdapter>)MDCAdapter.class.getClassLoader().loadClass("org.slf4j.helpers.NOPMDCAdapter");
               break;
            default:
               throw new Exception("Bad SLF4J");
         }
      }
      catch (Exception e)
      {
         throw new AssertionError(e);
      }
   }

   public MDCAdapter getMDCA()
   {
      try
      {
         return MDCAdapter_CLASS.newInstance();
      }
      catch (Exception e)
      {
         AssertionError afe = new AssertionError("Could not instantiate MDCAdapter");
         afe.initCause(e);
         throw afe;
      }
   }

   public String getMDCAdapterClassStr()
   {
      return MDCAdapter_CLASS.getName();
   }
}
