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

package com.arepresas.quicken4j;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.InputStream;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests the QIFReader class.
 *
 * @author FracPete (fracpete at waikato dot ac dot nz)
 * @version $Revision$
 */
class QIFReaderTest {

  /**
   * Loads the "simple" test.
   */
  @Test
  @DisplayName("Should correctly read a simple QIF file")
  void testSimple() throws Exception {
    QIFReader reader = new QIFReader();
    String resource = "simple.qif";

    try (InputStream is = QIFReaderTest.class.getClassLoader().getResourceAsStream(resource)) {
      assertNotNull(is, "Test resource not found: " + resource);

      Transactions transactions = reader.read(is);

      assertEquals("Bank", transactions.getType(), "Transactions type should be 'Bank'");
      assertEquals(3, transactions.size(), "There should be 3 transactions");
      assertEquals(-379.00, transactions.get(0).getAmount(), 0.001, "1st amount should be -379.00");
      assertEquals(-20.28, transactions.get(1).getAmount(), 0.001, "2nd amount should be -20.28");
      assertEquals(-421.35, transactions.get(2).getAmount(), 0.001, "3rd amount should be -421.35");

      Set<String> codes = transactions.getCodes();
      assertEquals(3, codes.size(), "Number of codes should be 3");
      assertTrue(codes.contains("D"), "Code D should be present");
      assertTrue(codes.contains("T"), "Code T should be present");
      assertTrue(codes.contains("P"), "Code P should be present");
    }
  }
}