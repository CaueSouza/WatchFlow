package com.example.watchflow.dashboard;

import static com.example.watchflow.Constants.RETRIEVE_HIGHEST_TIMESTAMP;
import static com.example.watchflow.Constants.RETRIEVE_HIGHEST_TOTAL;
import static com.example.watchflow.Constants.RETRIEVE_MINOR_TIMESTAMP;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.watchflow.R;
import com.example.watchflow.dashboard.configurations.DashboardConfigurationActivity;
import com.example.watchflow.databinding.ActivityDashboardBinding;
import com.jjoe64.graphview.DefaultLabelFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class DashboardActivity extends AppCompatActivity {
    ActivityDashboardBinding binding;
    DashboardViewModel viewModel;
    SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
    private GraphCameraDataSubtitleAdapter camerasAdapter;
    private ArrayList<GraphCameraData> cameraDataArrayList;

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
        binding.camsDataDashboardRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        cameraDataArrayList = new ArrayList<>();
        camerasAdapter = new GraphCameraDataSubtitleAdapter(this, cameraDataArrayList);
        binding.camsDataDashboardRecyclerView.setAdapter(camerasAdapter);
        binding.camsDataDashboardRecyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));

        viewModel.getAllCamerasHistoricMutableLiveData().observe(this, allCamerasHistoric -> {
            binding.graphView.getGridLabelRenderer().setLabelFormatter(new DefaultLabelFormatter() {
                @Override
                public String formatLabel(double value, boolean isValueX) {
                    return isValueX ? sdf.format(new Date((long) value * 1000)) : super.formatLabel(value, false);
                }
            });

            binding.graphView.getViewport().setYAxisBoundsManual(true);
            binding.graphView.getViewport().setMinY(0);
            binding.graphView.getViewport().setMaxY(getSpecificRecordAtHistoric(RETRIEVE_HIGHEST_TOTAL, allCamerasHistoric));

            binding.graphView.getViewport().setXAxisBoundsManual(true);
            binding.graphView.getViewport().setMinX(getSpecificRecordAtHistoric(RETRIEVE_MINOR_TIMESTAMP, allCamerasHistoric));
            binding.graphView.getViewport().setMaxX(getSpecificRecordAtHistoric(RETRIEVE_HIGHEST_TIMESTAMP, allCamerasHistoric));

            binding.graphView.getViewport().setScalable(true);
            binding.graphView.getViewport().setScalableY(true);

            binding.graphView.getGridLabelRenderer().setHorizontalLabelsAngle(10);

            for (int i = 0; i < allCamerasHistoric.size(); i++) {
                CameraHistoric cameraHistoric = allCamerasHistoric.get(i);
                int color = getLineColor(i);

                LineGraphSeries<DataPoint> series = new LineGraphSeries<>(getTotalData(cameraHistoric));
                series.setColor(color);
                binding.graphView.addSeries(series);

                cameraDataArrayList.add(new GraphCameraData(cameraHistoric.getIp(), "endereço", cameraHistoric, color));
            }

            camerasAdapter.notifyDataSetChanged();
        });

        viewModel.getDashboardDataError().observe(this, v -> createRedirectionDialog());
        viewModel.getDashboardInformation();
    }

    private int getLineColor(int count) {
        switch (count) {
            case 0:
                return Color.BLUE;
            case 1:
                return Color.RED;
            case 2:
                return Color.GREEN;
            case 3:
                return Color.YELLOW;
            case 4:
                return Color.MAGENTA;
            default:
                return Color.CYAN;
        }
    }

    private DataPoint[] getTotalData(CameraHistoric cameraHistoric) {
        ArrayList<Integer> x_axis = new ArrayList<>();
        ArrayList<Integer> y_axis = new ArrayList<>();

        for (ReconForTimestamp reconForTimestamp : cameraHistoric.getHistoricAtomUnits()) {
            x_axis.add(reconForTimestamp.getTimestamp());
            y_axis.add(reconForTimestamp.getTotal());
        }

        int n = x_axis.size();
        DataPoint[] values = new DataPoint[n];
        for (int i = 0; i < n; i++) {
            DataPoint v = new DataPoint(x_axis.get(i), y_axis.get(i));
            values[i] = v;
        }

        return values;
    }

    private int getSpecificRecordAtHistoric(int type, ArrayList<CameraHistoric> allCamerasHistoric) {
        int value;

        if (type == RETRIEVE_MINOR_TIMESTAMP) value = (int) (new Date().getTime() / 1000);
        else value = 0;

        switch (type) {
            case RETRIEVE_HIGHEST_TOTAL:
                for (CameraHistoric cameraHistoric : allCamerasHistoric) {
                    for (ReconForTimestamp reconForTimestamp : cameraHistoric.getHistoricAtomUnits()) {
                        value = Math.max(reconForTimestamp.getTotal(), value);
                    }
                }
                break;
            case RETRIEVE_HIGHEST_TIMESTAMP:
                for (CameraHistoric cameraHistoric : allCamerasHistoric) {
                    for (ReconForTimestamp reconForTimestamp : cameraHistoric.getHistoricAtomUnits()) {
                        value = Math.max(reconForTimestamp.getTimestamp(), value);
                    }
                }
                break;
            case RETRIEVE_MINOR_TIMESTAMP:
                for (CameraHistoric cameraHistoric : allCamerasHistoric) {
                    for (ReconForTimestamp reconForTimestamp : cameraHistoric.getHistoricAtomUnits()) {
                        value = Math.min(reconForTimestamp.getTimestamp(), value);
                    }
                }
                break;
        }

        return value;
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
