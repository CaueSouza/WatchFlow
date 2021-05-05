package com.example.watchflow;

import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.watchflow.databinding.ActivityMapsBinding;

public class MapsActivity extends FragmentActivity{

    MapsViewModel viewModel;
    ActivityMapsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_maps);
        setContentView(binding.getRoot());

        viewModel = new ViewModelProvider(this).get(MapsViewModel.class);

        viewModel.createGpsTracker(this);

        MapFragment mapFragment = new MapFragment();

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.maps_layout, mapFragment)
                .commit();
    }
}