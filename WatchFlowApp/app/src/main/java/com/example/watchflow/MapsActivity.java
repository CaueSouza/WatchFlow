package com.example.watchflow;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.example.watchflow.databinding.ActivityMapsBinding;

public class MapsActivity extends AppCompatActivity {

    MapsViewModel viewModel;
    ActivityMapsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_maps);
        binding.setLifecycleOwner(this);

        viewModel = new ViewModelProvider(this).get(MapsViewModel.class);

        MapFragment mapFragment = new MapFragment();

        getSupportActionBar().setTitle(R.string.app_name);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().show();

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.maps_layout, mapFragment)
                .commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.settings_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        UserIdPwd userIdPwd = UserIdPwd.getInstance();

        switch (item.getItemId()) {
            case R.id.refresh_item:
                viewModel.getRefreshEvent().call();
                return true;
            case R.id.add_user_item:
                if (userIdPwd.getAdm()) {
                    viewModel.getAddUserEvent().call();
                } else {
                    Toast.makeText(this, R.string.only_adm_message_txt, Toast.LENGTH_SHORT).show();
                }
                return true;
            case R.id.remove_user_item:
                if (userIdPwd.getAdm()) {
                    viewModel.getRemoveUserEvent().call();
                } else {
                    Toast.makeText(this, R.string.only_adm_message_txt, Toast.LENGTH_SHORT).show();
                }
                return true;
            case R.id.add_cam_item:
                if (userIdPwd.getAdm()) {
                    viewModel.getAddCamEvent().call();
                } else {
                    Toast.makeText(this, R.string.only_adm_message_txt, Toast.LENGTH_SHORT).show();
                }
                return true;
            case R.id.remove_cam_item:
                if (userIdPwd.getAdm()) {
                    viewModel.getRemoveCamEvent().call();
                } else {
                    Toast.makeText(this, R.string.only_adm_message_txt, Toast.LENGTH_SHORT).show();
                }
                return true;
            case R.id.logout_item:
                viewModel.getLogoutEvent().call();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {
        viewModel.logoutUser();
        super.onBackPressed();
    }
}