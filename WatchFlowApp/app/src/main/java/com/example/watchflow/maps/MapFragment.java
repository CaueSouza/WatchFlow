package com.example.watchflow.maps;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.watchflow.CameraInformations;
import com.example.watchflow.R;
import com.example.watchflow.UserInformations;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import static com.example.watchflow.Constants.AUTO_REFRESH_SECONDS;

public class MapFragment extends Fragment implements OnMapReadyCallback {

    private static final String TAG = MapFragment.class.getSimpleName();
    MapsViewModel viewModel;
    private int mInterval = AUTO_REFRESH_SECONDS * 1000; // Seconds * 1000
    private Handler mHandler;
    private GoogleMap mMap;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_maps, container, false);

        SupportMapFragment supportMapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);

        supportMapFragment.getMapAsync(this);

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
    public void onMapReady(@NotNull GoogleMap googleMap) {
        mMap = googleMap;

        mHandler = new Handler(Looper.myLooper());
        startRepeatingTask();

        initBindings();

        requestMapsInformation();
        moveToUserPosition();

        mMap.setOnMarkerClickListener(marker -> {
            String markerTitle = marker.getTitle();

            if (markerTitle != null && !markerTitle.equals(getString(R.string.your_position)) &&
                    marker.getTag() != null && ((int) marker.getTag()) != 1) {
                viewModel.getCameraInformation(markerTitle);
            }

            return false;
        });
    }

    private void initBindings() {
        viewModel.getAllCameras().observe(this, this::updateMapsCamerasMarkers);
        viewModel.getAllUsers().observe(this, this::updateMapsUsersMarkers);

        viewModel.getRefreshEvent().observe(this, v -> requestMapsInformation());
    }

    private void requestMapsInformation() {
        mMap.clear();

        setUserPosition();
        viewModel.updateAllRunningCameras();
        viewModel.updateAllLoggedUsers();
    }

    private void setUserPosition() {
        LatLng location = new LatLng(viewModel.getGpsTracker().getLatitude(), viewModel.getGpsTracker().getLongitude());
        mMap.addMarker(new MarkerOptions().position(location).title(getString(R.string.your_position)));
    }

    private void moveToUserPosition() {
        LatLng location = new LatLng(viewModel.getGpsTracker().getLatitude(), viewModel.getGpsTracker().getLongitude());
        mMap.moveCamera(CameraUpdateFactory.newLatLng(location));
    }

    private void updateMapsCamerasMarkers(List<CameraInformations> cameras) {
        for (CameraInformations camera : cameras) {
            LatLng location = new LatLng(camera.getLatitude(), camera.getLongitude());
            mMap.addMarker(new MarkerOptions().position(location)
                    .icon(BitmapDescriptorFactory.fromBitmap(getBitmap(R.drawable.ic_camera_pin)))
                    .title(camera.getIp())).setTag(0);
        }
    }

    private void updateMapsUsersMarkers(List<UserInformations> users) {
        for (UserInformations user : users) {
            LatLng location = new LatLng(user.getLatitude(), user.getLongitude());
            mMap.addMarker(new MarkerOptions().position(location)
                    .icon(BitmapDescriptorFactory.fromBitmap(getBitmap(R.drawable.ic_car_pin)))
                    .title(user.getUserName())).setTag(1);
        }
    }

    private Bitmap getBitmap(int drawableRes) {
        Drawable drawable = getResources().getDrawable(drawableRes);
        Canvas canvas = new Canvas();
        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        canvas.setBitmap(bitmap);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        drawable.draw(canvas);

        return bitmap;
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
