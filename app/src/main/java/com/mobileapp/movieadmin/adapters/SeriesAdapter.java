package com.mobileapp.movieadmin.adapters;

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
import com.mobileapp.movieadmin.R;
import com.mobileapp.movieadmin.SeriesFragment;
import com.mobileapp.movieadmin.models.Series;
import com.mobileapp.movieadmin.ui.SeriesBottomSheet;

import java.util.ArrayList;

public class SeriesAdapter extends RecyclerView.Adapter<SeriesAdapter.SeriesViewHolder> {

    private SeriesFragment fragment;
    private ArrayList<Series> series;

    public SeriesAdapter(SeriesFragment fragment, ArrayList<Series> series) {
        this.fragment = fragment;
        this.series = series;
    }

    @NonNull
    @Override
    public SeriesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.series_item, parent, false);

        return new SeriesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SeriesViewHolder holder, int position) {
        Glide.with(fragment.getContext())
                .load(series.get(position).getImageUrl())
                .into(holder.imageUrlImageView);

        holder.noTextView.setText("No: " + (position + 1));
        holder.titleTextView.setText("Title: " + series.get(position).getTitle());
        holder.genreNameTextView.setText("Genre: " + series.get(position).getGenreName());
        holder.epCountTextView.setText("Episodes: " + series.get(position).getEpisodesCount());
    }

    @Override
    public int getItemCount() {
        return series.size();
    }

    class SeriesViewHolder extends RecyclerView.ViewHolder {

        ImageView imageUrlImageView;
        TextView titleTextView;
        TextView noTextView;
        TextView genreNameTextView;
        TextView epCountTextView;

        public SeriesViewHolder(@NonNull View itemView) {
            super(itemView);

            imageUrlImageView = itemView.findViewById(R.id.image_url);
            titleTextView = itemView.findViewById(R.id.tv_title);
            noTextView = itemView.findViewById(R.id.tv_no);
            genreNameTextView = itemView.findViewById(R.id.tv_genre_name);
            epCountTextView = itemView.findViewById(R.id.tv_ep_count);

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(fragment.getContext());
                    String[] options = {"Edit", "Delete"};
                    builder.setItems(options, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            int position = getAdapterPosition();
                            switch (which){
                                case 0:
//                                    Edit
                                    SeriesBottomSheet bottomSheet = new SeriesBottomSheet(series.get(position));
                                    bottomSheet.show(fragment.getFragmentManager(), bottomSheet.getTag());
                                    break;
//                                    Delete
                                case 1:
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
