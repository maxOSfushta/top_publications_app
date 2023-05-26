package com.example.top_publication_app;

public class RedditPost {

    private final String title;
    private final String author;
    private final int numComments;
    private final long createdUtc;
    private final String thumbnailUrl;


    public RedditPost(String title, String author, int numComments, long createdUtc, String thumbnailUrl) {
        this.title = title;
        this.author = author;
        this.numComments = numComments;
        this.createdUtc = createdUtc;
        this.thumbnailUrl = thumbnailUrl;
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

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void setImageUrl(String imageUrl) {
    }

    public void setId(String id) {
    }
}