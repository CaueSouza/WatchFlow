package com.example.watchflow;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.watchflow.databinding.FragmentFabsBinding;

public class FabsFragment extends Fragment {
    MapsViewModel viewModel;
    FragmentFabsBinding binding;
    Boolean isAllFabsVisible;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_fabs, container, false);
        View view = binding.getRoot();

        viewModel = new ViewModelProvider(requireActivity()).get(MapsViewModel.class);

        initBindings();

        return view;
    }

    private void initBindings() {
        binding.reloadFab.setVisibility(View.GONE);
        binding.reloadActionText.setVisibility(View.GONE);
        binding.addUserFab.setVisibility(View.GONE);
        binding.addUserActionText.setVisibility(View.GONE);
        binding.removeUserFab.setVisibility(View.GONE);
        binding.removeUserActionText.setVisibility(View.GONE);
        binding.addCamFab.setVisibility(View.GONE);
        binding.addCamActionText.setVisibility(View.GONE);
        binding.logoutFab.setVisibility(View.GONE);
        binding.logoutActionText.setVisibility(View.GONE);

        isAllFabsVisible = false;

        binding.mainFab.shrink();

        binding.mainFab.setOnClickListener(v -> {
            if (!isAllFabsVisible) {

                binding.reloadFab.show();
                binding.addUserFab.show();
                binding.removeUserFab.show();
                binding.addCamFab.show();
                binding.logoutFab.show();
                binding.reloadActionText.setVisibility(View.VISIBLE);
                binding.addUserActionText.setVisibility(View.VISIBLE);
                binding.removeUserActionText.setVisibility(View.VISIBLE);
                binding.addCamActionText.setVisibility(View.VISIBLE);
                binding.logoutActionText.setVisibility(View.VISIBLE);
                binding.mainFab.extend();
                isAllFabsVisible = true;
            } else {
                binding.reloadFab.hide();
                binding.addUserFab.hide();
                binding.removeUserFab.hide();
                binding.addCamFab.hide();
                binding.logoutFab.hide();
                binding.reloadActionText.setVisibility(View.GONE);
                binding.addUserActionText.setVisibility(View.GONE);
                binding.removeUserActionText.setVisibility(View.GONE);
                binding.addCamActionText.setVisibility(View.GONE);
                binding.logoutActionText.setVisibility(View.GONE);
                binding.mainFab.shrink();
                isAllFabsVisible = false;
            }
        });

        binding.reloadFab.setOnClickListener(v -> {
            Toast.makeText(getContext(), "Reloading", Toast.LENGTH_SHORT).show();
        });

        binding.addUserFab.setOnClickListener(v -> {
            Toast.makeText(getContext(), "Add User", Toast.LENGTH_SHORT).show();
        });

        binding.removeUserFab.setOnClickListener(v -> {
            Toast.makeText(getContext(), "Remove User", Toast.LENGTH_SHORT).show();
        });

        binding.addCamFab.setOnClickListener(v -> {
            Toast.makeText(getContext(), "Add Cam", Toast.LENGTH_SHORT).show();
        });

        binding.logoutFab.setOnClickListener(v -> {
            Toast.makeText(getContext(), "Logout", Toast.LENGTH_SHORT).show();
        });
    }
}
