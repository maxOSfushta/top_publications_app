package com.example.top_publication_app;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

public class RedditPostViewHolder extends RecyclerView.ViewHolder {

    private TextView titleTextView;
    private TextView authorTextView;
    private TextView commentsTextView;
    private TextView timeTextView;
    ImageView thumbnailImageView;

    public RedditPostViewHolder(@NonNull View itemView) {
        super(itemView);

        titleTextView = itemView.findViewById(R.id.titleTextView);
        authorTextView = itemView.findViewById(R.id.authorTextView);
        commentsTextView = itemView.findViewById(R.id.commentsTextView);
        timeTextView = itemView.findViewById(R.id.timeTextView);
        thumbnailImageView = itemView.findViewById(R.id.thumbnailImageView);

        thumbnailImageView.setOnClickListener(v -> {
            RedditPost post = (RedditPost) itemView.getTag();
            if (post != null && post.getThumbnailUrl() != null && !post.getThumbnailUrl().isEmpty()) {
                openImageInBrowser(post.getThumbnailUrl());
            }
        });
    }

    @SuppressLint("SetTextI18n")
    public void bind(RedditPost post) {
        itemView.setTag(post);

        titleTextView.setText(post.getTitle());
        authorTextView.setText("Posted by " + post.getAuthor() + " • " + getFormattedTimeAgo(post.getCreatedUtc()));
        commentsTextView.setText("Comments: " + post.getNumComments());

        if (post.getThumbnailUrl() != null && !post.getThumbnailUrl().isEmpty()) {
            Picasso.get().load(post.getThumbnailUrl()).into(thumbnailImageView);
            thumbnailImageView.setVisibility(View.VISIBLE);
        } else {
            thumbnailImageView.setImageDrawable(null);
            thumbnailImageView.setVisibility(View.GONE);
        }
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

    private void openImageInBrowser(String imageUrl) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.parse(imageUrl), "image/*");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        itemView.getContext().startActivity(intent);
    }
}