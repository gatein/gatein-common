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
package org.gatein.common.net;

import org.apache.log4j.Logger;
import org.gatein.common.io.IOTools;
import org.gatein.common.text.FastURLDecoder;
import org.gatein.common.text.FastURLEncoder;
import org.gatein.common.util.ParameterValidation;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Authenticator;
import java.net.MalformedURLException;
import java.net.PasswordAuthentication;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author <a href="mailto:chris.laprun@jboss.com">Chris Laprun</a>
 * @version $Revision: 7686 $
 * @since 2.4 (May 26, 2006)
 */
public class URLTools
{
   public static final String RE_EMAIL_VALIDATION = "^([a-zA-Z0-9]+(([\\.\\-\\_]?[a-zA-Z0-9]+)+)?)\\@(([a-zA-Z0-9]+[\\.\\-\\_])+[a-zA-Z]{2,4})$";
   private static final Pattern LINK = Pattern.compile("(?:href|action|src|location)\\s*=\\s*('|\")\\s*([^'\"]*)\\s*('|\")",
      Pattern.CASE_INSENSITIVE);

   public static final String HTTP_PREFIX = "http://";
   public static final String HTTPS_PREFIX = "https://";
   public static final String FTP_PREFIX = "ftp://";
   public static final String FILE_PREFIX = "/";

   private static final Logger log = Logger.getLogger(URLTools.class);

   public static boolean isURLAbsolute(String url)
   {
      return isNetworkURL(url) || url.startsWith(FILE_PREFIX);
   }

   /**
    * Fetches content from of the URL as a byte array or <code>null</code> if a problem occurred. The timeout values
    * must not be negative integers, when it is equals to zero it means that it does not setup a timeout and use the
    * default values.
    *
    * @param url               the URL the URL of the resource
    * @param soTimeoutMillis   the socket connection timeout in millis
    * @param connTimeoutMillis the connection timeout in millis
    * @return the retrieved byte array
    * @throws IllegalArgumentException if the URL is null or any time out value is negative
    */
   public static byte[] getContent(URL url, int soTimeoutMillis, int connTimeoutMillis) throws IllegalArgumentException
   {
      InputStream in = null;
      try
      {
         in = getContentAsInputStream(url, soTimeoutMillis, connTimeoutMillis);
         return IOTools.getBytes(in);
      }
      catch (IOException e)
      {
         return null;
      }
      finally
      {
         IOTools.safeClose(in);
      }
   }

   /**
    * Fetches content from URL as an InputStream. The timeout values must not be negative integers, when it is equals to
    * zero it means that it does not setup a timeout and use the default values.
    *
    * @param url               the URL the URL of the resource
    * @param soTimeoutMillis   the socket connection timeout in millis
    * @param connTimeoutMillis the connection timeout in millis
    * @return the buffered content for the URL
    * @throws IllegalArgumentException if the URL is null or any time out value is negative
    * @since 1.1
    */
   public static InputStream getContentAsInputStream(URL url, int soTimeoutMillis, int connTimeoutMillis) throws IOException
   {
      if (url == null)
      {
         throw new IllegalArgumentException();
      }
      if (soTimeoutMillis < 0)
      {
         throw new IllegalArgumentException("No negative socket timeout " + soTimeoutMillis);
      }
      if (connTimeoutMillis < 0)
      {
         throw new IllegalArgumentException("No negative connection timeout" + connTimeoutMillis);
      }

      if (System.getProperty("http.proxyUser") != null)
      {
         Authenticator.setDefault(new Authenticator(){

            protected PasswordAuthentication getPasswordAuthentication()
            {
               return (new PasswordAuthentication(System.getProperty("http.proxyUser"), System.getProperty("http.proxyPassword").toCharArray()));
            }
         });
      }
      
      //
      URLConnection conn;
      try
      {
         conn = url.openConnection();
      }
      catch (IOException e)
      {
         return null;
      }

      // Configure
      conn.setConnectTimeout(soTimeoutMillis);
      conn.setReadTimeout(connTimeoutMillis);

      conn.connect();
      try
      {
         return new BufferedInputStream(conn.getInputStream());
      }
      catch (SocketTimeoutException e)
      {
         log.debug("Time out on: " + url);
         throw e;
      }
   }

   /**
    * @param url
    * @return
    * @since 2.4.2
    */
   public static boolean isNetworkURL(String url)
   {
      if (url == null || url.length() == 0)
      {
         return false;
      }

      return url.startsWith(HTTP_PREFIX) || url.startsWith(HTTPS_PREFIX) || url.startsWith(FTP_PREFIX);
   }

   /**
    * Enforces that the given URL is absolute
    *
    * @param url the String representation of the URL to be checked
    * @throws IllegalArgumentException if the given URL is not absolute
    */
   public static void enforceAbsoluteURL(String url) throws IllegalArgumentException
   {
      if (!isURLAbsolute(url))
      {
         throw new IllegalArgumentException("URL must be absolute. Was: " + url);
      }
   }

   public static String encodeXWWWFormURL(String s)
   {
      return FastURLEncoder.getUTF8Instance().encode(s);
   }

   public static String decodeXWWWFormURL(String s)
   {
      return FastURLDecoder.getUTF8Instance().encode(s);
   }

   /** Return true is the address is not null and matches the email validation regular expression. */
   public static boolean isEmailValid(String address)
   {
      return address != null && Pattern.matches(RE_EMAIL_VALIDATION, address);
   }

   /**
    * Determines that the specified URL corresponds to an existing resource by trying to open a stream from it. Same as
    * <code>exists(url, 1000)</code>
    *
    * @param url
    * @return
    */
   public static boolean exists(URL url)
   {
      return exists(url, 1000);
   }

   /**
    * Determines if the specified URL corresponds to an existing resource by trying to open a stream from it.
    *
    * @param url           the URL to be tested
    * @param waitForMillis the number of milliseconds to wait before timing out, 0 meaning never timing out.
    * @return
    * @throws IllegalArgumentException if the url is null or the time out negative
    * @since 2.4.2
    */
   public static boolean exists(URL url, long waitForMillis) throws IllegalArgumentException
   {
      if (url == null)
      {
         throw new IllegalArgumentException("No null URL accepted");
      }
      if (waitForMillis < 0)
      {
         throw new IllegalArgumentException("Read time out must be a positive value instead of " + waitForMillis);
      }

      //
      URLConnection conn;
      try
      {
         conn = url.openConnection();
      }
      catch (IOException e)
      {
         log.debug("Could not open connection for " + url, e);
         return false;
      }

      // Configure
      conn.setConnectTimeout((int)waitForMillis);
      conn.setReadTimeout((int)waitForMillis);

      //
      InputStream in = null;
      try
      {
         conn.connect();
         in = conn.getInputStream();
         return true;
      }
      catch (SocketTimeoutException e)
      {
         return false;
      }
      catch (IOException e)
      {
         return false;
      }
      finally
      {
         IOTools.safeClose(in);
      }
   }

   /**
    * to remove :  an API should not try to accomodate the client for that kind of situation, why not also something
    * like forbidStringLengthToDivisibleBy3 ?
    *
    * @param stringURL
    * @param allowNull <code>true</code> if passing <code>null</code> will be ignored and just return
    *                  <code>false</code>, <code>false</code> to throw an {@link IllegalArgumentException} is the given
    *                  URL is <code>null</code>.
    * @return
    * @since 2.4.2
    */
   public static boolean exists(String stringURL, boolean allowNull)
   {
      if (!allowNull)
      {
         ParameterValidation.throwIllegalArgExceptionIfNullOrEmpty(stringURL, "URL", null);
      }

      try
      {
         URL url = new URL(stringURL);
         return exists(url);
      }
      catch (MalformedURLException e)
      {
         return false;
      }
   }

   public static URLMatch[] extractURLsFrom(String markup)
   {
      // todo: will need to re-write without regex after 2.4
      int length;
      if (markup != null && (length = markup.length()) != 0)
      {
         Matcher matcher = LINK.matcher(markup);
         int currentIndex = 0;
         List links = new ArrayList();
         while (matcher.find(currentIndex) && currentIndex < length)
         {
            links.add(new URLMatch(matcher.start(2), matcher.end(2), matcher.group(2)));
            currentIndex = matcher.end();
         }

         return (URLMatch[])links.toArray(new URLMatch[0]);
      }
      throw new IllegalArgumentException("Cannot extract URLs from a null or empty markup string!");
   }

   public static int getURLCountIn(String markup)
   {
      int length;
      if (markup != null && (length = markup.length()) != 0)
      {
         Matcher matcher = LINK.matcher(markup);
         int count = 0;
         int currentIndex = 0;
         while (matcher.find(currentIndex) && currentIndex < length)
         {
            count++;
            currentIndex = matcher.end();
         }
         return count;
      }
      return 0;
   }

   public static String replaceURLsBy(String markup, final String[] replacements)
   {
      if (replacements == null || replacements.length == 0)
      {
         return markup;
      }

      int urlCount = getURLCountIn(markup);
      if (replacements.length != urlCount)
      {
         throw new IllegalArgumentException("Trying to replace " + urlCount + " URLs by " + replacements.length + " replacement(s).");
      }

      return replaceURLsBy(markup, new URLReplacementGenerator()
      {
         public String getReplacementFor(int currentIndex, URLMatch currentMatch)
         {
            return replacements[currentIndex];
         }
      });
   }

   public static String replaceURLsBy(String markup, URLReplacementGenerator generator)
   {

      URLMatch[] urls = extractURLsFrom(markup);
      if (urls.length > 0)
      {
         StringBuffer newMarkup = new StringBuffer(markup.length());
         int currentIndex = 0;
         for (int i = 0; i < urls.length; i++)
         {
            URLMatch url = urls[i];
            newMarkup.append(markup.substring(currentIndex, url.getStart())).append(generator.getReplacementFor(i, url));
            currentIndex = url.getEnd();
         }
         newMarkup.append(markup.substring(currentIndex));
         markup = newMarkup.toString();
      }
      return markup;
   }

   public static class URLMatch
   {
      private int start;
      private int end;
      private String urlAsString;

      private URLMatch(int start, int end, String urlAsString)
      {
         this.start = start;
         this.end = end;
         this.urlAsString = urlAsString;
      }

      public int getStart()
      {
         return start;
      }

      public int getEnd()
      {
         return end;
      }

      public String getURLAsString()
      {
         return urlAsString;
      }
   }

   /** @since 2.4.2 */
   public abstract static class URLReplacementGenerator
   {
      public abstract String getReplacementFor(int currentIndex, URLMatch currentMatch);
   }

   public static class PortReplacementGenerator extends URLReplacementGenerator
   {
      private int replacementPort;

      public PortReplacementGenerator(int replacementPort)
      {
         this.replacementPort = replacementPort;
      }

      public String getReplacementFor(int currentIndex, URLMatch currentMatch)
      {
         return replaceServerPortInURL(currentMatch.getURLAsString(), replacementPort);
      }
   }

   /**
    * @param url
    * @param newPort
    * @return
    * @since 2.4.2
    */
   public static String replaceServerPortInURL(String url, int newPort)
   {
      if (!isNetworkURL(url))
      {
         return url;
      }

      StringBuffer buf = new StringBuffer(url);
      int afterProtocol = url.indexOf("://") + 3;
      int beforePort = url.indexOf(':', afterProtocol);
      int afterPort;

      if (beforePort != -1)
      {
         afterPort = url.indexOf('/', beforePort);
         buf.delete(beforePort + 1, afterPort);
         buf.insert(beforePort + 1, newPort);
      }
      else
      {
         // port number was not present
         afterPort = url.indexOf('/', afterProtocol);
         buf.insert(afterPort, ":" + newPort);
      }

      return buf.toString();
   }

}
