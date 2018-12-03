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
 * Reader.java
 * Copyright (C) 2016 FracPete
 */

package com.github.fracpete.quicken4j;

import org.apache.commons.io.IOUtils;

import java.io.*;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Reads Quicken Interchange Format (QIF) files.
 *
 * @author FracPete (fracpete at gmail dot com)
 * @version $Revision$
 */
public class QIFReader {

  /** the date format. */
  protected String m_DateFormat;

  /**
   * Initializes the reader with the default date format.
   *
   * @see		Transaction#DEFAULT_DATE_FORMAT
   */
  public QIFReader() {
    this(Transaction.DEFAULT_DATE_FORMAT);
  }

  /**
   * Initializes the reader with the specified date format.
   *
   * @param dateFormat	the date format to use
   */
  public QIFReader(String dateFormat) {
    m_DateFormat = dateFormat;
  }

  /**
   * Reads the transactions from the specified file with the default charset.
   * @param file specified file
   * @return the transactions
   * @throws IOException if file reading fails
   */
  public Transactions read(File file) throws IOException {
    return read(file, Charset.defaultCharset());
  }
  
  /**
   * Reads the transactions from the specified file with the given encoding.
   *
   * @param file    the file to read
   * @param encoding
   * @return		the transactions
   * @param encoding the given encoding
   * @throws IOException if file reading fails
   * @throws Exception	if reading of file fails or invalid format
   */
  public Transactions read(File file, Charset encoding) throws IOException {
    Transactions result;
    Reader freader;
    BufferedReader breader;

    FileInputStream fis = new FileInputStream(file);
    freader = new InputStreamReader(fis, encoding);
    breader = new BufferedReader(freader);

    result = read(breader);

    IOUtils.closeQuietly(breader);
    IOUtils.closeQuietly(freader);

    return result;
  }

  /**
   * Reads the transactions from the specified stream.
   * NB: Doesn't close the stream.
   *
   * @param stream	the stream to use
   * @return		the transactions
   * @throws IOException
   * @throws Exception	if reading of stream fails or invalid format
   */
  public Transactions read(InputStream stream) throws IOException {
    return read(new InputStreamReader(stream));
  }
  
  /**
   * Reads the transactions from the specified reader.
   * NB: Doesn't close the reader.
   *
   * @param reader	the reader to use
   * @return		the transactions
   * @throws IOException
   * @throws Exception	if reading from reader fails or invalid format
   */
  public Transactions read(Reader reader) throws IOException {
    Transactions result;
    BufferedReader breader;
    String l;
    List<String> lines;
    List<String> current;
    Map<String, String> values;
    int i = 0;

    // load data
    lines = new ArrayList<>();
    if (reader instanceof BufferedReader) {
      breader = (BufferedReader) reader;
    } else {
      breader = new BufferedReader(reader);
    }
    while ((l = breader.readLine()) != null) {
      lines.add(l);
    }

    // sanity checks
    if (lines.size() == 0) {
      throw new IllegalStateException("No transaction type identifier found!");
    }
    if (lines.get(i).startsWith("!Option:")) {

      if (lines.get(i).equals("!Option:MDY")) {
        this.m_DateFormat="MM.dd.yyyy";
      } else {
        throw new IllegalArgumentException("Unknown option: "+lines.get(i));
      }
      i++;
    }
    if (!lines.get(i).startsWith("!Type:")) {
      throw new IllegalStateException("Invalid transaction type identifier at line 0: " + lines.get(i));
    }

    // parse transactions
    result = new Transactions(lines.get(i).substring(6));

    current = new ArrayList<>();
    while (i < lines.size() - 1) {
      i++;
      if (lines.get(i).startsWith("^")) {
        if (current.size() > 0) {
          values = new HashMap<>();
          for (final String line : current) {
            if (line.startsWith("X")) {
              values.put(line.substring(0, 2), line.substring(2));
            } else {
              values.put(line.substring(0, 1), line.substring(1));
            }
          }
          result.add(new Transaction(values, m_DateFormat));
        }
        current.clear();
        continue;
      }
      current.add(lines.get(i));
    }

    return result;
  }

  /**
   * For testing. Uses default date format.
   *
   * @param args	the files to read
   * @throws Exception	if parsing of a file fails
   */
  public static void main(String[] args) throws Exception {
    final QIFReader reader = new QIFReader();
    for (final String arg : args) {
      System.out.println("\n--> " + arg);
      final Transactions transactions = reader.read(new File(arg));
      for (final Transaction t : transactions) {
        System.out.println(t);
        System.out.println("  - Date: " + t.getDate());
        System.out.println("  - Amount: " + t.getAmount());
        System.out.println("  - Payee: " + t.getPayee());
        System.out.println("  - Memo: " + t.getMemo());
      }
    }
  }
}
