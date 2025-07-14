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
 * Transactions.java
 * Copyright (C) 2016 FracPete
 */

package com.arepresas.quicken4j;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * A container for a collection of transactions.
 * This class encapsulates a list of transactions and provides methods
 * to access them and their metadata.
 *
 * @author FracPete (fracpete at gmail dot com)
 * @version $Revision$
 */
public class Transactions implements Iterable<Transaction>, Serializable {

  /** The serial version UID for serialization. */
  private static final long serialVersionUID = 7861635223791823156L;

  /** The type of transactions (e.g., "Bank"). */
  private final String type;

  /** The internal list of transactions. */
  private final List<Transaction> transactions;

  /**
   * Initializes the transactions container.
   *
   * @param type	the type of the transactions
   */
  public Transactions(String type) {
    this.type = type;
    this.transactions = new ArrayList<>();
  }

  /**
   * Returns the type of the transactions.
   *
   * @return		the type
   */
  public String getType() {
    return this.type;
  }

  /**
   * Adds a transaction to the collection.
   *
   * @param transaction the transaction to add
   */
  public void add(Transaction transaction) {
    this.transactions.add(transaction);
  }

  /**
   * Retrieves a transaction by its index.
   *
   * @param index the index of the transaction to retrieve
   * @return the transaction at the specified index
   */
  public Transaction get(int index) {
    return this.transactions.get(index);
  }

  /**
   * Returns the number of transactions in the collection.
   *
   * @return the number of transactions
   */
  public int size() {
    return this.transactions.size();
  }

  /**
   * Returns a set of all unique field codes from all transactions.
   *
   * @return a set of all codes
   */
  public Set<String> getCodes() {
    return this.transactions.stream()
      .flatMap(t -> t.keys().stream())
      .collect(Collectors.toSet());
  }

  /**
   * Returns an iterator over the transactions.
   *
   * @return an iterator
   */
  @Override
  public Iterator<Transaction> iterator() {
    return this.transactions.iterator();
  }

  /**
   * Returns a string representation of the transactions list.
   *
   * @return the string representation
   */
  @Override
  public String toString() {
    return this.transactions.toString();
  }
}