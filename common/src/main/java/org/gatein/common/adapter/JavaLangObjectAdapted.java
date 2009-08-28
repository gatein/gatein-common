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
package org.gatein.common.adapter;

/**
 * Defines contract for the <code>java.lang.Object</code> adapted.
 *
 * @author <a href="mailto:julien@jboss.org">Julien Viet</a>
 * @version $Revision: 7228 $
 */
public interface JavaLangObjectAdapted
{
   /**
    * Implementation of <code>toString()</code>.
    *
    * @param adaptable the adaptable object
    * @return the string value
    */
   String toString(ClassAdaptable adaptable);

   /**
    * Implementation of <code>hashCode</code>.
    *
    * @param adaptable the adaptable object
    * @return the hash code value
    */
   int hashCode(ClassAdaptable adaptable);

   /**
    * Implementation of <code>equals</code>.
    *
    * @param adaptable the adaptable object
    * @param obj       the object to test equality with
    * @return true the equals value
    */
   boolean equals(ClassAdaptable adaptable, Object obj);
}
