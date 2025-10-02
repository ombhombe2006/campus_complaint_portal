package com.campus.controller;

import com.campus.dto.ApiResponse;
import com.campus.entity.Review;
import com.campus.entity.User;
import com.campus.entity.Facility;
import com.campus.repository.ReviewRepository;
import com.campus.repository.UserRepository;
import com.campus.repository.FacilityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/reviews")
@CrossOrigin(origins = "*", allowCredentials = "true")
public class ReviewController {
    
    @Autowired
    private ReviewRepository reviewRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private FacilityRepository facilityRepository;
    
    @GetMapping("/recent")
    public ResponseEntity<List<Map<String, Object>>> getRecentReviews() {
        try {
            List<Review> reviews = reviewRepository.findByStatusWithFacility("APPROVED")
                    .stream().limit(6).collect(Collectors.toList());
            
            List<Map<String, Object>> response = reviews.stream()
                    .map(this::convertToReviewMap)
                    .collect(Collectors.toList());
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    @GetMapping
    public ResponseEntity<List<Map<String, Object>>> getAllReviews() {
        try {
            List<Review> reviews = reviewRepository.findAllWithUserAndFacility();
            
            List<Map<String, Object>> response = reviews.stream()
                    .map(this::convertToFullReviewMap)
                    .collect(Collectors.toList());
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    @GetMapping("/user")
    public ResponseEntity<List<Map<String, Object>>> getUserReviews(HttpSession session) {
        if (session.getAttribute("userId") == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        
        try {
            Long userId = (Long) session.getAttribute("userId");
            Optional<User> userOpt = userRepository.findById(userId);
            
            if (userOpt.isPresent()) {
                List<Review> reviews = reviewRepository.findByUserOrderByCreatedAtDesc(userOpt.get());
                
                List<Map<String, Object>> response = reviews.stream()
                        .map(this::convertToUserReviewMap)
                        .collect(Collectors.toList());
                
                return ResponseEntity.ok(response);
            }
            
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    @GetMapping("/facilities")
    public ResponseEntity<List<Map<String, Object>>> getFacilities() {
        try {
            List<Facility> facilities = facilityRepository.findAllByOrderByName();
            
            List<Map<String, Object>> response = facilities.stream()
                    .map(facility -> {
                        Map<String, Object> map = new HashMap<>();
                        map.put("id", facility.getId());
                        map.put("name", facility.getName());
                        map.put("category", facility.getCategory());
                        return map;
                    })
                    .collect(Collectors.toList());
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    @PostMapping
    public ResponseEntity<ApiResponse<Void>> addReview(@RequestBody Map<String, Object> reviewData, HttpSession session) {
        if (session.getAttribute("userId") == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.error("Not authenticated"));
        }
        
        try {
            Long userId = (Long) session.getAttribute("userId");
            Long facilityId = Long.valueOf(reviewData.get("facilityId").toString());
            String title = (String) reviewData.get("title");
            String content = (String) reviewData.get("content");
            Integer rating = Integer.valueOf(reviewData.get("rating").toString());
            
            Optional<User> userOpt = userRepository.findById(userId);
            Optional<Facility> facilityOpt = facilityRepository.findById(facilityId);
            
            if (userOpt.isPresent() && facilityOpt.isPresent()) {
                Review review = new Review(userOpt.get(), facilityOpt.get(), title, content, rating);
                reviewRepository.save(review);
                
                return ResponseEntity.ok(ApiResponse.success("Review submitted successfully"));
            }
            
            return ResponseEntity.badRequest().body(ApiResponse.error("Invalid user or facility"));
            
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Server error: " + e.getMessage()));
        }
    }
    
    @PutMapping
    public ResponseEntity<ApiResponse<Void>> updateReviewStatus(@RequestBody Map<String, Object> updateData, HttpSession session) {
        if (!"admin".equals(session.getAttribute("userType"))) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.error("Admin access required"));
        }
        
        try {
            Long reviewId = Long.valueOf(updateData.get("reviewId").toString());
            String status = (String) updateData.get("status");
            
            Optional<Review> reviewOpt = reviewRepository.findById(reviewId);
            
            if (reviewOpt.isPresent()) {
                Review review = reviewOpt.get();
                review.setStatus(status);
                reviewRepository.save(review);
                
                return ResponseEntity.ok(ApiResponse.success("Review status updated"));
            }
            
            return ResponseEntity.notFound().body(ApiResponse.error("Review not found"));
            
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Server error: " + e.getMessage()));
        }
    }
    
    // Helper methods to convert entities to maps (similar to old JSON approach)
    private Map<String, Object> convertToReviewMap(Review review) {
        Map<String, Object> map = new HashMap<>();
        map.put("title", review.getTitle());
        map.put("content", review.getContent());
        map.put("rating", review.getRating());
        map.put("facilityName", review.getFacility().getName());
        return map;
    }
    
    private Map<String, Object> convertToFullReviewMap(Review review) {
        Map<String, Object> map = new HashMap<>();
        map.put("id", review.getId());
        map.put("title", review.getTitle());
        map.put("content", review.getContent());
        map.put("rating", review.getRating());
        map.put("status", review.getStatus());
        map.put("createdAt", review.getCreatedAt().toString());
        map.put("userName", review.getUser().getFullName());
        map.put("facilityName", review.getFacility().getName());
        return map;
    }
    
    private Map<String, Object> convertToUserReviewMap(Review review) {
        Map<String, Object> map = new HashMap<>();
        map.put("id", review.getId());
        map.put("title", review.getTitle());
        map.put("content", review.getContent());
        map.put("rating", review.getRating());
        map.put("status", review.getStatus());
        map.put("createdAt", review.getCreatedAt().toString());
        map.put("facilityName", review.getFacility().getName());
        return map;
    }
}