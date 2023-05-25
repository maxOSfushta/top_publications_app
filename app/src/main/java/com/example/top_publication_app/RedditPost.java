package com.example.top_publication_app;

public class RedditPost {
    private String title;
    private String author;
    private int numComments;
    private long createdUtc;

    public RedditPost(String title, String author, int numComments, long createdUtc) {
        this.title = title;
        this.author = author;
        this.numComments = numComments;
        this.createdUtc = createdUtc;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public int getNumComments() {
        return numComments;
    }

    public long getCreatedUtc() {
        return createdUtc;
    }
}