package com.example.watchflow.dashboard.cameraDashboard;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.watchflow.R;
import com.jjoe64.graphview.GraphView;

import java.util.ArrayList;

public class CameraDashboardAdapter extends RecyclerView.Adapter<CameraDashboardAdapter.DataHolder> {

    private final Context context;
    private final ArrayList<CameraDashboardGraphicsData> allGraphicsData;

    public CameraDashboardAdapter(Context context, ArrayList<CameraDashboardGraphicsData> graphicsData) {
        this.context = context;
        this.allGraphicsData = graphicsData;
    }

    @NonNull
    @Override
    public DataHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.camera_dashboard_graphic_layout_item, parent, false);

        return new DataHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DataHolder holder, int position) {
        CameraDashboardGraphicsData graphicsData = allGraphicsData.get(position);
        holder.setDetails(graphicsData);
    }

    @Override
    public int getItemCount() {
        return allGraphicsData.size();
    }

    static class DataHolder extends RecyclerView.ViewHolder {
        private final TextView title;
        private final GraphView graphView;

        public DataHolder(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.graphic_title);
            graphView = itemView.findViewById(R.id.graph_view);
        }

        void setDetails(CameraDashboardGraphicsData graphicsData) {
            title.setText(graphicsData.getTitle());
            //TODO POPULATE THE GRAPH
        }
    }
}
