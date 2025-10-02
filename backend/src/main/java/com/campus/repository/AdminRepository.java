package com.campus.repository;

import com.campus.entity.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AdminRepository extends JpaRepository<Admin, Long> {
    
    Optional<Admin> findByUsername(String username);
    
    Optional<Admin> findByUsernameAndPassword(String username, String password);
    
    boolean existsByUsername(String username);
}