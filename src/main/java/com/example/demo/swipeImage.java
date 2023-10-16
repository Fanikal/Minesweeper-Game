package com.example.demo;


public class swipeImage {

    private String difficulty;
    private String imageUrl;
    private String tooltipMessage;

    public swipeImage(String difficulty, String imageUrl, String tooltipMsg) {
        this.difficulty = difficulty;
        this.imageUrl = imageUrl;
        this.tooltipMessage = tooltipMsg;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getTooltipMessage() {
        return tooltipMessage;
    }
}
