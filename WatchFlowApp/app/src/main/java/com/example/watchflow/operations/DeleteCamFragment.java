package com.example.watchflow.operations;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.watchflow.IPv4ValidatorRegex;
import com.example.watchflow.R;
import com.example.watchflow.databinding.FragmentDeleteCamBinding;

public class DeleteCamFragment extends Fragment {
    OperationViewModel viewModel;
    FragmentDeleteCamBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_delete_cam, container, false);

        viewModel = new ViewModelProvider(requireActivity()).get(OperationViewModel.class);
        binding.setViewModel(viewModel);

        initBindings();
        return binding.getRoot();
    }

    private void initBindings() {
        binding.button.setOnClickListener(v -> {
            if (viewModel.getCameraInfo().getValue().getIP() == null ||
                    viewModel.getCameraInfo().getValue().getIP().isEmpty()) {
                Toast.makeText(getContext(), R.string.missing_fields_login, Toast.LENGTH_SHORT).show();
            }
            else if (!IPv4ValidatorRegex.isValid(viewModel.getCameraInfo().getValue().getIP())){
                Toast.makeText(getContext(), R.string.incorrect_ip_message_txt, Toast.LENGTH_SHORT).show();
            }
            else {
                viewModel.deleteCam();
            }
        });
    }
}
