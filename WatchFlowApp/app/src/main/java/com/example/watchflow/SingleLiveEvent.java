package com.example.watchflow;

import android.os.SystemClock;

import androidx.annotation.MainThread;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import java.util.concurrent.atomic.AtomicBoolean;

public class SingleLiveEvent<T> extends MutableLiveData<T> {
    private final AtomicBoolean pending = new AtomicBoolean(false);
    protected Integer minimumIntervalToRequest = 1000;
    private Long lastEvent = 0L;

    public SingleLiveEvent() {
    }

    public SingleLiveEvent(int minimumIntervalToRequest) {
        this.minimumIntervalToRequest = minimumIntervalToRequest;
    }

    @MainThread
    @Override
    public void observe(@NonNull LifecycleOwner owner, @NonNull Observer<? super T> observer) {
        super.observe(owner, v -> {

            if (SystemClock.elapsedRealtime() - lastEvent < minimumIntervalToRequest) {
                pending.set(false);
                return;
            }

            setLastEvent();
            if (pending.compareAndSet(true, false)) {
                observer.onChanged(v);
            }
        });
    }

    @MainThread
    public void setValue(@Nullable T value) {
        pending.set(true);
        super.setValue(value);
    }

    protected void setLastEvent() {
        lastEvent = SystemClock.elapsedRealtime();
    }

    public void call() {
        setValue(null);
    }

    public void backgroundCall() {
        postValue(null);
    }
}
