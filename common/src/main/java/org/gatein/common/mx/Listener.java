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
package org.gatein.common.mx;

import javax.management.InstanceNotFoundException;
import javax.management.ListenerNotFoundException;
import javax.management.MBeanServer;
import javax.management.Notification;
import javax.management.NotificationFilter;
import javax.management.NotificationListener;
import javax.management.ObjectName;

import org.apache.log4j.Logger;

/**
 * Helper class that filter and listen notifications and help to registration.
 *
 * @author <a href="mailto:julien@jboss.org">Julien Viet</a>
 * @version $Revision: 5451 $
 */
public class Listener implements NotificationFilter, NotificationListener
{

   /** The serialVersionUID */
   private static final long serialVersionUID = 444957117668223654L;
   protected final MBeanServer server;
   protected final Logger log;

   public Listener(MBeanServer server)
   {
      if (server == null)
      {
         throw new IllegalArgumentException("server must not be null");
      }
      this.server = server;
      this.log = Logger.getLogger(getClass());
   }

   public void register(ObjectName broadcaster)
   {
      register(broadcaster, null);
   }

   public void register(ObjectName broadcaster, Object handback)
   {
      try
      {
         if (broadcaster == null)
         {
            throw new IllegalArgumentException("Broadcaster is null");
         }

         log.debug("Register notifications on MBean " + broadcaster.getCanonicalName());
         server.addNotificationListener(broadcaster, this, this, handback);
      }
      catch (InstanceNotFoundException e)
      {
         throw new ListenerException(e);
      }
   }

   public void unregister(ObjectName broadcaster)
   {
      try
      {
         if (broadcaster == null)
         {
            throw new IllegalArgumentException("Broadcaster is null");
         }

         log.debug("Unregister notifications on MBean " + broadcaster.getCanonicalName());
         server.removeNotificationListener(broadcaster, this);
      }
      catch (InstanceNotFoundException e)
      {
         throw new ListenerException(e);
      }
      catch (ListenerNotFoundException e)
      {
         throw new ListenerException(e);
      }
   }

   /**
    * Returns true by default.
    */
   public boolean isNotificationEnabled(Notification notification)
   {
      return true;
   }

   /**
    * Does not perform anything by default.
    */
   public void handleNotification(Notification notification, Object handback)
   {
   }
}
