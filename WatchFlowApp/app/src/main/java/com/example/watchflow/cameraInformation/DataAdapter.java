package com.example.watchflow.cameraInformation;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.watchflow.R;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class DataAdapter extends RecyclerView.Adapter<DataAdapter.DataHolder> {

    private final Context context;
    private final ArrayList<Data> datas;

    public DataAdapter(Context context, ArrayList<Data> datas) {
        this.context = context;
        this.datas = datas;
    }

    @NonNull
    @NotNull
    @Override
    public DataAdapter.DataHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.data_layout_item, parent, false);

        return new DataHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull DataAdapter.DataHolder holder, int position) {
        Data data = datas.get(position);
        holder.setDetails(data);
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    static class DataHolder extends RecyclerView.ViewHolder {
        private final TextView txtTitle, txtContent;

        public DataHolder(@NonNull @NotNull View itemView) {
            super(itemView);

            txtTitle = itemView.findViewById(R.id.data_item_title);
            txtContent = itemView.findViewById(R.id.data_item_content);
        }

        void setDetails(Data data) {
            txtTitle.setText(data.getTitle());
            txtContent.setText(data.getContent());
        }
    }
}
