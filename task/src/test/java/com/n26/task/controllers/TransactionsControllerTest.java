package com.n26.task.controllers;

import static com.n26.task.domain.SchedulableSingleWorkerBaseTest.sleepFor;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import com.n26.task.bean.Transcation;
import com.n26.task.config.Application;
import com.n26.task.domain.SlidingStatisticsSamples;
import com.n26.task.domain.StatisticsChecker;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)

public class TransactionsControllerTest {
	@Autowired
	private TestRestTemplate restTemplate;

	@Autowired
	@Qualifier("slidingStatisticsSamples")
	private SlidingStatisticsSamples slidingStatisticsSamples;

	@Before
	public void setup() {
		slidingStatisticsSamples.resetStatistics();
	}

	@Test
	public void addTransaction201() {
		Transcation transcation = new Transcation();
		transcation.setAmount(123.45);
		transcation.setTimestamp(System.currentTimeMillis() / 1000);

		ResponseEntity<Void> response = restTemplate.postForEntity("/transactions", transcation, Void.class);
		assertThat(response.getStatusCodeValue(), is(equalTo(201)));

		sleepFor(50);

		StatisticsChecker.assertStatisticsAre(slidingStatisticsSamples.getStatistics(), 1, 123.45, 123.45, 123.45,
				123.45);
	}

	@Test
	public void addTransaction204() {
		Transcation transcation = new Transcation();
		transcation.setAmount(123.45);
		transcation.setTimestamp((System.currentTimeMillis() / 1000) - 80);

		ResponseEntity<Void> response = restTemplate.postForEntity("/transactions", transcation, Void.class);
		assertThat(response.getStatusCodeValue(), is(equalTo(204)));
	}

}
