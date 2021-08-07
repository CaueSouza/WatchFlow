package com.example.watchflow.retrofit;

import androidx.annotation.Nullable;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.ListIterator;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.example.watchflow.Constants.ALL_RUNNING_CAMERAS_ENDPOINT;
import static com.example.watchflow.Constants.BASE_URL;
import static com.example.watchflow.Constants.CAMERA_INFORMATIONS_ENDPOINT;
import static com.example.watchflow.Constants.DELETE_CAMERA_ENDPOINT;
import static com.example.watchflow.Constants.DELETE_USER_ENDPOINT;
import static com.example.watchflow.Constants.REGISTER_CAMERA_ENDPOINT;
import static com.example.watchflow.Constants.REGISTER_USER_ENDPOINT;
import static com.example.watchflow.Constants.UPDATE_PHONE_ENDPOINT;
import static com.example.watchflow.Constants.USERS_POSITIONS_ENDPOINT;
import static com.example.watchflow.Constants.USER_LOGIN_ENDPOINT;
import static com.example.watchflow.Constants.USER_LOGOUT_ENDPOINT;

public class ServerRepository {
    public static final String TAG = ServerRepository.class.getSimpleName();

    private static ServerRepository instance = null;
    public final WatchFlowServerApiInterface watchFlowServerApiInterface;

    private OkHttpClient getClient() {
        return new OkHttpClient.Builder()
                .readTimeout(15, TimeUnit.SECONDS)
                .connectTimeout(15, TimeUnit.SECONDS)
                .retryOnConnectionFailure(true)
                .addInterceptor(chain -> {
                    Request request = chain.request()
                            .newBuilder()
                            .build();

                    return chain.proceed(request);
                }).build();
    }

    private ServerRepository() {
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(getClient())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        watchFlowServerApiInterface = retrofit.create(WatchFlowServerApiInterface.class);
    }

    public static synchronized ServerRepository getInstance() {
        if (instance != null) {
            return instance;
        }

        instance = new ServerRepository();
        return instance;
    }

    private boolean checkFieldAndData(@Nullable List<String> fields, @Nullable List<Object> data) {
        return fields == null && data == null || fields != null && data != null && fields.size() == data.size();
    }

    public void createRequest(Callback<JsonObject> objectCallback, String endpoint, List<String> fields, List<Object> data, boolean sendAsHeader) {
        if (sendAsHeader) {
            createRequest(objectCallback, endpoint, fields, data, null, null);
        } else {
            createRequest(objectCallback, endpoint, null, null, fields, data);
        }
    }

    public void createRequest(Callback<JsonObject> objectCallback, String endpoint,
                              @Nullable List<String> header_fields, @Nullable List<Object> header_data,
                              @Nullable List<String> body_fields, @Nullable List<Object> body_data) {

        if (!(checkFieldAndData(header_fields, header_data) && checkFieldAndData(body_fields, body_data))) {
            return;
        }

        Call<JsonObject> call = null;
        JsonObject headersJson = new JsonObject();
        JsonObject bodyJson = new JsonObject();

        if (header_fields != null) {
            String headersData = formatJson(header_fields, header_data);
            headersJson = JsonParser.parseString(headersData).getAsJsonObject();
        }

        if (body_fields != null) {
            String bodyData = formatJson(body_fields, body_data);
            bodyJson = JsonParser.parseString(bodyData).getAsJsonObject();
        }

        switch (endpoint) {
            case ALL_RUNNING_CAMERAS_ENDPOINT:
                call = watchFlowServerApiInterface.getAllCamerasIps(headersJson);
                break;
            case USER_LOGIN_ENDPOINT:
                call = watchFlowServerApiInterface.userLogin(headersJson, bodyJson);
                break;
            case USER_LOGOUT_ENDPOINT:
                call = watchFlowServerApiInterface.userLogout(headersJson, bodyJson);
                break;
            case REGISTER_USER_ENDPOINT:
                call = watchFlowServerApiInterface.registerUser(headersJson, bodyJson);
                break;
            case DELETE_USER_ENDPOINT:
                call = watchFlowServerApiInterface.deleteUser(headersJson);
                break;
            case REGISTER_CAMERA_ENDPOINT:
                call = watchFlowServerApiInterface.registerCamera(headersJson, bodyJson);
                break;
            case DELETE_CAMERA_ENDPOINT:
                call = watchFlowServerApiInterface.deleteCamera(headersJson);
                break;
            case USERS_POSITIONS_ENDPOINT:
                call = watchFlowServerApiInterface.usersPositions(headersJson);
                break;
            case CAMERA_INFORMATIONS_ENDPOINT:
                call = watchFlowServerApiInterface.getCameraInformations(headersJson);
                break;
            case UPDATE_PHONE_ENDPOINT:
                call = watchFlowServerApiInterface.updatePhone(headersJson, bodyJson);
                break;
        }

        if (call != null) {
            call.enqueue(objectCallback);
        }
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
}
