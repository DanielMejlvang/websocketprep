package com.eikholm.websocketprep.model;

public class Greeting {

    private String content;
    private String livesLeft;

    public Greeting() {
    }

    public Greeting(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getLivesLeft() {
        return livesLeft;
    }

    public void setLivesLeft(String livesLeft) {
        this.livesLeft = livesLeft;
    }
}