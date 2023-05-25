package com.example.top_publication_app;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

public class RedditApiHelper {
    private final Context context;
    private final RequestQueue requestQueue;

    public RedditApiHelper(Context context) {
        this.context = context;
        requestQueue = Volley.newRequestQueue(context);
    }

    public void fetchTopPosts(Response.Listener<JSONObject> listener, Response.ErrorListener errorListener) {
        String url = RedditApiConstants.BASE_URL + RedditApiConstants.TOP_POSTS_ENDPOINT;
        url += "?limit" + RedditApiConstants.POSTS_LIMIT;

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, listener, errorListener);
        requestQueue.add(request);
    }
}
