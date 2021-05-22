package com.example.watchflow.cameraInformation;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.example.watchflow.R;
import com.example.watchflow.databinding.ActivityCameraInformationBinding;

import static com.example.watchflow.Constants.IMAGE;

public class CameraInformationActivity extends AppCompatActivity {

    ActivityCameraInformationBinding binding;
    CameraInformationViewModel viewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_camera_information);
        binding.setLifecycleOwner(this);

        viewModel = new ViewModelProvider(this).get(CameraInformationViewModel.class);
        binding.setViewModel(viewModel);

        getSupportActionBar().setTitle(R.string.title_activity_cam_information);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().show();

        viewModel.getImage().setValue(getIntent().getStringExtra(IMAGE));
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
