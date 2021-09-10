package com.cejajuan.flixster.features.moviesfeed;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.cejajuan.flixster.BuildConfig;
import com.cejajuan.flixster.R;
import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.RequestParams;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Headers;

public class NowPlayingActivity extends AppCompatActivity {
    private final String TAG = "NowPlayingActivity";
    private final String BASE_PATH = "https://api.themoviedb.org/3";
    private final String NOW_PLAYING_END_POINT = "/movie/now_playing";
    private final String API_KEY = BuildConfig.MVDB_KEY;
    private final String NOW_PLAYING_URL = BASE_PATH + NOW_PLAYING_END_POINT;
    private List<Movie> movies;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_now_playing);

        // create a movies arraylist here since the client.get() is an async call
        movies = new ArrayList<>();

        // Bind the adaptor to the recycler view

        // Lookup the recyclerview in activity layout
        RecyclerView rvMoviesFeed = (RecyclerView) findViewById(R.id.rvMoviesFeed);
        // Create adapter passing in the sample user data
        MovieAdaptor adapter = new MovieAdaptor(movies);
        // Attach the adapter to the recyclerview to populate items
        rvMoviesFeed.setAdapter(adapter);
        // Set layout manager to position the items
        rvMoviesFeed.setLayoutManager(new LinearLayoutManager(this));

        // create an async http object and add api_key query string param to url
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("api_key", API_KEY);

        // make the api request to the Movie database API for now playing movies
        client.get(NOW_PLAYING_URL , params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Headers headers, JSON json) {
                try {
                    // get the list of movies returned by the API
                    movies.addAll(Movie.fromJsonArray(json.jsonObject.getJSONArray("results")));

                    // let the adaptor know that the data set changed
                    adapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    Log.e(TAG, "Error getting results array from API. \n" + e);
                }
            }

            @Override
            public void onFailure(int statusCode, Headers headers, String response
                    , Throwable throwable) {
                Log.d(TAG, "Error getting results array from API.");
            }
        });
    }
}