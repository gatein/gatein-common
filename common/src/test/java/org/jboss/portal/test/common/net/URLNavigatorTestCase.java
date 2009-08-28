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

import java.io.File;
import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;

import org.gatein.common.net.URLFilter;
import org.gatein.common.net.URLNavigator;
import org.gatein.common.net.URLVisitor;
import org.gatein.common.net.URLTools;

/**
 * @author <a href="mailto:julien@jboss.org">Julien Viet</a>
 * @version $Revision: 7448 $
 */
public class URLNavigatorTestCase extends TestCase
{

   public URLNavigatorTestCase(String name)
   {
      super(name);
   }

   // the name of the jar that contains the tests
   private final String TEST_JAR_NAME="test.jar";

   
   ArrayList expectedAtomsC1 = new ArrayList();
   ArrayList expectedAtomsB1 = new ArrayList();
   ArrayList expectedAtomsA1 = new ArrayList();
   ArrayList expectedAtomsB1Dash = new ArrayList();
   ArrayList expectedAtomsD1txt = new ArrayList();
   ArrayList expectedAtomsC1txt = new ArrayList();
   ArrayList expectedAtomsB1txt = new ArrayList();
   ArrayList expectedAtomsB2txt = new ArrayList();
   ArrayList expectedAtomsA3txt = new ArrayList();
   
   ArrayList expectedURLsC1 = new ArrayList();
   ArrayList expectedURLsB1 = new ArrayList();
   ArrayList expectedURLsA1 = new ArrayList();
   ArrayList expectedURLsB1Dash = new ArrayList();
   ArrayList expectedURLsD1txt = new ArrayList();
   ArrayList expectedURLsC1txt = new ArrayList();
   ArrayList expectedURLsB1txt = new ArrayList();
   ArrayList expectedURLsB2txt = new ArrayList();
   ArrayList expectedURLsA3txt = new ArrayList();
   
   Filter noFilter;
   Filter fullFilter;
   Filter noDirFilter;
   Filter noFileFilter;
   
   protected void setUp() throws Exception
   {
      
      expectedURLsD1txt = new ArrayList();
      expectedURLsD1txt.add("/a1/b1/c1/d1.txt");
      
      expectedURLsC1 = new ArrayList();
      expectedURLsC1.add("/a1/b1/c1/");
      expectedURLsC1.addAll(expectedURLsD1txt);
      expectedURLsC1.add("/a1/b1/c1/");
      
      expectedURLsC1txt = new ArrayList();
      expectedURLsC1txt.add("/a1/b1/c1.txt");
      
      expectedURLsB1 = new ArrayList();
      expectedURLsB1.add("/a1/b1/");
      expectedURLsB1.addAll(expectedURLsC1);
      expectedURLsB1.addAll(expectedURLsC1txt);
      expectedURLsB1.add("/a1/b1/");
      
      expectedURLsB1Dash = new ArrayList();
      expectedURLsB1Dash.add("/a1/b1-/");
      expectedURLsB1Dash.add("/a1/b1-/");
      
      expectedURLsB1txt = new ArrayList();
      expectedURLsB1txt.add("/a1/b1.txt");
      
      expectedURLsB2txt = new ArrayList();
      expectedURLsB2txt.add("/a1/b2.txt");
      
      expectedURLsA1 = new ArrayList();
      expectedURLsA1.add("/a1/");
      expectedURLsA1.addAll(expectedURLsB1);
      expectedURLsA1.addAll(expectedURLsB1Dash);
      expectedURLsA1.addAll(expectedURLsB1txt);
      expectedURLsA1.addAll(expectedURLsB2txt);
      expectedURLsA1.add("/a1/");
   
      expectedURLsA3txt = new ArrayList();
      expectedURLsA3txt.add("a3.txt");
    
      expectedAtomsD1txt = new ArrayList();
      expectedAtomsD1txt.add("d1.txt");
      
      expectedAtomsC1 = new ArrayList();
      expectedAtomsC1.add("<c1>");
      expectedAtomsC1.addAll(expectedAtomsD1txt);
      expectedAtomsC1.add("</c1>");
     
      expectedAtomsC1txt = new ArrayList();
      expectedAtomsC1txt.add("c1.txt");
      
      expectedAtomsB1 = new ArrayList();
      expectedAtomsB1.add("<b1>");
      expectedAtomsB1.addAll(expectedAtomsC1);
      expectedAtomsB1.addAll(expectedAtomsC1txt);
      expectedAtomsB1.add("</b1>");
      
      expectedAtomsB1Dash = new ArrayList();
      expectedAtomsB1Dash.add("<b1->");
      expectedAtomsB1Dash.add("</b1->");
      
      expectedAtomsB1txt = new ArrayList();
      expectedAtomsB1txt.add("b1.txt");
      
      expectedAtomsB2txt = new ArrayList();
      expectedAtomsB2txt.add("b2.txt");
      
      expectedAtomsA1 = new ArrayList();
      expectedAtomsA1.add("<a1>");
      expectedAtomsA1.addAll(expectedAtomsB1);
      expectedAtomsA1.addAll(expectedAtomsB1Dash);
      expectedAtomsA1.addAll(expectedAtomsB1txt);
      expectedAtomsA1.addAll(expectedAtomsB2txt);
      expectedAtomsA1.add("</a1>");
      
      expectedAtomsA3txt = new ArrayList();
      expectedAtomsA3txt.add("a3.txt");
      
      noFilter = new Filter(true, true);
      fullFilter = new Filter(false, false);
      noDirFilter = new Filter(true, false);
      noFileFilter = new Filter(false, true);
   }

   protected void tearDown() throws Exception
   {
   }

   
//// Root tests
   
   public void testRootWithFile() throws Exception
   {
      URL fileURL = getFileURL ("test-jar");

      ArrayList expectedAtoms = new ArrayList();
      expectedAtoms.add("<test-jar>");
      expectedAtoms.addAll(expectedAtomsA1);
      expectedAtoms.addAll(expectedAtomsA3txt);
      expectedAtoms.add("</test-jar>");
      
      ArrayList expectedURLs = new ArrayList();
      expectedURLs.add("test-jar/");
      expectedURLs.addAll(expectedURLsA1);
      expectedURLs.addAll(expectedURLsA3txt);
      expectedURLs.add("test-jar/");
      
      doTest (fileURL, expectedAtoms, expectedURLs, noFilter);
      doTest (fileURL, expectedAtoms, expectedURLs, null);
      doTest (fileURL, new ArrayList(), new ArrayList(), fullFilter);
      doTest (fileURL, removeFiles(expectedAtoms), removeFiles(expectedURLs), noFileFilter);
      //since we pass it a directory, we can't enter the directory
      doTest (fileURL, new ArrayList(), new ArrayList(),noDirFilter);
     
   }

   public void testRootWithJar() throws Exception
   {
      URL jarURL = getJarURL ("/");
      
      ArrayList expectedAtoms = new ArrayList();
      expectedAtoms.add("</>");
      expectedAtoms.add("<META-INF>");
      expectedAtoms.add("MANIFEST.MF");
      expectedAtoms.add("</META-INF>");
      expectedAtoms.addAll(expectedAtomsA1);
      expectedAtoms.addAll(expectedAtomsA3txt);
      //TODO: should this really be //?
      expectedAtoms.add("<//>");
      
      ArrayList expectedURLs = new ArrayList();
      expectedURLs.add("/");
      expectedURLs.add("/META-INF/");
      expectedURLs.add("/META-INF/MANIFEST.MF");
      expectedURLs.add("/META-INF/");
      expectedURLs.addAll(expectedURLsA1);
      expectedURLs.addAll(expectedURLsA3txt);
      expectedURLs.add("/");
      
      doTest (jarURL, expectedAtoms, expectedURLs, noFilter);
      doTest (jarURL, new ArrayList(), new ArrayList(), fullFilter);      
      doTest (jarURL, removeFiles(expectedAtoms), removeFiles(expectedURLs), noFileFilter);
      doTest (jarURL, new ArrayList(), new ArrayList(), noDirFilter);
      
   }
   
//// Directory Test
       
   public void testDirectoryWithFile() throws Exception
   {
      URL fileURL = getFileURL("test-jar/a1/");
      doDirectoryTest(fileURL);
   }
   
   public void testDirectoryWithJar() throws Exception
   {
      URL jarURL = getJarURL("/a1/");
      doDirectoryTest(jarURL);
   }

   private void doDirectoryTest(URL url) throws Exception
   {
      doTest (url, expectedAtomsA1, expectedURLsA1, noFilter);
      doTest (url, expectedAtomsA1, expectedURLsA1, new Filter());
      doTest (url, new ArrayList(), new ArrayList(), fullFilter);
      doTest (url, removeFiles(expectedAtomsA1), removeFiles(expectedURLsA1), noFileFilter);
      // since we pass it a directory, we can't enter the directory
      doTest (url, new ArrayList(), new ArrayList(),noDirFilter);
      
      Filter mixFilter = new Filter();
      List dirSeq = new ArrayList();
      dirSeq.add(Boolean.TRUE); //a1
      dirSeq.add(Boolean.TRUE); //b1
      dirSeq.add(Boolean.TRUE); //c1
      dirSeq.add(Boolean.FALSE); //b1-
      
      List fileSeq = new ArrayList();
      fileSeq.add(Boolean.TRUE); //d1.txt
      fileSeq.add(Boolean.FALSE); //c1.txt
      fileSeq.add(Boolean.FALSE); //b1.txt
      fileSeq.add(Boolean.FALSE); //b2.txt
      
      mixFilter.setAcceptDir(dirSeq);
      mixFilter.setAcceptFile(fileSeq);
      
      ArrayList expectedMixAtoms = new ArrayList();
      expectedMixAtoms.add("<a1>");
      expectedMixAtoms.add("<b1>");
      expectedMixAtoms.add("<c1>");
      expectedMixAtoms.add("d1.txt");
      expectedMixAtoms.add("</c1>");
      expectedMixAtoms.add("</b1>");
      expectedMixAtoms.add("</a1>");
      
      ArrayList expectedMixURLs = new ArrayList();
      expectedMixURLs.add("/a1/");
      expectedMixURLs.add("/a1/b1/");
      expectedMixURLs.add("/a1/b1/c1/");
      expectedMixURLs.add("/a1/b1/c1/d1.txt");
      expectedMixURLs.add("/a1/b1/c1/");
      expectedMixURLs.add("/a1/b1/");
      expectedMixURLs.add("/a1/");
      
      doTest (url, expectedMixAtoms, expectedMixURLs, mixFilter);
   }
   
//// SubDirectory Test

   public void testSubDirectoryWithFile() throws Exception
   {
      URL fileURL = getFileURL ("test-jar/a1/b1");
      doSubDirectoryTest(fileURL);
   }
   
   public void testSubDirectoryWithJar() throws Exception
   {
      URL jarURL = getJarURL("/a1/b1/");
      doSubDirectoryTest(jarURL);
   }
   
   private void doSubDirectoryTest(URL url) throws Exception
   {
      doTest (url, expectedAtomsB1, expectedURLsB1, noFilter);
      doTest (url, new ArrayList(), new ArrayList(), fullFilter);
      doTest (url, new ArrayList(), new ArrayList(), fullFilter);
      doTest (url, removeFiles(expectedAtomsB1), removeFiles(expectedURLsB1), noFileFilter);
      // since we pass it a directory, we can't enter the directory
      doTest (url, new ArrayList(), new ArrayList(),noDirFilter);
      
      Filter mixFilter = new Filter();
      List dirSeq = new ArrayList();
      dirSeq.add(Boolean.TRUE); //b1
      dirSeq.add(Boolean.FALSE); //c1
      
      List fileSeq = new ArrayList();
      fileSeq.add (Boolean.TRUE); //c1.txt
      
      mixFilter.setAcceptDir(dirSeq);
      mixFilter.setAcceptFile(fileSeq);
      
      ArrayList expectedMixAtoms = new ArrayList();
      expectedMixAtoms.add("<b1>");
      expectedMixAtoms.addAll(expectedAtomsC1txt);
      expectedMixAtoms.add("</b1>");
            
      ArrayList expectedMixURLs = new ArrayList();
      expectedMixURLs.add("/a1/b1/");
      expectedMixURLs.add("/a1/b1/c1.txt");
      expectedMixURLs.add("/a1/b1/");
      
      doTest (url, expectedMixAtoms, expectedMixURLs, mixFilter);
   }
   
/// SingleFileTest
   
   public void testSingleFileWithFile() throws Exception
   {
      URL fileURL = getFileURL("test-jar/a1/b1/c1/d1.txt");
      doSingleFileTest(fileURL);
   }
   
   public void testSingleFileWithJar() throws Exception
   {
      URL jarURL = getJarURL("/a1/b1/c1/d1.txt");
      doSingleFileTest(jarURL);
   }

   private void doSingleFileTest(URL url) throws Exception
   {
      doTest (url, expectedAtomsD1txt, expectedURLsD1txt, noFilter);
      doTest (url, new ArrayList(), new ArrayList(), fullFilter);
      doTest (url, removeFiles(expectedAtomsD1txt), removeFiles(expectedAtomsD1txt), noFileFilter);
      doTest (url, expectedAtomsD1txt, expectedURLsD1txt, noDirFilter);
   }
   
//// Empty Directory Test
 
   public void testEmptyDirectoryWithFile() throws Exception
   {
      URL fileURL = getFileURL("test-jar/a1/b1-");
      doEmptyDirectoryTest(fileURL);
   }
   
   public void testEmptyDirectoryWithJar() throws Exception
   {
      URL jarURL = getJarURL("/a1/b1-/");
      doEmptyDirectoryTest(jarURL);
   }
   
   private void doEmptyDirectoryTest(URL url) throws Exception
   {
      doTest (url, expectedAtomsB1Dash, expectedURLsB1Dash, noFilter);
      doTest (url, new ArrayList(), new ArrayList(), fullFilter);
      doTest (url, expectedAtomsB1Dash, expectedURLsB1Dash, noFileFilter);
      doTest (url, new ArrayList(), new ArrayList(), noDirFilter);
   }
   
////
   
   public void testJarURLs() throws Exception
   {
      //Note no / at the end
      URL jarURL = getJarURL("/a1");
      doTest (jarURL, expectedAtomsA1, expectedURLsA1, noFilter);
      doTest (jarURL, new ArrayList(), new ArrayList(), fullFilter);
      doTest (jarURL, removeFiles(expectedAtomsA1), removeFiles(expectedURLsA1), noFileFilter);
      // since we pass it a directory, we can't enter the directory
      doTest (jarURL, new ArrayList(), new ArrayList(),noDirFilter);
      
      try
      {
      //Note extra / at front
      jarURL = getJarURL("//a1");
      doTest (jarURL, expectedAtomsA1, expectedURLsA1, noFilter);
      doTest (jarURL, new ArrayList(), new ArrayList(), fullFilter);
      doTest (jarURL, removeFiles(expectedAtomsA1), removeFiles(expectedURLsA1), noFileFilter);
      // since we pass it a directory, we can't enter the directory
      doTest (jarURL, new ArrayList(), new ArrayList(),noDirFilter);
      }
      catch (FileNotFoundException fnfe)
      {
    	  //expected result
      }
      
      try
      {
      jarURL = getJarURL("/foobar/");
      doTest (jarURL, expectedAtomsA1, expectedURLsA1, noFilter);
      fail ("An invalid jar url did not cause a FileNotFoundException");
      }
      catch (FileNotFoundException fnfe)
      {
         //expected result
      }
   }
   
   public void testFileURLs() throws Exception
   {
      //Note no / at the end
      URL fileURL = getFileURL("test-jar/a1");
      
      doTest (fileURL, expectedAtomsA1, expectedURLsA1, noFilter);
      doTest (fileURL, new ArrayList(), new ArrayList(), fullFilter);
      doTest (fileURL, removeFiles(expectedAtomsA1), removeFiles(expectedURLsA1), noFileFilter);
      // since we pass it a directory, we can't enter the directory
      doTest (fileURL, new ArrayList(), new ArrayList(),noDirFilter);
      
      try
      {
         fileURL = new URL("file:foobar");
         doTest (fileURL, expectedAtomsA1, expectedURLsA1, noFilter);
         fail ("An invalid file url did not cause a FileNotFoundException");
      }
      catch (FileNotFoundException fnfe)
      {
         //expected result
      }
   }
   
   
/*----------Utility Metods and Classes ---------------*/
   
   private URL getFileURL(String name) throws MalformedURLException
   {
      URL url = Thread.currentThread().getContextClassLoader().getResource(name);
      assertNotNull("Could not load URL for file " + name, url);
      assertTrue(URLTools.exists(url));
      return url;
   }
   
   private URL getJarURL(String name) throws MalformedURLException
   {
      URL url = getFileURL(TEST_JAR_NAME);
      File jarFile = new File(url.getFile());
      assertTrue(jarFile.exists());
      return new URL("jar", "", jarFile.toURL() + "!" + name);
   }
   
   public List removeFiles(ArrayList list)
   {
      ArrayList newList = (ArrayList)list.clone();
      ArrayList fileList = new ArrayList();
      
      Iterator iterator = newList.iterator();
      while (iterator.hasNext())
      {
         String element = (String)(iterator.next());
         if (element.endsWith(".txt") || element.endsWith(".MF"))
         {
            fileList.add(element);
         }
      }

      newList.removeAll(fileList);
      
      return newList;
   }
   
   private void doTest(URL url, List expectedAtoms, List expectedURLs, Filter filter) throws Exception
   {
      final List atoms = new ArrayList();
      final List urls = new ArrayList();
      URLNavigator.visit(url, new URLVisitor()
      {
         public void startDir(URL url, String name)
         {
            atoms.add("<" + name + ">");
            urls.add(url);
         }
         public void endDir(URL url, String name)
         {
            atoms.add("</" + name + ">");
            urls.add(url);
         }
         public void file(URL url, String name)
         {
            atoms.add(name);
            urls.add(url);
         }
      }, filter);

      //
      if (urls.size() !=  expectedURLs.size())
      {
         assertEquals(expectedURLs, urls);
         fail("URLs size does not match " + urls.size() + "!=" + expectedURLs.size());
      }

      //
      assertEquals(expectedAtoms, atoms);

      //
      for (int i = 0;i < urls.size();i++)
      {
         URL entryURL = (URL)urls.get(i);
         String suffix = (String)expectedURLs.get(i);
         if (!entryURL.getPath().endsWith(suffix))
         {
            fail("URL " + entryURL + " does not end with the suffix " + suffix + " at index " + i);
         }
         if (entryURL.getPath().endsWith ("//" + suffix.substring(1)))
         {
        	 fail("URL " + entryURL + " ends with /" + suffix + " at index " + i);
         }
      }
  
      if (filter != null)
      {
         assertTrue("The Sequence never completed", filter.SequenceComplete());
      }
   }

   /**
    * Class used to setup URLFilter behavior for tests
    * @author Matt Wringe
    */
   private static class Filter implements URLFilter
   {
      private boolean acceptFile;
      private boolean acceptDir;
      
      private List acceptFileSequence = null;
      private Iterator fileIterator = null;
      
      private List acceptDirSequence = null;
      private Iterator dirIterator = null;
      
      /**
       * Method used to setup URLFilter behavior
       * @param acceptFile Always accept files
       * @param acceptDir Always accept files
       */
      public Filter (boolean acceptFile, boolean acceptDir)
      {
         this.acceptFile = acceptFile;
         this.acceptDir = acceptDir;
      }
      
      /**
       * Method to setup URLFilter behavior which by default always accepts
       * files and directories
       */
      public Filter()
      {
         this.acceptDir = true;
         this.acceptFile = true;
      }
      
      /**
       * Set the sequence to accept or reject files
       * @param acceptFileSequence Sequence for accepting files
       */
      public void setAcceptFile (List acceptFileSequence)
      {
         this.acceptFileSequence = acceptFileSequence;
         this.fileIterator = acceptFileSequence.iterator();
      }
      
      /**
       * Set the sequence to accept or reject directories
       * @param acceptDirSequence Sequence for accepting directories
       */
      public void setAcceptDir (List acceptDirSequence)
      {
         this.acceptDirSequence = acceptDirSequence;
         this.dirIterator = acceptDirSequence.iterator();
      }
      
      /**
       * Returns true if the sequence is complete or if no sequence has been setup
       * @return True if the sequence is complete
       */
      public boolean SequenceComplete()
      {
         if ((dirIterator == null || !dirIterator.hasNext()) && (fileIterator == null || !fileIterator.hasNext()))
         {
            return true;
         }
         else
         {
            return false;
         }
      }
      
      public boolean acceptFile(URL url)
      {
         if (fileIterator != null)
         {
            return ((Boolean)fileIterator.next()).booleanValue();
         }
         else
         {
            return acceptFile;
         }
      }

      public boolean acceptDir(URL url)
      {
         if (dirIterator != null)
         {
            return ((Boolean)dirIterator.next()).booleanValue();
         }
         return acceptDir;
      }
   }
}

