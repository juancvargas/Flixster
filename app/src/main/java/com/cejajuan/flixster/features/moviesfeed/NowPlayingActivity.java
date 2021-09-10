package com.cejajuan.flixster.features.moviesfeed;

import androidx.appcompat.app.AppCompatActivity;

import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;

import com.cejajuan.flixster.BuildConfig;
import com.cejajuan.flixster.R;
import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.RequestParams;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import okhttp3.Headers;

public class NowPlayingActivity extends AppCompatActivity {
    private String TAG = "NowPlayingActivity";
    private final String BASE_PATH = "https://api.themoviedb.org/3";
    private final String NOW_PLAYING_END_POINT = "/movie/now_playing";
    private final String API_KEY = BuildConfig.MVDB_KEY;
    private final String NOW_PLAYING_URL = BASE_PATH + NOW_PLAYING_END_POINT;
    private List<Movie> movies;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_now_playing);

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
                    movies = Movie.fromJsonArray(json.jsonObject.getJSONArray("results"));
                } catch (JSONException e) {
                    Log.e(TAG, "Error getting results array from API. \n" + e);
                }
            }

            @Override
            public void onFailure(int statusCode, Headers headers, String response
                    , Throwable throwable) {
                Log.d(TAG, "On Failure:");
            }
        });
    }
}