package com.example.watchflow.retrofit;

import static com.example.watchflow.Constants.AUTHORIZATION;

import com.example.watchflow.Constants;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface WatchFlowServerApiInterface {

    @GET(Constants.ALL_RUNNING_CAMERAS_ENDPOINT)
    Call<JsonObject> getAllCamerasIps(@Header(AUTHORIZATION) JsonObject headers);

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
    Call<JsonObject> deleteUser(@Header(AUTHORIZATION) JsonObject headers);

    @POST(Constants.REGISTER_CAMERA_ENDPOINT)
    Call<JsonObject> registerCamera(@Header(AUTHORIZATION) JsonObject headers,
                                    @Body JsonObject body);

    @DELETE(Constants.DELETE_CAMERA_ENDPOINT)
    Call<JsonObject> deleteCamera(@Header(AUTHORIZATION) JsonObject headers);

    @GET(Constants.USERS_POSITIONS_ENDPOINT)
    Call<JsonObject> usersPositions(@Header(AUTHORIZATION) JsonObject headers);

    @GET(Constants.CAMERA_INFORMATIONS_ENDPOINT)
    Call<JsonObject> getCameraInformations(@Header(AUTHORIZATION) JsonObject headers);

    @POST(Constants.UPDATE_PHONE_ENDPOINT)
    Call<JsonObject> updatePhone(@Header(AUTHORIZATION) JsonObject headers,
                                 @Body JsonObject body);

    @GET(Constants.USER_INFORMATIONS_ENDPOINT)
    Call<JsonObject> getUserInformations(@Header(AUTHORIZATION) JsonObject headers);

    @GET(Constants.DASHBOARD_INFORMATION_ENDPOINT)
    Call<JsonObject> getDashboardInformation(@Header(AUTHORIZATION) JsonObject headers);

    @GET(Constants.MY_DASHBOARD_CAMS_ENDPOINT)
    Call<JsonObject> getMyDashboardCameras(@Header(AUTHORIZATION) JsonObject headers);

    @POST(Constants.SAVE_DASHBOARD_SELECTED_IPS_ENDPOINT)
    Call<JsonObject> saveDashboardSelectedIPs(@Header(AUTHORIZATION) JsonObject headers,
                                              @Body JsonObject body);
}
