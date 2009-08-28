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
package org.gatein.common.path;

/**
 * Utility class to pull parse a relative path. 
 *
 * @author <a href="mailto:julien@jboss.org">Julien Viet</a>
 * @version $Revision: 5451 $
 */
public class RelativePathParser
{

   // Constants ********************************************************************************************************

   private static final int STATE_BEGIN = 0;
   private static final int STATE_DOT = 1;
   private static final int STATE_DOT_DOT = 2;
   private static final int STATE_CHAR = 3;

   /**
    * The parser has reached the end of the stream. The values returned by <code>getOffset()</code> and
    * <code>getLength()</code> is undertermined.
    */
   public static final int NONE = 0;

   /**
    * The recognized token is up. The values returned by <code>getOffset()</code> and
    * <code>getLength()</code> is undertermined.
    */
   public static final int UP = 1;

   /**
    * The recognized token is down. The values returned by <code>getOffset()</code> and
    * <code>getLength()</code> determine the token string value.
    */
   public static final int DOWN = 2;

   // Variables ********************************************************************************************************

   /**
    * The path being parsed.
    */
   private final String path;

   /**
    * The current index of parsing.
    */
   private int current;

   /**
    * The current internal offset.
    */
   private int previous;

   /**
    * The offset value when a down token is recognized.
    */
   private int offset;

   /**
    * The length value when a down token is recognized.
    */
   private int length;

   public RelativePathParser(String path)
   {
      if (path == null)
      {
         throw new IllegalArgumentException("No path");
      }
      this.path = path;
      this.previous = 0;
      this.current = 0;
   }

   /**
    * Read the next token available.
    *
    * @return the token type recognized which can be NONE, UP or DOWN.
    */
   public int next()
   {
      //
      previous = current;
      int state = STATE_BEGIN;

      //
      while (current < path.length())
      {
         char c = path.charAt(current++);
         switch (c)
         {
            case '.':
            {
               switch (state)
               {
                  case STATE_BEGIN:
                     state = STATE_DOT;
                     break;
                  case STATE_DOT:
                     state = STATE_DOT_DOT;
                     break;
                  case STATE_CHAR:
                     break;
                  default:
                  case STATE_DOT_DOT:
                     throw new IllegalArgumentException();
               }
               break;
            }
            case '/':
            {
               switch (state)
               {
                  default:
                  case STATE_BEGIN:
                     throw new IllegalStateException("");
                  case STATE_DOT:
                     previous = current;
                     state = STATE_BEGIN;
                     break;
                  case STATE_DOT_DOT:
                     length = -1;
                     offset = -1;
                     return UP;
                  case STATE_CHAR:
                     length = current - previous - 1;
                     offset = previous;
                     return DOWN;
               }
               break;
            }
            default:
            {
               switch (state)
               {
                  case STATE_BEGIN:
                     state = STATE_CHAR;
                     break;
                  case STATE_CHAR:
                     break;
                  case STATE_DOT:
                  case STATE_DOT_DOT:
                  default:
                     throw new IllegalStateException("");
               }
            }
         }
      }

      //
      switch (state)
      {
         case STATE_DOT:
         case STATE_BEGIN:
            length = -1;
            offset = -1;
            return NONE;
         case STATE_CHAR:
            offset = previous;
            length = current - previous;
            return DOWN;
         case STATE_DOT_DOT:
            length = -1;
            offset = -1;
            return UP;
         default:
            throw new IllegalStateException("");
      }
   }

   public int getOffset()
   {
      return offset;
   }

   public int getLength()
   {
      return length;
   }
}
