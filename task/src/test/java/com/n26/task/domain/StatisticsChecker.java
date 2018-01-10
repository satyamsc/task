package com.n26.task.domain;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import com.n26.task.domain.Statistics;

public class StatisticsChecker
{
  public static void assertStatisticsAre(Statistics statistics,
                                         int count,
                                         double sum,
                                         double avg,
                                         double min,
                                         double max)
  {
    assertThat(statistics.getCount(), is(equalTo(count)));
    assertThat(statistics.getSum(), is(equalTo(sum)));

    assertThat(statistics.getAvg(), is(equalTo(avg)));

    assertThat(statistics.getMin(), is(equalTo(min)));
    assertThat(statistics.getMax(), is(equalTo(max)));
  }

  public static void assertStatisticsAre(com.n26.task.bean.Statistics statistics,
                                         int count,
                                         double sum,
                                         double avg,
                                         double min,
                                         double max)
  {
    assertThat(statistics.getCount(), is(equalTo(count)));
    assertThat(statistics.getSum(), is(equalTo(sum)));

    assertThat(statistics.getAvg(), is(equalTo(avg)));

    assertThat(statistics.getMin(), is(equalTo(min)));
    assertThat(statistics.getMax(), is(equalTo(max)));
  }
}
