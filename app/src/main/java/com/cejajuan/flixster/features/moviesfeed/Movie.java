package com.cejajuan.flixster.features.moviesfeed;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.List;

// This class will extract and encapsulate a movie returned from the movie
// database API's JSON response. Using the parceler library to serialize
// the object.
@Parcel
public class Movie {
    public String posterPath;
    public String title;
    public String overview;
    public String backdropPath;
    public double rating;
    public int movieID;

    // empty constructor needed by the Parceler library
    public Movie() { }

    public Movie(JSONObject jsonObject) throws JSONException {
        posterPath = jsonObject.getString("poster_path");
        title = jsonObject.getString("title");
        overview = jsonObject.getString("overview");
        backdropPath = jsonObject.getString("backdrop_path");
        rating = jsonObject.getDouble("vote_average");
        movieID = jsonObject.getInt("id");
    }

    // Extract the movies from the "results" JSONArray which is returned by the
    // Movie database API
    public static List<Movie> fromJsonArray(JSONArray movieJsonArray) throws JSONException {
        List<Movie> movies = new ArrayList<>(movieJsonArray.length());

        for (int i = 0; i < movieJsonArray.length();  i++)
            movies.add(new Movie(movieJsonArray.getJSONObject(i)));

        return movies;
    }

    // returned the full poster path the member variable contains a relative path
    public String getPosterUrl() {
        return String.format("https://image.tmdb.org/t/p/w342%s", posterPath);
    }

    public String getTitle() {
        return title;
    }

    public String getOverview() {
        return overview;
    }

    public String getBackdropPath() {
        return String.format("https://image.tmdb.org/t/p/w300%s", backdropPath);
    }

    public double getRating() {
        return rating;
    }

    public int getMovieID() { return movieID; }
}
