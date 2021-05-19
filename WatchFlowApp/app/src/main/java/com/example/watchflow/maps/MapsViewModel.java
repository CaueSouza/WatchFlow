package com.example.watchflow.maps;

import android.app.Application;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.watchflow.CameraInformations;
import com.example.watchflow.ImageUtil;
import com.example.watchflow.R;
import com.example.watchflow.SingleLiveEvent;
import com.example.watchflow.UserIdPwd;
import com.example.watchflow.UserInformations;
import com.example.watchflow.cameraInformation.CameraInformationActivity;
import com.example.watchflow.retrofit.ServerRepository;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ListIterator;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.watchflow.Constants.*;
import static com.example.watchflow.Constants.CAMERAS;
import static com.example.watchflow.Constants.CAMERA_INFORMATIONS_ENDPOINT;
import static com.example.watchflow.Constants.COMMON_HEADER_FIELDS;
import static com.example.watchflow.Constants.GET_INFO_OR_DELETE_CAM_HEADER_FIELDS;
import static com.example.watchflow.Constants.IP;
import static com.example.watchflow.Constants.LATITUDE;
import static com.example.watchflow.Constants.LOCATIONS;
import static com.example.watchflow.Constants.LONGITUDE;
import static com.example.watchflow.Constants.MESSAGE;
import static com.example.watchflow.Constants.USERNAME;
import static com.example.watchflow.Constants.USERS_POSITIONS_ENDPOINT;
import static com.example.watchflow.Constants.USER_LOGOUT_ENDPOINT;

public class MapsViewModel extends AndroidViewModel {

    private static final String TAG = MapsViewModel.class.getSimpleName();
    public GpsTracker gpsTracker;
    private final ServerRepository serverRepository = ServerRepository.getInstance();
    private final MutableLiveData<List<CameraInformations>> allCameras = new MutableLiveData<>();
    private final MutableLiveData<List<UserInformations>> allUsers = new MutableLiveData<>();
    private final SingleLiveEvent<Void> refreshEvent = new SingleLiveEvent<>();
    private final SingleLiveEvent<Void> endActivityEvent = new SingleLiveEvent<>();
    private final Application application;

    public MapsViewModel(@NonNull Application application) {
        super(application);
        this.application = application;
        gpsTracker = new GpsTracker(application.getApplicationContext());
    }

    public void updateAllRunningCameras() {
        serverRepository.createRequest(allRunningCamerasCallback, ALL_RUNNING_CAMERAS_ENDPOINT,
                COMMON_HEADER_FIELDS, UserIdPwd.getInstance().getUserIdPwdList(), true);
    }

    public void updateAllLoggedUsers() {
        serverRepository.createRequest(allLoggedUsersCallback, USERS_POSITIONS_ENDPOINT,
                COMMON_HEADER_FIELDS, UserIdPwd.getInstance().getUserIdPwdList(), true);
    }

    public void logoutUser() {
        serverRepository.createRequest(logoutUserCallback, USER_LOGOUT_ENDPOINT,
                COMMON_HEADER_FIELDS, UserIdPwd.getInstance().getUserIdPwdList(), true);
    }

    public void getCameraInformation(String cameraIp) {
        List<Object> headers_data;
        headers_data = UserIdPwd.getInstance().getUserIdPwdList();
        headers_data.add(cameraIp);

        serverRepository.createRequest(cameraInformationCallback, CAMERA_INFORMATIONS_ENDPOINT,
                GET_INFO_OR_DELETE_CAM_HEADER_FIELDS, headers_data, true);
    }

    private String formatJson(List<String> fields, List<Object> data) {
        final JSONObject root = new JSONObject();
        ListIterator<String> fieldsIterator = fields.listIterator();
        ListIterator<Object> dataIterator = data.listIterator();

        try {
            while (fieldsIterator.hasNext()) {
                root.put(fieldsIterator.next(), dataIterator.next());
            }

            return root.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    public void setAllCameras(List<CameraInformations> allCameras) {
        this.allCameras.setValue(allCameras);
    }

    public LiveData<List<CameraInformations>> getAllCameras() {
        return allCameras;
    }

    public void setAllUsers(List<UserInformations> allUsers) {
        this.allUsers.setValue(allUsers);
    }

    public LiveData<List<UserInformations>> getAllUsers() {
        return allUsers;
    }

    SingleLiveEvent<Void> getRefreshEvent() {
        return refreshEvent;
    }

    public SingleLiveEvent<Void> getEndActivityEvent() {
        return endActivityEvent;
    }

    Callback<JsonObject> allRunningCamerasCallback = new Callback<JsonObject>() {
        @Override
        public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
            if (!response.isSuccessful()) {
                Toast.makeText(application.getApplicationContext(), R.string.all_running_cams_fail_message, Toast.LENGTH_SHORT).show();
                return;
            }

            List<CameraInformations> allCameraInformations = new ArrayList<>();
            JsonObject responseData = (JsonObject) (response.body().get(MESSAGE));
            JsonArray camerasArray = (JsonArray) responseData.get(CAMERAS);

            for (JsonElement cameraJson : camerasArray) {
                JsonObject object = cameraJson.getAsJsonObject();
                allCameraInformations.add(
                        new CameraInformations(
                                object.get(IP).getAsString(),
                                object.get(LATITUDE).getAsDouble(),
                                object.get(LONGITUDE).getAsDouble()));
            }

            setAllCameras(allCameraInformations);

            Log.d(TAG, "onResponse: " + response);
        }

        @Override
        public void onFailure(Call<JsonObject> call, Throwable t) {
            Log.e(TAG, "onFailure: " + t);
            Toast.makeText(application.getApplicationContext(), R.string.server_error_message, Toast.LENGTH_SHORT).show();
        }
    };

    Callback<JsonObject> allLoggedUsersCallback = new Callback<JsonObject>() {

        @Override
        public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
            if (!response.isSuccessful()) {
                Toast.makeText(application.getApplicationContext(), R.string.all_logged_users_fail_message, Toast.LENGTH_SHORT).show();
                return;
            }

            List<UserInformations> allUsersInformations = new ArrayList<>();
            JsonObject responseData = (JsonObject) (response.body().get(MESSAGE));
            JsonArray usersArray = (JsonArray) responseData.get(LOCATIONS);

            for (JsonElement userJson : usersArray) {
                JsonObject object = userJson.getAsJsonObject();
                allUsersInformations.add(
                        new UserInformations(
                                object.get(USERNAME).getAsString(),
                                object.get(LATITUDE).getAsDouble(),
                                object.get(LONGITUDE).getAsDouble()));
            }

            setAllUsers(allUsersInformations);

            Log.d(TAG, "onResponse: " + response);
        }

        @Override
        public void onFailure(Call<JsonObject> call, Throwable t) {
            Log.e(TAG, "onFailure: " + t);
            Toast.makeText(application.getApplicationContext(), R.string.server_error_message, Toast.LENGTH_SHORT).show();
        }
    };

    Callback<JsonObject> logoutUserCallback = new Callback<JsonObject>() {

        @Override
        public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
            if (!response.isSuccessful()) {
                Toast.makeText(application.getApplicationContext(), R.string.logout_fail_message, Toast.LENGTH_SHORT).show();
                return;
            }

            endActivityEvent.call();
            Toast.makeText(application.getApplicationContext(), R.string.logout_success_message, Toast.LENGTH_SHORT).show();

            Log.d(TAG, "onResponse: " + response);
        }

        @Override
        public void onFailure(Call<JsonObject> call, Throwable t) {
            Log.e(TAG, "onFailure: " + t);
            Toast.makeText(application.getApplicationContext(), R.string.server_error_message, Toast.LENGTH_SHORT).show();
        }
    };

    Callback<JsonObject> cameraInformationCallback = new Callback<JsonObject>() {

        @Override
        public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
            if (!response.isSuccessful()) {
                Toast.makeText(application.getApplicationContext(), R.string.camera_informations_fail_message, Toast.LENGTH_SHORT).show();
                return;
            }


            try {

                String image = ImageUtil.saveImage(application, response.body().get("snapshot").getAsString());
                Intent intent = new Intent(application.getApplicationContext(), CameraInformationActivity.class);
                intent.putExtra(IMAGE,image);

                application.startActivity(intent);
                Toast.makeText(application.getApplicationContext(), "NAO DEU THROW", Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(application.getApplicationContext(), "DEU THROW", Toast.LENGTH_SHORT).show();
            }
            //byte[] imageBytes = Base64.decode(response.body().get("image").getAsString(), Base64.DEFAULT);
//            Bitmap bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);

            //String imagePath = saveImage(imageBytes);




            //byte[] bytes = response.body().get(IMAGE).getAsString().getBytes(StandardCharsets.UTF_8);


//            ByteBuffer buffer = StandardCharsets.UTF_8.encode(response.body().get("image").getAsString());
//            byte[] imageBytes = new byte[buffer.remaining()];
//            buffer.get(imageBytes);
//            Bitmap bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);


//            Bitmap bitmap = BitmapFactory.decodeStream(response.body().byteStream());
//            ByteArrayOutputStream stream = new ByteArrayOutputStream();
//            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
//            byte[] byteArray = stream.toByteArray();
//


//            Intent intent = new Intent(application.getApplicationContext(), CameraInformationActivity.class);
//            intent.putExtra(IMAGE,byteArray);
//
//            application.startActivity(intent);
//

            Log.d(TAG, "onResponse: " + response);
        }

        @Override
        public void onFailure(Call<JsonObject> call, Throwable t) {
            Log.e(TAG, "onFailure: " + t);
            Toast.makeText(application.getApplicationContext(), R.string.server_error_message, Toast.LENGTH_SHORT).show();
        }
    };
}
