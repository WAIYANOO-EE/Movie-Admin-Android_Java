package com.mobileapp.movieadmin.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.mobileapp.movieadmin.MoviesFragment;
import com.mobileapp.movieadmin.R;
import com.mobileapp.movieadmin.models.Movie;
import com.mobileapp.movieadmin.ui.MovieBottomSheet;

import java.util.ArrayList;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {

    private MoviesFragment fragment;
    private ArrayList<Movie> movies;

    public MovieAdapter(MoviesFragment fragment, ArrayList<Movie> movies) {
        this.fragment = fragment;
        this.movies = movies;
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.movie_item, parent, false);

        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {
        // Image
        Glide.with(fragment.getContext())
                .load(movies.get(position).getImageUrl())
                .into(holder.imageView);

        holder.noTextView.setText((position + 1) + "");
        holder.titleTextView.setText(movies.get(position).getTitle());
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    class MovieViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView noTextView;
        TextView titleTextView;

        public MovieViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.image_url);
            noTextView = itemView.findViewById(R.id.tv_movie_no);
            titleTextView = itemView.findViewById(R.id.tv_movie_title);

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(fragment.getContext());

                    String[] options = {"Edit", "Delete"};

                    builder.setItems(options, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            int position = getAdapterPosition();
                            switch (which) {
                                case 0:
                                    //Edit
                                    MovieBottomSheet bottomSheet = new MovieBottomSheet(movies.get(position));
                                    bottomSheet.show(fragment.getFragmentManager(), bottomSheet.getTag());
                                    break;
                                case 1:
                                    //Delete
                            }
                        }
                    });

                    builder.show();

                    return true;
                }
            });
        }
    }
}
