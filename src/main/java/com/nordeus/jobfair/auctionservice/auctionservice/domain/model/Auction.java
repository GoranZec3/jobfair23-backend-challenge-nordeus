package com.nordeus.jobfair.auctionservice.auctionservice.domain.model;

import com.nordeus.jobfair.auctionservice.auctionservice.domain.service.AuctionStatus;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Entity
public class Auction {

    @Id
    private AuctionId id;

    private String playerName;
    private Integer playerValue;
    private Integer tokenAmount;

    @OneToMany(fetch = FetchType.EAGER)
    private List<Bid> bids = new ArrayList<>();
    private LocalDateTime expirationTime;
    private AuctionStatus status;


    public Auction() {
        this.expirationTime = LocalDateTime.now().plusSeconds(100);
        this.status = AuctionStatus.ACTIVE;
    }

    public Auction(String playerName, Integer playerValue,
                   Integer tokenAmount, List<Bid> bids) {

        this.id = new AuctionId();
        this.playerName = playerName;
        this.playerValue = playerValue;
        this.tokenAmount = tokenAmount;
        this.bids = bids;
        //set 1 min expirationTime of the auction
        this.expirationTime = LocalDateTime.now().plusSeconds(100);
        this.status = AuctionStatus.ACTIVE;
    }

    public AuctionId getId() {
        return id;
    }

    public void setId(AuctionId id) {
        this.id = id;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public Integer getPlayerValue() {
        return playerValue;
    }

    public void setPlayerValue(Integer playerValue) {
        this.playerValue = playerValue;
    }

    public Integer getTokenAmount() {
        return tokenAmount;
    }

    public void setTokenAmount(Integer tokenAmount) {
        this.tokenAmount = tokenAmount;
    }

    public List<Bid> getBids() {
        return bids;
    }

    public void setBids(List<Bid> bids) {
        this.bids = bids;
    }

    public LocalDateTime getExpirationTime() {
        return expirationTime;
    }

    public void setExpirationTime(LocalDateTime expirationTime) {
        this.expirationTime = expirationTime;
    }

    public AuctionStatus getStatus() {
        return status;
    }

    public void setStatus(AuctionStatus status) {
        this.status = status;
    }


}
