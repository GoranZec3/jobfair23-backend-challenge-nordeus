package com.nordeus.jobfair.auctionservice.auctionservice.domain.service;

import com.nordeus.jobfair.auctionservice.auctionservice.domain.model.Auction;
import com.nordeus.jobfair.auctionservice.auctionservice.domain.model.Bid;
import org.springframework.stereotype.Repository;

import java.util.Collection;
@Repository
public interface AuctionNotifer {

    void auctionFinished(Auction auction);

    void bidPlaced(Bid bid);

    void activeAuctionsRefreshed(Collection<Auction> activeAuctions);
}
