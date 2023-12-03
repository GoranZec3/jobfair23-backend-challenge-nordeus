package com.nordeus.jobfair.auctionservice.auctionservice.domain;

import com.nordeus.jobfair.auctionservice.auctionservice.domain.model.*;
import com.nordeus.jobfair.auctionservice.auctionservice.domain.service.AuctionNotifer;
import com.nordeus.jobfair.auctionservice.auctionservice.domain.service.AuctionStatus;
import com.nordeus.jobfair.auctionservice.auctionservice.repository.AuctionRepository;
import com.nordeus.jobfair.auctionservice.auctionservice.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;


@Service
@EnableScheduling
public class AuctionServiceImpl implements AuctionService {

    private static final Logger logger = LoggerFactory.getLogger(AuctionServiceImpl.class);

    @Autowired
    private final AuctionNotifer auctionNotifer;
    @Autowired
    private final AuctionRepository auctionRepository;
    @Autowired
    private final UserRepository userRepository;

    private LocalDateTime lastCheckTime = LocalDateTime.now();

    public AuctionServiceImpl(AuctionNotifer auctionNotifer,
                              AuctionRepository auctionRepository, UserRepository userRepository) {
        this.auctionNotifer = auctionNotifer;
        this.auctionRepository = auctionRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Collection<Auction> getAllActive() {
        //check if there is active auction
        List<Auction> activeAuctions = auctionRepository.findAllByStatus(AuctionStatus.ACTIVE);
        return new LinkedList<>(activeAuctions);
    }


    @Override
    public Optional<Auction> getAuction(AuctionId auctionId) {
        return auctionRepository.findById(auctionId);
    }

    @Override
    public void join(AuctionId auctionId, User user) {

    }

    @Override
    public void bid(AuctionId auctionId, UserId userId) {
        //auctionNotifer.bidPlaced(new Bid());
    }

    public Collection<Auction> getAll(){
        return auctionRepository.findAll();
    }

    public Auction createAuction(Auction auction){
        return auctionRepository.save(auction);
    }

    //run this method every minute (60 sec)
    @Scheduled(fixedRate = 60000)
    public void checkExpiredAuctionsAndNotify(){
        LocalDateTime currentTime = LocalDateTime.now();
        try {
            List<Auction> expiredAuctions =
                    auctionRepository.findByExpirationTimeBetweenAndStatus(
                            lastCheckTime, currentTime, AuctionStatus.ACTIVE);

            lastCheckTime = currentTime;

            for (Auction auction : expiredAuctions) {

               if (auction.getExpirationTime() != null && currentTime.isAfter(auction.getExpirationTime())) {

                 auction.setStatus(AuctionStatus.FINISHED);
                 auctionRepository.save(auction);
                 sendNotificationToWinner(auction);
                 sendNotificationToParticipants(auction);
              }
            }
        }catch (Exception e){
            logger.error("Error occurred while checking expired auctions: {}", e.getMessage(), e);
        }
    }

    private void sendNotificationToWinner(Auction auction) {
        List<Bid> bids = auction.getBids();
        if (!bids.isEmpty()) {
            Bid winningBid = bids.get(0);
            UserId userId = winningBid.getUserId();

            Optional<User> userOptional = userRepository.findById(userId);
            if (userOptional.isPresent()) {
                User winnerUser = userOptional.get();
                String notificationMessage = String.format("You are the winner of the auction with ID: %s! Congratulations!", auction.getId().getId());
                winnerUser.setNotification(notificationMessage);
                userRepository.save(winnerUser);
            } else {
                logger.error("User {} associated with the winning bid is not found", userId.getId());
            }
        } else {
            logger.error("No bids found for the auction {}", auction.getId().getId());
        }
    }

    private void  sendNotificationToParticipants(Auction auction){
        List<Bid> bids = auction.getBids();

        String notificationMessage = String.format("The auction %s id has ended!" , auction.getId().getId());

        for (int i = 1; i < bids.size(); i++) {
            Bid currentBid = bids.get(i);
            UserId userId = currentBid.getUserId();
            Optional<User> optionalUser = userRepository.findById(userId);

            if (optionalUser.isPresent()) {
                User participant = optionalUser.get();
                participant.setNotification(notificationMessage);
                userRepository.save(participant);
            }else {
                logger.error("User {} is not presented", userId.getId());
            }
        }

    }
}
