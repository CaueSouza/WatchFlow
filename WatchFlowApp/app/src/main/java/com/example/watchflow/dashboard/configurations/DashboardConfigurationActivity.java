package com.example.watchflow.dashboard.configurations;

import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.watchflow.R;
import com.example.watchflow.databinding.ActivityDashboardConfigurationBinding;

import java.util.ArrayList;

public class DashboardConfigurationActivity extends AppCompatActivity {
    ActivityDashboardConfigurationBinding binding;
    DashboardConfigurationViewModel viewModel;

    private ConfigurationIPAdapter camerasIPsAdapter;
    private ArrayList<DashboardConfigurationCamData> camerasIPsDataArrayList;

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

    private void initBindings() {
        viewModel.getEndActivityEvent().observe(this, v -> {
            Toast.makeText(this, R.string.success_saving_text, Toast.LENGTH_SHORT).show();
            finish();
        });

        binding.saveButton.setOnClickListener(view -> {
            RecyclerView recyclerView = binding.camsIpsDashboardConfigurationRecyclerView;
            ArrayList<String> selectedIPs = new ArrayList<>();

            for (int i = 0; i < recyclerView.getAdapter().getItemCount(); i++) {
                RecyclerView.ViewHolder actualViewHolder = recyclerView.findViewHolderForAdapterPosition(i);
                CheckBox actualCheckBox = actualViewHolder.itemView.findViewById(R.id.item_checkbox);

                if (actualCheckBox.isChecked()) {
                    TextView textView = actualViewHolder.itemView.findViewById(R.id.ip_item_content);
                    selectedIPs.add(textView.getText().toString());
                }
            }

            viewModel.saveAllChanges(selectedIPs);
        });

        binding.camsIpsDashboardConfigurationRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        camerasIPsDataArrayList = new ArrayList<>();
        camerasIPsAdapter = new ConfigurationIPAdapter(this, camerasIPsDataArrayList);
        binding.camsIpsDashboardConfigurationRecyclerView.setAdapter(camerasIPsAdapter);
        binding.camsIpsDashboardConfigurationRecyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));

        createDataRequests();
    }

    private void createDataRequests() {
        viewModel.getAllCamerasIPs().observe(this, camerasInformations -> {
            camerasIPsDataArrayList.clear();
            camerasIPsDataArrayList.addAll(camerasInformations);
            camerasIPsAdapter.notifyDataSetChanged();
        });

        viewModel.requestMyDashboardCameras();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
