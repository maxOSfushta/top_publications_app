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

import java.util.List;

public class RedditPostViewHolder extends RecyclerView.ViewHolder {

    private final TextView titleTextView;
    private final TextView authorTextView;
    private final TextView commentsTextView;
    ImageView thumbnailImageView;

    public RedditPostViewHolder(@NonNull View itemView, List<RedditPost> posts) {
        super(itemView);
        thumbnailImageView = itemView.findViewById(R.id.thumbnailImageView);
        titleTextView = itemView.findViewById(R.id.titleTextView);
        authorTextView = itemView.findViewById(R.id.authorTextView);
        commentsTextView = itemView.findViewById(R.id.commentsTextView);

        thumbnailImageView.setOnClickListener(v -> {
            int position = getAdapterPosition();
            if (position != RecyclerView.NO_POSITION) {
                RedditPost post = posts.get(position);
                String originalUrl = post.getImageUrl();
                openOriginalUrl(originalUrl);
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

    private void openOriginalUrl(String originalUrl) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(originalUrl));
        itemView.getContext().startActivity(intent);
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