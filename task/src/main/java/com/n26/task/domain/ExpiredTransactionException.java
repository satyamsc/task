package com.n26.task.domain;

public class ExpiredTransactionException extends RuntimeException
{

  public ExpiredTransactionException(UnixEpoch unixEpoch)
  {
    super("Transaction timestamp ["+unixEpoch+"] is expired.");
  }
}
