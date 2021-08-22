package com.example.watchflow.dashboard.configurations;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.example.watchflow.R;
import com.example.watchflow.databinding.ActivityDashboardConfigurationBinding;

public class DashboardConfigurationActivity extends AppCompatActivity {
    ActivityDashboardConfigurationBinding binding;
    DashboardConfigurationViewModel viewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_dashboard_configuration);
        binding.setLifecycleOwner(this);

        viewModel = new ViewModelProvider(this).get(DashboardConfigurationViewModel.class);

        getSupportActionBar().setTitle(R.string.dashboard_config_txt);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().show();

        initBindings();
    }

    private void initBindings(){
        viewModel.requestAllRunningCameras();
    }
}
