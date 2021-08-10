package com.example.watchflow.cameraInformation;

import static com.example.watchflow.Constants.MARKER_TITLE;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.watchflow.R;
import com.example.watchflow.databinding.ActivityCameraInformationBinding;

import java.util.ArrayList;

public class CameraInformationActivity extends AppCompatActivity {

    ActivityCameraInformationBinding binding;
    CameraInformationViewModel viewModel;

    private DataAdapter localizationDataAdapter;
    private DataAdapter recognitionDataAdapter;
    private ArrayList<Data> localizationDataArrayList;
    private ArrayList<Data> recognitionDataArrayList;

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

        initBindings();
    }

    private void initBindings() {
        binding.localizationDataRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        localizationDataArrayList = new ArrayList<>();
        localizationDataAdapter = new DataAdapter(this, localizationDataArrayList);
        binding.localizationDataRecyclerView.setAdapter(localizationDataAdapter);
        binding.localizationDataRecyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));

        binding.recognitionDataRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        recognitionDataArrayList = new ArrayList<>();
        recognitionDataAdapter = new DataAdapter(this, recognitionDataArrayList);
        binding.recognitionDataRecyclerView.setAdapter(recognitionDataAdapter);
        binding.recognitionDataRecyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));

        createDataRequests();
    }

    private void createDataRequests() {
        viewModel.getCameraInformation(getIntent().getStringExtra(MARKER_TITLE));

        viewModel.getLocalizationData().observe(this, vmArrayList -> {
            localizationDataArrayList.clear();
            localizationDataArrayList.addAll(vmArrayList);
            localizationDataAdapter.notifyDataSetChanged();
        });

        viewModel.getRecognitionData().observe(this, vmArrayList -> {
            recognitionDataArrayList.clear();
            recognitionDataArrayList.addAll(vmArrayList);
            recognitionDataAdapter.notifyDataSetChanged();
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
