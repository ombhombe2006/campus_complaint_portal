package com.campus.repository;

import com.campus.entity.Review;
import com.campus.entity.User;
import com.campus.entity.Facility;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    
    List<Review> findByUserOrderByCreatedAtDesc(User user);
    
    List<Review> findByFacilityOrderByCreatedAtDesc(Facility facility);
    
    List<Review> findByStatusOrderByCreatedAtDesc(String status);
    
    @Query("SELECT r FROM Review r JOIN FETCH r.user JOIN FETCH r.facility ORDER BY r.createdAt DESC")
    List<Review> findAllWithUserAndFacility();
    
    @Query("SELECT r FROM Review r JOIN FETCH r.facility WHERE r.status = :status ORDER BY r.createdAt DESC")
    List<Review> findByStatusWithFacility(@Param("status") String status);
    
    @Query("SELECT r FROM Review r JOIN FETCH r.facility WHERE r.status = 'APPROVED' ORDER BY r.createdAt DESC LIMIT 6")
    List<Review> findRecentApprovedReviews();
}