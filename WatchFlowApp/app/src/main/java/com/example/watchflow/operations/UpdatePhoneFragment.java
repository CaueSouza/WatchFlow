package com.example.watchflow.operations;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.watchflow.Mask;
import com.example.watchflow.R;
import com.example.watchflow.databinding.FragmentUpdatePhoneBinding;

public class UpdatePhoneFragment extends Fragment {
    OperationViewModel viewModel;
    FragmentUpdatePhoneBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_update_phone, container, false);

        viewModel = new ViewModelProvider(requireActivity()).get(OperationViewModel.class);
        binding.setViewModel(viewModel);

        initBindings();
        return binding.getRoot();
    }

    private void initBindings() {
        binding.editTextPhone.addTextChangedListener(Mask.insert("(##)#####-####", binding.editTextPhone));

        binding.button.setOnClickListener(v -> {
            if (viewModel.getNewTelephone().getValue() == null || Mask.unmask(viewModel.getNewTelephone().getValue()).length() != 11) {
                Toast.makeText(getContext(), R.string.missing_fields_login, Toast.LENGTH_SHORT).show();
            } else {
                viewModel.updatePhone();
            }
        });
    }
}
