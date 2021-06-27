package com.thoughtworks.auction.auctionservice.settlement.repository;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient("settlement")
public interface SettlementServiceClient extends SettlementServiceRepository {

  @GetMapping(value = "/settlement/auction/{a_id}")
  double amountByAuction(@PathVariable(name = "a_id") Long aId);
}
