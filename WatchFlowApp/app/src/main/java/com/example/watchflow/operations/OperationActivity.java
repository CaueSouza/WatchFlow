package com.example.watchflow.operations;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.example.watchflow.R;
import com.example.watchflow.databinding.ActivityOperationBinding;

import static com.example.watchflow.Constants.CREATE_CAM_OPERATION;
import static com.example.watchflow.Constants.CREATE_USER_OPERATION;
import static com.example.watchflow.Constants.DELETE_CAM_OPERATION;
import static com.example.watchflow.Constants.DELETE_USER_OPERATION;
import static com.example.watchflow.Constants.OPERATION;

public class OperationActivity extends AppCompatActivity {

    ActivityOperationBinding binding;
    OperationViewModel viewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_operation);
        binding.setLifecycleOwner(this);

        viewModel = new ViewModelProvider(this).get(OperationViewModel.class);

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
            default:
                finish();
        }

        viewModel.getEndActivityEvent().observe(this, v -> finish());
    }
}
