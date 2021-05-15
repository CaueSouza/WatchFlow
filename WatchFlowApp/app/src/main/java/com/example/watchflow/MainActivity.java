package com.example.watchflow;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.example.watchflow.databinding.ActivityMainBinding;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.watchflow.BuildConfig.APPLICATION_ID;
import static com.example.watchflow.Constants.PASSWORD;
import static com.example.watchflow.Constants.PASSWORD_KEY;
import static com.example.watchflow.Constants.USER_ID;
import static com.example.watchflow.Constants.USER_ID_KEY;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = MainActivity.class.getSimpleName();
    MainActivityViewModel viewModel;
    ActivityMainBinding binding;
    private SharedPreferences mPreferences;
    private SharedPreferences.Editor mEditor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        binding.setLifecycleOwner(this);

        viewModel = new ViewModelProvider(this).get(MainActivityViewModel.class);
        binding.setViewModel(viewModel);

        mPreferences = getSharedPreferences(APPLICATION_ID, Context.MODE_PRIVATE);
        mEditor = mPreferences.edit();

        try {
            if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 101);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        initBindings();
    }

    private void initBindings() {
        binding.loginButton.setOnClickListener(v -> {

            if (viewModel.getUserName().getValue() == null || viewModel.getUserName().getValue().isEmpty() ||
                    viewModel.getPassword().getValue() == null || viewModel.getPassword().getValue().isEmpty()) {
                Toast.makeText(this, R.string.missing_fields_login, Toast.LENGTH_SHORT).show();
            } else {
                viewModel.loginUser(new Callback<JsonObject>() {
                    @Override
                    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                        if (!response.isSuccessful()) {
                            Toast.makeText(getApplicationContext(), R.string.fail_message_login, Toast.LENGTH_SHORT).show();
                            return;
                        }

                        mEditor.putString(USER_ID_KEY, response.body().get(USER_ID).getAsString());
                        mEditor.putString(PASSWORD_KEY, response.body().get(PASSWORD).getAsString());
                        mEditor.apply();

                        Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
                        startActivity(intent);

                        Log.d(TAG, "onResponse: " + response);
                    }

                    @Override
                    public void onFailure(Call<JsonObject> call, Throwable t) {
                        Log.e(TAG, "onFailure: " + t);
                        Toast.makeText(getApplicationContext(), R.string.invalid_user_message, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

}