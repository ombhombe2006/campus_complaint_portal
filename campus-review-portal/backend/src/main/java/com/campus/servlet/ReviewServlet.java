package com.campus.servlet;

import com.campus.util.DatabaseConnection;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ReviewServlet extends HttpServlet {
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        
        String pathInfo = request.getPathInfo();
        
        try {
            if (pathInfo == null || pathInfo.equals("/")) {
                // Get all reviews
                getAllReviews(request, response, out);
            } else if (pathInfo.equals("/recent")) {
                // Get recent reviews for homepage
                getRecentReviews(request, response, out);
            } else if (pathInfo.equals("/user")) {
                // Get reviews by user
                getUserReviews(request, response, out);
            } else if (pathInfo.equals("/facilities")) {
                // Get all facilities
                getFacilities(request, response, out);
            } else {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                out.write("{\"success\": false, \"message\": \"Endpoint not found\"}");
            }
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.write("{\"success\": false, \"message\": \"Server error: " + e.getMessage() + "\"}");
        } finally {
            out.close();
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        
        try {
            // Read JSON request body
            BufferedReader reader = request.getReader();
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
            
            // Parse JSON
            JSONParser parser = new JSONParser();
            JSONObject requestJson = (JSONObject) parser.parse(sb.toString());
            
            // Add new review
            addReview(requestJson, request, response, out);
            
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.write("{\"success\": false, \"message\": \"Server error: " + e.getMessage() + "\"}");
        } finally {
            out.close();
        }
    }
    
    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        
        try {
            // Read JSON request body
            BufferedReader reader = request.getReader();
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
            
            // Parse JSON
            JSONParser parser = new JSONParser();
            JSONObject requestJson = (JSONObject) parser.parse(sb.toString());
            
            // Update review status (admin only)
            updateReviewStatus(requestJson, request, response, out);
            
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.write("{\"success\": false, \"message\": \"Server error: " + e.getMessage() + "\"}");
        } finally {
            out.close();
        }
    }
    
    private void getAllReviews(HttpServletRequest request, HttpServletResponse response, PrintWriter out) {
        Connection connection = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            connection = DatabaseConnection.getConnection();
            String sql = "SELECT r.id, r.title, r.content, r.rating, r.status, r.created_at, " +
                        "u.full_name as user_name, f.name as facility_name " +
                        "FROM reviews r " +
                        "JOIN users u ON r.user_id = u.id " +
                        "JOIN facilities f ON r.facility_id = f.id " +
                        "ORDER BY r.created_at DESC";
            
            stmt = connection.prepareStatement(sql);
            rs = stmt.executeQuery();
            
            JSONArray reviews = new JSONArray();
            while (rs.next()) {
                JSONObject review = new JSONObject();
                review.put("id", rs.getInt("id"));
                review.put("title", rs.getString("title"));
                review.put("content", rs.getString("content"));
                review.put("rating", rs.getInt("rating"));
                review.put("status", rs.getString("status"));
                review.put("createdAt", rs.getTimestamp("created_at").toString());
                review.put("userName", rs.getString("user_name"));
                review.put("facilityName", rs.getString("facility_name"));
                reviews.add(review);
            }
            
            response.setStatus(HttpServletResponse.SC_OK);
            out.write(reviews.toJSONString());
            
        } catch (SQLException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.write("{\"success\": false, \"message\": \"Database error: " + e.getMessage() + "\"}");
        } finally {
            closeResources(rs, stmt, connection);
        }
    }
    
    private void getRecentReviews(HttpServletRequest request, HttpServletResponse response, PrintWriter out) {
        Connection connection = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            connection = DatabaseConnection.getConnection();
            String sql = "SELECT r.title, r.content, r.rating, f.name as facility_name " +
                        "FROM reviews r " +
                        "JOIN facilities f ON r.facility_id = f.id " +
                        "WHERE r.status = 'APPROVED' " +
                        "ORDER BY r.created_at DESC LIMIT 6";
            
            stmt = connection.prepareStatement(sql);
            rs = stmt.executeQuery();
            
            JSONArray reviews = new JSONArray();
            while (rs.next()) {
                JSONObject review = new JSONObject();
                review.put("title", rs.getString("title"));
                review.put("content", rs.getString("content"));
                review.put("rating", rs.getInt("rating"));
                review.put("facilityName", rs.getString("facility_name"));
                reviews.add(review);
            }
            
            response.setStatus(HttpServletResponse.SC_OK);
            out.write(reviews.toJSONString());
            
        } catch (SQLException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.write("{\"success\": false, \"message\": \"Database error: " + e.getMessage() + "\"}");
        } finally {
            closeResources(rs, stmt, connection);
        }
    }
    
    private void getUserReviews(HttpServletRequest request, HttpServletResponse response, PrintWriter out) {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("userId") == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            out.write("{\"success\": false, \"message\": \"Not authenticated\"}");
            return;
        }
        
        int userId = (Integer) session.getAttribute("userId");
        Connection connection = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            connection = DatabaseConnection.getConnection();
            String sql = "SELECT r.id, r.title, r.content, r.rating, r.status, r.created_at, " +
                        "f.name as facility_name " +
                        "FROM reviews r " +
                        "JOIN facilities f ON r.facility_id = f.id " +
                        "WHERE r.user_id = ? " +
                        "ORDER BY r.created_at DESC";
            
            stmt = connection.prepareStatement(sql);
            stmt.setInt(1, userId);
            rs = stmt.executeQuery();
            
            JSONArray reviews = new JSONArray();
            while (rs.next()) {
                JSONObject review = new JSONObject();
                review.put("id", rs.getInt("id"));
                review.put("title", rs.getString("title"));
                review.put("content", rs.getString("content"));
                review.put("rating", rs.getInt("rating"));
                review.put("status", rs.getString("status"));
                review.put("createdAt", rs.getTimestamp("created_at").toString());
                review.put("facilityName", rs.getString("facility_name"));
                reviews.add(review);
            }
            
            response.setStatus(HttpServletResponse.SC_OK);
            out.write(reviews.toJSONString());
            
        } catch (SQLException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.write("{\"success\": false, \"message\": \"Database error: " + e.getMessage() + "\"}");
        } finally {
            closeResources(rs, stmt, connection);
        }
    }
    
    private void getFacilities(HttpServletRequest request, HttpServletResponse response, PrintWriter out) {
        Connection connection = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            connection = DatabaseConnection.getConnection();
            String sql = "SELECT id, name, category FROM facilities ORDER BY name";
            
            stmt = connection.prepareStatement(sql);
            rs = stmt.executeQuery();
            
            JSONArray facilities = new JSONArray();
            while (rs.next()) {
                JSONObject facility = new JSONObject();
                facility.put("id", rs.getInt("id"));
                facility.put("name", rs.getString("name"));
                facility.put("category", rs.getString("category"));
                facilities.add(facility);
            }
            
            response.setStatus(HttpServletResponse.SC_OK);
            out.write(facilities.toJSONString());
            
        } catch (SQLException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.write("{\"success\": false, \"message\": \"Database error: " + e.getMessage() + "\"}");
        } finally {
            closeResources(rs, stmt, connection);
        }
    }
    
    private void addReview(JSONObject requestJson, HttpServletRequest request, 
                          HttpServletResponse response, PrintWriter out) {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("userId") == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            out.write("{\"success\": false, \"message\": \"Not authenticated\"}");
            return;
        }
        
        int userId = (Integer) session.getAttribute("userId");
        Connection connection = null;
        PreparedStatement stmt = null;
        
        try {
            int facilityId = Integer.parseInt(requestJson.get("facilityId").toString());
            String title = (String) requestJson.get("title");
            String content = (String) requestJson.get("content");
            int rating = Integer.parseInt(requestJson.get("rating").toString());
            
            connection = DatabaseConnection.getConnection();
            String sql = "INSERT INTO reviews (user_id, facility_id, title, content, rating) VALUES (?, ?, ?, ?, ?)";
            
            stmt = connection.prepareStatement(sql);
            stmt.setInt(1, userId);
            stmt.setInt(2, facilityId);
            stmt.setString(3, title);
            stmt.setString(4, content);
            stmt.setInt(5, rating);
            
            int result = stmt.executeUpdate();
            
            if (result > 0) {
                response.setStatus(HttpServletResponse.SC_OK);
                out.write("{\"success\": true, \"message\": \"Review submitted successfully\"}");
            } else {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                out.write("{\"success\": false, \"message\": \"Failed to submit review\"}");
            }
            
        } catch (SQLException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.write("{\"success\": false, \"message\": \"Database error: " + e.getMessage() + "\"}");
        } finally {
            closeResources(null, stmt, connection);
        }
    }
    
    private void updateReviewStatus(JSONObject requestJson, HttpServletRequest request, 
                                   HttpServletResponse response, PrintWriter out) {
        HttpSession session = request.getSession(false);
        if (session == null || !"admin".equals(session.getAttribute("userType"))) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            out.write("{\"success\": false, \"message\": \"Admin access required\"}");
            return;
        }
        
        Connection connection = null;
        PreparedStatement stmt = null;
        
        try {
            int reviewId = Integer.parseInt(requestJson.get("reviewId").toString());
            String status = (String) requestJson.get("status");
            
            connection = DatabaseConnection.getConnection();
            String sql = "UPDATE reviews SET status = ? WHERE id = ?";
            
            stmt = connection.prepareStatement(sql);
            stmt.setString(1, status);
            stmt.setInt(2, reviewId);
            
            int result = stmt.executeUpdate();
            
            if (result > 0) {
                response.setStatus(HttpServletResponse.SC_OK);
                out.write("{\"success\": true, \"message\": \"Review status updated\"}");
            } else {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                out.write("{\"success\": false, \"message\": \"Failed to update review\"}");
            }
            
        } catch (SQLException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.write("{\"success\": false, \"message\": \"Database error: " + e.getMessage() + "\"}");
        } finally {
            closeResources(null, stmt, connection);
        }
    }
    
    private void closeResources(ResultSet rs, PreparedStatement stmt, Connection connection) {
        try {
            if (rs != null) rs.close();
            if (stmt != null) stmt.close();
            if (connection != null) connection.close();
        } catch (SQLException e) {
            System.err.println("Error closing database resources: " + e.getMessage());
        }
    }
}