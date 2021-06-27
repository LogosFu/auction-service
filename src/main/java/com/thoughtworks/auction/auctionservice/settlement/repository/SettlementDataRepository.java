package com.thoughtworks.auction.auctionservice.settlement.repository;

import com.thoughtworks.auction.auctionservice.settlement.repository.Settlement;
import java.util.Optional;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

@Service("settlementDataRepository")
@Primary
public interface SettlementDataRepository {

  Optional<Settlement> findByAuctionId(Long auctionId);

  Settlement save(Settlement settlement);
}
