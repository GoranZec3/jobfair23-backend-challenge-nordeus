package com.nordeus.jobfair.auctionservice.auctionservice.domain.service;

import com.nordeus.jobfair.auctionservice.auctionservice.domain.model.User;
import com.nordeus.jobfair.auctionservice.auctionservice.domain.model.UserId;
import com.nordeus.jobfair.auctionservice.auctionservice.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;


    public User saveUser(User user){
        return userRepository.save(user);
    }

    public User getUserByName(String userName){
        return userRepository.findByUserName(userName);
    }

    public User getUserById(UserId userId){
        return userRepository.findById(userId).orElseThrow(() -> new RuntimeException(String.format("User under %s Id is not found", userId)));
    }






}
