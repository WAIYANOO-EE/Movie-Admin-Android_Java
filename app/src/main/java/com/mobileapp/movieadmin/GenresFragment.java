package com.mobileapp.movieadmin;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.mobileapp.movieadmin.adapters.GenreAdapter;
import com.mobileapp.movieadmin.helpers.ActivityHelper;
import com.mobileapp.movieadmin.models.Genre;
import com.mobileapp.movieadmin.ui.GenreBottomSheet;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class GenresFragment extends Fragment {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private ArrayList<Genre> genres = new ArrayList<>();

    public GenresFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {

        final Context context = container.getContext();
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_genres, container, false);

        // local variable
        FloatingActionButton createGenreFab = view.findViewById(R.id.fab_create_genre);
        final RecyclerView recyclerView = view.findViewById(R.id.rv_genres);
        final EditText searchEditText = view.findViewById(R.id.et_search);

        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String search = s.toString().trim();

                if (count > 0) {
                    // search
                    ArrayList<Genre> filterGenres = new ArrayList<>();

                    for (Genre genre: genres) {
                        if (genre.getName().toLowerCase().contains(search.toLowerCase())) {
                            filterGenres.add(genre);
                        }
                    }

                    GenreAdapter adapter = new GenreAdapter(GenresFragment.this, filterGenres);
                    recyclerView.setAdapter(adapter);
                } else {
                    GenreAdapter adapter = new GenreAdapter(GenresFragment.this, genres);
                    recyclerView.setAdapter(adapter);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        createGenreFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                GenreBottomSheet bottomSheet = new GenreBottomSheet();

                bottomSheet.show(getFragmentManager(), "addGenre");
            }
        });

        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManager);

        db.collection(Genre.COLLECTION_NAME)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value,
                                        @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            Toast.makeText(context, "Listen failed", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        genres.clear();

                        for (QueryDocumentSnapshot doc : value) {

                            Genre genre = doc.toObject(Genre.class);
                            genres.add(genre);

                        }

                        GenreAdapter adapter = new GenreAdapter(GenresFragment.this, genres);
                        recyclerView.setAdapter(adapter);
                    }
                });

        return view;
    }

}
