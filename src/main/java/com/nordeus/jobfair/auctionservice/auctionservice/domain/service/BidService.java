package com.nordeus.jobfair.auctionservice.auctionservice.domain.service;

import com.nordeus.jobfair.auctionservice.auctionservice.domain.model.*;
import com.nordeus.jobfair.auctionservice.auctionservice.repository.AuctionRepository;
import com.nordeus.jobfair.auctionservice.auctionservice.repository.BidRepository;
import com.nordeus.jobfair.auctionservice.auctionservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class BidService {

    @Autowired
    private BidRepository bidRepository;
    @Autowired
    private AuctionRepository auctionRepository;
    @Autowired
    private UserRepository userRepository;



    public void placeBid(UserId userId, AuctionId auctionId) {
        Optional<Auction> optionalAuction = auctionRepository.findById(auctionId);
        Optional<User> optionalUser = userRepository.findById(userId);

        if (optionalAuction.isEmpty()) {
            throw new IllegalArgumentException("Auction with the provided ID does not exist");
        }

        Auction auction = optionalAuction.get();
        LocalDateTime currentTime = LocalDateTime.now();
        LocalDateTime expirationTime = auction.getExpirationTime();

        checkAuctionStatus(currentTime, expirationTime);
        checkIfUserBidIsActive(auction, userId);

        List<Bid> bids = auction.getBids();

        if (bids.isEmpty()) {
            createFirstBid(auction, optionalUser, userId);
        } else {
            createAdditionalBid(auction, optionalUser, userId, bids);
        }
    }

    private void checkAuctionStatus(LocalDateTime currentTime, LocalDateTime expirationTime) {
        if (currentTime.isAfter(expirationTime)) {
            throw new IllegalArgumentException("Auction is not active anymore");
        }
    }

    private void checkIfUserBidIsActive(Auction auction, UserId userId) {
        List<Bid> bids = auction.getBids();
        if (!bids.isEmpty() && bids.get(0).getUserId().equals(userId)) {
            throw new IllegalArgumentException("Your bid is still active");
        }
    }

    private void checkExpirationTime(Auction auction) {
        LocalDateTime currentTime = LocalDateTime.now();
        LocalDateTime expirationTime = auction.getExpirationTime();
        Duration difference = Duration.between(currentTime, expirationTime);
        Duration threshold = Duration.ofSeconds(5);

        if (difference.getSeconds() <= threshold.getSeconds()) {
            auction.setExpirationTime(expirationTime.plusSeconds(5));
        }
    }

    private void createFirstBid(Auction auction, Optional<User> optionalUser, UserId userId) {
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();

            if (user.getAvailableTokens() > 1 && (user.getAvailableMoney() + 10) > auction.getPlayerValue()) {
                newBidCreator(auction, userId, auction.getPlayerValue(), user);
            }
        }
    }

    private void createAdditionalBid(Auction auction, Optional<User> optionalUser, UserId userId, List<Bid> bids) {
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            int currentMaxAmount = bids.get(0).getAmount();
            if (user.getAvailableTokens() > 1 && (user.getAvailableMoney() + 10) > currentMaxAmount) {
                newBidCreator(auction, userId, currentMaxAmount + 10, user);
            }
        }
    }

    private void newBidCreator(Auction auction, UserId userId, int amount, User user) {
        Bid newBid = new Bid();
        newBid.setUserId(userId);
        newBid.setAmount(amount);

        auction.setTokenAmount(auction.getTokenAmount() + 1);
        user.setAvailableTokens(user.getAvailableTokens() - 1);
        user.setAvailableMoney(user.getAvailableMoney() - amount);

        bidRepository.save(newBid);
        auction.getBids().add(0, newBid);

        checkExpirationTime(auction);
        auctionRepository.save(auction);
    }



}
