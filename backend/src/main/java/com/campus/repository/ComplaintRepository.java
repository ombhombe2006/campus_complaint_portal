package com.campus.repository;

import com.campus.entity.Complaint;
import com.campus.entity.User;
import com.campus.entity.Facility;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ComplaintRepository extends JpaRepository<Complaint, Long> {
    
    List<Complaint> findByUserOrderByCreatedAtDesc(User user);
    
    List<Complaint> findByFacilityOrderByCreatedAtDesc(Facility facility);
    
    List<Complaint> findByStatusOrderByCreatedAtDesc(String status);
    
    @Query("SELECT c FROM Complaint c JOIN FETCH c.user JOIN FETCH c.facility ORDER BY c.createdAt DESC")
    List<Complaint> findAllWithUserAndFacility();
    
    @Query("SELECT c FROM Complaint c JOIN FETCH c.facility WHERE c.status = :status ORDER BY c.createdAt DESC")
    List<Complaint> findByStatusWithFacility(@Param("status") String status);
}