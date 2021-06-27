package com.thoughtworks.auction.auctionservice.settlement.service;


import com.thoughtworks.auction.auctionservice.settlement.controller.dto.SettlementDTO;
import com.thoughtworks.auction.auctionservice.settlement.controller.dto.SettlementServiceException;
import com.thoughtworks.auction.auctionservice.settlement.controller.dto.SettlementStatus;
import com.thoughtworks.auction.auctionservice.settlement.repository.Settlement;
import com.thoughtworks.auction.auctionservice.settlement.repository.SettlementDataRepository;
import com.thoughtworks.auction.auctionservice.settlement.repository.SettlementMessageRepository;
import com.thoughtworks.auction.auctionservice.settlement.repository.SettlementServiceRepository;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.stereotype.Service;

@Service
public class SettlementService {


  private final SettlementDataRepository settlementDataRepository;
  private final SettlementServiceRepository settlementServiceRepository;
  private final SettlementMessageRepository settlementMessageRepository;

  public SettlementService(
      SettlementDataRepository settlementDataRepository,
      SettlementServiceRepository settlementServiceRepository,
      SettlementMessageRepository settlementMessageRepository) {
    this.settlementDataRepository = settlementDataRepository;
    this.settlementServiceRepository = settlementServiceRepository;
    this.settlementMessageRepository = settlementMessageRepository;
  }

  public SettlementDTO settlementRequest(Long aid) {

    return settlementDataRepository.findByAuctionId(aid).map(settlement -> {
      if (settlement.getAmount() == null) {
        throw new SettlementServiceException();
      } else {
        return settlement.toDto();
      }
    }).orElseGet(() -> createNewSettlement(aid));
  }

  public SettlementDTO updateRequest(Long aid, double amount) throws NotFoundException {
  return   settlementDataRepository.findByAuctionId(aid).map(settlement -> {
      settlement.setAmount(amount);
      return settlementDataRepository.save(settlement).toDto();
    }).orElseThrow(NotFoundException::new);
  }

  private SettlementDTO createNewSettlement(Long aid) {
    Settlement settlement = Settlement.builder().status(SettlementStatus.PAYMENT).auctionId(aid)
        .build();

    try {
      double amount = settlementServiceRepository.amountByAuction(aid);
      settlement.setAmount(amount);
      settlementDataRepository.save(settlement);
    } catch (RuntimeException e) {
      settlementMessageRepository.sendSettlementMessage(aid);
      settlementDataRepository.save(settlement);
      throw new SettlementServiceException();
    }
    return settlement.toDto();
  }
}
