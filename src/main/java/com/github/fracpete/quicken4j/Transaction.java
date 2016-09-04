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

  /** the fields and their values. */
  protected Map<String,String> m_Values;

  /**
   * Initializes the transaction.
   */
  public Transaction() {
    m_Values = new HashMap<>();
  }

  /**
   * Initializes the transaction with the specified values.
   *
   * @param values	the values to initialize with
   */
  public Transaction(Map<String,String> values) {
    this();
    if (values != null)
      m_Values.putAll(values);
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
   * Returns the stored values as string.
   *
   * @return		the string representation
   */
  public String toString() {
    return m_Values.toString();
  }
}
