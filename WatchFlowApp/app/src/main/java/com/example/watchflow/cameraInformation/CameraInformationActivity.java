package com.example.watchflow.cameraInformation;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.watchflow.R;
import com.example.watchflow.databinding.ActivityCameraInformationBinding;

import java.util.ArrayList;

import static com.example.watchflow.Constants.IMAGE;

public class CameraInformationActivity extends AppCompatActivity {

    ActivityCameraInformationBinding binding;
    CameraInformationViewModel viewModel;

    private DataAdapter cameraDataAdapter;
    private DataAdapter recognitionDataAdapter;
    private ArrayList<Data> cameraDataArrayList;
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

        viewModel.getImage().setValue(getIntent().getStringExtra(IMAGE));

        binding.cameraDataRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        cameraDataArrayList = new ArrayList<>();
        cameraDataAdapter = new DataAdapter(this, cameraDataArrayList);
        binding.cameraDataRecyclerView.setAdapter(cameraDataAdapter);
        binding.cameraDataRecyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));

        binding.recognitionDataRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        recognitionDataArrayList = new ArrayList<>();
        recognitionDataAdapter = new DataAdapter(this, recognitionDataArrayList);
        binding.recognitionDataRecyclerView.setAdapter(recognitionDataAdapter);
        binding.recognitionDataRecyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));

        createListData();
    }

    private void createListData() {
        Data data = new Data("IP", "127.0.0.1");
        Data data2 = new Data("Rua", "visconde");
        cameraDataArrayList.add(data);
        cameraDataArrayList.add(data2);

        Data data3 = new Data("Carros", "13");
        Data data4 = new Data("Motos", "5");
        recognitionDataArrayList.add(data3);
        recognitionDataArrayList.add(data4);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
