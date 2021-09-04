package com.example.watchflow.dashboard;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.watchflow.R;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class GraphCameraDataSubtitleAdapter extends RecyclerView.Adapter<GraphCameraDataSubtitleAdapter.DataHolder> {

    private final Context context;
    private final ArrayList<GraphCameraData> cameraData;

    public GraphCameraDataSubtitleAdapter(Context context, ArrayList<GraphCameraData> cameraData) {
        this.context = context;
        this.cameraData = cameraData;
    }

    @NonNull
    @NotNull
    @Override
    public GraphCameraDataSubtitleAdapter.DataHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.dashboard_cam_data_layout_item, parent, false);

        return new GraphCameraDataSubtitleAdapter.DataHolder(view, context);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull GraphCameraDataSubtitleAdapter.DataHolder holder, int position) {
        GraphCameraData cameraInformations = cameraData.get(position);
        holder.setDetails(cameraInformations);
    }

    @Override
    public int getItemCount() {
        return cameraData.size();
    }

    static class DataHolder extends RecyclerView.ViewHolder {
        private final TextView txtIP, txtAddress;
        private final ImageView colorBox;
        private final Context context;

        public DataHolder(@NonNull @NotNull View itemView, Context context) {
            super(itemView);

            this.context = context;
            colorBox = itemView.findViewById(R.id.dashboard_item_color);
            txtIP = itemView.findViewById(R.id.dashboard_ip_item_content);
            txtAddress = itemView.findViewById(R.id.dashboard_address_item_content);
            itemView.setOnClickListener(view -> {
                //TODO CALL INTENT FOR ACTIVITY THAT SHOWS THE SPECIFIC DATA FOR THE CAMERA
                //TODO STORAGE THE HISTORIC
                Toast.makeText(context, "Call Activity", Toast.LENGTH_SHORT).show();
            });
        }

        void setDetails(GraphCameraData cameraInformations) {
            colorBox.setBackgroundColor(cameraInformations.getColor());
            txtIP.setText(cameraInformations.getIp());
            txtAddress.setText(cameraInformations.getAddress());
        }
    }
}
