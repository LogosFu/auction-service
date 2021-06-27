package com.thoughtworks.auction.auctionservice.settlement.controller;

import com.thoughtworks.auction.auctionservice.settlement.controller.dto.SettlementDTO;
import com.thoughtworks.auction.auctionservice.settlement.service.SettlementService;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
public class SettlementConsumer {

  private final SettlementService settlementService;

  public SettlementConsumer(
      SettlementService settlementService) {
    this.settlementService = settlementService;
  }

  @JmsListener(destination = "settlement.service.result")
  public void onGetUpdateSettlementRequest(SettlementDTO settlementDTO) throws NotFoundException {
    settlementService.updateRequest(settlementDTO.getAuctionId(), settlementDTO.getAmount());
  }
}
