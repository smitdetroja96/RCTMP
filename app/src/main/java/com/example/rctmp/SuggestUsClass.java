package com.example.rctmp;

public class SuggestUsClass {

    String std_id,suggestions;

    public SuggestUsClass() {
    }

    public SuggestUsClass(String std_id, String suggestions) {
        this.std_id = std_id;
        this.suggestions = suggestions;
    }

    public String getStd_id() {
        return std_id;
    }

    public void setStd_id(String std_id) {
        this.std_id = std_id;
    }

    public String getSuggestions() {
        return suggestions;
    }

    public void setSuggestions(String suggestions) {
        this.suggestions = suggestions;
    }
}
