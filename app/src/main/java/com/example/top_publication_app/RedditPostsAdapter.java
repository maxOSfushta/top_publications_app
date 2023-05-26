package com.example.top_publication_app;

import android.annotation.SuppressLint;
import android.content.res.Configuration;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class RedditPostsAdapter extends RecyclerView.Adapter<RedditPostViewHolder> {

    private List<RedditPost> posts;

    public RedditPostsAdapter(List<RedditPost> posts) {
        this.posts = posts;
    }

    @NonNull
    @Override
    public RedditPostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        int orientation = parent.getContext().getResources().getConfiguration().orientation;
        int layoutRes;
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            layoutRes = R.layout.item_post_land;
        } else {
            layoutRes = R.layout.item_post_port;
        }
        View view = LayoutInflater.from(parent.getContext()).inflate(layoutRes, parent, false);
        return new RedditPostViewHolder(view, posts);
    }

    @Override
    public void onBindViewHolder(@NonNull RedditPostViewHolder holder, int position) {
        RedditPost post = posts.get(position);
        if (post.getThumbnailUrl() != null && !post.getThumbnailUrl().isEmpty()) {
            String thumbnailUrl = post.getThumbnailUrl();
            if (thumbnailUrl.endsWith(".jpg") || thumbnailUrl.endsWith(".jpeg") || thumbnailUrl.endsWith(".png")) {
                Picasso.get().load(thumbnailUrl).into(holder.thumbnailImageView);
                holder.thumbnailImageView.setVisibility(View.VISIBLE);
            } else {
                holder.thumbnailImageView.setImageDrawable(null);
                holder.thumbnailImageView.setVisibility(View.GONE);
            }
        } else {
            holder.thumbnailImageView.setImageDrawable(null);
            holder.thumbnailImageView.setVisibility(View.GONE);
        }
        holder.bind(post);
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setPosts(List<RedditPost> posts) {
        this.posts = posts;
        notifyDataSetChanged();
    }
}