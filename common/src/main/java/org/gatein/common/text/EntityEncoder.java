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
package org.gatein.common.text;

import org.gatein.common.util.ParameterValidation;

/**
 * This encoder performs lookup for converting a char to its HTML entity representation.
 *
 * @author <a href="mailto:julien@jboss.org">Julien Viet</a>
 * @version $Revision: 630 $
 */
public class EntityEncoder extends AbstractCharEncoder
{

   public void encode(char c, CharWriter writer) throws EncodingException
   {
      ParameterValidation.throwIllegalArgExceptionIfNull(writer, "CharWriter");

      String s = charToEntity[c];

      //
      if (s != null)
      {
         writer.append('&');
         writer.append(s);
         writer.append(';');
      }
      else
      {
         writer.append(c);
      }
   }

   public void safeEncode(char[] chars, int off, int len, CharWriter writer) throws EncodingException
   {
      // The index of the last copied char
      int previous = off;

      //
      int to = off + len;

      // Perform lookup char by char
      for (int current = off; current < to; current++)
      {
         // Lookup
         String replacement = lookup(chars[current]);

         // Do we have a replacement
         if (replacement != null)
         {
            // We lazy create the result

            // Append the previous chars if any
            writer.append(chars, previous, current - previous);

            // Append the replaced entity
            writer.append('&').append(replacement).append(';');

            // Update the previous pointer
            previous = current + 1;
         }
      }

      //
      writer.append(chars, previous, chars.length - previous);
   }

   /** All HTML entities. */
   public static final EntityEncoder FULL = new EntityEncoder();

   /** All HTML entities except the HTML chars which are used to do HTML itself. */
   public static final EntityEncoder BASIC = new BasicEntityTable();

   private static class BasicEntityTable extends EntityEncoder
   {
      public BasicEntityTable()
      {
         remove('<');
         remove('>');
         remove('"');
         remove('&');
      }
   }

   /** . */
   private String[] charToEntity = new String[65536];

   protected EntityEncoder()
   {
      put(160, "nbsp");
      put(161, "iexcl");
      put(162, "cent");
      put(163, "pound");
      put(164, "curren");
      put(165, "yen");
      put(166, "brvbar");
      put(167, "sect");
      put(168, "uml");
      put(169, "copy");
      put(170, "ordf");
      put(171, "laquo");
      put(172, "not");
      put(173, "shy");
      put(174, "reg");
      put(175, "macr");
      put(176, "deg");
      put(177, "plusmn");
      put(178, "sup2");
      put(179, "sup3");
      put(180, "acute");
      put(181, "micro");
      put(182, "para");
      put(183, "middot");
      put(184, "cedil");
      put(185, "sup1");
      put(186, "ordm");
      put(187, "raquo");
      put(188, "frac14");
      put(189, "frac12");
      put(190, "frac34");
      put(191, "iquest");
      put(192, "Agrave");
      put(193, "Aacute");
      put(194, "Acirc");
      put(195, "Atilde");
      put(196, "Auml");
      put(197, "Aring");
      put(198, "AElig");
      put(199, "Ccedil");
      put(200, "Egrave");
      put(201, "Eacute");
      put(202, "Ecirc");
      put(203, "Euml");
      put(204, "Igrave");
      put(205, "Iacute");
      put(206, "Icirc");
      put(207, "Iuml");
      put(208, "ETH");
      put(209, "Ntilde");
      put(210, "Ograve");
      put(211, "Oacute");
      put(212, "Ocirc");
      put(213, "Otilde");
      put(214, "Ouml");
      put(215, "times");
      put(216, "Oslash");
      put(217, "Ugrave");
      put(218, "Uacute");
      put(219, "Ucirc");
      put(220, "Uuml");
      put(221, "Yacute");
      put(222, "THORN");
      put(223, "szlig");
      put(224, "agrave");
      put(225, "aacute");
      put(226, "acirc");
      put(227, "atilde");
      put(228, "auml");
      put(229, "aring");
      put(230, "aelig");
      put(231, "ccedil");
      put(232, "egrave");
      put(233, "eacute");
      put(234, "ecirc");
      put(235, "euml");
      put(236, "igrave");
      put(237, "iacute");
      put(238, "icirc");
      put(239, "iuml");
      put(240, "eth");
      put(241, "ntilde");
      put(242, "ograve");
      put(243, "oacute");
      put(244, "ocirc");
      put(245, "otilde");
      put(246, "ouml");
      put(247, "divide");
      put(248, "oslash");
      put(249, "ugrave");
      put(250, "uacute");
      put(251, "ucirc");
      put(252, "uuml");
      put(253, "yacute");
      put(254, "thorn");
      put(255, "yuml");
      put(402, "fnof");
      put(913, "Alpha");
      put(914, "Beta");
      put(915, "Gamma");
      put(916, "Delta");
      put(917, "Epsilon");
      put(918, "Zeta");
      put(919, "Eta");
      put(920, "Theta");
      put(921, "Iota");
      put(922, "Kappa");
      put(923, "Lambda");
      put(924, "Mu");
      put(925, "Nu");
      put(926, "Xi");
      put(927, "Omicron");
      put(928, "Pi");
      put(929, "Rho");
      put(931, "Sigma");
      put(932, "Tau");
      put(933, "Upsilon");
      put(934, "Phi");
      put(935, "Chi");
      put(936, "Psi");
      put(937, "Omega");
      put(945, "alpha");
      put(946, "beta");
      put(947, "gamma");
      put(948, "delta");
      put(949, "epsilon");
      put(950, "zeta");
      put(951, "eta");
      put(952, "theta");
      put(953, "iota");
      put(954, "kappa");
      put(955, "lambda");
      put(956, "mu");
      put(957, "nu");
      put(958, "xi");
      put(959, "omicron");
      put(960, "pi");
      put(961, "rho");
      put(962, "sigmaf");
      put(963, "sigma");
      put(964, "tau");
      put(965, "upsilon");
      put(966, "phi");
      put(967, "chi");
      put(968, "psi");
      put(969, "omega");
      put(977, "thetasym");
      put(978, "upsih");
      put(982, "piv");
      put(8226, "bull");
      put(8230, "hellip");
      put(8242, "prime");
      put(8243, "Prime");
      put(8254, "oline");
      put(8260, "frasl");
      put(8472, "weierp");
      put(8465, "image");
      put(8476, "real");
      put(8482, "trade");
      put(8501, "alefsym");
      put(8592, "larr");
      put(8593, "uarr");
      put(8594, "rarr");
      put(8595, "darr");
      put(8596, "harr");
      put(8629, "crarr");
      put(8656, "lArr");
      put(8657, "uArr");
      put(8658, "rArr");
      put(8659, "dArr");
      put(8660, "hArr");
      put(8704, "forall");
      put(8706, "part");
      put(8707, "exist");
      put(8709, "empty");
      put(8711, "nabla");
      put(8712, "isin");
      put(8713, "notin");
      put(8715, "ni");
      put(8719, "prod");
      put(8721, "sum");
      put(8722, "minus");
      put(8727, "lowast");
      put(8730, "radic");
      put(8733, "prop");
      put(8734, "infin");
      put(8736, "ang");
      put(8743, "and");
      put(8744, "or");
      put(8745, "cap");
      put(8746, "cup");
      put(8747, "int");
      put(8756, "there4");
      put(8764, "sim");
      put(8773, "cong");
      put(8776, "asymp");
      put(8800, "ne");
      put(8801, "equiv");
      put(8804, "le");
      put(8805, "ge");
      put(8834, "sub");
      put(8835, "sup");
      put(8836, "nsub");
      put(8838, "sube");
      put(8839, "supe");
      put(8853, "oplus");
      put(8855, "otimes");
      put(8869, "perp");
      put(8901, "sdot");
      put(8968, "lceil");
      put(8969, "rceil");
      put(8970, "lfloor");
      put(8971, "rfloor");
      put(9001, "lang");
      put(9002, "rang");
      put(9674, "loz");
      put(9824, "spades");
      put(9827, "clubs");
      put(9829, "hearts");
      put(9830, "diams");
      put(34, "quot");
      put(38, "amp");
      put(60, "lt");
      put(62, "gt");
      put(338, "OElig");
      put(339, "oelig");
      put(352, "Scaron");
      put(353, "scaron");
      put(376, "Yuml");
      put(710, "circ");
      put(732, "tilde");
      put(8194, "ensp");
      put(8195, "emsp");
      put(8201, "thinsp");
      put(8204, "zwnj");
      put(8205, "zwj");
      put(8206, "lrm");
      put(8207, "rlm");
      put(8211, "ndash");
      put(8212, "mdash");
      put(8216, "lsquo");
      put(8217, "rsquo");
      put(8218, "sbquo");
      put(8220, "ldquo");
      put(8221, "rdquo");
      put(8222, "bdquo");
      put(8224, "dagger");
      put(8225, "Dagger");
      put(8240, "permil");
      put(8249, "lsaquo");
      put(8250, "rsaquo");
      put(8364, "euro");
   }

   protected final void put(int c, String entity)
   {
      charToEntity[c] = entity;
   }

   protected final void remove(int c)
   {
      charToEntity[c] = null;
   }

   /**
    * Returns null if no entity is found or return the converted entity.
    *
    * @param c the char to encode
    * @return the corresponding encoded string or null
    */
   public final String lookup(char c)
   {
      return charToEntity[c];
   }
}
