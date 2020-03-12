package com.mobileapp.movieadmin.adapters;

import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.mobileapp.movieadmin.GenresFragment;
import com.mobileapp.movieadmin.R;
import com.mobileapp.movieadmin.models.Genre;
import com.mobileapp.movieadmin.ui.GenreBottomSheet;

import java.util.ArrayList;

public class GenreAdapter extends RecyclerView.Adapter<GenreAdapter.GenreViewHolder> {

    GenresFragment genresFragment;
    ArrayList<Genre> genres;

    public GenreAdapter(GenresFragment genresFragment, ArrayList<Genre> genres) {
        this.genresFragment = genresFragment;
        this.genres = genres;
    }

    @NonNull
    @Override
    public GenreViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.genre_item, parent, false);

        return new GenreViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GenreViewHolder holder, int position) {
        holder.noTextView.setText((position + 1) + "");
        holder.nameTextView.setText(genres.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return genres.size();
    }

    class GenreViewHolder extends RecyclerView.ViewHolder {

        TextView noTextView;
        TextView nameTextView;

        public GenreViewHolder(@NonNull final View itemView) {
            super(itemView);

            noTextView = itemView.findViewById(R.id.tv_no);
            nameTextView = itemView.findViewById(R.id.tv_genre_name);

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {

                    AlertDialog.Builder builder = new AlertDialog.Builder(itemView.getContext());

                    String[] items = {"Edit", "Delete"};

                    builder.setItems(items, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            FirebaseFirestore db = FirebaseFirestore.getInstance();

                            int position = getAdapterPosition();

                            switch (which) {
                                case 0:
                                    // Edit
                                    GenreBottomSheet bottomSheet = new GenreBottomSheet(genres.get(position));
                                    bottomSheet.show(genresFragment.getFragmentManager(), "editGenre");

                                    break;
                                case 1:
                                    // Delete
                                    db.collection(Genre.COLLECTION_NAME)
                                            .document(genres.get(position).getId())
                                            .delete();
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
