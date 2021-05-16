package com.example.watchflow.operations;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.watchflow.R;
import com.example.watchflow.databinding.FragmentRegisterCamBinding;

public class RegisterCamFragment extends Fragment {
    OperationViewModel viewModel;
    FragmentRegisterCamBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_register_cam, container, false);

        viewModel = new ViewModelProvider(requireActivity()).get(OperationViewModel.class);
        binding.setViewModel(viewModel);

        initBindings();
        return binding.getRoot();
    }

    private void initBindings() {
        binding.button.setOnClickListener(v -> {
            if (viewModel.getNewUserName().getValue() == null || viewModel.getNewUserName().getValue().isEmpty() ||
                    viewModel.getNewPassword().getValue() == null || viewModel.getNewPassword().getValue().isEmpty()) {
                Toast.makeText(getContext(), R.string.missing_fields_login, Toast.LENGTH_SHORT).show();
            } else {
                viewModel.registerCam();
            }
        });
    }
}
