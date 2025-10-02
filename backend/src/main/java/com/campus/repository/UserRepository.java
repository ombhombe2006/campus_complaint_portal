package com.campus.repository;

import com.campus.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    
    Optional<User> findByUsername(String username);
    
    Optional<User> findByUsernameAndPassword(String username, String password);
    
    boolean existsByUsername(String username);
    
    boolean existsByEmail(String email);
}