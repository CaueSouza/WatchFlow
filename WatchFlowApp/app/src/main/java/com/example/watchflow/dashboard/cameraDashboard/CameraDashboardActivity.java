package com.example.watchflow.dashboard.cameraDashboard;

import static com.example.watchflow.Constants.ARTICULATED_TRUCK;
import static com.example.watchflow.Constants.BICYCLE;
import static com.example.watchflow.Constants.BUS;
import static com.example.watchflow.Constants.CAR;
import static com.example.watchflow.Constants.MOTORCYCLE;
import static com.example.watchflow.Constants.MOTORIZED_VEHICLE;
import static com.example.watchflow.Constants.NON_MOTORIZED_VEHICLE;
import static com.example.watchflow.Constants.PEDESTRIAN;
import static com.example.watchflow.Constants.PICKUP_TRUCK;
import static com.example.watchflow.Constants.RECOGNITION_FIELDS;
import static com.example.watchflow.Constants.SINGLE_UNIT_TRUCK;
import static com.example.watchflow.Constants.TOTAL;
import static com.example.watchflow.Constants.WORK_VAN;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.watchflow.R;
import com.example.watchflow.dashboard.GraphCameraData;
import com.example.watchflow.dashboard.ReconForTimestamp;
import com.example.watchflow.dashboard.SpecificReconForTimestamp;
import com.example.watchflow.databinding.ActivityCameraDashboardBinding;

import java.util.ArrayList;

public class CameraDashboardActivity extends AppCompatActivity {
    ActivityCameraDashboardBinding binding;
    CameraDashboardViewModel viewModel;

    private CameraDashboardAdapter cameraReconAdapter;
    private ArrayList<CameraDashboardGraphicsData> cameraReconArrayList;

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

        initBindings();
    }

    private void initBindings() {
        viewModel.setGraphCameraData((GraphCameraData) getIntent().getSerializableExtra(String.valueOf(R.string.historic_tag_for_intent)));
        binding.setIp(viewModel.getGraphCameraData().getHistoric().getIp());
        binding.setAddress(viewModel.getGraphCameraData().getHistoric().getAddress());

        cameraReconArrayList = new ArrayList<>();
        ArrayList<ReconForTimestamp> allRecons = viewModel.getGraphCameraData().getHistoric().getHistoricAtomUnits();

        ArrayList<SpecificReconForTimestamp> totalRecon = getReconsForSpecificField(TOTAL, allRecons);
        int total = getFieldHighestValue(totalRecon);

        if (!isReconEmpty(totalRecon)) {
            cameraReconArrayList.add(new CameraDashboardGraphicsData(getFieldTitle(TOTAL), total, totalRecon));
        }

        for (String field : RECOGNITION_FIELDS) {
            if (!field.equals(TOTAL)) {
                ArrayList<SpecificReconForTimestamp> specificRecon = getReconsForSpecificField(field, allRecons);

                if (!isReconEmpty(specificRecon)) {
                    cameraReconArrayList.add(new CameraDashboardGraphicsData(getFieldTitle(field), total, specificRecon));
                }
            }
        }

        binding.camDashGraphsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        cameraReconAdapter = new CameraDashboardAdapter(this, cameraReconArrayList);
        binding.camDashGraphsRecyclerView.setAdapter(cameraReconAdapter);
        binding.camDashGraphsRecyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
    }

    boolean isReconEmpty(ArrayList<SpecificReconForTimestamp> recons) {
        int count = 0;

        for (SpecificReconForTimestamp recon : recons) {
            if (recon.getData() == 0) {
                count++;
            }
        }

        return count == recons.size();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private String getFieldTitle(String field) {
        switch (field) {
            case TOTAL:
                return getString(R.string.recognitions_total);

            case ARTICULATED_TRUCK:
                return getString(R.string.recognitions_articulated_truck);

            case BICYCLE:
                return getString(R.string.recognitions_bicycle);

            case BUS:
                return getString(R.string.recognitions_bus);

            case CAR:
                return getString(R.string.recognitions_car);

            case MOTORCYCLE:
                return getString(R.string.recognitions_motorcycle);

            case MOTORIZED_VEHICLE:
                return getString(R.string.recognitions_motorized_vehicle);

            case NON_MOTORIZED_VEHICLE:
                return getString(R.string.recognitions_non_motorized_vehicle);

            case PEDESTRIAN:
                return getString(R.string.recognitions_pedestrian);

            case PICKUP_TRUCK:
                return getString(R.string.recognitions_pickup_truck);

            case SINGLE_UNIT_TRUCK:
                return getString(R.string.recognitions_single_unit_truck);

            case WORK_VAN:
                return getString(R.string.recognitions_work_van);

            default:
                return "";
        }
    }

    private int getFieldHighestValue(ArrayList<SpecificReconForTimestamp> allRecons) {
        int highest = 0;

        for (SpecificReconForTimestamp recon : allRecons) {
            if (recon.getData() > highest) highest = recon.getData();
        }

        return highest;
    }

    private ArrayList<SpecificReconForTimestamp> getReconsForSpecificField(String field, ArrayList<ReconForTimestamp> allRecons) {
        ArrayList<SpecificReconForTimestamp> allReconsForField = new ArrayList<>();

        switch (field) {
            case TOTAL:
                for (ReconForTimestamp recon : allRecons) {
                    allReconsForField.add(
                            new SpecificReconForTimestamp(recon.getTimestamp(), recon.getTotal()));
                }

                break;
            case ARTICULATED_TRUCK:
                for (ReconForTimestamp recon : allRecons) {
                    allReconsForField.add(
                            new SpecificReconForTimestamp(recon.getTimestamp(), recon.getArticulated_truck()));
                }

                break;
            case BICYCLE:
                for (ReconForTimestamp recon : allRecons) {
                    allReconsForField.add(
                            new SpecificReconForTimestamp(recon.getTimestamp(), recon.getBicycle()));
                }

                break;
            case BUS:
                for (ReconForTimestamp recon : allRecons) {
                    allReconsForField.add(
                            new SpecificReconForTimestamp(recon.getTimestamp(), recon.getBus()));
                }

                break;
            case CAR:
                for (ReconForTimestamp recon : allRecons) {
                    allReconsForField.add(
                            new SpecificReconForTimestamp(recon.getTimestamp(), recon.getCar()));
                }

                break;
            case MOTORCYCLE:
                for (ReconForTimestamp recon : allRecons) {
                    allReconsForField.add(
                            new SpecificReconForTimestamp(recon.getTimestamp(), recon.getMotorcycle()));
                }

                break;
            case MOTORIZED_VEHICLE:
                for (ReconForTimestamp recon : allRecons) {
                    allReconsForField.add(
                            new SpecificReconForTimestamp(recon.getTimestamp(), recon.getMotorized_vehicle()));
                }

                break;
            case NON_MOTORIZED_VEHICLE:
                for (ReconForTimestamp recon : allRecons) {
                    allReconsForField.add(
                            new SpecificReconForTimestamp(recon.getTimestamp(), recon.getNon_motorized_vehicle()));
                }

                break;
            case PEDESTRIAN:
                for (ReconForTimestamp recon : allRecons) {
                    allReconsForField.add(
                            new SpecificReconForTimestamp(recon.getTimestamp(), recon.getPedestrian()));
                }

                break;
            case PICKUP_TRUCK:
                for (ReconForTimestamp recon : allRecons) {
                    allReconsForField.add(
                            new SpecificReconForTimestamp(recon.getTimestamp(), recon.getPickup_truck()));
                }

                break;
            case SINGLE_UNIT_TRUCK:
                for (ReconForTimestamp recon : allRecons) {
                    allReconsForField.add(
                            new SpecificReconForTimestamp(recon.getTimestamp(), recon.getSingle_unit_truck()));
                }

                break;
            case WORK_VAN:
                for (ReconForTimestamp recon : allRecons) {
                    allReconsForField.add(
                            new SpecificReconForTimestamp(recon.getTimestamp(), recon.getWork_van()));
                }

                break;
        }

        return allReconsForField;
    }
}
