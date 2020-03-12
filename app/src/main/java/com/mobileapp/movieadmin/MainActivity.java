package com.mobileapp.movieadmin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    FragmentManager manager = getSupportFragmentManager();
    Fragment genreFrag = new GenresFragment();
    Fragment movieFrag = new MoviesFragment();
    Fragment seriesFrag = new SeriesFragment();
    Fragment episodesFrag = new EpisodesFragment();
    Fragment activeFrag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_nav);

        manager.beginTransaction().add(R.id.fragment_container, movieFrag).hide(movieFrag).commit();
        manager.beginTransaction().add(R.id.fragment_container, seriesFrag).hide(seriesFrag).commit();
        manager.beginTransaction().add(R.id.fragment_container, episodesFrag).hide(episodesFrag).commit();
        manager.beginTransaction().add(R.id.fragment_container, genreFrag).commit();
        activeFrag = genreFrag;

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                Fragment selectedFrag = null;

                switch (item.getItemId()) {
                    case R.id.action_genres:
                        selectedFrag = genreFrag;
                        setTitle("Genres");
                        break;
                    case R.id.action_movies:
                        selectedFrag = movieFrag;
                        setTitle("Movies");
                        break;
                    case R.id.action_series:
                        selectedFrag = seriesFrag;
                        setTitle("Series");
                        break;
                    case R.id.action_episodes:
                        selectedFrag = episodesFrag;
                        setTitle("Episodes");
                }

                manager.beginTransaction().hide(activeFrag).show(selectedFrag).commit();
                activeFrag = selectedFrag;

                return true;
            }
        });

    }
}
