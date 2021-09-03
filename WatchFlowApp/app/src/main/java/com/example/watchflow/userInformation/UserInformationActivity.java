package com.example.watchflow.userInformation;

import static com.example.watchflow.Constants.MARKER_TITLE;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.example.watchflow.R;
import com.example.watchflow.databinding.ActivityUserInformationBinding;

public class UserInformationActivity extends AppCompatActivity {
    ActivityUserInformationBinding binding;
    UserInformationViewModel viewModel;
    public static final int REQUEST_CALL_PHONE_CODE = 1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_user_information);
        binding.setLifecycleOwner(this);

        viewModel = new ViewModelProvider(this).get(UserInformationViewModel.class);
        binding.setViewModel(viewModel);

        getSupportActionBar().setTitle(R.string.title_activity_user_information);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().show();

        String userName = getIntent().getStringExtra(MARKER_TITLE);

        viewModel.getUserName().setValue(userName);
        viewModel.getUserInformation(getIntent().getStringExtra(MARKER_TITLE));

        binding.callButton.setOnClickListener(v -> {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                if (!ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CALL_PHONE)) {
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, REQUEST_CALL_PHONE_CODE);
                }
            } else {
                requestCallPermission();
            }
        });
    }

    private void requestCallPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CALL_PHONE)) {
            new AlertDialog.Builder(this)
                    .setTitle(R.string.permission_needed_txt)
                    .setMessage(R.string.permission_needed_message_txt)
                    .setPositiveButton(R.string.OK, (dialog, which) -> ActivityCompat.requestPermissions(UserInformationActivity.this, new String[]{Manifest.permission.CALL_PHONE}, REQUEST_CALL_PHONE_CODE))
                    .setNegativeButton(R.string.Cancel, (dialog, which) -> dialog.dismiss()).create().show();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, REQUEST_CALL_PHONE_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CALL_PHONE_CODE) {

            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:" + viewModel.getUserPhone().getValue()));
                startActivity(callIntent);
            } else {
                requestCallPermission();
            }
        }

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
