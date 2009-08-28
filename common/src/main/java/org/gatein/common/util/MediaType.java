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
package org.gatein.common.util;

import javax.activation.MimeType;
import javax.activation.MimeTypeParseException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * This class is going to be removed any time soon in favoir of org.jboss.portal.common.net.media.MediaType
 *
 * This is a immutable wrapper to the activation MimeTpye.
 * <p>This class contains some extensions to the activation MimeType, such as the typesafe enum
 * pattern, and allows for a mime type to specify allowed sub types.</p>
 *
 * @author <a href="mailto:mholzner@novell.com">Martin Holzner</a>
 * @version $LastChangedRevision: 6704 $, $LastChangedDate: 2007-03-15 23:36:31 +0100 (Thu, 15 Mar 2007) $
 * @see javax.activation.MimeType
 *      see also ftp://ftp.isi.edu/in-notes/iana/assignments/media-types/
 */
@Deprecated
public final class MediaType
{
   private static Map allowedTypes = new HashMap();
   private static Map supportedExtensions = new HashMap();

   /**
    * Mime type 'Any' maps to an accept mime type of '*.*' (used by IE for css, images, etc.)
    */
   public static final MediaType ANY =
      new MediaType("*", "*", new String[]{});

   /**
    * Mime type xhtml
    */
   public static final MediaType XHTML = new MediaType("application", "xhtml+xml",
      new String[]{"xhtml"});

   /**
    * Mime type html
    */
   public static final MediaType HTML = new MediaType("text", "html",
      new String[]{"html", "htm"},
      new MediaType[]{XHTML});

   /**
    * Mime type form (application/x-www-form-urlencoded)
    */
   public static final MediaType FORM =
      new MediaType("application", "x-www-form-urlencoded", new String[]{});

   /**
    * Mime type xml
    */
   public static final MediaType XML = new MediaType("text", "xml", new String[]{"xml"},
      new MediaType[]{XHTML});

   /**
    * Mime type wml
    */
   public static final MediaType WML = new MediaType("text", "vnd.wap.wml",
      new String[]{"wml"});

   /**
    * Mime type css
    */
   public static final MediaType CSS = new MediaType("text", "css",
      new String[]{"css"});

   /**
    * Mime type text
    */
   public static final MediaType TEXT = new MediaType("text", "plain",
      new String[]{"txt"});

   /**
    * Mime type js
    */
   public static final MediaType JS = new MediaType("text", "javascript",
      new String[]{"js"});

   /**
    * Mime type svg
    */
   public static final MediaType SVG = new MediaType("image", "svg+xml",
      new String[]{"svg"});

   /**
    * Mime type jpeg
    */
   public static final MediaType JPEG = new MediaType("image", "jpeg",
      new String[]{"jpeg", "jpg"});

   /**
    * Mime type gif
    */
   public static final MediaType GIF = new MediaType("image", "gif",
      new String[]{"gif"});

   /**
    * Mime type png
    */
   public static final MediaType PNG = new MediaType("image", "png",
      new String[]{"png"});

   /**
    * Mime type wbmp
    */
   public static final MediaType WBMP = new MediaType("image", "vnd.wap.wbmp",
      new String[]{"wbpm"});

   /**
    * Mime type rss
    */
   public static final MediaType RSS = new MediaType("application", "rss+xml",
      new String[]{});

   /**
    * Mime type ico (see http://filext.com/detaillist.php?extdetail=ICO)
    */
   public static final MediaType ICO = new MediaType("application", "octet-stream",
      new String[]{"ico"});
   private MimeType m_mimeType = null;
   private MediaType[] m_allowedSubTypes;
   private Set allowSubTypeSet;

   /**
    * Construct a mime type instance without any allowed subtypes.
    *
    * @param primaryType the primary type of the mime type (i.e. 'text')
    * @param subType     the sub type of the mime type (i.e. 'html')
    */
   private MediaType(String primaryType, String subType, String[] extensions)
   {
      try
      {
         m_mimeType = new MimeType(primaryType, subType);
         m_allowedSubTypes = null;
         allowSubTypeSet = null;
         allowedTypes.put(m_mimeType.getBaseType(), this);
         for (int i = 0; i < extensions.length; i++)
         {
            supportedExtensions.put(extensions[i], this);
         }
      }
      catch (MimeTypeParseException e)
      {
         // +++TODO handle this , but where ?
         System.out.println(e.getMessage());
      }
   }

   /**
    * Construct a mime type with the provided allowed subtypes.
    *
    * @param primaryType     the primary type of the mime type (i.e. 'text')
    * @param subType         the sub type of the mime type (i.e. 'html')
    * @param allowedSubTypes an array of <code>MediaType</code>s to allow as valid subtypes of this type
    */
   private MediaType(String primaryType, String subType, String[] extensions, MediaType[] allowedSubTypes)
   {
      this(primaryType, subType, extensions);
      // only if the mime type was sucessfully created
      if (m_mimeType != null)
      {
         m_allowedSubTypes = allowedSubTypes;
         allowSubTypeSet = new HashSet(Arrays.asList(allowedSubTypes));
      }
   }

   /**
    * Get the mime type for the presented string, if the string contains a valid mime type.
    *
    * @param mimeType the <code>java.lang.String</code> to parse into a <code>RegistryMimeTpye</code>
    * @return the <code>RegistryMimeTpye</code> that matches with the presented string
    * @throws MimeTypeParseException   if the presented mimetype is not supported
    * @throws IllegalArgumentException if the presented string is null or empty
    */
   public static MediaType parseMimeType(String mimeType) throws MimeTypeParseException
   {
      if (mimeType == null || "".equals(mimeType))
      {
         throw new IllegalArgumentException("no valid mime type provided");
      }

      String type = mimeType.trim().toLowerCase();
      if (allowedTypes.keySet().contains(type))
      {
         return (MediaType)allowedTypes.get(type);
      }

      throw new MimeTypeParseException("Type [" + mimeType + "] not supported");
   }

   /**
    * Get the mime type for the presented string.
    * <p>The string is handles as a file name extension. example: 'xml' returns MediaType.XML</p>
    *
    * @param extension the <code>java.lang.String</code> to parse into a <code>RegistryMimeTpye</code>
    * @return the <code>RegistryMimeTpye</code> that matches with the presented string
    * @throws MimeTypeParseException   if the presented mimetype is not supported
    * @throws IllegalArgumentException if the presented string is null or empty
    */
   public static MediaType parseMimeTypeByExtension(String extension)
      throws MimeTypeParseException
   {
      if (extension == null || "".equals(extension))
      {
         throw new IllegalArgumentException("no valid mime type provided [" + extension + "]");
      }

      String ext = extension.trim().toLowerCase();
      if (supportedExtensions.keySet().contains(ext))
      {
         return (MediaType)supportedExtensions.get(ext);
      }

      throw new MimeTypeParseException("Extension [" + extension + "] not supported");
   }

   /**
    * Get a list of allowed sub types for the passed mime type.
    *
    * @param mimeType the <code>RegistryMimeTpye</code> to get the list of allowed subtypes for
    * @return a <code>java.util.List</code> of <code>PortalMimeTpye</code>s
    */
   public static List getAllowedSubTypes(MediaType mimeType)
   {
      if (mimeType.m_allowedSubTypes == null)
      {
         return Collections.EMPTY_LIST;
      }
      else
      {
         return Collections.unmodifiableList(Arrays.asList(mimeType.m_allowedSubTypes));
      }
   }

   /**
    * Get a list of allowed sub types for for this mime type.
    *
    * @return a <code>java.util.List</code> of <code>RegistryMimeTpye</code>s
    */
   public List getAllowedSubTypes()
   {
      if (m_allowedSubTypes == null)
      {
         return Collections.EMPTY_LIST;
      }
      else
      {
         return Collections.unmodifiableList(Arrays.asList(m_allowedSubTypes));
      }
   }

   /**
    * Return true if the allowed sub types contains the specified media type.
    *
    * @param other the sub type to test
    * @return true if it is an allowed sub type
    */
   public boolean isAllowedSubType(MediaType other)
   {
      if (equals(other))
      {
         return true;
      }
      if (allowSubTypeSet == null)
      {
         return false;
      }
      return allowSubTypeSet.contains(other);
   }

   /**
    * Get the String representation of the mime type (i.e. 'text/html').
    *
    * @return the mime type as a <code>java.lang.String</code>
    * @see java.lang.Object#toString
    */
   public String toString()
   {
      return m_mimeType.getBaseType();
   }

   /**
    * compare the parameter with this instance and see if they are equals.
    *
    * @param o the Object to compare this instance to
    * @return true if this and the paramters o are equal
    * @see java.lang.Object#equals
    */
   public boolean equals(Object o)
   {
      if (this == o)
      {
         return true;
      }
      if (!(o instanceof MediaType))
      {
         return false;
      }

      final MediaType type = (MediaType)o;

      return m_mimeType.equals(type.m_mimeType);
   }

   /**
    * Get the hascode for this mime type.
    *
    * @return an int value representing this instance
    * @see java.lang.Object#hashCode
    */
   public int hashCode()
   {
      return m_mimeType.hashCode();
   }
}
