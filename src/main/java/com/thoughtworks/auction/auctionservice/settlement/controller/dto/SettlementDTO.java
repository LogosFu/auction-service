package com.thoughtworks.auction.auctionservice.settlement.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SettlementDTO {

  private Double amount;
  private Long auctionId;
  private SettlementStatus status;
}
