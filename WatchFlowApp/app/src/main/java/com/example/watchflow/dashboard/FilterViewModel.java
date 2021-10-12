package com.example.watchflow.dashboard;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class FilterViewModel extends AndroidViewModel {
    public static final String TAG = FilterViewModel.class.getSimpleName();
    private final MutableLiveData<Long> startDateTimeFilterTimestamp = new MutableLiveData<>();
    private final MutableLiveData<Long> finalDateTimeFilterTimestamp = new MutableLiveData<>();

    public FilterViewModel(@NonNull Application application) {
        super(application);
    }

    public MutableLiveData<Long> getStartDateTimeFilterTimestamp() {
        return startDateTimeFilterTimestamp;
    }

    public MutableLiveData<Long> getFinalDateTimeFilterTimestamp() {
        return finalDateTimeFilterTimestamp;
    }
}
