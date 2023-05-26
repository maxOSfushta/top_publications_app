package com.example.top_publication_app;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class RedditPost implements Parcelable {

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

    protected RedditPost(Parcel in) {
        title = in.readString();
        author = in.readString();
        numComments = in.readInt();
        createdUtc = in.readLong();
        thumbnailUrl = in.readString();
    }

    public static final Creator<RedditPost> CREATOR = new Creator<RedditPost>() {
        @Override
        public RedditPost createFromParcel(Parcel in) {
            return new RedditPost(in);
        }

        @Override
        public RedditPost[] newArray(int size) {
            return new RedditPost[size];
        }
    };

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(author);
        dest.writeInt(numComments);
        dest.writeLong(createdUtc);
        dest.writeString(thumbnailUrl);
    }
}