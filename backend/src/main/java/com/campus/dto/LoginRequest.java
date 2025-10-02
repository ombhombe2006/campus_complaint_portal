package com.campus.dto;

import javax.validation.constraints.NotBlank;

public class LoginRequest {
    
    @NotBlank(message = "Username is required")
    private String username;
    
    @NotBlank(message = "Password is required")
    private String password;
    
    @NotBlank(message = "Login type is required")
    private String loginType;
    
    public LoginRequest() {}
    
    public LoginRequest(String username, String password, String loginType) {
        this.username = username;
        this.password = password;
        this.loginType = loginType;
    }
    
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    
    public String getLoginType() { return loginType; }
    public void setLoginType(String loginType) { this.loginType = loginType; }
}