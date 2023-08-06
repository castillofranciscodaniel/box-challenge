package com.box.challenge.repository;

import com.box.challenge.model.Document;
import com.box.challenge.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

// UserRepository.java
@Repository
public interface DocumentRepository extends JpaRepository<Document, Long> {
    List<Document> findByUser(User user);

    Document findByUserAndHashSha256(User user, String hashSha256);

    Document findByUserAndHashSha512(User user, String hashSha512);
}