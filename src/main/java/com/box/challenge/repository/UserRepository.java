package com.box.challenge.repository;

import com.box.challenge.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// UserRepository.java
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    User findByUsername(String username);
}