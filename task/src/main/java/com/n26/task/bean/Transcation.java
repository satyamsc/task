package com.n26.task.bean;

public class Transcation
{
  double amount;
  long timestamp;

  public double getAmount()
  {
    return amount;
  }

  public void setAmount(double amount)
  {
    this.amount = amount;
  }

  public long getTimestamp()
  {
    return timestamp;
  }

  public void setTimestamp(long timestamp)
  {
    this.timestamp = timestamp;
  }

  @Override public String toString()
  {
    return "Event{" +
        "amount=" + amount +
        ", timestamp=" + timestamp +
        '}';
  }
}
