package com.example.watchflow;

import android.content.Intent;
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

import static com.example.watchflow.Constants.USER_ID;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = MainActivity.class.getSimpleName();
    MainActivityViewModel viewModel;
    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        binding.setLifecycleOwner(this);

        viewModel = new ViewModelProvider(this).get(MainActivityViewModel.class);
        binding.setViewModel(viewModel);

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
                Toast.makeText(this, "Preencha corretamente", Toast.LENGTH_SHORT).show();
            } else {
                viewModel.loginUser(new Callback<JsonObject>() {
                    @Override
                    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                        if (!response.isSuccessful()) {
                            Toast.makeText(getApplicationContext(), "Nao foi possivel efetuar o login", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        UserId.getInstance().setUserId(response.body().get(USER_ID).getAsString());

                        Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
                        startActivity(intent);

                        Log.d(TAG, "onResponse: " + response);
                    }

                    @Override
                    public void onFailure(Call<JsonObject> call, Throwable t) {
                        Log.e(TAG, "onFailure: " + t);
                        Toast.makeText(getApplicationContext(), "Usuario Invalido", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

}