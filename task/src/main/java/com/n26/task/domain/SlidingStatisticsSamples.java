package com.n26.task.domain;

import java.util.Collection;

public interface SlidingStatisticsSamples
{
  public void addTransaction(Transaction transaction);

  public void addTransactions(Collection<Transaction> transactions);

  public Statistics getStatistics();

  public void slide();

  public void resetStatistics();
}
