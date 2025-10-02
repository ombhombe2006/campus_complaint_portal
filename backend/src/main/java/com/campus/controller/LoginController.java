package com.campus.controller;

import com.campus.dto.ApiResponse;
import com.campus.dto.LoginRequest;
import com.campus.entity.User;
import com.campus.entity.Admin;
import com.campus.repository.UserRepository;
import com.campus.repository.AdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*", allowCredentials = "true")
public class LoginController {
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private AdminRepository adminRepository;
    
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<Map<String, Object>>> login(
            @Valid @RequestBody LoginRequest loginRequest,
            HttpSession session) {
        
        try {
            String username = loginRequest.getUsername();
            String password = loginRequest.getPassword();
            String loginType = loginRequest.getLoginType();
            
            Map<String, Object> responseData = new HashMap<>();
            
            if ("admin".equals(loginType)) {
                // Admin login
                Optional<Admin> adminOpt = adminRepository.findByUsernameAndPassword(username, password);
                if (adminOpt.isPresent()) {
                    Admin admin = adminOpt.get();
                    
                    // Set session attributes
                    session.setAttribute("userId", admin.getId());
                    session.setAttribute("username", admin.getUsername());
                    session.setAttribute("userType", "admin");
                    session.setAttribute("fullName", admin.getFullName());
                    
                    // Prepare response data
                    responseData.put("userId", admin.getId());
                    responseData.put("fullName", admin.getFullName());
                    responseData.put("userType", "admin");
                    
                    return ResponseEntity.ok(ApiResponse.success("Login successful", responseData));
                } else {
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                            .body(ApiResponse.error("Invalid username or password"));
                }
                
            } else if ("user".equals(loginType)) {
                // User login
                Optional<User> userOpt = userRepository.findByUsernameAndPassword(username, password);
                if (userOpt.isPresent()) {
                    User user = userOpt.get();
                    
                    // Set session attributes
                    session.setAttribute("userId", user.getId());
                    session.setAttribute("username", user.getUsername());
                    session.setAttribute("userType", "user");
                    session.setAttribute("fullName", user.getFullName());
                    
                    // Prepare response data
                    responseData.put("userId", user.getId());
                    responseData.put("fullName", user.getFullName());
                    responseData.put("userType", "user");
                    
                    return ResponseEntity.ok(ApiResponse.success("Login successful", responseData));
                } else {
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                            .body(ApiResponse.error("Invalid username or password"));
                }
            } else {
                return ResponseEntity.badRequest()
                        .body(ApiResponse.error("Invalid login type"));
            }
            
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Server error: " + e.getMessage()));
        }
    }
    
    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<Void>> logout(HttpSession session) {
        try {
            session.invalidate();
            return ResponseEntity.ok(ApiResponse.success("Logout successful"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Logout failed"));
        }
    }
    
    @GetMapping("/session")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getSession(HttpSession session) {
        try {
            Map<String, Object> sessionData = new HashMap<>();
            
            if (session.getAttribute("userId") != null) {
                sessionData.put("userId", session.getAttribute("userId"));
                sessionData.put("username", session.getAttribute("username"));
                sessionData.put("userType", session.getAttribute("userType"));
                sessionData.put("fullName", session.getAttribute("fullName"));
                sessionData.put("authenticated", true);
            } else {
                sessionData.put("authenticated", false);
            }
            
            return ResponseEntity.ok(ApiResponse.success("Session info", sessionData));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Failed to get session info"));
        }
    }
}