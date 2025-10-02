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

public class ComplaintServlet extends HttpServlet {
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        
        String pathInfo = request.getPathInfo();
        
        try {
            if (pathInfo == null || pathInfo.equals("/")) {
                // Get all complaints (admin only)
                getAllComplaints(request, response, out);
            } else if (pathInfo.equals("/user")) {
                // Get complaints by user
                getUserComplaints(request, response, out);
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
            
            // Add new complaint
            addComplaint(requestJson, request, response, out);
            
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
            
            // Update complaint status and response (admin only)
            updateComplaint(requestJson, request, response, out);
            
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.write("{\"success\": false, \"message\": \"Server error: " + e.getMessage() + "\"}");
        } finally {
            out.close();
        }
    }
    
    private void getAllComplaints(HttpServletRequest request, HttpServletResponse response, PrintWriter out) {
        HttpSession session = request.getSession(false);
        if (session == null || !"admin".equals(session.getAttribute("userType"))) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            out.write("{\"success\": false, \"message\": \"Admin access required\"}");
            return;
        }
        
        Connection connection = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            connection = DatabaseConnection.getConnection();
            String sql = "SELECT c.id, c.title, c.description, c.status, c.admin_response, c.created_at, " +
                        "u.full_name as user_name, f.name as facility_name " +
                        "FROM complaints c " +
                        "JOIN users u ON c.user_id = u.id " +
                        "JOIN facilities f ON c.facility_id = f.id " +
                        "ORDER BY c.created_at DESC";
            
            stmt = connection.prepareStatement(sql);
            rs = stmt.executeQuery();
            
            JSONArray complaints = new JSONArray();
            while (rs.next()) {
                JSONObject complaint = new JSONObject();
                complaint.put("id", rs.getInt("id"));
                complaint.put("title", rs.getString("title"));
                complaint.put("description", rs.getString("description"));
                complaint.put("status", rs.getString("status"));
                complaint.put("adminResponse", rs.getString("admin_response"));
                complaint.put("createdAt", rs.getTimestamp("created_at").toString());
                complaint.put("userName", rs.getString("user_name"));
                complaint.put("facilityName", rs.getString("facility_name"));
                complaints.add(complaint);
            }
            
            response.setStatus(HttpServletResponse.SC_OK);
            out.write(complaints.toJSONString());
            
        } catch (SQLException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.write("{\"success\": false, \"message\": \"Database error: " + e.getMessage() + "\"}");
        } finally {
            closeResources(rs, stmt, connection);
        }
    }
    
    private void getUserComplaints(HttpServletRequest request, HttpServletResponse response, PrintWriter out) {
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
            String sql = "SELECT c.id, c.title, c.description, c.status, c.admin_response, c.created_at, " +
                        "f.name as facility_name " +
                        "FROM complaints c " +
                        "JOIN facilities f ON c.facility_id = f.id " +
                        "WHERE c.user_id = ? " +
                        "ORDER BY c.created_at DESC";
            
            stmt = connection.prepareStatement(sql);
            stmt.setInt(1, userId);
            rs = stmt.executeQuery();
            
            JSONArray complaints = new JSONArray();
            while (rs.next()) {
                JSONObject complaint = new JSONObject();
                complaint.put("id", rs.getInt("id"));
                complaint.put("title", rs.getString("title"));
                complaint.put("description", rs.getString("description"));
                complaint.put("status", rs.getString("status"));
                complaint.put("adminResponse", rs.getString("admin_response"));
                complaint.put("createdAt", rs.getTimestamp("created_at").toString());
                complaint.put("facilityName", rs.getString("facility_name"));
                complaints.add(complaint);
            }
            
            response.setStatus(HttpServletResponse.SC_OK);
            out.write(complaints.toJSONString());
            
        } catch (SQLException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.write("{\"success\": false, \"message\": \"Database error: " + e.getMessage() + "\"}");
        } finally {
            closeResources(rs, stmt, connection);
        }
    }
    
    private void addComplaint(JSONObject requestJson, HttpServletRequest request, 
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
            String description = (String) requestJson.get("description");
            
            connection = DatabaseConnection.getConnection();
            String sql = "INSERT INTO complaints (user_id, facility_id, title, description) VALUES (?, ?, ?, ?)";
            
            stmt = connection.prepareStatement(sql);
            stmt.setInt(1, userId);
            stmt.setInt(2, facilityId);
            stmt.setString(3, title);
            stmt.setString(4, description);
            
            int result = stmt.executeUpdate();
            
            if (result > 0) {
                response.setStatus(HttpServletResponse.SC_OK);
                out.write("{\"success\": true, \"message\": \"Complaint submitted successfully\"}");
            } else {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                out.write("{\"success\": false, \"message\": \"Failed to submit complaint\"}");
            }
            
        } catch (SQLException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.write("{\"success\": false, \"message\": \"Database error: " + e.getMessage() + "\"}");
        } finally {
            closeResources(null, stmt, connection);
        }
    }
    
    private void updateComplaint(JSONObject requestJson, HttpServletRequest request, 
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
            int complaintId = Integer.parseInt(requestJson.get("complaintId").toString());
            String status = (String) requestJson.get("status");
            String adminResponse = (String) requestJson.get("adminResponse");
            
            connection = DatabaseConnection.getConnection();
            String sql = "UPDATE complaints SET status = ?, admin_response = ? WHERE id = ?";
            
            stmt = connection.prepareStatement(sql);
            stmt.setString(1, status);
            stmt.setString(2, adminResponse);
            stmt.setInt(3, complaintId);
            
            int result = stmt.executeUpdate();
            
            if (result > 0) {
                response.setStatus(HttpServletResponse.SC_OK);
                out.write("{\"success\": true, \"message\": \"Complaint updated successfully\"}");
            } else {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                out.write("{\"success\": false, \"message\": \"Failed to update complaint\"}");
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