package com.mobileapp.movieadmin.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
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

import java.util.ArrayList;
import java.util.UUID;

public class SeriesBottomSheet extends BaseBottomSheet {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private ArrayList<Genre> genres = new ArrayList<>();
    private int selectedPosition = 0;
    private Series series;

    public SeriesBottomSheet() {}

    public SeriesBottomSheet(Series series){
        this.series = series;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.series_form, container, false);

        TextView formTitleTextView = view.findViewById(R.id.tv_form_title);
        final EditText titleEditText = view.findViewById(R.id.et_series_title);
        final EditText descEditText = view.findViewById(R.id.et_series_description);
        final EditText epCountEditText = view.findViewById(R.id.et_series_episodes_count);
        final EditText imageUrlEditText = view.findViewById(R.id.et_series_image_url);
        final Spinner genresSpinner = view.findViewById(R.id.sp_genres);
        Button btnSave = view.findViewById(R.id.btn_save);

        if (series != null){
            formTitleTextView.setText("Edit Series");
            titleEditText.setText(series.getTitle());
            descEditText.setText(series.getDescription());
//            Enter Int with String
            epCountEditText.setText(series.getEpisodesCount() + "");
            imageUrlEditText.setText(series.getImageUrl());

        }

        epCountEditText.setText("0");

        db.collection(Genre.COLLECTION_NAME)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                        ArrayList<String> genreNames = new ArrayList<>();

                        int index = 0;
                        int selectedGenrePosition = 0;
                        for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                            Genre genre = doc.toObject(Genre.class);
                            genreNames.add(genre.getName());

                            genres.add(genre);

                            if (series != null){
                                if (series.getGenreId().equals(genre.getId())){
                                    selectedGenrePosition = index;
                                }
                            }

                            index++;
                        }

                        ArrayAdapter<String> adapter = new ArrayAdapter<>(view.getContext(), android.R.layout.simple_dropdown_item_1line, genreNames);
                        genresSpinner.setAdapter(adapter);

                        if (series != null){
                            genresSpinner.setSelection(selectedGenrePosition);
                        }

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(view.getContext(), "Please check your internet connection and try again!", Toast.LENGTH_SHORT).show();
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

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String title = titleEditText.getText().toString();
                final String desc = descEditText.getText().toString();
                final String imageUrl = imageUrlEditText.getText().toString();
                final int epCount = Integer.parseInt(epCountEditText.getText().toString());

                String id = null;
                if (series != null){
                    id = series.getId();
                }else {
                    id = UUID.randomUUID().toString();
                }

                final Series series = new Series(id,
                        title, desc, epCount, imageUrl,
                        genres.get(selectedPosition).getId(),
                        genres.get(selectedPosition).getName());

                db.collection(Series.COLLECTION_NAME)
                        .document(id)
                        .set(series)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {

                                Toast.makeText(view.getContext(), "Save Success!", Toast.LENGTH_SHORT).show();
                                //Error series
                                if (SeriesBottomSheet.this.series != null){
                                    dismiss();
                                }


                                titleEditText.setText("");
                                descEditText.setText("");
                                imageUrlEditText.setText("");
                                epCountEditText.setText("0");
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(view.getContext(), "Save Failed!", Toast.LENGTH_SHORT).show();
                            }
                        });

            }
        });

        return view;
    }
}
