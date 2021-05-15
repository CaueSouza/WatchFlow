package com.example.watchflow.retrofit;

import com.example.watchflow.Constants;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Header;
import retrofit2.http.POST;

import static com.example.watchflow.Constants.AUTHORIZATION;

public interface WatchFlowServerApiInterface {

    @POST(Constants.ALL_RUNNING_CAMERAS_ENDPOINT)
    Call<JsonObject> getAllCamerasIps(@Header(AUTHORIZATION) JsonObject headers,
                                      @Body JsonObject body);

    @POST(Constants.USER_LOGIN_ENDPOINT)
    Call<JsonObject> userLogin(@Header(AUTHORIZATION) JsonObject headers,
                               @Body JsonObject body);

    @POST(Constants.USER_LOGOUT_ENDPOINT)
    Call<JsonObject> userLogout(@Header(AUTHORIZATION) JsonObject headers,
                                @Body JsonObject body);

    @POST(Constants.REGISTER_USER_ENDPOINT)
    Call<JsonObject> registerUser(@Header(AUTHORIZATION) JsonObject headers,
                                  @Body JsonObject body);

    @DELETE(Constants.DELETE_USER_ENDPOINT)
    Call<JsonObject> deleteUser(@Header(AUTHORIZATION) JsonObject headers,
                                @Body JsonObject body);

    @POST(Constants.REGISTER_CAMERA_ENDPOINT)
    Call<JsonObject> registerCamera(@Header(AUTHORIZATION) JsonObject headers,
                                    @Body JsonObject body);

    @DELETE(Constants.DELETE_CAMERA_ENDPOINT)
    Call<JsonObject> deleteCamera(@Header(AUTHORIZATION) JsonObject headers,
                                  @Body JsonObject body);

    @POST(Constants.USERS_POSITIONS_ENDPOINT)
    Call<JsonObject> usersPositions(@Header(AUTHORIZATION) JsonObject headers,
                                    @Body JsonObject body);
}
