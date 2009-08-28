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
package org.gatein.common.concurrent;

/**
 * Start in closed mode and adds an open() method to keep the same valve.
 *
 * @author <a href="mailto:adrian@jboss.org">Adrian Brock</a>
 * @author <a href="mailto:julien@jboss.org">Julien Viet</a>
 * @version $Revision: 5451 $
 */
public class Valve
{

   // Constants -----------------------------------------------------

   /**
    * The valve is open.
    */
   public static final int OPEN = 0;

   /**
    * The valve is in holding state.
    */
   public static final int CLOSING = 1;

   /**
    * The valve is in hold state.
    */
   public static final int CLOSED = 2;

   /**
    * User friendly names.
    */
   private static final String[] STATE_NAMES = {"OPEN","CLOSING","CLOSED"};

   // Attributes ----------------------------------------------------

   /**
    * The state lock.
    */
   protected final Object stateLock = new Object();

   /**
    * The state.
    */
   protected int state;

   /**
    * The invocation count.
    */
   protected int invocations = 0;

   // Static --------------------------------------------------------

   // Constructors --------------------------------------------------

   /**
    * Create a valve in the closed state
    */
   public Valve()
   {
      this(CLOSED);
   }

   /**
    * Create a valve with the give initial state
    *
    * @param state the initial state
    */
   protected Valve(int state)
   {
      this.state = state;
   }

   // Public --------------------------------------------------------

   /**
    * Are we closed?
    *
    * @return true when closing or closed, false otherwise
    */
   public boolean isClosed()
   {
      synchronized (stateLock)
      {
         return state != OPEN;
      }
   }

   /**
    * Invoked before an invocation
    *
    * @return true if allowed entry, false otherwise
    */
   public boolean beforeInvocation()
   {
      synchronized (stateLock)
      {
         if (state != OPEN)
         {
            return false;
         }
         ++invocations;
      }
      return true;
   }

   /**
    * Invoked after an invocation
    */
   public void afterInvocation()
   {
      synchronized (stateLock)
      {
         --invocations;
         stateLock.notifyAll();
      }
   }

   /**
    * Return the state.
    */
   public int getState()
   {
      return state;
   }

   /**
    * How many invocations are held in the valve.
    */
   public int getInvocations()
   {
      return invocations;
   }

   /**
    * Open the valve.
    *
    * @throws IllegalStateException if the valve is not closed
    */
   public void open() throws IllegalStateException
   {
      synchronized (stateLock)
      {
         if (state != CLOSED)
         {
            throw new IllegalStateException("Cannot invoke open() valve in state " + STATE_NAMES[state]);
         }
         state = OPEN;
      }
   }

   /**
    * Invoked before closing.
    *
    * @throws IllegalStateException if the valve is closed
    */
   public void closing() throws IllegalStateException
   {
      closing(0);
   }

   /**
    * Invoked before closing.
    *
    * @throws IllegalStateException if the valve is closed
    */
   public boolean closing(long millis) throws IllegalStateException
   {
      boolean interrupted = false;
      boolean empty = false;
      synchronized (stateLock)
      {
         if (state == CLOSED)
         {
            throw new IllegalStateException("Cannot invoke closing() valve in state " + STATE_NAMES[state]);
         }

         //
         state = CLOSING;

         //
         long finished = -1;
         if (millis > 0)
         {
            finished = System.currentTimeMillis() + millis;
         }

         while (invocations > 0)
         {
            try
            {
               if (finished == -1)
               {
                  stateLock.wait();
               }
               else
               {
                  long time = finished - System.currentTimeMillis();
                  if (time > 0)
                  {
                     stateLock.wait(time);
                  }
                  else
                  {
                     break;
                  }
               }
            }
            catch (InterruptedException e)
            {
               interrupted = true;
            }
         }

         empty = invocations == 0;
      }

      if (interrupted)
      {
         Thread.currentThread().interrupt();
      }

      return empty;
   }

   /**
    * Invoked after closing.
    *
    * @throws IllegalStateException if the valve is not closing
    */
   public void closed() throws IllegalStateException
   {
      synchronized (stateLock)
      {
         if (state != CLOSING)
         {
            throw new IllegalStateException("Cannot invoke close() valve in state " + STATE_NAMES[state]);
         }
         state = CLOSED;
      }
   }

   // Protected -----------------------------------------------------

   // Package Private -----------------------------------------------

   // Private -------------------------------------------------------

   // Inner classes -------------------------------------------------

}
