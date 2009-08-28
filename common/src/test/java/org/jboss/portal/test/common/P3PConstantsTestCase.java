/*
* JBoss, a division of Red Hat
* Copyright 2006, Red Hat Middleware, LLC, and individual contributors as indicated
* by the @authors tag. See the copyright.txt in the distribution for a
* full listing of individual contributors.
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

package org.jboss.portal.test.common;

import junit.framework.TestCase;
import static org.gatein.common.p3p.P3PConstants.*;

/**
 * @author <a href="mailto:chris.laprun@jboss.com">Chris Laprun</a>
 * @version $Revision: 9048 $
 */
public class P3PConstantsTestCase extends TestCase
{
   public void testGetPostalUserInfoKey()
   {
      assertEquals(INFO_USER_BUSINESS_INFO_POSTAL_CITY, getPostalUserInfoKey(PostalInfo.CITY, true));
      assertEquals(INFO_USER_BUSINESS_INFO_POSTAL_COUNTRY, getPostalUserInfoKey(PostalInfo.COUNTRY, true));
      assertEquals(INFO_USER_BUSINESS_INFO_POSTAL_NAME, getPostalUserInfoKey(PostalInfo.NAME, true));
      assertEquals(INFO_USER_BUSINESS_INFO_POSTAL_ORGANIZATION, getPostalUserInfoKey(PostalInfo.ORGANIZATION, true));
      assertEquals(INFO_USER_BUSINESS_INFO_POSTAL_POSTALCODE, getPostalUserInfoKey(PostalInfo.POSTALCODE, true));
      assertEquals(INFO_USER_BUSINESS_INFO_POSTAL_STATEPROV, getPostalUserInfoKey(PostalInfo.STATEPROV, true));
      assertEquals(INFO_USER_BUSINESS_INFO_POSTAL_STREET, getPostalUserInfoKey(PostalInfo.STREET, true));

      assertEquals(INFO_USER_HOME_INFO_POSTAL_CITY, getPostalUserInfoKey(PostalInfo.CITY, false));
      assertEquals(INFO_USER_HOME_INFO_POSTAL_COUNTRY, getPostalUserInfoKey(PostalInfo.COUNTRY, false));
      assertEquals(INFO_USER_HOME_INFO_POSTAL_NAME, getPostalUserInfoKey(PostalInfo.NAME, false));
      assertEquals(INFO_USER_HOME_INFO_POSTAL_ORGANIZATION, getPostalUserInfoKey(PostalInfo.ORGANIZATION, false));
      assertEquals(INFO_USER_HOME_INFO_POSTAL_POSTALCODE, getPostalUserInfoKey(PostalInfo.POSTALCODE, false));
      assertEquals(INFO_USER_HOME_INFO_POSTAL_STATEPROV, getPostalUserInfoKey(PostalInfo.STATEPROV, false));
      assertEquals(INFO_USER_HOME_INFO_POSTAL_STREET, getPostalUserInfoKey(PostalInfo.STREET, false));
   }

   public void testGetOnlineInfoKey()
   {
      assertEquals(INFO_USER_BUSINESS_INFO_ONLINE_EMAIL, getOnlineUserInfoKey(OnlineInfo.EMAIL, true));
      assertEquals(INFO_USER_BUSINESS_INFO_ONLINE_URI, getOnlineUserInfoKey(OnlineInfo.URI, true));

      assertEquals(INFO_USER_HOME_INFO_ONLINE_EMAIL, getOnlineUserInfoKey(OnlineInfo.EMAIL, false));
      assertEquals(INFO_USER_HOME_INFO_ONLINE_URI, getOnlineUserInfoKey(OnlineInfo.URI, false));
   }


   public void testGetTelecomInfoKey()
   {
      // business fax
      assertEquals(INFO_USER_BUSINESS_INFO_TELECOM_FAX_COMMENT, getTelecomInfoKey(TelecomType.FAX, TelecomInfo.COMMENT, true));
      assertEquals(INFO_USER_BUSINESS_INFO_TELECOM_FAX_EXT, getTelecomInfoKey(TelecomType.FAX, TelecomInfo.EXT, true));
      assertEquals(INFO_USER_BUSINESS_INFO_TELECOM_FAX_INTCODE, getTelecomInfoKey(TelecomType.FAX, TelecomInfo.INTCODE, true));
      assertEquals(INFO_USER_BUSINESS_INFO_TELECOM_FAX_LOCCODE, getTelecomInfoKey(TelecomType.FAX, TelecomInfo.LOCCODE, true));
      assertEquals(INFO_USER_BUSINESS_INFO_TELECOM_FAX_NUMBER, getTelecomInfoKey(TelecomType.FAX, TelecomInfo.NUMBER, true));

      // home fax
      assertEquals(INFO_USER_HOME_INFO_TELECOM_FAX_COMMENT, getTelecomInfoKey(TelecomType.FAX, TelecomInfo.COMMENT, false));
      assertEquals(INFO_USER_HOME_INFO_TELECOM_FAX_EXT, getTelecomInfoKey(TelecomType.FAX, TelecomInfo.EXT, false));
      assertEquals(INFO_USER_HOME_INFO_TELECOM_FAX_INTCODE, getTelecomInfoKey(TelecomType.FAX, TelecomInfo.INTCODE, false));
      assertEquals(INFO_USER_HOME_INFO_TELECOM_FAX_LOCCODE, getTelecomInfoKey(TelecomType.FAX, TelecomInfo.LOCCODE, false));
      assertEquals(INFO_USER_HOME_INFO_TELECOM_FAX_NUMBER, getTelecomInfoKey(TelecomType.FAX, TelecomInfo.NUMBER, false));

      // business mobile
      assertEquals(INFO_USER_BUSINESS_INFO_TELECOM_MOBILE_COMMENT, getTelecomInfoKey(TelecomType.MOBILE, TelecomInfo.COMMENT, true));
      assertEquals(INFO_USER_BUSINESS_INFO_TELECOM_MOBILE_EXT, getTelecomInfoKey(TelecomType.MOBILE, TelecomInfo.EXT, true));
      assertEquals(INFO_USER_BUSINESS_INFO_TELECOM_MOBILE_INTCODE, getTelecomInfoKey(TelecomType.MOBILE, TelecomInfo.INTCODE, true));
      assertEquals(INFO_USER_BUSINESS_INFO_TELECOM_MOBILE_LOCCODE, getTelecomInfoKey(TelecomType.MOBILE, TelecomInfo.LOCCODE, true));
      assertEquals(INFO_USER_BUSINESS_INFO_TELECOM_MOBILE_NUMBER, getTelecomInfoKey(TelecomType.MOBILE, TelecomInfo.NUMBER, true));

      // home mobile
      assertEquals(INFO_USER_HOME_INFO_TELECOM_MOBILE_COMMENT, getTelecomInfoKey(TelecomType.MOBILE, TelecomInfo.COMMENT, false));
      assertEquals(INFO_USER_HOME_INFO_TELECOM_MOBILE_EXT, getTelecomInfoKey(TelecomType.MOBILE, TelecomInfo.EXT, false));
      assertEquals(INFO_USER_HOME_INFO_TELECOM_MOBILE_INTCODE, getTelecomInfoKey(TelecomType.MOBILE, TelecomInfo.INTCODE, false));
      assertEquals(INFO_USER_HOME_INFO_TELECOM_MOBILE_LOCCODE, getTelecomInfoKey(TelecomType.MOBILE, TelecomInfo.LOCCODE, false));
      assertEquals(INFO_USER_HOME_INFO_TELECOM_MOBILE_NUMBER, getTelecomInfoKey(TelecomType.MOBILE, TelecomInfo.NUMBER, false));

      // business pager
      assertEquals(INFO_USER_BUSINESS_INFO_TELECOM_PAGER_COMMENT, getTelecomInfoKey(TelecomType.PAGER, TelecomInfo.COMMENT, true));
      assertEquals(INFO_USER_BUSINESS_INFO_TELECOM_PAGER_EXT, getTelecomInfoKey(TelecomType.PAGER, TelecomInfo.EXT, true));
      assertEquals(INFO_USER_BUSINESS_INFO_TELECOM_PAGER_INTCODE, getTelecomInfoKey(TelecomType.PAGER, TelecomInfo.INTCODE, true));
      assertEquals(INFO_USER_BUSINESS_INFO_TELECOM_PAGER_LOCCODE, getTelecomInfoKey(TelecomType.PAGER, TelecomInfo.LOCCODE, true));
      assertEquals(INFO_USER_BUSINESS_INFO_TELECOM_PAGER_NUMBER, getTelecomInfoKey(TelecomType.PAGER, TelecomInfo.NUMBER, true));

      // home pager
      assertEquals(INFO_USER_HOME_INFO_TELECOM_PAGER_COMMENT, getTelecomInfoKey(TelecomType.PAGER, TelecomInfo.COMMENT, false));
      assertEquals(INFO_USER_HOME_INFO_TELECOM_PAGER_EXT, getTelecomInfoKey(TelecomType.PAGER, TelecomInfo.EXT, false));
      assertEquals(INFO_USER_HOME_INFO_TELECOM_PAGER_INTCODE, getTelecomInfoKey(TelecomType.PAGER, TelecomInfo.INTCODE, false));
      assertEquals(INFO_USER_HOME_INFO_TELECOM_PAGER_LOCCODE, getTelecomInfoKey(TelecomType.PAGER, TelecomInfo.LOCCODE, false));
      assertEquals(INFO_USER_HOME_INFO_TELECOM_PAGER_NUMBER, getTelecomInfoKey(TelecomType.PAGER, TelecomInfo.NUMBER, false));

      // business telephone
      assertEquals(INFO_USER_BUSINESS_INFO_TELECOM_TELEPHONE_COMMENT, getTelecomInfoKey(TelecomType.TELEPHONE, TelecomInfo.COMMENT, true));
      assertEquals(INFO_USER_BUSINESS_INFO_TELECOM_TELEPHONE_EXT, getTelecomInfoKey(TelecomType.TELEPHONE, TelecomInfo.EXT, true));
      assertEquals(INFO_USER_BUSINESS_INFO_TELECOM_TELEPHONE_INTCODE, getTelecomInfoKey(TelecomType.TELEPHONE, TelecomInfo.INTCODE, true));
      assertEquals(INFO_USER_BUSINESS_INFO_TELECOM_TELEPHONE_LOCCODE, getTelecomInfoKey(TelecomType.TELEPHONE, TelecomInfo.LOCCODE, true));
      assertEquals(INFO_USER_BUSINESS_INFO_TELECOM_TELEPHONE_NUMBER, getTelecomInfoKey(TelecomType.TELEPHONE, TelecomInfo.NUMBER, true));

      // home telephone
      assertEquals(INFO_USER_HOME_INFO_TELECOM_TELEPHONE_COMMENT, getTelecomInfoKey(TelecomType.TELEPHONE, TelecomInfo.COMMENT, false));
      assertEquals(INFO_USER_HOME_INFO_TELECOM_TELEPHONE_EXT, getTelecomInfoKey(TelecomType.TELEPHONE, TelecomInfo.EXT, false));
      assertEquals(INFO_USER_HOME_INFO_TELECOM_TELEPHONE_INTCODE, getTelecomInfoKey(TelecomType.TELEPHONE, TelecomInfo.INTCODE, false));
      assertEquals(INFO_USER_HOME_INFO_TELECOM_TELEPHONE_LOCCODE, getTelecomInfoKey(TelecomType.TELEPHONE, TelecomInfo.LOCCODE, false));
      assertEquals(INFO_USER_HOME_INFO_TELECOM_TELEPHONE_NUMBER, getTelecomInfoKey(TelecomType.TELEPHONE, TelecomInfo.NUMBER, false));
   }
}
