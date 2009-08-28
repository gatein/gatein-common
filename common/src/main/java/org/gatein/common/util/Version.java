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

/**
 * Version class conforming to JBoss Product Version Conventions post 2006-03-01. See
 * http://wiki.jboss.org/wiki/Wiki.jsp?page=JBossProductVersioning.
 *
 * @author <a href="mailto:julien@jboss.org">Julien Viet</a>
 * @version $Revision: 7165 $
 */
public class Version
{

   /** . */
   private final String codeName;

   /** . */
   private final String toString;

   /** . */
   private final String name;

   /** . */
   private final int major;

   /** . */
   private final int minor;

   /** . */
   private final int patch;

   /** . */
   private final Qualifier qualifier;

   public Version(String name, int major, int minor, int patch, Qualifier qualifier, String codeName)
   {
      if (name == null)
      {
         throw new IllegalArgumentException("No name provided");
      }
      if (major < 0)
      {
         throw new IllegalArgumentException("Major cannot be negative");
      }
      if (minor < 0)
      {
         throw new IllegalArgumentException("Minor cannot be negative");
      }
      if (patch < 0)
      {
         throw new IllegalArgumentException("Patch cannot be negative");
      }
      if (qualifier == null)
      {
         throw new IllegalArgumentException("Qualifier cannot be null");
      }
      if (codeName == null)
      {
         throw new IllegalArgumentException("No code name provided");
      }
      this.name = name;
      this.major = major;
      this.minor = minor;
      this.patch = patch;
      this.qualifier = qualifier;
      this.codeName = codeName;
      this.toString = Format.JBOSS_PRODUCT_CONVENTION.toString(this);
   }

   public String getCodeName()
   {
      return codeName;
   }

   /** Return the name; */
   public String getName()
   {
      return name;
   }

   /** Return the major. */
   public int getMajor()
   {
      return major;
   }

   /** Return the minor. */
   public int getMinor()
   {
      return minor;
   }

   /** Return the patch. */
   public int getPatch()
   {
      return patch;
   }

   /** Return the intermediate major. */
   public Qualifier getQualifier()
   {
      return qualifier;
   }

   public String toString()
   {
      return toString;
   }

   public String toString(Format format) throws IllegalArgumentException
   {
      if (format == null)
      {
         throw new IllegalArgumentException();
      }
      return format.toString(this);
   }

   /** Type safe enum for intermediate major. */
   public static class Qualifier
   {

      public enum Prefix
      {

         SNAPSHOT("SNAPSHOT", false, "Snapshot"),
         ALPHA("ALPHA", true, "Alpha"),
         BETA("BETA", true, "Beta"),
         CR("CR", true, "Candidate for release"),
         GA("GA", false, "General Availability"),
         SP("SP", true, "Service pack");

         /** . */
         private final String name;

         /** . */
         private final String description;

         /** . */
         private final boolean suffixable;

         private Prefix(String name, boolean suffixable, String description)
         {
            this.name = name;
            this.suffixable = suffixable;
            this.description = description;
         }

         public String getName()
         {
            return name;
         }

         public boolean isSuffixable()
         {
            return suffixable;
         }

         public String getDescription()
         {
            return description;
         }

         public String toString()
         {
            return name;
         }
      }

      public enum Suffix
      {

         EMPTY(""),
         SUFFIX_1("1"),
         SUFFIX_2("2"),
         SUFFIX_3("3"),
         SUFFIX_4("4"),
         SUFFIX_5("5"),
         SUFFIX_6("6");

         /** . */
         private final String value;

         private Suffix(String value)
         {
            this.value = value;
         }

         public String toString()
         {
            return value;
         }
      }

      /** . */
      private final String toString;

      /** . */
      private final Prefix prefix;

      /** . */
      private final Suffix suffix;

      public Qualifier(Prefix prefix)
      {
         this(prefix, Suffix.EMPTY);
      }

      public Qualifier(Prefix prefix, Suffix suffix)
      {
         if (prefix == null)
         {
            throw new IllegalArgumentException("No prefix provided");
         }
         if (suffix == null)
         {
            suffix = Suffix.EMPTY;
         }
         if (prefix.isSuffixable() == false && suffix.value.length() > 0)
         {
            throw new IllegalArgumentException("The prefix " + prefix + " is not suffixable");
         }
         this.toString = "" + prefix + suffix;
         this.prefix = prefix;
         this.suffix = suffix;
      }

      public Prefix getPrefix()
      {
         return prefix;
      }

      public Suffix getSuffix()
      {
         return suffix;
      }

      public String toString()
      {
         return toString;
      }
   }

   public interface Format
   {

      /**
       * Implement formatting as defined <a href="http://wiki.jboss.org/wiki/Wiki.jsp?page=JBossProductVersioning">here</a>
       */
      Format JBOSS_PRODUCT_CONVENTION = new Format()
      {
         public String toString(Version version)
         {
            StringBuffer buffer = new StringBuffer(version.getName());
            buffer.append(" ")
               .append(version.getMajor()).append('.')
               .append(version.getMinor()).append('.')
               .append(version.getPatch()).append('-')
               .append(version.getQualifier());
            return buffer.toString();
         }
      };

      String toString(Version version);
   }
}
