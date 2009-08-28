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
package org.gatein.common.junit;

import junit.framework.Assert;
import junit.framework.AssertionFailedError;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.transaction.Status;
import javax.transaction.UserTransaction;

/**
 * @author <a href="mailto:julien@jboss.org">Julien Viet</a>
 * @version $Revision: 7228 $
 */
public class TransactionAssert extends Assert
{

   private static final String[] STATUS_NAMES = {
      "STATUS_ACTIVE",
      "STATUS_MARKED_ROLLBACK",
      "STATUS_PREPARED",
      "STATUS_COMMITTED",
      "STATUS_ROLLEDBACK",
      "STATUS_UNKNOWN",
      "STATUS_NO_TRANSACTION",
      "STATUS_PREPARING",
      "STATUS_COMMITTING",
      "STATUS_ROLLING_BACK"};

   /**
    * Decode the status name.
    *
    * @param status the status value
    * @return the translated status name or null if it is not valid
    */
   public static String decodeStatus(int status)
   {
      if (status >= 0 && status <= STATUS_NAMES.length)
      {
         return STATUS_NAMES[status];
      }
      else
      {
         return null;
      }
   }

   /** . */
   public static final Terminator NONE = new Terminator("NONE");

   /** . */
   public static final Terminator MARKED_AS_ROLLBACK = new Terminator("MARKED_AS_ROLLBACK");

   /** . */
   public static final Terminator MUST_COMMIT = new Terminator("MUST_COMMIT");

   /** . */
   public static final Terminator MUST_ROLLBACK = new Terminator("MUST ROLLBACK");

   /**
    *
    */
   public static final class Terminator
   {

      /** . */
      private final String name;

      public Terminator(String name)
      {
         this.name = name;
      }
   }

   /**
    * Assert the status of the current transaction.
    *
    * @param expectedStatus the expected status
    */
   public static void assertStatusEquals(int expectedStatus)
   {
      try
      {
         UserTransaction ut = getUserTransaction();
         int status = ut.getStatus();
         if (status != expectedStatus)
         {
            fail("The status of the current transaction is " + status + " was expecting " + expectedStatus);
         }
      }
      catch (Exception e)
      {
         e.printStackTrace();
         fail("Unexpected exception");
      }
   }

   /** Commit the transaction or fail. */
   public static void commitTransaction()
   {
      try
      {
         getUserTransaction().commit();
      }
      catch (Exception e)
      {
         e.printStackTrace();
         fail("Cannot commit transaction");
      }
   }

   /** Rollback the transaction or fail. */
   public static void rollbackTransaction()
   {
      rollbackTransaction(true);
   }

   public static void rollbackTransaction(boolean marked)
   {
      try
      {
         UserTransaction ut = getUserTransaction();
         int status = ut.getStatus();
         if (status == Status.STATUS_MARKED_ROLLBACK)
         {
            ut.rollback();
            if (marked == false)
            {
               fail("Transaction should have been marked as rollback");
            }
         }
         else if (status == Status.STATUS_ACTIVE)
         {
            ut.rollback();
            if (marked)
            {
               fail("Transaction should have been marked as rollback");
            }
         }
         else
         {
            if (marked)
            {
               fail("Unexpected transaction status " + status);
            }
         }
      }
      catch (Exception e)
      {
         e.printStackTrace();
         fail("Cannot end transaction");
      }
   }

   /** Begin a transaction or fail. */
   public static void beginTransaction()
   {
      try
      {
         getUserTransaction().begin();
      }
      catch (Exception e)
      {
         e.printStackTrace();
         fail("Cannot begin transaction");
      }
   }

   /** If no transaction do nothing. End the transaction if it is active or marked for rollback otherwise fail. */
   public static int endTransaction()
   {
      try
      {
         UserTransaction ut = getUserTransaction();
         int status = ut.getStatus();
         if (status == Status.STATUS_MARKED_ROLLBACK)
         {
            ut.rollback();
         }
         else if (status == Status.STATUS_ACTIVE)
         {
            ut.commit();
         }
         else if (status == Status.STATUS_NO_TRANSACTION)
         {
            // Do nothing
         }
         else
         {
            fail("Unexpected transaction status " + status);
         }
         return status;
      }
      catch (Exception e)
      {
         e.printStackTrace();
         throw new AssertionFailedError("Cannot end transaction");
      }
   }

   /** If no transaction do nothing. End the transaction if it is active or marked for rollback otherwise fail. */
   public static int endTransaction(Terminator expectedTerminator)
   {
      if (expectedTerminator == null)
      {
         throw new IllegalArgumentException();
      }
      try
      {
         UserTransaction ut = getUserTransaction();
         int status = ut.getStatus();
         if (status == Status.STATUS_MARKED_ROLLBACK)
         {
            ut.rollback();
            if (expectedTerminator != MARKED_AS_ROLLBACK)
            {
               fail("Was expecting the transaction to be marked as rollback instead got " + decodeStatus(status));
            }
         }
         else if (status == Status.STATUS_ACTIVE)
         {
            if (expectedTerminator == MUST_COMMIT)
            {
               ut.commit();
            }
            else if (expectedTerminator == MUST_ROLLBACK)
            {
               ut.rollback();
            }
            else
            {
               fail("Was expecting the transaction to be either commiting or rollbacking instead got " + decodeStatus(status));
            }
         }
         else if (status == Status.STATUS_NO_TRANSACTION)
         {
            if (expectedTerminator != NONE)
            {
               fail("Was expecting no transaction instead got " + decodeStatus(status));
            }
         }
         else
         {
            fail("Unexpected transaction status " + decodeStatus(status));
         }
         return status;
      }
      catch (Exception e)
      {
         e.printStackTrace();
         throw new AssertionFailedError("Cannot end transaction");
      }
   }

   public static UserTransaction getUserTransaction()
   {
      try
      {
         return (UserTransaction)new InitialContext().lookup("UserTransaction");
      }
      catch (NamingException e)
      {
         e.printStackTrace();
         fail("Cannot obtain user transaction");
         return null;
      }
   }
}
