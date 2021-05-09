package com.example.watchflow;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;

import com.example.watchflow.retrofit.WatchFlowServerApiInterface;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.example.watchflow.Constants.BASE_URL;

public class MapFragment extends Fragment implements OnMapReadyCallback {

    private static final String TAG = MapFragment.class.getSimpleName();
    MapsViewModel viewModel;
    private int mInterval = 10000; // 10 seconds by default, can be changed later - 5 min = 300000
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

        // Add a marker in current location and move the camera
        LatLng location = new LatLng(viewModel.currentLatitude, viewModel.currentLongitude);
        mMap.addMarker(new MarkerOptions().position(location).title("Your position"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(location));

        mHandler = new Handler(Looper.myLooper());
        startRepeatingTask();

        requestMapsInformation();
    }

    public void requestMapsInformation() {

        viewModel.getAllCameras().observe(this, this::updateMapsCamerasMarkers);

        viewModel.getAllRunningCameras(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(getContext(), "Nao foi possivel receber as cameras", Toast.LENGTH_SHORT).show();
                    return;
                }

                List<CameraInformations> allCameraInformations = new ArrayList<>();
                JsonArray jsonArray = (JsonArray) (response.body().get("cameras"));

                for (JsonElement cameraJson: jsonArray) {
                    JsonObject object = cameraJson.getAsJsonObject();
                    allCameraInformations.add(
                            new CameraInformations(
                                    object.get("ip").getAsString(),
                                    object.get("latitude").getAsDouble(),
                                    object.get("longitude").getAsDouble()));
                }

                viewModel.setAllCameras(allCameraInformations);

                Log.d(TAG, "onResponse: " + response);
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.e(TAG, "onFailure: " + t);
            }
        });
    }

    public void updateMapsCamerasMarkers(List<CameraInformations> cameras){
        for (CameraInformations camera : cameras){
            LatLng location = new LatLng(camera.getLatitude(), camera.getLongitude());
            mMap.addMarker(new MarkerOptions().position(location).title(camera.getIp()));
        }

        Toast.makeText(getContext(), "Updated all camera markers", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopRepeatingTask();
    }

    Runnable mRequestRepeater = new Runnable() {
        @Override
        public void run() {
            try {
                Toast.makeText(getContext(), "Teste " + value, Toast.LENGTH_LONG).show();
                value++;
                //updateStatus(); //this function can change value of mInterval.
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
}
