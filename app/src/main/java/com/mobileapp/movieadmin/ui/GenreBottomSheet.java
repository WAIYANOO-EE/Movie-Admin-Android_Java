package com.mobileapp.movieadmin.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.mobileapp.movieadmin.R;
import com.mobileapp.movieadmin.models.Genre;
import com.mobileapp.movieadmin.models.Movie;
import com.mobileapp.movieadmin.models.Series;

import java.util.UUID;

public class GenreBottomSheet extends BaseBottomSheet {

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private Genre genre = null;

    public GenreBottomSheet() {
    }

    public GenreBottomSheet(Genre genre) {
        this.genre = genre;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.genre_form, container, false);

        final TextView titleTextView = view.findViewById(R.id.tv_title);
        final EditText genreNameEditText = view.findViewById(R.id.et_genre_name);
        Button btnSave = view.findViewById(R.id.btn_save);

        if (genre != null) {
            titleTextView.setText("Edit Genre");
            genreNameEditText.setText(genre.getName());
        }

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String name = genreNameEditText.getText().toString().trim();

                db.collection(Genre.COLLECTION_NAME)
                        .get()
                        .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                            @Override
                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                boolean isGenreExist = false;
                                for (QueryDocumentSnapshot doc: queryDocumentSnapshots) {
                                    Genre genre = doc.toObject(Genre.class);
                                    if (genre.getName().toLowerCase().equals(name.toLowerCase())) {
                                        isGenreExist = true;
                                    }
                                }

                                if (isGenreExist) {
                                    Toast.makeText(view.getContext(), "Genre is already exist", Toast.LENGTH_SHORT).show();
                                    return;
                                }

                                String id;

                                if (genre != null) {
                                    id = genre.getId();
                                } else {
                                    id = UUID.randomUUID().toString();
                                }

                                db.collection(Genre.COLLECTION_NAME)
                                        .document(id)
                                        .set(new Genre(id, name))
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                if (genre != null) {

                                                    // genre update inside movies
                                                    db.collection(Movie.COLLECTION_NAME)
                                                            .get()
                                                            .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                                                @Override
                                                                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                                                                    for (QueryDocumentSnapshot doc: queryDocumentSnapshots) {
                                                                        Movie movie = doc.toObject(Movie.class);

                                                                        if (movie.getGenreId().equals(genre.getId())) {

                                                                            movie.setGenreName(name);

                                                                            db.collection(Movie.COLLECTION_NAME)
                                                                                    .document(movie.getId())
                                                                                    .set(movie)
                                                                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                        @Override
                                                                                        public void onSuccess(Void aVoid) {

                                                                                        }
                                                                                    })
                                                                                    .addOnFailureListener(new OnFailureListener() {
                                                                                        @Override
                                                                                        public void onFailure(@NonNull Exception e) {
                                                                                            Toast.makeText(view.getContext(), "Genre Update Failed! Please try again!", Toast.LENGTH_SHORT).show();
                                                                                        }
                                                                                    });
                                                                        }
                                                                    }

                                                                }
                                                            })
                                                            .addOnFailureListener(new OnFailureListener() {
                                                                @Override
                                                                public void onFailure(@NonNull Exception e) {
                                                                    Toast.makeText(view.getContext(), "Genre Update Failed! Please try again!", Toast.LENGTH_SHORT).show();
                                                                }
                                                            });

                                                    // genre update inside series
                                                    db.collection(Series.COLLECTION_NAME)
                                                            .get()
                                                            .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                                                @Override
                                                                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                                                                    for (QueryDocumentSnapshot doc: queryDocumentSnapshots) {
                                                                        Series series = doc.toObject(Series.class);
                                                                        if (series.getGenreId().equals(genre.getId())) {

                                                                            series.setGenreName(name);

                                                                            db.collection(Series.COLLECTION_NAME)
                                                                                    .document(series.getId())
                                                                                    .set(series)
                                                                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                        @Override
                                                                                        public void onSuccess(Void aVoid) {

                                                                                        }
                                                                                    })
                                                                                    .addOnFailureListener(new OnFailureListener() {
                                                                                        @Override
                                                                                        public void onFailure(@NonNull Exception e) {
                                                                                            Toast.makeText(view.getContext(), "Genre Update Failed! Please try again!", Toast.LENGTH_SHORT).show();
                                                                                        }
                                                                                    });
                                                                        }
                                                                    }

                                                                }
                                                            })
                                                            .addOnFailureListener(new OnFailureListener() {
                                                                @Override
                                                                public void onFailure(@NonNull Exception e) {
                                                                    Toast.makeText(view.getContext(), "Genre Update Failed! Please try again!", Toast.LENGTH_SHORT).show();
                                                                }
                                                            });

                                                    dismiss();

                                                } else {
                                                    genreNameEditText.setText("");
                                                }
                                                Toast.makeText(getContext(), "Save Success!", Toast.LENGTH_SHORT).show();
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(getContext(), "Save Failed!", Toast.LENGTH_SHORT).show();
                                            }
                                        });


                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {

                                    }
                                }
                        );




            }
        });

        return view;
    }
}
