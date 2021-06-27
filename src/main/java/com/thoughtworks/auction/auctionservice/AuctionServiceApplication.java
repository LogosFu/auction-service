package com.thoughtworks.auction.auctionservice;

import com.spring4all.swagger.EnableSwagger2Doc;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.jms.annotation.EnableJms;

@SpringBootApplication
@EnableSwagger2Doc
@EnableEurekaClient
@ComponentScan(basePackages = {"com.thoughtworks.auction"})
@EnableFeignClients
@EnableJms
public class AuctionServiceApplication {

  public static void main(String[] args) {
    SpringApplication.run(AuctionServiceApplication.class, args);
  }

}
