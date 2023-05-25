package com.example.top_publication_app;

import android.annotation.SuppressLint;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class RedditPostViewHolder extends RecyclerView.ViewHolder {

    private final TextView titleTextView;
    private final TextView authorTextView;
    private final TextView commentsTextView;
    ImageView thumbnailImageView;

    public RedditPostViewHolder(@NonNull View itemView) {
        super(itemView);

        titleTextView = itemView.findViewById(R.id.titleTextView);
        authorTextView = itemView.findViewById(R.id.authorTextView);
        commentsTextView = itemView.findViewById(R.id.commentsTextView);
        TextView timeTextView = itemView.findViewById(R.id.timeTextView);
        thumbnailImageView = itemView.findViewById(R.id.thumbnailImageView);
    }

    @SuppressLint("SetTextI18n")
    public void bind(RedditPost post) {
        titleTextView.setText(post.getTitle());
        authorTextView.setText("Posted by " + post.getAuthor() + " • " + getFormattedTimeAgo(post.getCreatedUtc()));
        commentsTextView.setText("Comments: " + post.getNumComments());
    }

    private String getFormattedTimeAgo(long createdUtc) {
        long currentTime = System.currentTimeMillis() / 1000L;
        long timeDifference = currentTime - createdUtc;

        if (timeDifference < 60) {
            return timeDifference + " seconds ago";
        } else if (timeDifference < 3600) {
            long minutes = timeDifference / 60;
            return minutes + " minutes ago";
        } else if (timeDifference < 86400) {
            long hours = timeDifference / 3600;
            return hours + " hours ago";
        } else {
            long days = timeDifference / 86400;
            return days + " days ago";
        }
    }
}