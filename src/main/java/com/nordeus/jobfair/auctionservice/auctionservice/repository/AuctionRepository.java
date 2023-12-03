package com.nordeus.jobfair.auctionservice.auctionservice.repository;

import com.nordeus.jobfair.auctionservice.auctionservice.domain.model.Auction;
import com.nordeus.jobfair.auctionservice.auctionservice.domain.model.AuctionId;
import com.nordeus.jobfair.auctionservice.auctionservice.domain.service.AuctionStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AuctionRepository extends JpaRepository<Auction, AuctionId> {

    List<Auction> findByExpirationTimeBetweenAndStatus(
            LocalDateTime startTime, LocalDateTime endTime, AuctionStatus status);

    List<Auction> findAllByStatus(AuctionStatus active);
}
