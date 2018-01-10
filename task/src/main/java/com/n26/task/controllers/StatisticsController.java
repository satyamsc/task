package com.n26.task.controllers;

import com.n26.task.bean.Statistics;
import com.n26.task.domain.SlidingStatisticsSamples;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class StatisticsController
{
  Logger logger = LoggerFactory.getLogger(StatisticsController.class);

  private final SlidingStatisticsSamples slidingStatisticsSamples;

  @Autowired
  public StatisticsController(@Qualifier("slidingStatisticsSamples") SlidingStatisticsSamples slidingStatisticsSamples)
  {
    this.slidingStatisticsSamples = slidingStatisticsSamples;
  }

  @RequestMapping(path = "/statistics", method = RequestMethod.GET)
  public Statistics getStatistics()
  {

    com.n26.task.domain.Statistics domainStatistics = slidingStatisticsSamples
        .getStatistics();

    Statistics statistics = new Statistics(domainStatistics.getSum(),
                                           domainStatistics.getAvg(),
                                           domainStatistics.getMax(),
                                           domainStatistics.getMin(),
                                           domainStatistics.getCount());
    logger.info("Returning statistics {}", statistics);
    return statistics;
  }

  @RequestMapping(path = "/statistics", method = RequestMethod.DELETE)
  @ResponseStatus(HttpStatus.OK)
  public void clearStatistics()
  {
    slidingStatisticsSamples.resetStatistics();
  }

}
