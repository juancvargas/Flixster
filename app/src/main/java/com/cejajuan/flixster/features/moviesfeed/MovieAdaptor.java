package com.cejajuan.flixster.features.moviesfeed;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.MultiTransformation;
import com.bumptech.glide.load.resource.bitmap.FitCenter;
import com.bumptech.glide.request.Request;
import com.cejajuan.flixster.R;

import java.util.List;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

public class MovieAdaptor extends RecyclerView.Adapter<MovieAdaptor.ViewHolder> {
    private List<Movie> movies;

    public MovieAdaptor(List<Movie> movies) {
        this.movies = movies;
    }

    // inflate a new layout from XML and return ViewHolder
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View movieView = inflater.inflate(R.layout.movie_item, parent, false);
        return new ViewHolder(movieView);
    }

    // populate the data in the item using the ViewHolder
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(holder.itemView.getContext(), movies.get(position));
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView imgPoster;
        TextView tvTitle;
        TextView tvOverview;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgPoster = itemView.findViewById(R.id.imgPoster);
            tvTitle = itemView.findViewById(R.id.txtTitle);
            tvOverview= itemView.findViewById(R.id.txtOverview);
        }

        // adaptor calls this method to bind data to the ViewHolder
        public void bind(Context context, Movie movie) {
            int radius = 30; // corner radius, higher value = more rounded
            int margin = 0; // crop margin, set to 0 for corners with no crop

            tvTitle.setText(movie.getTitle());
            tvOverview.setText(movie.getOverview());

            // determine the image to use based on the orientation
            String imageUrl;
            int orientation = context.getResources().getConfiguration().orientation;
            if ( orientation == Configuration.ORIENTATION_PORTRAIT)
                imageUrl = movie.getPosterUrl();
            else
                imageUrl = movie.getBackdropPath();

            // set the image view using the Glide library round the corners using the
            // Glide transformations library
            MultiTransformation roundedCorners = new MultiTransformation(new FitCenter(),
                    new RoundedCornersTransformation(radius, margin
                            , RoundedCornersTransformation.CornerType.ALL));

            Glide.with(context)
                    .load(imageUrl)
                    .transform(roundedCorners)
                    .into(imgPoster);
        }
    }
}
