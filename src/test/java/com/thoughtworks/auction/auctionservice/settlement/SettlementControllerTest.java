package com.thoughtworks.auction.auctionservice.settlement;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thoughtworks.auction.auctionservice.AuctionServiceApplication;
import com.thoughtworks.auction.auctionservice.settlement.controller.dto.SettlementDTO;
import com.thoughtworks.auction.auctionservice.settlement.controller.dto.SettlementServiceException;
import com.thoughtworks.auction.auctionservice.settlement.controller.dto.SettlementStatus;
import com.thoughtworks.auction.auctionservice.settlement.service.SettlementService;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = AuctionServiceApplication.class)
class SettlementControllerTest {

  @Autowired
  private TestRestTemplate restTemplate;
  @Autowired
  private ObjectMapper mapper;
  @MockBean
  private SettlementService settlementService;


  //工序1
  @Test
  void should_return200_and_dto_from_service_and_call_service_with_aid_given_when_call_settlement_request_given_stub_dto()
      throws JsonProcessingException {

    //given
    Mockito.when(settlementService.settlementRequest(1L)).thenReturn(
        SettlementDTO.builder().auctionId(1L).amount(20D).status(SettlementStatus.PAYMENT).build());
    //when
    ResponseEntity<String> response = restTemplate
        .getForEntity("/auction/1/settlement", String.class);

    //then
    ArgumentCaptor<Long> aidArg = ArgumentCaptor.forClass(Long.class);
    verify(settlementService).settlementRequest(aidArg.capture());
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(aidArg.getValue()).isEqualTo(1L);
    SettlementDTO settlementDTO = mapper.readValue(response.getBody(), SettlementDTO.class);
    assertThat(settlementDTO.getAmount()).isEqualTo(20d);
    assertThat(settlementDTO.getAuctionId()).isEqualTo(1L);
  }
  //工序2
  @Test
  void should_return500_and_call_service_with_aid_given_when_call_settlement_request_given_exception_from_service() {

    //given
    Mockito.when(settlementService.settlementRequest(1L)).thenThrow(new SettlementServiceException());
    //when
    ResponseEntity<String> response = restTemplate
        .getForEntity("/auction/1/settlement", String.class);

    //then
    ArgumentCaptor<Long> aidArg = ArgumentCaptor.forClass(Long.class);
    verify(settlementService).settlementRequest(aidArg.capture());
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
    assertThat(aidArg.getValue()).isEqualTo(1L);
  }


}
