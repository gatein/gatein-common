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

import junit.framework.TestCase;
import org.apache.log4j.Logger;
import org.gatein.common.junit.ExtendedAssert;
import org.gatein.common.net.URLTools;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * @author <a href="mailto:chris.laprun@jboss.com">Chris Laprun</a>
 * @version $Revision: 7379 $
 * @since 2.4 (May 29, 2006)
 */
public class URLToolsTestCase extends TestCase
{

   /** . */
   private static final Logger log = Logger.getLogger(URLToolsTestCase.class);

   private static final String MARKUP = "Hello, Anonymous!\n" +
      "Counter: 0<a href='wsrp_rewrite?wsrp-urlType=render&amp;wsrp-navigationalState=rO0ABXNyACdvcmc" +
      "uamJvc3MucG9ydGFsLnNlcnZlci51dGlsLlBhcmFtZXRlcnOJoAlMQZGhngIAAUwAA21hcHQAD0xqYXZhL3V0aWwvTWFwO3hwc3IAEWphd" +
      "mEudXRpbC5IYXNoTWFwBQfawcMWYNEDAAJGAApsb2FkRmFjdG9ySQAJdGhyZXNob2xkeHA_QAAAAAAADHcIAAAAEAAAAAF0AARuYW1ldXI" +
      "AE1tMamF2YS5sYW5nLlN0cmluZzut0lbn6R17RwIAAHhwAAAAAXQABkp1bGllbng*/wsrp_rewrite'>My name is Julien</a><a hr" +
      "ef='wsrp_rewrite?wsrp-urlType=render&amp;wsrp-navigationalState=rO0ABXNyACdvcmcuamJvc3MucG9ydGFsLnNlcnZlci" +
      "51dGlsLlBhcmFtZXRlcnOJoAlMQZGhngIAAUwAA21hcHQAD0xqYXZhL3V0aWwvTWFwO3hwc3IAEWphdmEudXRpbC5IYXNoTWFwBQfawcMW" +
      "YNEDAAJGAApsb2FkRmFjdG9ySQAJdGhyZXNob2xkeHA_QAAAAAAADHcIAAAAEAAAAAF0AARuYW1ldXIAE1tMamF2YS5sYW5nLlN0cmluZz" +
      "ut0lbn6R17RwIAAHhwAAAAAXQAA1JveXg*/wsrp_rewrite'>My name is Roy</a><action='wsrp_rewrite?wsrp-urlType=bloc" +
      "kingAction&amp;wsrp-interactionState=rO0ABXNyACdvcmcuamJvc3MucG9ydGFsLnNlcnZlci51dGlsLlBhcmFtZXRlcnOJoAlMQ" +
      "ZGhngIAAUwAA21hcHQAD0xqYXZhL3V0aWwvTWFwO3hwc3IAEWphdmEudXRpbC5IYXNoTWFwBQfawcMWYNEDAAJGAApsb2FkRmFjdG9ySQA" +
      "JdGhyZXNob2xkeHA_QAAAAAAADHcIAAAAEAAAAAF0AAJvcHVyABNbTGphdmEubGFuZy5TdHJpbmc7rdJW5-kde0cCAAB4cAAAAAF0AAIrK" +
      "3g*/wsrp_rewrite'>counter++</a><a href='wsrp_rewrite?wsrp-urlType=blockingAction&amp;wsrp-interactionState" +
      "=rO0ABXNyACdvcmcuamJvc3MucG9ydGFsLnNlcnZlci51dGlsLlBhcmFtZXRlcnOJoAlMQZGhngIAAUwAA21hcHQAD0xqYXZhL3V0aWwvT" +
      "WFwO3hwc3IAEWphdmEudXRpbC5IYXNoTWFwBQfawcMWYNEDAAJGAApsb2FkRmFjdG9ySQAJdGhyZXNob2xkeHA_QAAAAAAADHcIAAAAEAA" +
      "AAAF0AAJvcHVyABNbTGphdmEubGFuZy5TdHJpbmc7rdJW5-kde0cCAAB4cAAAAAF0AAItLXg*/wsrp_rewrite'>counter--</a>";

   public void testExtractURLs()
   {
      String markup = MARKUP;

      URLTools.URLMatch[] links = URLTools.extractURLsFrom(markup);
      assertEquals(4, links.length);
      URLTools.URLMatch link = links[0];
      assertEquals("wsrp_rewrite?wsrp-urlType=render&amp;wsrp-navigationalState=rO0ABXNyACdvcmcuamJvc3MucG9ydGFsLnNl" +
         "cnZlci51dGlsLlBhcmFtZXRlcnOJoAlMQZGhngIAAUwAA21hcHQAD0xqYXZhL3V0aWwvTWFwO3hwc3IAEWphdmEudXRpbC5IYXNoTWFwBQ" +
         "fawcMWYNEDAAJGAApsb2FkRmFjdG9ySQAJdGhyZXNob2xkeHA_QAAAAAAADHcIAAAAEAAAAAF0AARuYW1ldXIAE1tMamF2YS5sYW5nLlN0" +
         "cmluZzut0lbn6R17RwIAAHhwAAAAAXQABkp1bGllbng*/wsrp_rewrite", link.getURLAsString());
      assertEquals("wsrp_rewrite?wsrp-urlType=blockingAction&amp;wsrp-interactionState=rO0ABXNyACdvcmcuamJvc3MucG9yd" +
         "GFsLnNlcnZlci51dGlsLlBhcmFtZXRlcnOJoAlMQZGhngIAAUwAA21hcHQAD0xqYXZhL3V0aWwvTWFwO3hwc3IAEWphdmEudXRpbC5IYXN" +
         "oTWFwBQfawcMWYNEDAAJGAApsb2FkRmFjdG9ySQAJdGhyZXNob2xkeHA_QAAAAAAADHcIAAAAEAAAAAF0AAJvcHVyABNbTGphdmEubGFuZ" +
         "y5TdHJpbmc7rdJW5-kde0cCAAB4cAAAAAF0AAIrK3g*/wsrp_rewrite", links[2].getURLAsString());

      String url = "wsrp_rewrite?wsrp-urlType=render&amp;wsrp-mode=help/wsrp_rewrite";
      markup = "12345href='" + url + "'76";
      links = URLTools.extractURLsFrom(markup);
      link = links[0];
      int startIndex = 11;
      assertEquals(startIndex, link.getStart());
      assertEquals(url.length() + startIndex, link.getEnd());
      assertEquals(url, markup.substring(link.getStart(), link.getEnd()));
      assertEquals(url, link.getURLAsString());
   }

   public void testReplaceURLs()
   {
      String markup = URLTools.replaceURLsBy(MARKUP, new String[]{"foo", "bar", "baz", "buz"});
      String replaced = "Hello, Anonymous!\nCounter: 0<a href='foo'>My name is Julien</a><a href='bar'>My name is Roy</a>" +
         "<action='baz'>counter++</a><a href='buz'>counter--</a>";
      assertEquals(replaced, markup);
      assertEquals(replaced, URLTools.replaceURLsBy(replaced, (String[])null));

      String mixed = "<a href='wsrp_rewrite?wsrp-urlType=render&amp;wsrp-mode=help/wsrp_rewrite'>My name is Julien</a>" +
         "<a href='bar'>My name is Roy</a>";
      assertEquals("<a href='foo'>My name is Julien</a><a href='bar'>My name is Roy</a>",
         URLTools.replaceURLsBy(mixed, new URLTools.URLReplacementGenerator()
         {
            public String getReplacementFor(int currentIndex, URLTools.URLMatch currentMatch)
            {
               String urlAsString = currentMatch.getURLAsString();
               if (urlAsString.startsWith("wsrp_rewrite"))
               {
                  return "foo";
               }
               return urlAsString;
            }
         }));
   }

   public void testReplaceAllPorts()
   {
      String original = "<wsdl:definitions targetNamespace='urn:oasis:names:tc:wsrp:v1:wsdl'\n" +
         "                  xmlns:bind='urn:oasis:names:tc:wsrp:v1:bind'\n" +
         "                  xmlns='http://schemas.xmlsoap.org/wsdl/'\n" +
         "                  xmlns:wsdl='http://schemas.xmlsoap.org/wsdl/'\n" +
         "                  xmlns:soap='http://schemas.xmlsoap.org/wsdl/soap/'\n" +
         "                  xmlns:intf='urn:oasis:names:tc:wsrp:v1:intf'\n" +
         "                  xmlns:tns='urn:oasis:names:tc:wsrp:v1:wsdl'>\n" +
         "   <import namespace='urn:oasis:names:tc:wsrp:v1:bind' location='wsrp_v1_bindings.wsdl'/>\n" +
         "   <wsdl:service name='WSRPService'>\n" +
         "      <wsdl:port binding='bind:WSRP_v1_Markup_Binding_SOAP' name='WSRPBaseService'>\n" +
         "         <soap:address location='http://localhost/portal-wsrp/ServiceDescriptionService'/>\n" +
         "      </wsdl:port>\n" +
         "      <wsdl:port binding='bind:WSRP_v1_ServiceDescription_Binding_SOAP' name='WSRPServiceDescriptionService'>\n" +
         "         <soap:address location='http://localhost/portal-wsrp/MarkupService'/>\n" +
         "      </wsdl:port>\n" +
         "      <wsdl:port binding='bind:WSRP_v1_Registration_Binding_SOAP' name='WSRPRegistrationService'>\n" +
         "         <soap:address location='http://localhost/portal-wsrp/RegistrationService'/>\n" +
         "      </wsdl:port>\n" +
         "      <wsdl:port binding='bind:WSRP_v1_PortletManagement_Binding_SOAP' name='WSRPPortletManagementService'>\n" +
         "         <soap:address location='http://localhost/portal-wsrp/PortletManagementService'/>\n" +
         "      </wsdl:port>\n" +
         "   </wsdl:service>\n" +
         "</wsdl:definitions>";
      String result = "<wsdl:definitions targetNamespace='urn:oasis:names:tc:wsrp:v1:wsdl'\n" +
         "                  xmlns:bind='urn:oasis:names:tc:wsrp:v1:bind'\n" +
         "                  xmlns='http://schemas.xmlsoap.org/wsdl/'\n" +
         "                  xmlns:wsdl='http://schemas.xmlsoap.org/wsdl/'\n" +
         "                  xmlns:soap='http://schemas.xmlsoap.org/wsdl/soap/'\n" +
         "                  xmlns:intf='urn:oasis:names:tc:wsrp:v1:intf'\n" +
         "                  xmlns:tns='urn:oasis:names:tc:wsrp:v1:wsdl'>\n" +
         "   <import namespace='urn:oasis:names:tc:wsrp:v1:bind' location='wsrp_v1_bindings.wsdl'/>\n" +
         "   <wsdl:service name='WSRPService'>\n" +
         "      <wsdl:port binding='bind:WSRP_v1_Markup_Binding_SOAP' name='WSRPBaseService'>\n" +
         "         <soap:address location='http://localhost:8888/portal-wsrp/ServiceDescriptionService'/>\n" +
         "      </wsdl:port>\n" +
         "      <wsdl:port binding='bind:WSRP_v1_ServiceDescription_Binding_SOAP' name='WSRPServiceDescriptionService'>\n" +
         "         <soap:address location='http://localhost:8888/portal-wsrp/MarkupService'/>\n" +
         "      </wsdl:port>\n" +
         "      <wsdl:port binding='bind:WSRP_v1_Registration_Binding_SOAP' name='WSRPRegistrationService'>\n" +
         "         <soap:address location='http://localhost:8888/portal-wsrp/RegistrationService'/>\n" +
         "      </wsdl:port>\n" +
         "      <wsdl:port binding='bind:WSRP_v1_PortletManagement_Binding_SOAP' name='WSRPPortletManagementService'>\n" +
         "         <soap:address location='http://localhost:8888/portal-wsrp/PortletManagementService'/>\n" +
         "      </wsdl:port>\n" +
         "   </wsdl:service>\n" +
         "</wsdl:definitions>";

      assertEquals(result, URLTools.replaceURLsBy(original, new URLTools.PortReplacementGenerator(8888)));
   }

   public void testReplaceServerPort()
   {
      assertEquals("http://hostname:8088/some/path", URLTools.replaceServerPortInURL("http://hostname:8080/some/path", 8088));
      assertEquals("https://hostname:8088/some/path", URLTools.replaceServerPortInURL("https://hostname:8080/some/path", 8088));
      assertEquals("http://hostname:8088/some/path", URLTools.replaceServerPortInURL("http://hostname/some/path", 8088));
      assertEquals("https://hostname:8088/some/path", URLTools.replaceServerPortInURL("https://hostname/some/path", 8088));
   }

   public void testExistsURL()
   {
      assertFalse(URLTools.exists(null, true));


   }

   public void testURLExistsTimeout() throws Exception
   {
      AbstractSynchronizedServer server = new AbstractSynchronizedServer.AbstractTimeoutServer(8080)
      {
         protected void doClient()
         {
            boolean exist = true;
            try
            {
               URL url = new URL("http://localhost:8080/");
               exist = URLTools.exists(url, 500);
            }
            catch (MalformedURLException e)
            {
               log.error("Cannot create URL", e);
               fail("Cannot create URL " + e.getMessage());
            }

            //
            assertFalse("Was not expecting the URL to exist", exist);
         }
      };

      //
      server.performInteraction();
   }

   public void testURLPerformGETTimeout() throws Exception
   {
      AbstractSynchronizedServer server = new AbstractSynchronizedServer.AbstractTimeoutServer(8080)
      {
         protected void doClient()
         {
            byte[] bytes = null;
            try
            {
               URL url = new URL("http://localhost:8080/");
               bytes = URLTools.getContent(url, 500, 500);
            }
            catch (MalformedURLException e)
            {
               log.error("Cannot create URL", e);
               fail("Cannot create URL " + e.getMessage());
            }

            //
            assertNull("Was not expecting the URL GET to return a non null value", bytes);
         }
      };

      //
      server.performInteraction();
   }

   public void testURLPerformGET() throws Exception
   {
      AbstractSynchronizedServer server = new AbstractSynchronizedServer.AbstractOKServer(8080)
      {

         protected void doClient() throws Exception
         {
            URL url = new URL("http://localhost:8080/");
            byte[] bytes = URLTools.getContent(url, 5000, 5000);
            ExtendedAssert.assertEquals("CAFEBABE".getBytes("UTF-8"), bytes);
         }
      };

      //
      server.performInteraction();
   }

   public void testURLExists() throws Exception
   {
      AbstractSynchronizedServer server = new AbstractSynchronizedServer.AbstractOKServer(8080)
      {

         protected void doClient() throws Exception
         {
            URL url = new URL("http://localhost:8080/");
            boolean exists = URLTools.exists(url, 5000);
            ExtendedAssert.assertTrue("Was expecting the URL to exist", exists);
         }
      };

      //
      server.performInteraction();
   }
}
