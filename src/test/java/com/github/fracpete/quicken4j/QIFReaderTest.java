/*
 *   This program is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

/**
 * QIFReaderTest.java
 * Copyright (C) 2016 University of Waikato, Hamilton, NZ
 */

package com.github.fracpete.quicken4j;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import junit.textui.TestRunner;

import java.util.Set;

/**
 * Tests the QIFReader class.
 *
 * @author FracPete (fracpete at waikato dot ac dot nz)
 * @version $Revision$
 */
public class QIFReaderTest
  extends TestCase {

  /**
   * Loads the "simple" test.
   */
  public void testSimple() {
    QIFReader reader = new QIFReader();
    String resource = "simple.qif";
    try {
      Transactions transactions = reader.read(ClassLoader.getSystemResourceAsStream(resource));
      assertEquals("Transactions type", "Bank", transactions.getType());
      assertEquals("Numbers of transactions", 3, transactions.size());
      assertEquals("1st amount", -379.00, transactions.get(0).getAmount());
      assertEquals("2nd amount", -20.28, transactions.get(1).getAmount());
      assertEquals("3rd amount", -421.35, transactions.get(2).getAmount());
      Set<String> codes = transactions.getCodes();
      assertEquals("Number of codes", 3, codes.size());
      assertTrue("Code D present", codes.contains("D"));
      assertTrue("Code T present", codes.contains("T"));
      assertTrue("Code P present", codes.contains("P"));
    }
    catch (Exception e) {
      fail("Failed to read resource: " + resource + "\n" + e);
    }
  }

  /**
   * Returns a test suite.
   *
   * @return		the test suite
   */
  public static Test suite() {
    return new TestSuite(QIFReaderTest.class);
  }

  /**
   * Runs the test from commandline.
   *
   * @param args	ignored
   */
  public static void main(String[] args) {
    TestRunner.run(suite());
  }
}
