// ApiResponse.java
package com.example.yumcycle.models;

public class ApiResponse {
    private String message;

    // Default Constructor
    public ApiResponse() {}

    // Parameterized Constructor
    public ApiResponse(String message) {
        this.message = message;
    }

    // Getter
    public String getMessage() {
        return message;
    }

    // Setter
    public void setMessage(String message) {
        this.message = message;
    }
}
