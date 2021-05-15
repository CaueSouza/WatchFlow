package com.example.watchflow;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;

import com.example.watchflow.retrofit.WatchFlowServerApiInterface;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

import static com.example.watchflow.Constants.AUTO_REFRESH_SECONDS;

public class MapFragment extends Fragment implements OnMapReadyCallback {

    private static final String TAG = MapFragment.class.getSimpleName();
    MapsViewModel viewModel;
    private int mInterval = AUTO_REFRESH_SECONDS * 1000; // Seconds * 1000
    private Handler mHandler;
    private GoogleMap mMap;
    private int value = 0;
    private WatchFlowServerApiInterface watchFlowServerApiInterface;
    private MutableLiveData<CameraInformations> cameraInformationsLiveData = new MutableLiveData<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_maps, container, false);

        SupportMapFragment supportMapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);

        supportMapFragment.getMapAsync(this::onMapReady);

        viewModel = new ViewModelProvider(requireActivity()).get(MapsViewModel.class);

        return view;
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mHandler = new Handler(Looper.myLooper());
        startRepeatingTask();

        initBindings();

        requestMapsInformation();
    }

    private void initBindings() {
        viewModel.getAllCameras().observe(this, this::updateMapsCamerasMarkers);
        viewModel.getAllUsers().observe(this, this::updateMapsUsersMarkers);

        viewModel.getLogoutEvent().observe(this, v-> viewModel.logoutUser());
        viewModel.getRefreshEvent().observe(this, v -> requestMapsInformation());
    }

    private void requestMapsInformation() {
        mMap.clear();

        setUserPosition();
        viewModel.updateAllRunningCameras();
        viewModel.updateAllLoggedUsers();
    }

    private void setUserPosition(){
        LatLng location = new LatLng(viewModel.gpsTracker.getLatitude(), viewModel.gpsTracker.getLongitude());
        mMap.addMarker(new MarkerOptions().position(location).title("Sua posição"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(location));
    }

    private void updateMapsCamerasMarkers(List<CameraInformations> cameras) {
        for (CameraInformations camera : cameras) {
            LatLng location = new LatLng(camera.getLatitude(), camera.getLongitude());
            mMap.addMarker(new MarkerOptions().position(location).title(camera.getIp()));
        }
    }

    private void updateMapsUsersMarkers(List<UserInformations> users) {
        for (UserInformations user : users) {
            LatLng location = new LatLng(user.getLatitude(), user.getLongitude());
            mMap.addMarker(new MarkerOptions().position(location).title(user.getUserName()));
        }
    }

    Runnable mRequestRepeater = new Runnable() {
        @Override
        public void run() {
            try {
                viewModel.getRefreshEvent().call();
            } finally {
                mHandler.postDelayed(mRequestRepeater, mInterval);
            }
        }
    };

    void startRepeatingTask() {
        mRequestRepeater.run();
    }

    void stopRepeatingTask() {
        mHandler.removeCallbacks(mRequestRepeater);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopRepeatingTask();
    }
}
