package com.campus.controller;

import com.campus.dto.ApiResponse;
import com.campus.entity.Facility;
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
@RequestMapping("/api/facilities")
@CrossOrigin(origins = "*", allowCredentials = "true")
public class FacilityController {
    
    @Autowired
    private FacilityRepository facilityRepository;
    
    @GetMapping
    public ResponseEntity<List<Map<String, Object>>> getAllFacilities() {
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
    public ResponseEntity<ApiResponse<Void>> addFacility(@RequestBody Map<String, Object> facilityData, HttpSession session) {
        if (!"admin".equals(session.getAttribute("userType"))) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.error("Admin access required"));
        }
        
        try {
            String name = (String) facilityData.get("name");
            String category = (String) facilityData.get("category");
            
            if (name == null || name.trim().isEmpty() || category == null || category.trim().isEmpty()) {
                return ResponseEntity.badRequest().body(ApiResponse.error("Name and category are required"));
            }
            
            // Check if facility already exists
            Optional<Facility> existingFacility = facilityRepository.findByName(name.trim());
            if (existingFacility.isPresent()) {
                return ResponseEntity.badRequest().body(ApiResponse.error("Facility with this name already exists"));
            }
            
            Facility facility = new Facility(name.trim(), category.trim());
            facilityRepository.save(facility);
            
            return ResponseEntity.ok(ApiResponse.success("Facility added successfully"));
            
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Server error: " + e.getMessage()));
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteFacility(@PathVariable Long id, HttpSession session) {
        if (!"admin".equals(session.getAttribute("userType"))) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.error("Admin access required"));
        }
        
        try {
            Optional<Facility> facilityOpt = facilityRepository.findById(id);
            
            if (facilityOpt.isPresent()) {
                facilityRepository.delete(facilityOpt.get());
                return ResponseEntity.ok(ApiResponse.success("Facility deleted successfully"));
            }
            
            return ResponseEntity.notFound().body(ApiResponse.error("Facility not found"));
            
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Server error: " + e.getMessage()));
        }
    }
}