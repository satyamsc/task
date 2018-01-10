package com.n26.task.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.n26.task.domain.AsyncTransactionUpdaterStatisticsSamples;
import com.n26.task.domain.AutoSlidingStatisticsSamples;
import com.n26.task.domain.FixedSizeSlidingStatisticsSamples;
import com.n26.task.domain.SlidingStatisticsSamples;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

@Configuration
public class Config
{
  @Value("${controller.sliding.period}")
  long slidingInterval;
  @Value("${controller.sliding.timeUnit}")
  TimeUnit slidingPeriod;

  @Value("${controller.transactionUpdater.period}")
  long transactionUpdaterInterval;
  @Value("${controller.transactionUpdater.timeUnit}")
  TimeUnit transactionUpdaterPeriod;
  @Value("${controller.transactionUpdater.bufferSize}")
  int transactionUpdaterSize;

  @Bean(name = "realSlidingStatisticsSample")
  public FixedSizeSlidingStatisticsSamples fixedSizeSlidingStatisticsSamples()
  {
    return new FixedSizeSlidingStatisticsSamples(60);
  }

  @Bean(name = "transactionUpdaterScheduledExecutorService")
  public ScheduledExecutorService transactionUpdaterScheduledExecutorService()
  {
    return Executors.newScheduledThreadPool(1, new ThreadFactory()
    {
      @Override public Thread newThread(Runnable r)
      {
        return new Thread(r, "TransactionUpdater");
      }
    });
  }

  @Bean(name = "transactionUpdaterStatisticsSamples")
  @Autowired
  public AsyncTransactionUpdaterStatisticsSamples asyncTransactionUpdaterStatisticsSamples(
      @Qualifier("realSlidingStatisticsSample") SlidingStatisticsSamples delegate,
      @Qualifier("transactionUpdaterScheduledExecutorService") ScheduledExecutorService executorService)
  {
    return new AsyncTransactionUpdaterStatisticsSamples(delegate,
                                                        executorService,
                                                        transactionUpdaterInterval,
                                                        transactionUpdaterPeriod,
                                                        transactionUpdaterSize);
  }

  @Bean(name = "autoSlidingScheduledExecutorService")
  public ScheduledExecutorService autoSlidingScheduledExecutorService()
  {
    return Executors.newScheduledThreadPool(1, new ThreadFactory()
    {
      @Override public Thread newThread(Runnable r)
      {
        return new Thread(r, "SamplesSlider");
      }
    });
  }

  @Bean(name = "slidingStatisticsSamples")
  @Autowired
  public AutoSlidingStatisticsSamples autoSlidingStatisticsSamples(
      @Qualifier("transactionUpdaterStatisticsSamples") SlidingStatisticsSamples delegate,
      @Qualifier("autoSlidingScheduledExecutorService") ScheduledExecutorService executorService)
  {
    return new AutoSlidingStatisticsSamples(delegate,
                                            executorService,
                                            slidingInterval,
                                            slidingPeriod);
  }

}
