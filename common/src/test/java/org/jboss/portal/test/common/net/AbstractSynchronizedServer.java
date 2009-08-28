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
package org.jboss.portal.test.common.net;

import org.apache.log4j.Logger;
import org.gatein.common.junit.ExtendedAssert;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicInteger;
import java.io.BufferedInputStream;
import java.io.LineNumberReader;
import java.io.InputStreamReader;
import java.io.BufferedOutputStream;
import java.io.OutputStreamWriter;

/**
 * @author <a href="mailto:julien@jboss.org">Julien Viet</a>
 * @version $Revision: 1.1 $
 */
public abstract class AbstractSynchronizedServer extends AbstractServer
{

   /** . */
   private static final Logger log = Logger.getLogger(AbstractSynchronizedServer.class);

   /** . */
   private final Object lock = new Object();

   /** . */
   private final AtomicInteger b = new AtomicInteger(0);

   /** . */
   private Throwable failure;

   protected AbstractSynchronizedServer(int port)
   {
      super(port);
   }

   protected void run(ServerSocket server) throws Exception
   {
      synchronized (lock)
      {
         b.set(1);
         lock.notifyAll();
      }

      //
      log.debug("Ready for accept");

      //
      try
      {
         doServer(server);
      }
      catch (Throwable throwable)
      {
         failure = throwable;
      }

      //
      synchronized (lock)
      {
         lock.wait();
      }

      //
      log.debug("Shutting down");
   }

   protected abstract void doServer(ServerSocket server) throws Exception;

   protected abstract void doClient() throws Exception;

   public void performInteraction() throws Exception
   {
      try
      {
         start();

         // Wait until the we know the server will accept
         synchronized (lock)
         {
            while (b.get() != 1)
            {
               lock.wait();
            }
         }

         // Perform client action
         doClient();
      }
      finally
      {
         synchronized (lock)
         {
            lock.notify();
         }
         stop();
      }

      //
      if (failure != null)
      {
         log.error("The server reported a failure", failure);

         ExtendedAssert.fail("The server reported a failure");
      }
   }

   public static abstract class AbstractTimeoutServer extends AbstractSynchronizedServer
   {

      protected AbstractTimeoutServer(int port)
      {
         super(port);
      }

      protected void doServer(ServerSocket server) throws Exception
      {
         server.accept();
      }

   }

   public static abstract class AbstractOKServer extends AbstractSynchronizedServer
   {

      protected AbstractOKServer(int port)
      {
         super(port);
      }

      protected void doServer(ServerSocket server) throws Exception
      {
         //
         Socket s = server.accept();
         BufferedInputStream in = new BufferedInputStream(s.getInputStream());
         LineNumberReader reader = new LineNumberReader(new InputStreamReader(in, "ISO-8859-1"));
         for (String line = reader.readLine();line.length() > 0;line = reader.readLine())
         {
            log.debug("server received = " + line);
         }

         //
         log.debug("Finished reading");

         //
         BufferedOutputStream out = new BufferedOutputStream(s.getOutputStream());
         OutputStreamWriter writer = new OutputStreamWriter(out, "UTF-8");
         writer.write("HTTP/1.1 200 OK\r\n");
         writer.write("\r\n");
         writer.flush();
         out.write("CAFEBABE".getBytes("UTF-8"));
         out.close();
      }
   }

}
