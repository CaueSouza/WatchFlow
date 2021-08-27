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
                new DataPoint(4, 6)
        });
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
