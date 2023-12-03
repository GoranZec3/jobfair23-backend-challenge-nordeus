package com.nordeus.jobfair.auctionservice.auctionservice.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nordeus.jobfair.auctionservice.auctionservice.domain.AuctionService;
import com.nordeus.jobfair.auctionservice.auctionservice.domain.AuctionServiceImpl;
import com.nordeus.jobfair.auctionservice.auctionservice.domain.model.*;
import com.nordeus.jobfair.auctionservice.auctionservice.domain.service.BidService;
import com.nordeus.jobfair.auctionservice.auctionservice.domain.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.Map;

@AllArgsConstructor
@RestController
@RequestMapping(path = "/auctions")
public class HttpController {

    private AuctionService auctionService;
    @Autowired
    private UserService userService;
    @Autowired
    private AuctionServiceImpl auctionServiceImpl;
    @Autowired
    private BidService bidService;

    @GetMapping("/active")
    public Collection<Auction> getAllActive() {
        return auctionService.getAllActive();
    }


    @PostMapping("/user")
    public ResponseEntity<String> addUser(@RequestBody User user){
        userService.saveUser(user);
        return new ResponseEntity<>("User added ", HttpStatus.OK);
    }

//    @PostMapping("/bid")
//    public ResponseEntity<String> placeBid(@RequestBody Bid bid, AuctionId auctionId){
//        bidService.createBid(bid, auctionId);
//        return new ResponseEntity<>("Bid is Placed", HttpStatus.OK);
//    }

    @PostMapping("/bid")
    public ResponseEntity<String> placeBid(@RequestBody Map<String, Object> newBid) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            UserId userId = objectMapper.convertValue(newBid.get("userId"), UserId.class);
            AuctionId auctionId = objectMapper.convertValue(newBid.get("auctionId"), AuctionId.class);

            bidService.placeBid(userId, auctionId);
            return new ResponseEntity<>("Bid is Placed", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Failed to place the bid: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/user/{userName}")
    public ResponseEntity<User> findUser(@PathVariable String userName){
      return new ResponseEntity<>(userService.getUserByName(userName), HttpStatus.OK);
    }


    @GetMapping("/all")
    public ResponseEntity<Collection<Auction>> findAll(){
        return new ResponseEntity<>(auctionServiceImpl.getAll(), HttpStatus.OK);
    }

    @GetMapping("/allActiveAuctions")
    public ResponseEntity<Collection<Auction>> getAllActiveAuctions(){
        return new ResponseEntity<>(auctionServiceImpl.getAllActive(), HttpStatus.OK);
    }
}
