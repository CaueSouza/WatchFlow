package com.example.watchflow;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.example.watchflow.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = MainActivity.class.getSimpleName();
    private static final int REQUEST_ACCESS_FINE_CODE = 1;
    MainActivityViewModel viewModel;
    ActivityMainBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        binding.setLifecycleOwner(this);

        viewModel = new ViewModelProvider(this).get(MainActivityViewModel.class);
        binding.setViewModel(viewModel);

        initBindings();
    }

    private void initBindings() {
        binding.loginButton.setOnClickListener(v -> {

            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                if (!ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_ACCESS_FINE_CODE);
                } else {
                    if (viewModel.getUserName().getValue() == null || viewModel.getUserName().getValue().isEmpty() ||
                            viewModel.getPassword().getValue() == null || viewModel.getPassword().getValue().isEmpty()) {
                        Toast.makeText(this, R.string.missing_fields_login, Toast.LENGTH_SHORT).show();
                    } else {
                        viewModel.loginUser();
                    }
                }
            } else {
                requestAllPermissions();
            }
        });
    }

    private void requestAllPermissions() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
            new AlertDialog.Builder(this)
                    .setTitle(R.string.permission_needed_txt)
                    .setMessage(R.string.permission_needed_message_txt)
                    .setPositiveButton(R.string.OK, (dialog, which) -> ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_ACCESS_FINE_CODE))
                    .setNegativeButton(R.string.Cancel, (dialog, which) -> dialog.dismiss()).create().show();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_ACCESS_FINE_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_ACCESS_FINE_CODE) {

            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (viewModel.getUserName().getValue() == null || viewModel.getUserName().getValue().isEmpty() ||
                        viewModel.getPassword().getValue() == null || viewModel.getPassword().getValue().isEmpty()) {
                    Toast.makeText(this, R.string.missing_fields_login, Toast.LENGTH_SHORT).show();
                } else {
                    viewModel.loginUser();
                }
            } else {
                requestAllPermissions();
            }
        }

    }
}