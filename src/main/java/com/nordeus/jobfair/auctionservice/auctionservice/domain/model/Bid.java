package com.nordeus.jobfair.auctionservice.auctionservice.domain.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.mapping.ToOne;



@Entity
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Table(name = "Bid_table")
public class Bid {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private UserId userId;
    private Integer amount;

}
