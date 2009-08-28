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
package org.gatein.common.http;

import org.gatein.common.text.CharEncoder;
import org.gatein.common.text.FastURLDecoder;
import org.gatein.common.text.CharBuffer;
import org.gatein.common.text.EncodingException;
import org.gatein.common.util.ParameterMap;
import org.apache.log4j.Logger;

/**
 * A parser for query string for the HTTP protocol. This class is thread safe.
 *
 * @author <a href="mailto:julien@jboss.org">Julien Viet</a>
 * @version $Revision: 7379 $
 */
public class QueryStringParser
{

   /** . */
   private static QueryStringParser DEFAULT_PARSER = new QueryStringParser();

   public static QueryStringParser getInstance()
   {
      return DEFAULT_PARSER;
   }

   /** . */
   private static final Logger log = Logger.getLogger(QueryStringParser.class);

   /** . */
   private static final int LOOKUP = 0;

   /** . */
   private static final int INVALID_CHUNK = 1;

   /** . */
   private static final int PARAM_NAME = 2;

   /** . */
   private static final int PARAM_VALUE = 3;

   /** . */
   private CharEncoder parameterDecoder;

   /**
    * Create a new parser with the specified parameter decoder.
    *
    * @param parameterDecoder the parameter decoder
    * @throws IllegalArgumentException if the decoder is null
    */
   public QueryStringParser(CharEncoder parameterDecoder) throws IllegalArgumentException
   {
      if (parameterDecoder == null)
      {
         throw new IllegalArgumentException("No parameter decoder");
      }
      this.parameterDecoder = parameterDecoder;
   }

   /**
    * Creates a new parser with a decoder that will decode x-www-formurlencoded parameters with the
    * UTF-8 charset.
    */
   public QueryStringParser()
   {
      this(FastURLDecoder.getUTF8Instance());
   }

   public CharEncoder getParameterDecoder()
   {
      return parameterDecoder;
   }

   private void append(ParameterMap parameterMap, String parameterName, String parameterValue)
   {
      String[] values = parameterMap.getValues(parameterName);

      //
      if (values == null)
      {
         values = new String[]{parameterValue};
      }
      else
      {
         String[] tmp = new String[values.length + 1];
         System.arraycopy(values, 0, tmp, 0, values.length);
         values = tmp;
         values[values.length - 1] = parameterValue;
      }

      //
      parameterMap.setValues(parameterName, values);
   }

   /**
    * Parse the query string and build an unmodifiable parameter map of it.
    *
    * @param queryString the non null query string
    * @return a <String,String[]> map
    * @throws IllegalArgumentException if the query string is null
    */
   public ParameterMap parseQueryString(String queryString) throws IllegalArgumentException
   {
      if (queryString == null)
      {
         throw new IllegalArgumentException();
      }

      //
      // Map result = Collections.EMPTY_MAP;

      ParameterMap parameterMap = new ParameterMap();

      String encodedName = null;
      CharBuffer buffer = new CharBuffer(50);


      int pos = 0;
      int len = queryString.length();
      int state = LOOKUP;
      while (true)
      {
         char c;
         if (pos < len)
         {
            c = queryString.charAt(pos++);
         }
         else if (pos == len)
         {
            c = '&';
            pos++;
         }
         else
         {
            break;
         }

         //
         switch(state)
         {
            case LOOKUP:
               if (c == '&')
               {
                  // Do nothing
               }
               else if (c == '=')
               {
                  state = INVALID_CHUNK;
               }
               else
               {
                  state = PARAM_NAME;
                  buffer.append(c);
               }
               break;
            case PARAM_NAME:
               if (c == '&')
               {
                  String tmp = buffer.asString();

                  //
                  try
                  {
                     parameterDecoder.encode(tmp, buffer);
                     append(parameterMap, buffer.asString(false), "");
                  }
                  catch (EncodingException e)
                  {
                     log.debug("Could not decode parameter name " + tmp, e);
                  }

                  //
                  buffer.reset();
                  state = LOOKUP;
               }
               else if (c == '=')
               {
                  encodedName = buffer.asString();
                  buffer.reset();
                  state = PARAM_VALUE;
               }
               else
               {
                  buffer.append(c);
               }
               break;
            case PARAM_VALUE:
               if (c == '&')
               {

                  //
                  try
                  {
                     // Save value
                     String encodedValue = buffer.asString();

                     // Decode parameter name
                     parameterDecoder.encode(encodedName, buffer);
                     String name = buffer.asString(false);

                     // Now decode parameter value
                     try
                     {
                        buffer.reset();
                        parameterDecoder.encode(encodedValue, buffer);
                        String value = buffer.asString();
                        append(parameterMap, name, value);
                     }
                     catch (EncodingException e)
                     {
                        log.debug("Could not decode parameter value " + encodedValue, e);
                     }
                  }
                  catch (EncodingException e)
                  {
                     log.debug("Could not decode parameter name " + encodedName, e);
                  }

                  //
                  buffer.reset();
                  state = LOOKUP;
               }
               else
               {
                  buffer.append(c);
               }
               break;
            case INVALID_CHUNK:
               if (c == '&')
               {
                  state = LOOKUP;
               }
               break;
         }
      }

      //
      return parameterMap;
   }
}
