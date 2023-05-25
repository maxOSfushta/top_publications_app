package com.example.top_publication_app;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class RedditPostsAdapter extends RecyclerView.Adapter<RedditPostsAdapter.RedditPostViewHolder> {
    private List<RedditPost> posts;
    private Context context;

    public RedditPostsAdapter() {
    }

    public RedditPostsAdapter(Context context) {
        this.context = context;
        this.posts = new ArrayList<>();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setPosts(List<RedditPost> posts) {
        if (posts == null) {
            this.posts = new ArrayList<>();
        } else {
            this.posts = posts;
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RedditPostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_post, parent, false);
        return new RedditPostViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RedditPostViewHolder holder, int position) {
        RedditPost post = posts.get(position);
        holder.bind(post);
    }

    @Override
    public int getItemCount() {
        if (posts == null) {
            return 0;
        }
        return posts.size();
    }

    public class RedditPostViewHolder extends RecyclerView.ViewHolder {
        private final TextView titleTextView;
        private final TextView authorTextView;
        private final TextView scoreTextView;
        private final TextView commentsTextView;
        private final ImageView thumbnailImageView;

        public RedditPostViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.titleTextView);
            authorTextView = itemView.findViewById(R.id.authorTextView);
            scoreTextView = itemView.findViewById(R.id.scoreTextView);
            commentsTextView = itemView.findViewById(R.id.commentsTextView);
            thumbnailImageView = itemView.findViewById(R.id.thumbnailImageView);
        }

        public void bind(RedditPost post) {
            titleTextView.setText(post.getTitle());
            authorTextView.setText(post.getAuthor());
            scoreTextView.setText(String.valueOf(post.getScore()));
            commentsTextView.setText(String.valueOf(post.getCommentCount()));

            CharSequence timeAgo = DateUtils.getRelativeTimeSpanString(
                    post.getCreatedUtc() * 1000,
                    System.currentTimeMillis(),
                    DateUtils.MINUTE_IN_MILLIS
            );
            authorTextView.append(" • " + timeAgo);

            if (post.hasThumbnail()) {
                thumbnailImageView.setVisibility(View.VISIBLE);
                Glide.with(context)
                        .load(post.getThumbnail())
                        .centerCrop()
                        .into(thumbnailImageView);
                thumbnailImageView.setOnClickListener(v -> openImage(post.getThumbnail()));
            } else {
                thumbnailImageView.setVisibility(View.GONE);
            }
        }

        private void openImage(String imageUrl) {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(imageUrl));
            context.startActivity(intent);
        }
    }
}