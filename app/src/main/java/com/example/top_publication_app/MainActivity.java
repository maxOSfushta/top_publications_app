package com.example.top_publication_app;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RedditPostsAdapter adapter;
    private RedditApiHelper redditApiHelper;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new RedditPostsAdapter();
        recyclerView.setAdapter(adapter);

        redditApiHelper = new RedditApiHelper(this);
        fetchTopPosts();
    }

    private void fetchTopPosts() {
        redditApiHelper.fetchTopPosts(new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                // Обработка ответа от сервера
                List<RedditPost> redditPosts = parseResponse(response);
                adapter.setPosts(redditPosts);
            }
        }, error -> {
            // Обработка ошибки запроса
            Toast.makeText(MainActivity.this, "Ошибка при получении данных", Toast.LENGTH_SHORT).show();
        });
    }

    private List<RedditPost> parseResponse(JSONObject response) {
        List<RedditPost> redditPosts = new ArrayList<>();

        try {
            JSONObject data = response.getJSONObject("data");
            JSONArray children = data.getJSONArray("children");

            for (int i = 0; i < children.length(); i++) {
                JSONObject postObject = children.getJSONObject(i).getJSONObject("data");
                String title = postObject.getString("title");
                String author = postObject.getString("author");
                int score = postObject.getInt("score");

                RedditPost redditPost = new RedditPost(title, author, score);
                redditPosts.add(redditPost);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return redditPosts;
    }
}
