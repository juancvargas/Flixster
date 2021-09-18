package com.cejajuan.flixster.features.moviedetails;



import android.os.Bundle;
import android.util.Log;
import android.widget.RatingBar;
import android.widget.TextView;

import com.cejajuan.flixster.BuildConfig;
import com.cejajuan.flixster.R;
import com.cejajuan.flixster.features.moviesfeed.Movie;
import com.cejajuan.flixster.features.moviesfeed.MovieAdaptor;
import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.RequestParams;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.parceler.Parcels;

import okhttp3.Headers;

public class MovieDetailsActivity extends YouTubeBaseActivity {
    private final String VIDEOS_BASE_PATH = "https://api.themoviedb.org/3/";
    private final String VIDEOS_EP_PREFIX = "movie/";
    private final String VIDEOS_EP_SUFFIX = "/videos";
    private final String MVDB_API_KEY = BuildConfig.MVDB_KEY;
    private final String LANGUAGE_CODE = "en-US";
    private static final String YT_API_KEY = BuildConfig.YOUTUBE_KEY;
    final String TAG = "MovieDetailsActivity";
    final float GOOD_MOVIE_RATING = 7.0f;
    TextView tvTitle;
    TextView tvOverview;
    RatingBar ratingBar;
    YouTubePlayerView videoPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        tvTitle = findViewById(R.id.tvTitle);
        tvOverview = findViewById(R.id.tvOverview);
        ratingBar = findViewById(R.id.ratingBar);
        videoPlayer = findViewById(R.id.videoPlayer);

        Movie movie  = Parcels.unwrap(getIntent().getParcelableExtra(MovieAdaptor.MOVIE_KEY));
        tvTitle.setText(movie.getTitle());
        tvOverview.setText(movie.getOverview());
        ratingBar.setRating((float)movie.getRating());

        // call the MovieDatabase video endpoint to get the youtube video key of the movie trailer
        String videosUrl = VIDEOS_BASE_PATH + VIDEOS_EP_PREFIX + movie.getMovieID() + VIDEOS_EP_SUFFIX;

        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("api_key", MVDB_API_KEY);
        params.put("language", LANGUAGE_CODE);

        client.get(videosUrl, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Headers headers, JSON json) {
                try {
                    // look for the movie trailer in the returned array
                    JSONArray results = json.jsonObject.getJSONArray("results");
                    if (results.length() == 0) {
//                        initYoutubePlayer(null, movie);
                        return;
                    }
                    String youtubeKey = results.getJSONObject(0).getString("key");
                    initYoutubePlayer(youtubeKey, movie);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                Log.e(TAG, "Error getting trailer youtube key.");
            }
        });
    }

    public void initYoutubePlayer(String youtubeKey, Movie movie) {
        videoPlayer.initialize(YT_API_KEY, new YouTubePlayer.OnInitializedListener() {
            /*Note: if you wish to only load the video but not play, use cueVideo() instead of
             * loadVideo(). Playing videos involves passing along the YouTube video key (do not
             * include the full URL)
             */
            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider
                    , YouTubePlayer youTubePlayer, boolean b) {

                if (movie.getRating() >= GOOD_MOVIE_RATING) {
                    youTubePlayer.loadVideo(youtubeKey);
                    return;
                }
                youTubePlayer.cueVideo(youtubeKey);
            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider
                    , YouTubeInitializationResult youTubeInitializationResult) {
                Log.e(TAG, "Youtube video loading error.");
                Log.e(TAG, youTubeInitializationResult.toString());
            }
        });
    }
}