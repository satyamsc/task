package com.n26.task.config;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"com.n26.exercise.statisticscollector.spring","com.n26.exercise.statisticscollector.api.controllers"})
public class Application
{

  public static void main(String[] args)
  {
    SpringApplication.run(Application.class, args);
  }

}
