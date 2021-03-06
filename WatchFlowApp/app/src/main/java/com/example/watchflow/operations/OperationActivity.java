package com.example.watchflow.operations;

import static com.example.watchflow.Constants.CREATE_CAM_OPERATION;
import static com.example.watchflow.Constants.CREATE_USER_OPERATION;
import static com.example.watchflow.Constants.DELETE_CAM_OPERATION;
import static com.example.watchflow.Constants.DELETE_USER_OPERATION;
import static com.example.watchflow.Constants.OPERATION;
import static com.example.watchflow.Constants.UPDATE_PHONE_OPERATION;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.example.watchflow.R;
import com.example.watchflow.databinding.ActivityOperationBinding;

public class OperationActivity extends AppCompatActivity {

    ActivityOperationBinding binding;
    OperationViewModel viewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_operation);
        binding.setLifecycleOwner(this);

        viewModel = new ViewModelProvider(this).get(OperationViewModel.class);

        getSupportActionBar().setTitle(R.string.title_activity_operations);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().show();

        int operation = getIntent().getIntExtra(OPERATION, 0);

        switch (operation) {
            case CREATE_USER_OPERATION:
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.operation_layout, new RegisterUserFragment())
                        .commit();
                break;
            case DELETE_USER_OPERATION:
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.operation_layout, new DeleteUserFragment())
                        .commit();
                break;
            case CREATE_CAM_OPERATION:
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.operation_layout, new RegisterCamFragment())
                        .commit();
                break;
            case DELETE_CAM_OPERATION:
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.operation_layout, new DeleteCamFragment())
                        .commit();
                break;
            case UPDATE_PHONE_OPERATION:
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.operation_layout, new UpdatePhoneFragment())
                        .commit();
                break;
            default:
                finish();
        }

        initBindings();
    }

    private void initBindings() {
        viewModel.getEndActivityEvent().observe(this, v -> finish());
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
