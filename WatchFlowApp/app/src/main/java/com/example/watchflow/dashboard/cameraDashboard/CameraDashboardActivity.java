package com.example.watchflow.dashboard.cameraDashboard;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.example.watchflow.R;
import com.example.watchflow.dashboard.GraphCameraData;
import com.example.watchflow.databinding.ActivityCameraDashboardBinding;

public class CameraDashboardActivity extends AppCompatActivity {
    ActivityCameraDashboardBinding binding;
    CameraDashboardViewModel viewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_camera_dashboard);
        binding.setLifecycleOwner(this);

        viewModel = new ViewModelProvider(this).get(CameraDashboardViewModel.class);

        getSupportActionBar().setTitle(R.string.title_camera_dashboard);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().show();

        viewModel.setGraphCameraData((GraphCameraData) getIntent().getSerializableExtra(String.valueOf(R.string.historic_tag_for_intent)));
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
