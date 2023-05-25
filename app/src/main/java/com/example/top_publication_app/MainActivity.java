package com.example.top_publication_app;

import android.os.AsyncTask;
import android.os.Bundle;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new RedditPostsAdapter();
        recyclerView.setAdapter(adapter);

        fetchTopPosts();
    }

    private void fetchTopPosts() {
        String url = "https://www.reddit.com/top.json?limit=100";
        new FetchPostsTask().execute(url);
    }

    private class FetchPostsTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {
            String url = urls[0];
            String response = null;

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

                response = stringBuilder.toString();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return response;
        }

        @Override
        protected void onPostExecute(String response) {
            super.onPostExecute(response);

            try {
                JSONObject jsonObject = new JSONObject(response);
                JSONObject data = jsonObject.getJSONObject("data");
                JSONArray children = data.getJSONArray("children");

                List<RedditPost> posts = new ArrayList<>();
                for (int i = 0; i < children.length(); i++) {
                    JSONObject postObject = children.getJSONObject(i).getJSONObject("data");

                    String title = postObject.getString("title");
                    String author = postObject.getString("author");
                    int numComments = postObject.getInt("num_comments");
                    long createdUtc = postObject.getLong("created_utc");
                    String thumbnailUrl = postObject.getString("thumbnail");
                    String imageUrl = postObject.getString("url");

                    RedditPost post = new RedditPost(title, author, numComments, createdUtc, thumbnailUrl);
                    post.setImageUrl(imageUrl);
                    posts.add(post);
                }

                adapter.setPosts(posts);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}