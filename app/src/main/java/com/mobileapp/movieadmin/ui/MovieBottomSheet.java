package com.mobileapp.movieadmin.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.mobileapp.movieadmin.R;
import com.mobileapp.movieadmin.models.Genre;
import com.mobileapp.movieadmin.models.Movie;

import java.util.ArrayList;
import java.util.UUID;

public class MovieBottomSheet extends BaseBottomSheet {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private ArrayList<Genre> genres = new ArrayList<>();
    private int selectedPosition = 0;
    private Movie movie;

    public MovieBottomSheet() {}

    public MovieBottomSheet(Movie movie) {
        this.movie = movie;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.movie_form, container, false);

        TextView formTitleTextView = view.findViewById(R.id.tv_form_title);
        FloatingActionButton saveFAB = view.findViewById(R.id.fab_save);
        final EditText titleEditText = view.findViewById(R.id.et_movie_title);
        final EditText imageUrlEditText = view.findViewById(R.id.et_movie_image_url);
        final EditText videoLinkEditText = view.findViewById(R.id.et_movie_video_link);
        final Spinner genresSpinner = view.findViewById(R.id.sp_genres);

        if (movie != null) {
            formTitleTextView.setText("Edit Movie");
            titleEditText.setText(movie.getTitle());
            imageUrlEditText.setText(movie.getImageUrl());
            videoLinkEditText.setText(movie.getVideoLink());
        }

        db.collection(Genre.COLLECTION_NAME)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                        ArrayList<String> genreNames = new ArrayList<>();

                        int index = 0;
                        int selectedGenrePosition = 0;
                        for (QueryDocumentSnapshot doc: queryDocumentSnapshots) {
                            Genre genre = doc.toObject(Genre.class);

                            genreNames.add(genre.getName());

                            genres.add(genre);

                            if (movie != null) {
                                if (movie.getGenreId().equals(genre.getId())) {
                                    selectedGenrePosition = index;
                                }
                            }

                            index++;
                        }

                        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_dropdown_item_1line, genreNames);
                        genresSpinner.setAdapter(adapter);

                        if (movie != null) {
                            genresSpinner.setSelection(selectedGenrePosition);
                        }

                    }

                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getContext(), "Check your internet connection and try again!", Toast.LENGTH_SHORT).show();
                    }
                });

        genresSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedPosition = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        saveFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String title = titleEditText.getText().toString();
                final String imageUrl = imageUrlEditText.getText().toString();
                final String videoLink = videoLinkEditText.getText().toString();

                String movieId;
                if (movie != null) {
                    movieId = movie.getId();
                } else {
                    movieId = UUID.randomUUID().toString();
                }

                Movie movie = new Movie(
                        movieId, title, imageUrl, videoLink,
                        genres.get(selectedPosition).getId(),
                        genres.get(selectedPosition).getName());

                db.collection(Movie.COLLECTION_NAME)
                        .document(movieId)
                        .set(movie)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(getContext(), "Save Success!", Toast.LENGTH_SHORT).show();

                                if (MovieBottomSheet.this.movie != null) {
                                    dismiss();
                                }

                                titleEditText.setText("");
                                imageUrlEditText.setText("");
                                videoLinkEditText.setText("");


                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getContext(), "Save Failed!", Toast.LENGTH_SHORT).show();
                            }
                        });

            }
        });


        return view;
    }
}
