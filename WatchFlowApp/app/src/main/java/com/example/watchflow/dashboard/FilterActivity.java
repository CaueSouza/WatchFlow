package com.example.watchflow.dashboard;

import static com.example.watchflow.Constants.FINAL_TIMESTAMP;
import static com.example.watchflow.Constants.FIVE_MINUTES_IN_MILLIS;
import static com.example.watchflow.Constants.INITIAL_TIMESTAMP;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
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
    FilterViewModel viewModel;
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yy HH:mm");
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_filter);
        binding.setLifecycleOwner(this);

        viewModel = new ViewModelProvider(this).get(FilterViewModel.class);

        getSupportActionBar().setTitle(R.string.title_activity_filter);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().show();

        sharedPreferences = getBaseContext().getSharedPreferences(getString(R.string.shared_pref_dashboard_key), Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        initBindings();
    }

    private void initBindings() {
        binding.startDateTimeButton.setOnClickListener(v -> showDateTimeDialog(binding.startDateTimeEditText, true));
        binding.finalDateTimeButton.setOnClickListener(v -> showDateTimeDialog(binding.finalDateTimeEditText, false));

        binding.finishButton.setOnClickListener(v -> saveDataAndFinish());


        long initialTimestamp = sharedPreferences.getLong(INITIAL_TIMESTAMP, 0);
        long finalTimestamp = sharedPreferences.getLong(FINAL_TIMESTAMP, 0);

        viewModel.getStartDateTimeFilterTimestamp().setValue(initialTimestamp);
        viewModel.getFinalDateTimeFilterTimestamp().setValue(finalTimestamp);

        if (initialTimestamp != 0 && finalTimestamp != 0) {
            binding.startDateTimeEditText.setText(simpleDateFormat.format(initialTimestamp * 1000));
            binding.finalDateTimeEditText.setText(simpleDateFormat.format(finalTimestamp * 1000));
        }
    }

    private void saveDataAndFinish() {
        Long initialTimestamp = viewModel.getStartDateTimeFilterTimestamp().getValue();
        Long finalTimestamp = viewModel.getFinalDateTimeFilterTimestamp().getValue();

        if (initialTimestamp == null || finalTimestamp == null || initialTimestamp == 0 || finalTimestamp == 0) {
            Toast.makeText(this, "Selecione ambas as datas", Toast.LENGTH_SHORT).show();
            return;
        } else if (initialTimestamp > finalTimestamp) {
            Toast.makeText(this, "Data final anterior a inicial", Toast.LENGTH_SHORT).show();
            return;
        }

        editor.putLong(INITIAL_TIMESTAMP, initialTimestamp);
        editor.putLong(FINAL_TIMESTAMP, finalTimestamp);
        editor.commit();
        editor.apply();
        setResult(RESULT_OK);
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

                editText.setText(simpleDateFormat.format(calendar.getTime()));

                if (isStartDateTime) {
                    viewModel.getStartDateTimeFilterTimestamp().setValue(calendar.getTimeInMillis() / 1000);
                } else {
                    viewModel.getFinalDateTimeFilterTimestamp().setValue(calendar.getTimeInMillis() / 1000);
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
