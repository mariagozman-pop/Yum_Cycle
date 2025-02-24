// OllamaRequest.java
package com.example.yumcycle.models;

public class OllamaRequest {
    private String model;
    private String prompt;

    public OllamaRequest(String model, String prompt) {
        this.model = model;
        this.prompt = prompt;
    }

    public String getModel() {
        return model;
    }

    public String getPrompt() {
        return prompt;
    }
}
