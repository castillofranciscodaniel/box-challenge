package com.box.challenge.service;

import jakarta.persistence.PersistenceException;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

public abstract class GenericService<T> {

    private final JpaRepository<T, Long> jpaRepository;

    public GenericService(JpaRepository<T, Long> jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Transactional
    public T save(T t) throws ResponseStatusException {
        try {
            return this.jpaRepository.saveAndFlush(t);
        } catch (DataAccessException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error: " + e);
        }
    }

    @Transactional
    public void saveAll(List<T> t) {
        try {
            this.jpaRepository.saveAll(t);
        } catch (Exception ignored) {

        }
    }

    @Transactional
    public Boolean delete(T t) {
        try {
            this.jpaRepository.delete(t);
            return true;
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error id no debe ser null. Error: " + e);
        } catch (PersistenceException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error en el servidor. Error: " + e);
        }
    }

    @Transactional
    public Boolean delete(Long id) {
        try {
            this.jpaRepository.deleteById(id);
            return true;
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error id no debe ser null. Error: " + e);
        } catch (PersistenceException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error en el servidor. Error: " + e);
        }

    }

    @Transactional
    public Boolean deleteAll(List<T> listT) {
        try {
            this.jpaRepository.deleteAllInBatch(listT);
            return true;
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error: " + e);
        } catch (PersistenceException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error en el servidor. Error: " + e);
        }

    }

    public T findById(Long id) {
        try {
            Optional<T> tOptional = this.jpaRepository.findById(id);
            return tOptional.orElse(null);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error id no debe ser null. Error: " + e);
        } catch (PersistenceException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error en el servidor. Error: " + e);
        }
    }

    public List<T> findAll() {
        try {
            return this.jpaRepository.findAll();
        } catch (Exception e) {
            return null;
        }
    }

    public Page<T> findAll(Pageable pageable) {
        try {
            return this.jpaRepository.findAll(pageable);
        } catch (Exception e) {
            return null;
        }
    }
}
