package com.example.watchflow.dashboard.configurations;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.watchflow.R;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class ConfigurationIPAdapter extends RecyclerView.Adapter<ConfigurationIPAdapter.DataHolder> {

    private final Context context;
    private final ArrayList<DashboardConfigurationCamData> camerasIPs;

    public ConfigurationIPAdapter(Context context, ArrayList<DashboardConfigurationCamData> camerasIPs) {
        this.context = context;
        this.camerasIPs = camerasIPs;
    }

    @NonNull
    @NotNull
    @Override
    public ConfigurationIPAdapter.DataHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.configuration_dashboard_layout_item, parent, false);

        return new DataHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ConfigurationIPAdapter.DataHolder holder, int position) {
        DashboardConfigurationCamData cameraInformations = camerasIPs.get(position);
        holder.setDetails(cameraInformations);
    }

    @Override
    public int getItemCount() {
        return camerasIPs.size();
    }

    static class DataHolder extends RecyclerView.ViewHolder {
        private final TextView txtIP, txtAddress;
        private final CheckBox checkBox;

        public DataHolder(@NonNull @NotNull View itemView) {
            super(itemView);

            checkBox = itemView.findViewById(R.id.item_checkbox);
            txtIP = itemView.findViewById(R.id.ip_item_content);
            txtAddress = itemView.findViewById(R.id.address_item_content);
        }

        void setDetails(DashboardConfigurationCamData cameraInformations) {
            checkBox.setChecked(cameraInformations.isChecked());
            txtIP.setText(cameraInformations.getIp());
            txtAddress.setText(cameraInformations.getAddress());
        }
    }
}
