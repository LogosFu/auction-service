package com.thoughtworks.auction.auctionservice.settlement.repository;

import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

@Component
public class SettlementJMSClient implements SettlementMessageRepository {

  private final JmsTemplate jmsTemplate;

  public SettlementJMSClient(JmsTemplate jmsTemplate) {
    this.jmsTemplate = jmsTemplate;
  }


  @Override
  public void sendSettlementMessage(Long aId) {
    jmsTemplate.convertAndSend("settlement.service.amount", aId);
  }
}
