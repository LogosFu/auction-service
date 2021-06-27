package com.thoughtworks.auction.auctionservice.settlement;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.thoughtworks.auction.auctionservice.settlement.controller.dto.SettlementDTO;
import com.thoughtworks.auction.auctionservice.settlement.controller.dto.SettlementServiceException;
import com.thoughtworks.auction.auctionservice.settlement.controller.dto.SettlementStatus;
import com.thoughtworks.auction.auctionservice.settlement.repository.Settlement;
import com.thoughtworks.auction.auctionservice.settlement.repository.SettlementDataRepository;
import com.thoughtworks.auction.auctionservice.settlement.repository.SettlementMessageRepository;
import com.thoughtworks.auction.auctionservice.settlement.service.SettlementService;
import com.thoughtworks.auction.auctionservice.settlement.repository.SettlementServiceRepository;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;

@SpringBootTest
class SettlementServiceTest {


  SettlementService settlementService;
  SettlementServiceRepository settlementServiceRepository;
  SettlementDataRepository settlementDataRepository;
  SettlementMessageRepository settlementMessageRepository;

  @BeforeEach
  void setUp() {
    settlementDataRepository = mock(SettlementDataRepository.class);
    settlementServiceRepository = mock(SettlementServiceRepository.class);
    settlementMessageRepository = mock(SettlementMessageRepository.class);
    settlementService = new SettlementService(settlementDataRepository,
        settlementServiceRepository, settlementMessageRepository);
  }

  //工序4
  @Test
  void should_create_settlement_when_settlementRequest_given_no_settlement_with_auction_in_db_and_call_service_success_return_20() {
    //given
    when(settlementDataRepository.findByAuctionId(any())).thenReturn(Optional.empty());
    when(settlementServiceRepository.amountByAuction(any())).thenReturn(20d);
    Settlement settlement = Settlement.builder().auctionId(1L).amount(20D)
        .status(SettlementStatus.PAYMENT).build();
    when(settlementDataRepository.save(
        settlement))
        .thenReturn(settlement);

    //when
    SettlementDTO settlementDTO = settlementService.settlementRequest(1L);
    //then
    ArgumentCaptor<Long> aIdArg = ArgumentCaptor.forClass(Long.class);
    verify(settlementServiceRepository).amountByAuction(aIdArg.capture());
    assertThat(aIdArg.getValue()).isEqualTo(1L);
    ArgumentCaptor<Settlement> settlementEntityArg =
        ArgumentCaptor.forClass(Settlement.class);
    verify(settlementDataRepository, times(1)).save(settlementEntityArg.capture());
    assertThat(settlementEntityArg.getValue().getAuctionId()).isEqualTo(1L);
    assertThat(settlementEntityArg.getValue().getAmount()).isEqualTo(20D);
    assertThat(settlementDTO.getAmount()).isEqualTo(20D);
  }
  //工序4
  @Test
  void should_return_settlement_in_db_no_call_save_and_no_call_service_when_settlementRequest_given_settlement_with_auction_in_db() {
    //given
    when(settlementDataRepository.findByAuctionId(any())).thenReturn(
        Optional.of(Settlement.builder().id(1L).auctionId(1L).amount(30d)
            .status(SettlementStatus.REQUEST)
            .build()));

    //when
    SettlementDTO settlementDTO = settlementService.settlementRequest(1L);
    //then
    verify(settlementServiceRepository, times(0)).amountByAuction(any());
    verify(settlementDataRepository, times(0)).save(any());
    assertThat(settlementDTO.getAmount()).isEqualTo(30D);
  }
  //工序5
  @Test
  void should_create_settlement_with_null_amount_and_send_message_with_aid_when_settlementRequest_given_no_settlement_with_auction_in_db_and_call_service_wrong() {
    //given
    when(settlementDataRepository.findByAuctionId(any())).thenReturn(Optional.empty());
    when(settlementServiceRepository.amountByAuction(any()))
        .thenThrow(new RuntimeException());
    Settlement settlement = Settlement.builder().auctionId(1L).amount(null)
        .status(SettlementStatus.PAYMENT).build();
    when(settlementDataRepository.save(
        settlement))
        .thenReturn(settlement);

    //when
    assertThatThrownBy(() -> settlementService.settlementRequest(1L))
        .isInstanceOf(SettlementServiceException.class);
    //then
    ArgumentCaptor<Long> aIdArg = ArgumentCaptor.forClass(Long.class);
    verify(settlementServiceRepository).amountByAuction(aIdArg.capture());
    assertThat(aIdArg.getValue()).isEqualTo(1L);
    ArgumentCaptor<Settlement> settlementEntityArg =
        ArgumentCaptor.forClass(Settlement.class);
    verify(settlementDataRepository, times(1)).save(settlementEntityArg.capture());
    assertThat(settlementEntityArg.getValue().getAuctionId()).isEqualTo(1L);
    assertThat(settlementEntityArg.getValue().getAmount()).isNull();
  }

  //工序5
  @Test
  void should_return_exception_no_call_save_and_no_call_service_when_settlementRequest_given_settlement_with_auction_in_db_and_amount_is_null() {
    //given
    when(settlementDataRepository.findByAuctionId(any())).thenReturn(
        Optional.of(Settlement.builder().id(1L).auctionId(1L).amount(null)
            .status(SettlementStatus.REQUEST)
            .build()));

    //when
    assertThatThrownBy(() -> settlementService.settlementRequest(1L))
        .isInstanceOf(SettlementServiceException.class);
    //then
    verify(settlementServiceRepository, times(0)).amountByAuction(any());
    verify(settlementDataRepository, times(0)).save(any());
  }

  //工序4
  @Test
  void should_update_amount_when_updateRequest_given_settlement_with_auction_in_db_and_amount_20()
      throws NotFoundException {
    //given
    when(settlementDataRepository.findByAuctionId(any())).thenReturn(
        Optional.of(Settlement.builder().id(1L).auctionId(1L).amount(null)
            .status(SettlementStatus.REQUEST)
            .build()));

    Settlement settlement = Settlement.builder().id(1L).auctionId(1L).amount(20d)
        .status(SettlementStatus.REQUEST).build();
    when(settlementDataRepository.save(
        settlement))
        .thenReturn(settlement);
    //when
    SettlementDTO settlementDTO = settlementService.updateRequest(1L, 20d);
    //then
    verify(settlementServiceRepository, times(0)).amountByAuction(any());
    ArgumentCaptor<Settlement> settlementEntityArg =
        ArgumentCaptor.forClass(Settlement.class);
    verify(settlementDataRepository, times(1)).save(settlementEntityArg.capture());
    assertThat(settlementEntityArg.getValue().getAmount()).isEqualTo(20d);
    assertThat(settlementEntityArg.getValue().getId()).isEqualTo(1L);
    assertThat(settlementEntityArg.getValue().getAuctionId()).isEqualTo(1L);
    assertThat(settlementDTO.getAmount()).isEqualTo(20D);
  }
}
