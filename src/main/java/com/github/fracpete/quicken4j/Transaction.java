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
 * Transaction.java
 * Copyright (C) 2016 FracPete
 */

package com.github.fracpete.quicken4j;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Container for a single transaction.
 *
 * @author FracPete (fracpete at gmail dot com)
 * @version $Revision$
 */
public class Transaction {

  /** the default date format. */
  public static final String DEFAULT_DATE_FORMAT = "dd/MM/yyyy";

  /** the date format. */
  protected String m_DateFormat;

  /** the fields and their values. */
  protected Map<String,String> m_Values;

  /**
   * Initializes the transaction.
   * Uses default date format.
   *
   * @see		#DEFAULT_DATE_FORMAT
   */
  public Transaction() {
    this(null, DEFAULT_DATE_FORMAT);
  }

  /**
   * Initializes the transaction.
   *
   * @param dateFormat	the date format to use
   */
  public Transaction(String dateFormat) {
    this(null, dateFormat);
  }

  /**
   * Initializes the transaction with the specified values.
   * Uses default date format.
   *
   * @param values	the values to initialize with
   * @see		#DEFAULT_DATE_FORMAT
   */
  public Transaction(Map<String,String> values) {
    this(values, DEFAULT_DATE_FORMAT);
  }

  /**
   * Initializes the transaction with the specified values.
   *
   * @param values	the values to initialize with
   */
  public Transaction(Map<String,String> values, String dateFormat) {
    super();

    if (dateFormat == null) {
      m_DateFormat = DEFAULT_DATE_FORMAT;
    } else {
      m_DateFormat = dateFormat;
    }

    m_Values = new HashMap<>();
    if (values != null) {
      m_Values.putAll(values);
    }
  }

  /**
   * Returns all stored keys.
   *
   * @return		the keys
   */
  public Set<String> keys() {
    return m_Values.keySet();
  }

  /**
   * Returns the value associated with the key.
   *
   * @param key		the key to retrieve the value for
   * @return		the value, null if not found
   */
  public String getValue(String key) {
    return m_Values.get(key);
  }

  /**
   * Returns the number of stored values.
   *
   * @return		the number of values.
   */
  public int size() {
    return m_Values.size();
  }

  /**
   * Returns the date of the transaction.
   *
   * @return		the date, null if failed to parse or not present
   */
  public Date getDate() {
    if (getValue("D") == null) {
      return null;
    }
    try {
      return new SimpleDateFormat(m_DateFormat).parse(getValue("D"));
    }
    catch (final ParseException e) {
      return null;
    }
  }

  /**
   * Returns the amount of the transaction.
   *
   * @return		the amount, null if failed to parse or not present
   */
  public Double getAmount() {
    if (getValue("T") == null) {
      return null;
    }
    try {
      return new Double(getValue("T").replaceAll(",", ""));
    }
    catch (final NumberFormatException e) {
      return null;
    }
  }

  /**
   * Returns the number of the transaction.
   *
   * @return		the number, null if not present
   */
  public String getNumber() {
    return getValue("N");
  }

  /**
   * Returns the payee of the transaction.
   *
   * @return		the payee, null if not present
   */
  public String getPayee() {
    return getValue("P");
  }

  /**
   * Returns the memo of the transaction.
   *
   * @return		the memo, null if not present
   */
  public String getMemo() {
    return getValue("M");
  }

  /**
   * Returns the stored values as string.
   *
   * @return		the string representation
   */
  @Override
  public String toString() {
    return m_Values.toString();
  }
}
