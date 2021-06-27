package com.thoughtworks.auction.auctionservice.settlement.repository;

import org.springframework.stereotype.Service;

@Service
public interface SettlementServiceRepository {

  double amountByAuction(Long aId);
}
