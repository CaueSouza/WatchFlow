package com.example.watchflow.userInformation;

import static com.example.watchflow.Constants.MARKER_TITLE;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.example.watchflow.R;
import com.example.watchflow.databinding.ActivityUserInformationBinding;

public class UserInformationActivity extends AppCompatActivity {
    ActivityUserInformationBinding binding;
    UserInformationViewModel viewModel;

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
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
