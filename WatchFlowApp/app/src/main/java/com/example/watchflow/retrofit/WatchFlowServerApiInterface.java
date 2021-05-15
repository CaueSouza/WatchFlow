package com.example.watchflow.retrofit;

import com.example.watchflow.Constants;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface WatchFlowServerApiInterface {

    @POST(Constants.ALL_RUNNING_CAMERAS_ENDPOINT)
    Call<JsonObject> getAllCamerasIps(@Header("authentication") JsonObject params,
                                      @Body JsonObject body);

    @POST(Constants.USER_LOGIN_ENDPOINT)
    Call<JsonObject> userLogin(@Header("authentication") JsonObject params,
                               @Body JsonObject body);

    @POST(Constants.USER_LOGOUT_ENDPOINT)
    Call<JsonObject> userLogout(@Header("authentication") JsonObject params,
                                @Body JsonObject body);

    @POST(Constants.REGISTER_USER_ENDPOINT)
    Call<JsonObject> registerUser(@Header("authentication") JsonObject params,
                                  @Body JsonObject body);

    @DELETE(Constants.DELETE_USER_ENDPOINT)
    Call<JsonObject> deleteUser(@Header("authentication") JsonObject params,
                                @Body JsonObject body);

    @POST(Constants.REGISTER_CAMERA_ENDPOINT)
    Call<JsonObject> registerCamera(@Header("authentication") JsonObject params,
                                    @Body JsonObject body);

    @DELETE(Constants.DELETE_CAMERA_ENDPOINT)
    Call<JsonObject> deleteCamera(@Header("authentication") JsonObject params,
                                  @Body JsonObject body);

    @POST(Constants.USERS_POSITIONS_ENDPOINT)
    Call<JsonObject> usersPositions(@Header("authentication") JsonObject params,
                                    @Body JsonObject body);
}
