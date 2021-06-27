package com.thoughtworks.auction.auctionservice.settlement;

import static org.assertj.core.api.Assertions.assertThat;

import com.thoughtworks.auction.auctionservice.AuctionServiceApplication;
import com.thoughtworks.auction.auctionservice.settlement.controller.dto.SettlementStatus;
import com.thoughtworks.auction.auctionservice.settlement.repository.Settlement;
import com.thoughtworks.auction.auctionservice.settlement.repository.SettlementRepository;
import com.thoughtworks.auction.auctionservice.settlement.repository.SettlementDataRepository;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT, classes = AuctionServiceApplication.class)
class SettlementDataRepositoryTest {

  @Autowired
  SettlementDataRepository settlementDataRepository;
  @Qualifier("settlementRepository")
  @Autowired
  SettlementRepository settlementRepository;

  @BeforeEach
  void setUp() {
    settlementRepository.deleteAll();
  }

  //工序6
  @Test
  void should_save_settlement_when_save_given_settlement() {
    //given
    Settlement settlement = Settlement.builder().auctionId(1L).status(SettlementStatus.REQUEST)
        .build();
    //when
    settlementDataRepository.save(settlement);
    //then
    List<Settlement> settlements = settlementRepository.findAll();
    assertThat(settlements.size()).isEqualTo(1);
    assertThat(settlements.get(0).getAuctionId()).isEqualTo(1L);
  }

  //工序7
  @Test
  void should_return_settlement_in_db_when_find_by_auction_id_given_settlement_in_db() {

    //given
    Settlement settlement = Settlement.builder().auctionId(1L).status(SettlementStatus.REQUEST)
        .build();
    settlementDataRepository.save(settlement);

    //when
    Optional<Settlement> settlementOptional = settlementDataRepository.findByAuctionId(1L);

    //then
    assertThat(settlementOptional.isPresent()).isTrue();
  }
}
