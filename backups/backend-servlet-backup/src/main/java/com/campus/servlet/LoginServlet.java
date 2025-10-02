package com.campus.servlet;

import com.campus.util.DatabaseConnection;
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

public class LoginServlet extends HttpServlet {
    
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
            
            String username = (String) requestJson.get("username");
            String password = (String) requestJson.get("password");
            String loginType = (String) requestJson.get("loginType"); // "user" or "admin"
            
            // Validate input
            if (username == null || password == null || loginType == null) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                out.write("{\"success\": false, \"message\": \"Missing required fields\"}");
                return;
            }
            
            // Authenticate user
            JSONObject result = authenticateUser(username, password, loginType);
            
            if ((Boolean) result.get("success")) {
                // Create session
                HttpSession session = request.getSession(true);
                session.setAttribute("userId", result.get("userId"));
                session.setAttribute("username", username);
                session.setAttribute("userType", loginType);
                session.setAttribute("fullName", result.get("fullName"));
                
                response.setStatus(HttpServletResponse.SC_OK);
            } else {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            }
            
            out.write(result.toJSONString());
            
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.write("{\"success\": false, \"message\": \"Server error: " + e.getMessage() + "\"}");
        } finally {
            out.close();
        }
    }
    
    private JSONObject authenticateUser(String username, String password, String loginType) {
        JSONObject result = new JSONObject();
        Connection connection = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            connection = DatabaseConnection.getConnection();
            
            String tableName = loginType.equals("admin") ? "admins" : "users";
            String sql = "SELECT id, full_name FROM " + tableName + " WHERE username = ? AND password = ?";
            
            stmt = connection.prepareStatement(sql);
            stmt.setString(1, username);
            stmt.setString(2, password);
            
            rs = stmt.executeQuery();
            
            if (rs.next()) {
                result.put("success", true);
                result.put("message", "Login successful");
                result.put("userId", rs.getInt("id"));
                result.put("fullName", rs.getString("full_name"));
                result.put("userType", loginType);
            } else {
                result.put("success", false);
                result.put("message", "Invalid username or password");
            }
            
        } catch (SQLException e) {
            result.put("success", false);
            result.put("message", "Database error: " + e.getMessage());
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                System.err.println("Error closing database resources: " + e.getMessage());
            }
        }
        
        return result;
    }
}