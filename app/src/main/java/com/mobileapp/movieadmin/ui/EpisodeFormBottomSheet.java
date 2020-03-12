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
import com.mobileapp.movieadmin.models.Episode;
import com.mobileapp.movieadmin.models.Series;

import java.util.ArrayList;
import java.util.UUID;

public class EpisodeFormBottomSheet extends BaseBottomSheet {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private ArrayList<Series> seriesCollection;
    private int selectedSeries = 0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.episode_form, container, false);

        final TextView titleTextView = view.findViewById(R.id.tv_title);
        final EditText nameEditText = view.findViewById(R.id.et_ep_name);
        final EditText videoLinkEditText = view.findViewById(R.id.et_ep_video_link);
        final Spinner seriesSpinner = view.findViewById(R.id.sp_series);
        final Button savButton = view.findViewById(R.id.btn_save);

        seriesCollection = new ArrayList<>();

        db.collection(Series.COLLECTION_NAME)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                        ArrayList<String> seriesNames = new ArrayList<>();

                        for (QueryDocumentSnapshot doc: queryDocumentSnapshots) {
                            Series series = doc.toObject(Series.class);
                            seriesNames.add(series.getTitle());

                            seriesCollection.add(series);
                        }

                        ArrayAdapter<String> adapter = new ArrayAdapter<>(view.getContext(), android.R.layout.simple_dropdown_item_1line, seriesNames);
                        seriesSpinner.setAdapter(adapter);

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });

        seriesSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedSeries = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        savButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String name = nameEditText.getText().toString().trim();
                final String videoLink = videoLinkEditText.getText().toString().trim();

                String id = UUID.randomUUID().toString();

                Episode episode = new Episode(id, name, videoLink, seriesCollection.get(selectedSeries).getId());

                db.collection(Episode.COLLECTION_NAME)
                        .document(id)
                        .set(episode)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(view.getContext(), "Save Success!", Toast.LENGTH_SHORT).show();

                                nameEditText.setText("");
                                videoLinkEditText.setText("");
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
