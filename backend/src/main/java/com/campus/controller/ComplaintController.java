package com.campus.controller;

import com.campus.dto.ApiResponse;
import com.campus.entity.Complaint;
import com.campus.entity.User;
import com.campus.entity.Facility;
import com.campus.repository.ComplaintRepository;
import com.campus.repository.UserRepository;
import com.campus.repository.FacilityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/complaints")
@CrossOrigin(origins = "*", allowCredentials = "true")
public class ComplaintController {
    
    @Autowired
    private ComplaintRepository complaintRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private FacilityRepository facilityRepository;
    
    @GetMapping
    public ResponseEntity<List<Map<String, Object>>> getAllComplaints(HttpSession session) {
        if (!"admin".equals(session.getAttribute("userType"))) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        
        try {
            List<Complaint> complaints = complaintRepository.findAllWithUserAndFacility();
            
            List<Map<String, Object>> response = complaints.stream()
                    .map(this::convertToFullComplaintMap)
                    .collect(Collectors.toList());
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    @GetMapping("/user")
    public ResponseEntity<List<Map<String, Object>>> getUserComplaints(HttpSession session) {
        if (session.getAttribute("userId") == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        
        try {
            Long userId = (Long) session.getAttribute("userId");
            Optional<User> userOpt = userRepository.findById(userId);
            
            if (userOpt.isPresent()) {
                List<Complaint> complaints = complaintRepository.findByUserOrderByCreatedAtDesc(userOpt.get());
                
                List<Map<String, Object>> response = complaints.stream()
                        .map(this::convertToUserComplaintMap)
                        .collect(Collectors.toList());
                
                return ResponseEntity.ok(response);
            }
            
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    @PostMapping
    public ResponseEntity<ApiResponse<Void>> addComplaint(@RequestBody Map<String, Object> complaintData, HttpSession session) {
        if (session.getAttribute("userId") == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.error("Not authenticated"));
        }
        
        try {
            Long userId = (Long) session.getAttribute("userId");
            Long facilityId = Long.valueOf(complaintData.get("facilityId").toString());
            String title = (String) complaintData.get("title");
            String description = (String) complaintData.get("description");
            
            Optional<User> userOpt = userRepository.findById(userId);
            Optional<Facility> facilityOpt = facilityRepository.findById(facilityId);
            
            if (userOpt.isPresent() && facilityOpt.isPresent()) {
                Complaint complaint = new Complaint(userOpt.get(), facilityOpt.get(), title, description);
                complaintRepository.save(complaint);
                
                return ResponseEntity.ok(ApiResponse.success("Complaint submitted successfully"));
            }
            
            return ResponseEntity.badRequest().body(ApiResponse.error("Invalid user or facility"));
            
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Server error: " + e.getMessage()));
        }
    }
    
    @PutMapping
    public ResponseEntity<ApiResponse<Void>> updateComplaintStatus(@RequestBody Map<String, Object> updateData, HttpSession session) {
        if (!"admin".equals(session.getAttribute("userType"))) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.error("Admin access required"));
        }
        
        try {
            Long complaintId = Long.valueOf(updateData.get("complaintId").toString());
            String status = (String) updateData.get("status");
            String adminResponse = (String) updateData.get("adminResponse");
            
            Optional<Complaint> complaintOpt = complaintRepository.findById(complaintId);
            
            if (complaintOpt.isPresent()) {
                Complaint complaint = complaintOpt.get();
                complaint.setStatus(status);
                if (adminResponse != null && !adminResponse.trim().isEmpty()) {
                    complaint.setAdminResponse(adminResponse);
                }
                complaintRepository.save(complaint);
                
                return ResponseEntity.ok(ApiResponse.success("Complaint updated successfully"));
            }
            
            return ResponseEntity.notFound().body(ApiResponse.error("Complaint not found"));
            
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Server error: " + e.getMessage()));
        }
    }
    
    private Map<String, Object> convertToFullComplaintMap(Complaint complaint) {
        Map<String, Object> map = new HashMap<>();
        map.put("id", complaint.getId());
        map.put("title", complaint.getTitle());
        map.put("description", complaint.getDescription());
        map.put("status", complaint.getStatus());
        map.put("adminResponse", complaint.getAdminResponse());
        map.put("createdAt", complaint.getCreatedAt().toString());
        map.put("userName", complaint.getUser().getFullName());
        map.put("facilityName", complaint.getFacility().getName());
        return map;
    }
    
    private Map<String, Object> convertToUserComplaintMap(Complaint complaint) {
        Map<String, Object> map = new HashMap<>();
        map.put("id", complaint.getId());
        map.put("title", complaint.getTitle());
        map.put("description", complaint.getDescription());
        map.put("status", complaint.getStatus());
        map.put("adminResponse", complaint.getAdminResponse());
        map.put("createdAt", complaint.getCreatedAt().toString());
        map.put("facilityName", complaint.getFacility().getName());
        return map;
    }
}