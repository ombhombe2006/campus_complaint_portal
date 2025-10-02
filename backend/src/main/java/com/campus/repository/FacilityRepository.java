package com.campus.repository;

import com.campus.entity.Facility;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FacilityRepository extends JpaRepository<Facility, Long> {
    
    Optional<Facility> findByName(String name);
    
    List<Facility> findByCategory(String category);
    
    List<Facility> findAllByOrderByName();
}