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
import com.example.watchflow.databinding.FragmentRegisterCamBinding;

public class RegisterCamFragment extends Fragment {
    OperationViewModel viewModel;
    FragmentRegisterCamBinding binding;
    private static final String IPV4_PATTERN =
            "^(([0-9]|[1-9][0-9]|1[0-9][0-9]|2[0-4][0-9]|25[0-5])\\.){3}" +
                    "([0-9]|[1-9][0-9]|1[0-9][0-9]|2[0-4][0-9]|25[0-5])$";

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

            if (validateFields()) {
                Toast.makeText(getContext(), R.string.missing_fields_login, Toast.LENGTH_SHORT).show();
            }
            else if (!IPv4ValidatorRegex.isValid(viewModel.getCameraInfo().getValue().getIP())){
                Toast.makeText(getContext(), R.string.incorrect_ip_message_txt, Toast.LENGTH_SHORT).show();
            }
            else {
                viewModel.registerCam();
            }
        });
    }

    private boolean validateFields(){
        CameraAddressPOJO cameraAddress = viewModel.getCameraInfo().getValue();
        return cameraAddress == null &&
                cameraAddress.getIP() != null && !cameraAddress.getIP().isEmpty() &&
                cameraAddress.getStreet() != null && !cameraAddress.getStreet().isEmpty() &&
                cameraAddress.getNumber() != null && !cameraAddress.getNumber().isEmpty() &&
                cameraAddress.getNeighborhood() != null && !cameraAddress.getNeighborhood().isEmpty() &&
                cameraAddress.getCity() != null && !cameraAddress.getCity().isEmpty() &&
                cameraAddress.getCountry() != null && !cameraAddress.getCountry().isEmpty();
    }
}
