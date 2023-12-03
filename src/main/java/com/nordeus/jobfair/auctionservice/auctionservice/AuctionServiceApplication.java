package com.nordeus.jobfair.auctionservice.auctionservice;

import com.nordeus.jobfair.auctionservice.auctionservice.domain.AuctionService;
import com.nordeus.jobfair.auctionservice.auctionservice.domain.model.Auction;
import com.nordeus.jobfair.auctionservice.auctionservice.domain.model.AuctionId;
import com.nordeus.jobfair.auctionservice.auctionservice.repository.AuctionRepository;
import com.nordeus.jobfair.auctionservice.auctionservice.repository.BidRepository;
import com.nordeus.jobfair.auctionservice.auctionservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class AuctionServiceApplication implements CommandLineRunner{

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AuctionService auctionService;
    @Autowired
    private AuctionRepository auctionRepository;
    @Autowired
    private BidRepository bidRepository;

    public static void main(String[] args) {
        SpringApplication.run(AuctionServiceApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {

        Auction auction1 = new Auction();
        auction1.setPlayerName("Gabriel Omar Batistuta");
        auction1.setPlayerValue(40);
        auction1.setTokenAmount(10);
        auction1.setId(new AuctionId());

        Auction auction2 = new Auction();
        auction2.setPlayerName("Peredrag Mijatovic");
        auction2.setPlayerValue(30);
        auction2.setTokenAmount(1);
        auction2.setId(new AuctionId());

        auctionRepository.save(auction1);
        auctionRepository.save(auction2);


    }
}
