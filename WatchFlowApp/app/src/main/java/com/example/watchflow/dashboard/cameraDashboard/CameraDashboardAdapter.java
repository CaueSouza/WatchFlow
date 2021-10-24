package com.example.watchflow.dashboard.cameraDashboard;

import static com.example.watchflow.Constants.RETRIEVE_HIGHEST_TIMESTAMP;
import static com.example.watchflow.Constants.RETRIEVE_HIGHEST_TOTAL;
import static com.example.watchflow.Constants.RETRIEVE_MINOR_TIMESTAMP;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.watchflow.R;
import com.example.watchflow.dashboard.CameraHistoric;
import com.example.watchflow.dashboard.GraphCameraData;
import com.example.watchflow.dashboard.ReconForTimestamp;
import com.example.watchflow.dashboard.SpecificReconForTimestamp;
import com.jjoe64.graphview.DefaultLabelFormatter;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

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
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM HH:mm");

        public DataHolder(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.graphic_title);
            graphView = itemView.findViewById(R.id.graph_view);
        }

        void setDetails(CameraDashboardGraphicsData graphicsData) {
            title.setText(graphicsData.getTitle());

            graphView.getGridLabelRenderer().setLabelFormatter(new DefaultLabelFormatter() {
                @Override
                public String formatLabel(double value, boolean isValueX) {
                    return isValueX ? sdf.format(new Date((long) value * 1000)) : super.formatLabel(value, false);
                }
            });

            graphView.getViewport().setYAxisBoundsManual(true);
            graphView.getViewport().setMinY(0);
            graphView.getViewport().setMaxY(graphicsData.getTotal());

            graphView.getViewport().setXAxisBoundsManual(true);
            graphView.getViewport().setMinX(getSpecificRecordAtHistoric(RETRIEVE_MINOR_TIMESTAMP, graphicsData.getReconForTimestamps()));
            graphView.getViewport().setMaxX(getSpecificRecordAtHistoric(RETRIEVE_HIGHEST_TIMESTAMP, graphicsData.getReconForTimestamps()));

            graphView.getViewport().setScalable(true);
            graphView.getViewport().setScalableY(true);

            graphView.getGridLabelRenderer().setHorizontalLabelsAngle(10);

            for (int i = 0; i < graphicsData.getReconForTimestamps().size(); i++) {
                LineGraphSeries<DataPoint> series = new LineGraphSeries<>(getTotalData(graphicsData.getReconForTimestamps()));
                graphView.addSeries(series);
            }
        }

        int getSpecificRecordAtHistoric(int type, ArrayList<SpecificReconForTimestamp> allRecons) {
            int value;

            if (type == RETRIEVE_MINOR_TIMESTAMP) value = (int) (new Date().getTime() / 1000);
            else value = 0;

            switch (type) {
                case RETRIEVE_HIGHEST_TOTAL:
                    for (SpecificReconForTimestamp specificReconForTimestamp : allRecons) {
                        value = Math.max(specificReconForTimestamp.getData(), value);
                    }
                    break;
                case RETRIEVE_HIGHEST_TIMESTAMP:
                    for (SpecificReconForTimestamp specificReconForTimestamp : allRecons) {
                        value = Math.max(specificReconForTimestamp.getTimestamp(), value);
                    }
                    break;
                case RETRIEVE_MINOR_TIMESTAMP:
                    for (SpecificReconForTimestamp specificReconForTimestamp : allRecons) {
                        value = Math.min(specificReconForTimestamp.getTimestamp(), value);
                    }
                    break;
            }

            return value;
        }

        private DataPoint[] getTotalData(ArrayList<SpecificReconForTimestamp> allSpecificReconForTimestamp) {
            ArrayList<Integer> x_axis = new ArrayList<>();
            ArrayList<Integer> y_axis = new ArrayList<>();

            for (SpecificReconForTimestamp reconForTimestamp : allSpecificReconForTimestamp) {
                x_axis.add(reconForTimestamp.getTimestamp());
                y_axis.add(reconForTimestamp.getData());
            }

            int n = x_axis.size();
            DataPoint[] values = new DataPoint[n];
            for (int i = 0; i < n; i++) {
                DataPoint v = new DataPoint(x_axis.get(i), y_axis.get(i));
                values[i] = v;
            }

            return values;
        }
    }
}
