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

package com.arepresas.quicken4j;

import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

/**
 * Reads Quicken Interchange Format (QIF) files. This reader is memory-efficient
 * as it processes files line-by-line.
 *
 * @author FracPete (fracpete at gmail dot com)
 * @version $Revision$
 */
@Slf4j
public class QIFReader {

  private static final String HEADER_OPTION_PREFIX = "!Option:";
  private static final String HEADER_TYPE_PREFIX = "!Type:";
  private static final String TRANSACTION_END_MARKER = "^";
  private static final String EXTRA_FIELD_PREFIX = "X";

  /** The default date format to use if not specified in the file. */
  private final String defaultDateFormat;

  /**
   * Initializes the reader with the default date format.
   *
   * @see Transaction#DEFAULT_DATE_FORMAT
   */
  public QIFReader() {
    this(Transaction.DEFAULT_DATE_FORMAT);
  }

  /**
   * Initializes the reader with the specified default date format.
   *
   * @param dateFormat the date format to use as a default
   */
  public QIFReader(String dateFormat) {
    this.defaultDateFormat = dateFormat;
  }

  /**
   * Reads the transactions from the specified file using the system's default charset.
   *
   * @param file The file to read from.
   * @return The parsed transactions.
   * @throws IOException if an I/O error occurs.
   */
  public Transactions read(File file) throws IOException {
    return read(file, Charset.defaultCharset());
  }

  /**
   * Reads the transactions from the specified file with a given charset.
   *
   * @param file     The file to read from.
   * @param encoding The charset to use for reading the file.
   * @return The parsed transactions.
   * @throws IOException if an I/O error occurs.
   */
  public Transactions read(File file, Charset encoding) throws IOException {
    try (FileInputStream fis = new FileInputStream(file);
         InputStreamReader reader = new InputStreamReader(fis, encoding)) {
      return read(reader);
    }
  }

  /**
   * Reads the transactions from an input stream.
   * Note: This method does not close the provided stream.
   *
   * @param stream The stream to read from.
   * @return The parsed transactions.
   * @throws IOException if an I/O error occurs.
   */
  public Transactions read(InputStream stream) throws IOException {
    return read(new InputStreamReader(stream));
  }

  /**
   * Reads transactions from a reader, processing the data line-by-line.
   * Note: This method does not close the provided reader.
   *
   * @param reader The reader to use.
   * @return The parsed transactions.
   * @throws IOException if an I/O error occurs or the QIF format is invalid.
   */
  public Transactions read(Reader reader) throws IOException {
    BufferedReader breader = (reader instanceof BufferedReader) ? (BufferedReader) reader : new BufferedReader(reader);

    String currentLine = breader.readLine();
    if (currentLine == null) {
      throw new IOException("Cannot read from an empty or closed reader.");
    }

    String fileDateFormat = this.defaultDateFormat;
    if (currentLine.startsWith(HEADER_OPTION_PREFIX)) {
      if (currentLine.equals("!Option:MDY")) {
        fileDateFormat = "MM/dd/yyyy";
      } else {
        throw new IllegalArgumentException("Unknown option: " + currentLine);
      }
      currentLine = breader.readLine();
    }

    if (currentLine == null || !currentLine.startsWith(HEADER_TYPE_PREFIX)) {
      throw new IOException("Invalid QIF header. Expected '!Type:', but found: " + currentLine);
    }

    String type = currentLine.substring(HEADER_TYPE_PREFIX.length());
    Transactions transactions = new Transactions(type);
    Map<String, String> currentValues = new HashMap<>();

    while ((currentLine = breader.readLine()) != null) {
      if (currentLine.trim().equals(TRANSACTION_END_MARKER)) {
        if (!currentValues.isEmpty()) {
          transactions.add(new Transaction(currentValues, fileDateFormat));
          currentValues = new HashMap<>(); // Reset for the next transaction
        }
        continue;
      }

      if (currentLine.length() > 1) {
        String key = currentLine.startsWith(EXTRA_FIELD_PREFIX) ? currentLine.substring(0, 2) : currentLine.substring(0, 1);
        String value = currentLine.substring(key.length());
        currentValues.put(key, value);
      }
    }

    // Add the last transaction if the file doesn't end with a '^'
    if (!currentValues.isEmpty()) {
      transactions.add(new Transaction(currentValues, fileDateFormat));
    }

    return transactions;
  }

  /**
   * A simple command-line runner for testing the reader.
   *
   * @param args A list of file paths to read.
   * @throws Exception if any file parsing fails.
   */
  public static void main(String[] args) throws Exception {
    if (args.length == 0) {
      log.info("Usage: QIFReader <file1.qif> <file2.qif> ...");
      return;
    }

    final QIFReader reader = new QIFReader();
    for (final String arg : args) {
      log.info("\n--> Reading file: {}", arg);
      try {
        final Transactions transactions = reader.read(new File(arg));
        log.info("Type: {}, Count: {}", transactions.getType(), transactions.size());
        for (final Transaction t : transactions) {
          log.info("  - Date: {}, Amount: {}, Payee: {}, Memo: {}", t.getDate(), t.getAmount(), t.getPayee(), t.getMemo());
        }
      } catch (Exception e) {
        log.error("Failed to process file: {}", arg, e);
      }
    }
  }
}