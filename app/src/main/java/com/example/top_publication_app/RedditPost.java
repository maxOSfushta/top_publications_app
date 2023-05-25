package com.example.top_publication_app;

public class RedditPost {
    private String title;
    private String author;
    private int score;
    private int commentCount;
    private long createdUtc;
    private String thumbnail;

    public RedditPost(String title, String author, int score) {
        this.title = title;
        this.author = author;
        this.score = score;
    }

    public RedditPost(String title, String author, int score, int commentCount, long createdUtc, String thumbnail) {
        this.title = title;
        this.author = author;
        this.score = score;
        this.commentCount = commentCount;
        this.createdUtc = createdUtc;
        this.thumbnail = thumbnail;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public int getScore() {
        return score;
    }

    public int getCommentCount() {
        return commentCount;
    }

    public long getCreatedUtc() {
        return createdUtc;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public boolean hasThumbnail() {
        return thumbnail != null && !thumbnail.isEmpty();
    }
}