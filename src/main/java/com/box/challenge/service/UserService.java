package com.box.challenge.service;

import com.box.challenge.model.User;
import com.box.challenge.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService extends GenericService<User> {

    private final UserRepository userRepository;


    public UserService(UserRepository userRepository) {
        super(userRepository);
        this.userRepository = userRepository;
    }

    public User findByUsername(String username) {
        return this.userRepository.findByUsername(username);
    }

}
