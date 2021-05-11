package com.example.watchflow.retrofit;

import com.example.watchflow.Constants;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.POST;

public interface WatchFlowServerApiInterface {

    @POST(Constants.ALL_RUNNING_CAMERAS_ENDPOINT)
    Call<JsonObject> getAllCamerasIps(@Body JsonObject body);

    @POST(Constants.USER_LOGIN_ENDPOINT)
    Call<JsonObject> userLogin(@Body JsonObject body);

    @POST(Constants.USER_LOGOUT_ENDPOINT)
    Call<JsonObject> userLogout(@Body JsonObject body);

    @POST(Constants.REGISTER_USER_ENDPOINT)
    Call<JsonObject> registerUser(@Body JsonObject body);

    @DELETE(Constants.DELETE_USER_ENDPOINT)
    Call<JsonObject> deleteUser(@Body JsonObject body);

    @POST(Constants.REGISTER_CAMERA_ENDPOINT)
    Call<JsonObject> registerCamera(@Body JsonObject body);

    @DELETE(Constants.DELETE_CAMERA_ENDPOINT)
    Call<JsonObject> deleteCamera(@Body JsonObject body);

    @POST(Constants.USERS_POSITIONS_ENDPOINT)
    Call<JsonObject> usersPositions(@Body JsonObject body);
}
