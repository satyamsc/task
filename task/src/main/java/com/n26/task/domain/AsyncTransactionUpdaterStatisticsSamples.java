package com.n26.task.domain;

import java.util.Collection;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AsyncTransactionUpdaterStatisticsSamples extends SchedulableSingleWorker
    implements SlidingStatisticsSamples
{
  private final static Logger logger = LoggerFactory
      .getLogger(AsyncTransactionUpdaterStatisticsSamples.class);

  private final SlidingStatisticsSamples delegate;
  private final BlockingQueue<Transaction> transactions;

  public AsyncTransactionUpdaterStatisticsSamples(SlidingStatisticsSamples delegate,
                                                  ScheduledExecutorService scheduledExecutorService,
                                                  long interval,
                                                  TimeUnit period,
                                                  int bufferSize)
  {
    super(scheduledExecutorService, interval, period);
    this.delegate = delegate;
    this.transactions = new LinkedBlockingQueue<>(bufferSize);
  }

  @Override public void addTransaction(Transaction transaction)
  {
    try
    {
      if (!transactions.offer(transaction, 50, TimeUnit.MILLISECONDS))
      {
        throw new TransactionBufferFullException();
      }
    }
    catch (InterruptedException ex)
    {
      throw new InterruptedWhileAddingTransactionException();
    }
  }

  @Override public void addTransactions(Collection<Transaction> transactions)
  {
    for (Transaction transaction : transactions)
    {
      addTransaction(transaction);
    }

  }

  @Override public Statistics getStatistics()
  {
    return delegate.getStatistics();
  }

  @Override public void slide()
  {
    delegate.slide();
  }

  @Override public void resetStatistics()
  {
    delegate.resetStatistics();
  }

  @Override protected Runnable getWorker()
  {
    return new TransactionUpdater();
  }

  class TransactionUpdater implements Runnable
  {

    @Override public void run()
    {
      Transaction transaction = transactions.poll();
      if (transaction != null)
      {
        logger.info("Asynchronous adding transaction {}", transaction);
        delegate.addTransaction(transaction);
      }
    }
  }

  private class TransactionBufferFullException extends RuntimeException
  {
    public TransactionBufferFullException()
    {
      super("Unable to place another transaction, buffer full.");
    }
  }

  private class InterruptedWhileAddingTransactionException extends RuntimeException
  {
    public InterruptedWhileAddingTransactionException()
    {
      super("Interrupted while placing another transaction into the buffer.");
    }
  }
}
