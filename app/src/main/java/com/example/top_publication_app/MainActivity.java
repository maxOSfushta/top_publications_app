package com.example.top_publication_app;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private RedditPostsAdapter adapter;
    private List<RedditPost> allPosts;
    private String after;
    private boolean isLoading = false;
    private LinearLayoutManager layoutManager;

    private static final String KEY_POSTS = "posts";
    private static final String KEY_AFTER = "after";
    private static final String KEY_IS_LOADING = "isLoading";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        allPosts = new ArrayList<>();
        adapter = new RedditPostsAdapter(allPosts);
        recyclerView.setAdapter(adapter);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                int visibleItemCount = layoutManager.getChildCount();
                int totalItemCount = layoutManager.getItemCount();
                int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();

                if (!isLoading && (visibleItemCount + firstVisibleItemPosition) >= totalItemCount) {
                    loadMorePosts();
                }
            }
        });

        if (savedInstanceState != null) {
            allPosts = savedInstanceState.getParcelableArrayList(KEY_POSTS);
            after = savedInstanceState.getString(KEY_AFTER);
            isLoading = savedInstanceState.getBoolean(KEY_IS_LOADING);
            adapter.setPosts(allPosts);
        } else {
            fetchTopPosts();
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(KEY_POSTS, new ArrayList<>(allPosts));
        outState.putString(KEY_AFTER, after);
        outState.putBoolean(KEY_IS_LOADING, isLoading);
    }

    private void fetchTopPosts() {
        String url = "https://www.reddit.com/top.json";
        new FetchPostsTask().execute(url);
    }

    private void loadMorePosts() {
        isLoading = true;
        String url = "https://www.reddit.com/top.json?after=" + after;
        new FetchPostsTask().execute(url);
    }

    @SuppressLint("StaticFieldLeak")
    private class FetchPostsTask extends AsyncTask<String, Void, List<RedditPost>> {

        @Override
        protected List<RedditPost> doInBackground(String... urls) {
            String url = urls[0];
            List<RedditPost> posts = new ArrayList<>();

            try {
                URL apiUrl = new URL(url);
                HttpURLConnection connection = (HttpURLConnection) apiUrl.openConnection();
                connection.setRequestMethod("GET");

                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder stringBuilder = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    stringBuilder.append(line).append("\n");
                }
                reader.close();

                String response = stringBuilder.toString();

                JSONObject jsonObject = new JSONObject(response);
                JSONObject data = jsonObject.getJSONObject("data");
                JSONArray children = data.getJSONArray("children");

                after = data.getString("after");

                for (int i = 0; i < children.length(); i++) {
                    JSONObject postObject = children.getJSONObject(i).getJSONObject("data");

                    String title = postObject.getString("title");
                    String author = postObject.getString("author");
                    int numComments = postObject.getInt("num_comments");
                    long createdUtc = postObject.getLong("created_utc");
                    String thumbnailUrl = postObject.getString("thumbnail");
                    String imageUrl = postObject.getString("url");
                    String postId = postObject.getString("id");

                    RedditPost post = new RedditPost(title, author, numComments, createdUtc, thumbnailUrl, imageUrl);
                    post.setImageUrl(imageUrl);
                    post.setId(postId);
                    posts.add(post);
                }

            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }

            return posts;
        }

        @SuppressLint("NotifyDataSetChanged")
        @Override
        protected void onPostExecute(List<RedditPost> posts) {
            super.onPostExecute(posts);

            allPosts.addAll(posts);
            adapter.notifyDataSetChanged();
            isLoading = false;
        }
    }
}