package com.mobileapp.movieadmin;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.mobileapp.movieadmin.ui.EpisodeFormBottomSheet;


/**
 * A simple {@link Fragment} subclass.
 */
public class EpisodesFragment extends Fragment {

    public EpisodesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       View view = inflater.inflate(R.layout.fragment_episodes, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.recycler_view);
        FloatingActionButton actionButton = view.findViewById(R.id.fab_create_episode);
        actionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EpisodeFormBottomSheet bottomSheet = new EpisodeFormBottomSheet();
                bottomSheet.show(getFragmentManager(), bottomSheet.getTag());
            }
        });
       return view;
    }
}
