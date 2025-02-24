// OllamaResponse.java
package com.example.yumcycle.models;

public class OllamaResponse {
    private String id;
    private String object;
    private String created;
    private String model;
    private Choice[] choices;
    private String completion;

    public Choice[] getChoices() {
        return choices;
    }

    public String getCompletion() {
        return completion;
    }

    public static class Choice {
        private String text;

        public String getText() {
            return text;
        }
    }
}
