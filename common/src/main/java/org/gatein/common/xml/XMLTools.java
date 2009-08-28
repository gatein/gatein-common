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
package org.gatein.common.xml;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Set;

/**
 * Utilities for dealing with XML.
 *
 * @author <a href="mailto:mholzner@novell.com">Martin Holzner</a>
 * @author <a href="mailto:julien@jboss.org">Julien Viet</a>
 * @version $Revision: 7686 $
 */
public class XMLTools
{

   /** Namespace URI for XML lang. */
   public static final String XML_LANG_NAMESPACE_URI = "http://www.w3.org/XML/1998/namespace";

   /** Default output format which is : no xml declaration, no document type, indent. */
   private static Properties DEFAULT_FORMAT = createFormat(true, false, true, "utf-8");

   /** . */
   public static final String PARAM_YES = "yes";

   /** . */
   public static final String PARAM_NO = "no";

   /** . */
   public static final String ATTRIB_OMIT_XML_DECLARATION = "omit-xml-declaration";

   /** . */
   public static final String ATTRIB_CDATA_SECTION_ELEMENTS = "cdata-section-elements";

   /** . */
   public static final String ATTRIB_METHOD = "method";

   /** . */
   public static final String ATTRIB_INDENT = "indent";

   /** . */
   public static final String ATTRIB_HREF = "href";

   /** . */
   public static final String DIRECTIVE_IMPORT = "xsl:import";

   /** . */
   public static final String DIRECTIVE_INCLUDE = "xsl:include";

   /** . */
   public static final boolean DEFAULT_NAMESPACE_AWARE = true;

   /** . */
   public static final boolean DEFAULT_VALIDATION = false;

   /** prevent instantiation */
   private XMLTools()
   {
   }

   /** Return the builder factory. */
   public static DocumentBuilderFactory getDocumentBuilderFactory()
   {
      return DocumentBuilderFactory.newInstance();
   }

   /**
    *
    */
   private static Properties createFormat(boolean omitXMLDeclaration, boolean standalone, boolean indented, String encoding)
   {
      Properties format = new Properties();
      format.setProperty(OutputKeys.OMIT_XML_DECLARATION, omitXMLDeclaration ? "yes" : "no");
      format.setProperty(OutputKeys.STANDALONE, standalone ? "yes" : "no");
      format.setProperty(OutputKeys.INDENT, indented ? "yes" : "no");
      format.setProperty(OutputKeys.ENCODING, encoding);
      return format;
   }

   /**
    *
    */
   public static String toString(Document doc, boolean omitXMLDeclaration, boolean standalone, boolean indented, String encoding) throws TransformerException
   {
      Properties format = createFormat(omitXMLDeclaration, standalone, indented, encoding);
      return toString(doc, format);
   }

   /**
    * Serialize the document with the default format : - No XML declaration - Indented - Encoding is UTF-8
    *
    * @see #toString(Document,Properties)
    */
   public static String toString(Document doc) throws TransformerException
   {
      return toString(doc, DEFAULT_FORMAT);
   }

   /** @see #toString(Document) */
   public static String toString(Element element) throws ParserConfigurationException, TransformerException
   {
      return toString(element, DEFAULT_FORMAT);
   }

   /** Converts an element to a String representation. */
   private static String toString(Element element, Properties properties) throws ParserConfigurationException, TransformerException
   {
      Document doc = getDocumentBuilderFactory().newDocumentBuilder().newDocument();
      element = (Element)doc.importNode(element, true);
      doc.appendChild(element);
      return toString(doc, properties);
   }

   /** Converts an document to a String representation. */
   private static String toString(Document doc, Properties format) throws TransformerException
   {
      Transformer transformer = TransformerFactory.newInstance().newTransformer();
      transformer.setOutputProperties(format);
      StringWriter writer = new StringWriter();
      Source source = new DOMSource(doc);
      Result result = new StreamResult(writer);
      transformer.transform(source, result);
      return writer.toString();
   }

   /** Parse a string into a document. */
   public static Document toDocument(String text) throws ParserConfigurationException, SAXException, IOException
   {
      DocumentBuilder builder = getDocumentBuilderFactory().newDocumentBuilder();
      StringReader reader = new StringReader(text);
      InputSource source = new InputSource();
      source.setCharacterStream(reader);
      return builder.parse(source);
   }

   /** Parse a string into an element. */
   public static Element toElement(String text) throws ParserConfigurationException, SAXException, IOException
   {
      Document doc = toDocument(text);
      return doc.getDocumentElement();
   }

   public static Document toDocument(Element element) throws ParserConfigurationException, SAXException, IOException
   {
      if (element == null)
      {
         throw new IllegalArgumentException();
      }

      //
      Document doc = getDocumentBuilderFactory().newDocumentBuilder().newDocument();
      element = (Element)doc.importNode(element, true);
      doc.appendChild(element);
      return doc;
   }

   /**
    * Perform trimming by default
    *
    * @param element
    * @return
    * @throws IllegalArgumentException
    * @see #asString(org.w3c.dom.Element,boolean)
    */
   public static String asString(Element element) throws IllegalArgumentException
   {
      return asString(element, true);
   }

   /**
    * Get the element's content as a string.
    *
    * @param element the container
    * @param trim    true if text should be trimmed before returning result
    * @throws IllegalArgumentException if the element content is mixed or null
    */
   public static String asString(Element element, boolean trim) throws IllegalArgumentException
   {
      if (element == null)
      {
         throw new IllegalArgumentException("No null element allowed");
      }

      //
      StringBuffer buffer = new StringBuffer();
      NodeList children = element.getChildNodes();
      for (int i = 0; i < children.getLength(); i++)
      {
         Node child = children.item(i);
         switch (child.getNodeType())
         {
            case Node.CDATA_SECTION_NODE:
            case Node.TEXT_NODE:
               buffer.append(((Text)child).getData());
               break;
            case Node.ELEMENT_NODE:
               throw new IllegalArgumentException("Mixed content not allowed");
            default:
               break;
         }
      }
      String result = buffer.toString();
      if (trim)
      {
         result = result.trim();
      }
      return result;
   }

   /**
    * Return the optional unique child of an element.
    *
    * @param element the parent element
    * @param strict  true if the element must be present
    * @return the child element or null if it does not exist and strict is false
    * @throws IllegalArgumentException if an argument is null
    * @throws NoSuchElementException   if strict is true and the element is not present
    * @throws TooManyElementException  if more than one element is found
    */
   public static Element getUniqueChild(Element element, boolean strict) throws IllegalArgumentException,
      NoSuchElementException, TooManyElementException
   {
      if (element == null)
      {
         throw new IllegalArgumentException("No element specified");
      }
      Element childElt = null;
      NodeList list = element.getChildNodes();
      for (int i = 0; i < list.getLength(); i++)
      {
         Node childNode = list.item(i);
         if (childNode instanceof Element)
         {
            if (childElt == null)
            {
               childElt = (Element)childNode;
            }
            else
            {
               throw new TooManyElementException("More than one child element for element " + element.getNodeName());
            }
         }
      }
      if (strict && childElt == null)
      {
         throw new NoSuchElementException("No child element for element " + element.getNodeName());
      }
      return childElt;
   }

   /**
    * Return an optional child of an element with the specified name.
    *
    * @param element the parent element
    * @param name    the child name
    * @param strict  if the child must be present
    * @return the child element or null if it does not exist and strict is set to false
    * @throws IllegalArgumentException if an argument is null
    * @throws NoSuchElementException   if strict is true and the element is not present
    * @throws TooManyElementException  if more than one element is found
    */
   public static Element getUniqueChild(Element element, String name, boolean strict) throws IllegalArgumentException,
      NoSuchElementException, TooManyElementException
   {
      return getUniqueChild(element, null, name, strict);
   }

   /**
    * Return an optional child of an element with the specified name and the optionally specified namespace uri.
    *
    * @param element the parent element
    * @param name the child name
    * @param uri the child uri
    * @param strict if the child must be present
    * @return the child element or null if it does not exist and strict is set to false
    * @throws IllegalArgumentException if an argument is null
    * @throws NoSuchElementException   if strict is true and the element is not present
    * @throws TooManyElementException  if more than one element is found
    */
   public static Element getUniqueChild(Element element, String uri, String name, boolean strict) throws IllegalArgumentException,
      NoSuchElementException, TooManyElementException
   {
      List list = getChildren(element, uri, name);
      switch (list.size())
      {
         case 0:
            if (strict)
            {
               throw new NoSuchElementException("Missing child " + name + " of element " + element.getNodeName());
            }
            else
            {
               return null;
            }
         case 1:
            return (Element)list.get(0);
         default:
            throw new TooManyElementException("Too many children for element " + element.getNodeName());
      }
   }

   /**
    * Return an iterator for all the children of the given element.
    *
    * @param element the parent element
    * @return an iterator for the designated elements
    * @throws IllegalArgumentException if the element is null or the name is null
    */
   public static Iterator<Element> getChildrenIterator(Element element) throws IllegalArgumentException
   {
      return getChildren(element).iterator();
   }

   /**
    * Return an iterator for all the children of the given element having the specified name.
    *
    * @param element the parent element
    * @param name    the child names
    * @return an iterator for the designated elements
    * @throws IllegalArgumentException if the element is null or the name is null
    */
   public static Iterator<Element> getChildrenIterator(Element element, String name) throws IllegalArgumentException
   {
      return getChildren(element, name).iterator();
   }

   /**
    * Return an iterator for all the children of the given element having the specified name and the optionally
    * specified namesspace uri.
    *
    * @param element the parent element
    * @param uri the children uri
    * @param name the children name
    * @return an iterator for the designated elements
    * @throws IllegalArgumentException if the element is null or the name is null
    */
   public static Iterator<Element> getChildrenIterator(Element element, String uri, String name) throws IllegalArgumentException
   {
      return getChildren(element, uri, name).iterator();
   }

   /**
    * Return all the children of the given element. The collection object can be modified.
    *
    * @param element the parent element
    * @return a list of elements
    * @throws IllegalArgumentException if the element is null or the name is null
    */
   public static List<Element> getChildren(Element element) throws IllegalArgumentException
   {
      return getChildren(element, null, null);
   }

   /**
    * Return all the children of the given element having the specified name. The collection object can be modified.
    *
    * @param element the parent element
    * @param name    the child names
    * @return a list of elements
    * @throws IllegalArgumentException if the element is null or the name is null
    */
   public static List<Element> getChildren(Element element, String name) throws IllegalArgumentException
   {
      return getChildren(element, null, name);
   }

   /**
    * <p>Return all the children of the given element having the optionally specified name and the namespace URI.</p>
    *
    * <p>If the URI is specified then the element must have the same URI namespace in order to be included otherwise
    * it will be included. If the URI is specified the name matching will be done against the element local name
    * otherwise it will be done against the element tag name.</p>
    *
    * <p>If the name is specified then the element must have the same tag name or the same local name to be retained
    * otherwise it will be included.</p>
    *
    * <p>The resulting element collection can be safely modified.</p>
    *
    * @param element the parent element
    * @param uri the children uri
    * @param name the children name
    * @return a list of elements
    * @throws IllegalArgumentException if the element is null
    */
   public static List<Element> getChildren(Element element, String uri, String name) throws IllegalArgumentException
   {
      return getChildren(element, byName(uri, name));
   }

   /**
    * <p>Return all the children of the given node that match the provided filter.</p>
    *
    * <p>The resulting element collection can be safely modified.</p>
    *
    * @param node the parent element
    * @param filter the filter
    * @return a list of elements
    * @throws IllegalArgumentException if the element is null
    */
   @SuppressWarnings("unchecked")
   public static <T extends Node> List<T> getChildren(Node node, Filter<T> filter) throws IllegalArgumentException
   {
      if (node == null)
      {
         throw new IllegalArgumentException("No node provided");
      }
      if (filter == null)
      {
         throw new IllegalArgumentException("No filter provided");
      }

      //
      ArrayList result = new ArrayList();

      //
      NodeList list = node.getChildNodes();
      for (int i = 0; i < list.getLength(); i++)
      {
         Node child = list.item(i);

         //
         Class<T> nodeType = filter.getNodeClass();

         //
         if (nodeType.isInstance(child))
         {
            T typedChild = nodeType.cast(child);

            //
            if (filter.accept(typedChild))
            {
               result.add(child);
            }
         }
      }

      // It is fine
      return result;
   }

   public static interface Filter<N extends Node>
   {

      Class<N> getNodeClass();

      boolean accept(N node);
   }

   public static Filter<Element> byName(final String uri, final Set<String> names)
   {
      return new Filter<Element>()
      {
         public Class<Element> getNodeClass()
         {
            return Element.class;
         }

         public boolean accept(Element element)
         {
            if (uri == null)
            {
               if (names.contains(element.getTagName()))
               {
                  return true;
               }
            }
            else if (uri.equals(element.getNamespaceURI()))
            {
               if (names.contains(element.getLocalName()))
               {
                  return true;
               }
            }

            //
            return false;
         }
      };

   }

   public static Filter<Element> byName(final String uri, final String name)
   {
      return new Filter<Element>()
      {
         public Class<Element> getNodeClass()
         {
            return Element.class;
         }

         public boolean accept(Element element)
         {
            if (uri == null)
            {
               if (name == null || element.getTagName().equals(name))
               {
                  return true;
               }
            }
            else if (uri.equals(element.getNamespaceURI()))
            {
               if (name == null || element.getLocalName().equals(name))
               {
                  return true;
               }
            }

            //
            return false;
         }
      };

   }

   public static Properties loadXMLProperties(Element propertiesElt)
   {
      if (propertiesElt == null)
      {
         throw new IllegalArgumentException();
      }
      Properties props = new Properties();
      for (Element entryElt : getChildren(propertiesElt, "entry"))
      {
         String key = entryElt.getAttribute("key");
         String value = asString(entryElt);
         props.put(key, value);
      }
      return props;
   }

   public static Properties loadXMLProperties(Document doc)
   {
      if (doc == null)
      {
         throw new IllegalArgumentException();
      }
      return loadXMLProperties(doc.getDocumentElement());
   }

}
