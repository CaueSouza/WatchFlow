package com.example.watchflow.dashboard;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.example.watchflow.R;
import com.example.watchflow.databinding.ActivityDashboardBinding;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

public class DashboardActivity extends AppCompatActivity {
    ActivityDashboardBinding binding;
    DashboardViewModel viewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_dashboard);
        binding.setLifecycleOwner(this);

        viewModel = new ViewModelProvider(this).get(DashboardViewModel.class);

        getSupportActionBar().setTitle(R.string.title_activity_dashboard);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().show();

        initBindings();
    }

    private void initBindings() {

        //TODO CREATE CALL TO ENDPOINT THAT RETURN ALL MY SELECTED IPS FOR DASHBOARD IF RETURN NOTHING, CALL CONFIGURATION SCREEN INSTEAD
        //TODO REMOVE HARDCODED GRAPH
        LineGraphSeries<DataPoint> series = new LineGraphSeries<>(new DataPoint[]{
                new DataPoint(0, 1),
                new DataPoint(1, 5),
                new DataPoint(2, 3),
                new DataPoint(3, 2),
                new DataPoint(4, 6)
        });
        binding.graphView.addSeries(series);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
