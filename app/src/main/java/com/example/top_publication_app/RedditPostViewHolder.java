package com.example.top_publication_app;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class RedditPostViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    private Context context;
    private List<RedditPost> posts;

    public RedditPostViewHolder(@NonNull View itemView, Context context, List<RedditPost> posts) {
        super(itemView);
        this.context = context;
        this.posts = posts;
        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int position = getAdapterPosition();
        if (position != RecyclerView.NO_POSITION) {
            RedditPost post = posts.get(position);
            openDetailedInformation(post);
        }
    }

    private void openDetailedInformation(RedditPost post) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra("post", (CharSequence) post);
        context.startActivity(intent);
    }
}
