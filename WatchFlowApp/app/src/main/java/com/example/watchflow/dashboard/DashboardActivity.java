package com.example.watchflow.dashboard;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.example.watchflow.R;
import com.example.watchflow.dashboard.configurations.DashboardConfigurationActivity;
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
        viewModel.getDashboardDataError().observe(this, v -> createRedirectionDialog());

        viewModel.getDashboardInformation();

        //TODO REMOVE HARDCODED GRAPH
        LineGraphSeries<DataPoint> series = new LineGraphSeries<>(new DataPoint[]{
                new DataPoint(0, 1),
                new DataPoint(1, 5),
                new DataPoint(2, 3),
                new DataPoint(3, 2),
                new DataPoint(4, 6),
                new DataPoint(5, 1),
                new DataPoint(6, 5),
                new DataPoint(7, 3),
                new DataPoint(8, 2),
                new DataPoint(9, 6),
                new DataPoint(10, 10),
                new DataPoint(11, 11),
                new DataPoint(12, 12),
                new DataPoint(13, 13),
                new DataPoint(14, 14)
        });

        binding.graphView.getViewport().setYAxisBoundsManual(true);
        binding.graphView.getViewport().setMinY(-150);
        binding.graphView.getViewport().setMaxY(150);

        binding.graphView.getViewport().setXAxisBoundsManual(true);
        binding.graphView.getViewport().setMinX(4);
        binding.graphView.getViewport().setMaxX(80);

        binding.graphView.getViewport().setScalable(true);
        binding.graphView.getViewport().setScalableY(true);
        binding.graphView.addSeries(series);
    }

    private void createRedirectionDialog() {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);

        alertDialog.setTitle(R.string.dashboardErrorDialogTitle);
        alertDialog.setMessage(R.string.dashboardErrorDialogMessage);

        alertDialog.setPositiveButton(R.string.OK, (dialog, which) -> {
            Intent intent = new Intent(this, DashboardConfigurationActivity.class);
            startActivity(intent);
            finish();
        });

        alertDialog.setNegativeButton(R.string.Cancel, (dialog, which) -> {
            dialog.cancel();
            finish();
        });

        alertDialog.setOnDismissListener(v -> finish());

        alertDialog.show();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
