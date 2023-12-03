package com.nordeus.jobfair.auctionservice.auctionservice.domain.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;


@Entity
@Table(name = "user_account")
public class User {

    @Id
    private UserId userId;

    private String userName;
    private Integer availableTokens;
    private Integer availableMoney;
    private String notification;

    public User() {
        this.userId = new UserId();
    }

    public User(String userName, Integer availableTokens, Integer availableMoney, String notification) {
        this.userId = new UserId();
        this.userName = userName;
        this.availableTokens = availableTokens;
        this.availableMoney = availableMoney;
        this.notification = notification;
    }

    public UserId getUserId() {
        return userId;
    }

    public void setUserId(UserId userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Integer getAvailableTokens() {
        return availableTokens;
    }

    public void setAvailableTokens(Integer availableTokens) {
        this.availableTokens = availableTokens;
    }

    public Integer getAvailableMoney() {
        return availableMoney;
    }

    public void setAvailableMoney(Integer availableMoney) {
        this.availableMoney = availableMoney;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return userId != null && userId.equals(user.userId);
    }

    @Override
    public int hashCode() {
        return userId != null ? userId.hashCode() : 0;
    }

    public String getNotification() {
        return notification;
    }

    public void setNotification(String notification) {
        this.notification = notification;
    }
}
