package com.thoughtworks.auction.auctionservice.settlement;

import com.thoughtworks.auction.auctionservice.AuctionServiceApplication;
import com.thoughtworks.auction.auctionservice.settlement.controller.SettlementConsumer;
import com.thoughtworks.auction.auctionservice.settlement.controller.dto.SettlementDTO;
import com.thoughtworks.auction.auctionservice.settlement.service.SettlementService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = AuctionServiceApplication.class)
class SettlementConsumerTest {

  @MockBean
  private SettlementService settlementService;
  @Autowired
  SettlementConsumer settlementConsumer;

  //工序3
  @Test
  void should_call_settlement_service_update_request_with_aid_3_amount_50_when_get_update_request()
      throws NotFoundException {

    //when
    settlementConsumer
        .onGetUpdateSettlementRequest(SettlementDTO.builder().amount(50d).auctionId(3L).build());
    //then
    Mockito.verify(settlementService).updateRequest(3L, 50);
  }
}
