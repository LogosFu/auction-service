package com.thoughtworks.auction.auctionservice.settlement;

import com.thoughtworks.auction.auctionservice.settlement.repository.SettlementMessageRepository;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.jms.core.JmsTemplate;

@SpringBootTest
class SettlementMessageRepositoryTest {

  @MockBean
  JmsTemplate jmsTemplate;
  @Autowired
  SettlementMessageRepository settlementMessageRepository;

  // 工序8
  @Test
  void should_call_jms_template_with_aid_1_when_sendSettlementMessage_given_aid_1() {

    //when
    settlementMessageRepository.sendSettlementMessage(1L);
    //then
    ArgumentCaptor<Long> messageCaptor = ArgumentCaptor.forClass(Long.class);
    Mockito.verify(jmsTemplate).convertAndSend("settlement.service.amount", 1L);
  }
}
