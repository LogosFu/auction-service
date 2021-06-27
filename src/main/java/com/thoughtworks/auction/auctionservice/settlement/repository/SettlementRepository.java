package com.thoughtworks.auction.auctionservice.settlement.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("settlementRepository")
public interface SettlementRepository extends JpaRepository<Settlement, Long>,
    SettlementDataRepository {

  Optional<Settlement> findByAuctionId(Long auctionId);

  Settlement save(Settlement settlement);
}
