package com.thoughtworks.auction.auctionservice.settlement.repository;

import com.thoughtworks.auction.auctionservice.settlement.controller.dto.SettlementDTO;
import com.thoughtworks.auction.auctionservice.settlement.controller.dto.SettlementStatus;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Settlement {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;
  private Long auctionId;
  private Double amount;
  private SettlementStatus status;

  public SettlementDTO toDto() {
    return SettlementDTO.builder().amount(amount).auctionId(auctionId).status(status).build();
  }
}
