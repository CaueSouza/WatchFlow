package com.example.watchflow.retrofit;

import androidx.lifecycle.LiveData;

import com.example.watchflow.CameraInformations;
import com.example.watchflow.Constants;
import com.google.gson.JsonObject;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface WatchFlowServerApiInterface {

    @GET(Constants.ALL_RUNNING_CAMERAS)
    Call<JsonObject> getAllCamerasIps();
//
//    @FormUrlEncoded
//    @POST(Constants.LOGIN_USER)
//    Call<JsonObject> postLogin(@FieldMap Map<String, String> fields);
//
//    @FormUrlEncoded
//    @POST(Constants.USER_DATA)
//    Call<JsonObject> postUserData(@Field(ID_USUARIO) String id);
//
//    @POST(Constants.CREATE_ALARM)
//    Call<JsonObject> postCreateAlarm(@Body JsonObject body);
//
//    @POST(Constants.MODIFY_ALARM)
//    Call<JsonObject> postModifyAlarm(@Body JsonObject body);
//
//    @POST(Constants.DELETE_ALARM)
//    Call<JsonObject> postDeleteAlarm(@Body JsonObject body);
//
//    @POST(Constants.CREATE_UPDATE_BOX)
//    Call<JsonObject> postCreateUpdateBox(@Body JsonObject body);
//
//    @POST(Constants.DELETE_BOX)
//    Call<JsonObject> postDeleteBox(@Body JsonObject body);
}