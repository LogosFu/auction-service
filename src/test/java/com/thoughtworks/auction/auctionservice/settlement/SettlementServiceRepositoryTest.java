package com.thoughtworks.auction.auctionservice.settlement;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.thoughtworks.auction.auctionservice.AuctionServiceApplication;
import com.thoughtworks.auction.auctionservice.settlement.repository.SettlementServiceClient;
import com.thoughtworks.auction.auctionservice.settlement.repository.SettlementServiceRepository;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = AuctionServiceApplication.class)
class SettlementServiceRepositoryTest {

  @Autowired
  SettlementServiceRepository settlementServiceRepository;

  @MockBean
  SettlementServiceClient settlementServiceClient;

  //工序9
  @Test
  void should_call_with_a_id_1L_when_amountByAuction() {

    //given
    when(settlementServiceClient.amountByAuction(any())).thenReturn(20d);

    //when
    double count = settlementServiceRepository.amountByAuction(1L);

    //then
    ArgumentCaptor<Long> argumentCaptor =
        ArgumentCaptor.forClass(Long.class);
    verify(settlementServiceClient).amountByAuction(argumentCaptor.capture());
    assertThat(argumentCaptor.getValue()).isEqualTo(1L);
    assertThat(count).isEqualTo(20d);
  }
}
