package com.example.top_publication_app;

import android.content.res.Configuration;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class RedditPostsAdapter extends RecyclerView.Adapter<RedditPostViewHolder> {

    private List<RedditPost> posts = new ArrayList<>();

    public void setPosts(List<RedditPost> posts) {
        this.posts = posts;
        notifyDataSetChanged();
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
        return new RedditPostViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RedditPostViewHolder holder, int position) {
        RedditPost post = posts.get(position);
        holder.bind(post);
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }
}