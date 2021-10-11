package com.example.watchflow.dashboard;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.example.watchflow.R;
import com.example.watchflow.databinding.ActivityFilterBinding;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class FilterActivity extends AppCompatActivity {
    ActivityFilterBinding binding;
    DashboardViewModel viewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_filter);
        binding.setLifecycleOwner(this);

        viewModel = new ViewModelProvider(this).get(DashboardViewModel.class);

        getSupportActionBar().setTitle(R.string.title_activity_filter);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().show();

        initBindings();
    }

    private void initBindings() {
        binding.startDateTimeButton.setOnClickListener(v -> showDateTimeDialog(binding.startDateTimeEditText, true));
        binding.finalDateTimeButton.setOnClickListener(v -> showDateTimeDialog(binding.finalDateTimeEditText, false));

        binding.finishButton.setOnClickListener(v -> saveDataAndFinish());
    }

    private void saveDataAndFinish() {

        if (viewModel.getStartDateTimeFilterTimestamp().getValue() == null || viewModel.getFinalDateTimeFilterTimestamp().getValue() == null){
            Toast.makeText(this, "Selecione ambas as datas", Toast.LENGTH_SHORT).show();
            return;
        } else if (viewModel.getStartDateTimeFilterTimestamp().getValue() > viewModel.getFinalDateTimeFilterTimestamp().getValue()){
            Toast.makeText(this, "Data final anterior a final", Toast.LENGTH_SHORT).show();
            return;
        }

        finish();
    }

    private void showDateTimeDialog(EditText editText, Boolean isStartDateTime) {
        Calendar calendar = Calendar.getInstance();

        DatePickerDialog.OnDateSetListener dateSetListener = (datePicker, year, month, dayOfMonth) -> {
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, month);
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

            TimePickerDialog.OnTimeSetListener timeSetListener = (timePicker, hourOfDay, minute) -> {
                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                calendar.set(Calendar.MINUTE, minute);

                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yy HH:mm");
                editText.setText(simpleDateFormat.format(calendar.getTime()));

                if (isStartDateTime) {
                    viewModel.getStartDateTimeFilterTimestamp().setValue(calendar.getTimeInMillis());
                } else {
                    viewModel.getFinalDateTimeFilterTimestamp().setValue(calendar.getTimeInMillis());
                }
            };

            new TimePickerDialog(this, timeSetListener, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true).show();
        };

        new DatePickerDialog(this, dateSetListener, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
