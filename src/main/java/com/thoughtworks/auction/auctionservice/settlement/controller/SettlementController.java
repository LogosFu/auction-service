package com.thoughtworks.auction.auctionservice.settlement.controller;

import com.thoughtworks.auction.auctionservice.settlement.controller.dto.SettlementDTO;
import com.thoughtworks.auction.auctionservice.settlement.service.SettlementService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/auction")
public class SettlementController {

  private final SettlementService settlementService;

  public SettlementController(
      SettlementService settlementService) {
    this.settlementService = settlementService;
  }

  @GetMapping("/{a_id}/settlement")
  public ResponseEntity<SettlementDTO> settlementRequest(@PathVariable("a_id") Long aid) {

    return ResponseEntity.ok(settlementService.settlementRequest(aid));
  }
}
