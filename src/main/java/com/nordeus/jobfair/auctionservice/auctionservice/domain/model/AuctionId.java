package com.nordeus.jobfair.auctionservice.auctionservice.domain.model;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

@Entity
public class AuctionId implements Serializable {

    @Id
    private final UUID id;

    public AuctionId() {
        this.id = UUID.randomUUID();
    }

    public UUID getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AuctionId auctionId = (AuctionId) o;
        return Objects.equals(id, auctionId.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
