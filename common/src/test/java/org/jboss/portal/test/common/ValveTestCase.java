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

import java.io.PrintStream;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.BrokenBarrierException;

import junit.framework.TestCase;

import org.gatein.common.concurrent.Valve;

/**
 * @author <a href="mailto:julien@jboss.org">Julien Viet</a>
 * @version $Revision: 5451 $
 */
public class ValveTestCase extends TestCase
{

   public ValveTestCase(String key)
   {
      super(key);
   }

   public static final PrintStream out = System.out;

   public void testTransitions()
   {
      Valve valve = new Valve();
      assertEquals(Valve.CLOSED, valve.getState());
      assertFalse(valve.beforeInvocation());
      try
      {
         valve.closing(10);
         fail("IllegalStateException expected");
      }
      catch (IllegalStateException expected)
      {
      }
      try
      {
         valve.closing();
         fail("IllegalStateException expected");
      }
      catch (IllegalStateException expected)
      {
      }
      try
      {
         valve.closed();
         fail("IllegalStateException expected");
      }
      catch (IllegalStateException expected)
      {
      }

      // Open the valve
      valve.open();
      try
      {
         assertTrue(valve.beforeInvocation());
      }
      finally
      {
         valve.afterInvocation();
      }
      try
      {
         valve.open();
         fail("IllegalStateException expected");
      }
      catch (IllegalStateException e)
      {
      }
      try
      {
         valve.closed();
         fail("IllegalStateException expected");
      }
      catch (IllegalStateException e)
      {
      }


      // Closing the valve
      valve.closing();
      assertEquals(Valve.CLOSING, valve.getState());
      assertFalse(valve.beforeInvocation());
      try
      {
         valve.open();
         fail("IllegalStateException expected");
      }
      catch (IllegalStateException e)
      {
      }
      valve.closing();

      // Close the valve
      valve.closed();
      assertEquals(Valve.CLOSED, valve.getState());
   }

   public void testTimeOut() throws Exception
   {
      final Valve valve = new Valve();
      final CyclicBarrier barrier = new CyclicBarrier(2);

      Thread thread = new Thread()
      {
         public void run()
         {
            try
            {
               assertTrue(valve.beforeInvocation());

               // The valve is enterred
               barrier.await();

               // Wait
               barrier.await();
            }
            catch (BrokenBarrierException e)
            {
               fail("" + e.getMessage());
            }
            catch (InterruptedException ignore)
            {
            }
            finally
            {
               valve.afterInvocation();
               try
               {
                  // Tell the other thread we have finished
                  barrier.await();
               }
               catch (BrokenBarrierException e)
               {
                  fail("" + e.getMessage());
               }
               catch (InterruptedException ignore)
               {
               }
            }
         }
      };

      // Open valve and start thread
      valve.open();
      thread.start();

      // Wait until the thread called beforeInvocation
      barrier.await();

      // Attemtp to close
      assertFalse(valve.closing(100));

      // Check it is in closing state
      assertEquals(Valve.CLOSING, valve.getState());

      // Ask the invoker thread to finish
      barrier.await();

      // Wait until invocation is finished
      barrier.await();

      // Close, no thread are inside
      valve.closing();
      assertEquals(Valve.CLOSING, valve.getState());

      //
      valve.closed();
      assertEquals(Valve.CLOSED, valve.getState());
   }

}
