package com.example.watchflow.retrofit;

import androidx.annotation.Nullable;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.ListIterator;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.example.watchflow.Constants.ALL_RUNNING_CAMERAS_ENDPOINT;
import static com.example.watchflow.Constants.BASE_URL;
import static com.example.watchflow.Constants.DELETE_CAMERA_ENDPOINT;
import static com.example.watchflow.Constants.DELETE_USER_ENDPOINT;
import static com.example.watchflow.Constants.REGISTER_CAMERA_ENDPOINT;
import static com.example.watchflow.Constants.REGISTER_USER_ENDPOINT;
import static com.example.watchflow.Constants.USERS_POSITIONS_ENDPOINT;
import static com.example.watchflow.Constants.USER_LOGIN_ENDPOINT;
import static com.example.watchflow.Constants.USER_LOGOUT_ENDPOINT;

public class ServerRepository {
    public static final String TAG = ServerRepository.class.getSimpleName();

    private static ServerRepository instance = null;
    private final WatchFlowServerApiInterface watchFlowServerApiInterface;

    private ServerRepository() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
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

    public void createPost(Callback<JsonObject> objectCallback, String postRequest, List<String> fields, List<Object> data, boolean sendAsParams) {
        if (sendAsParams) {
            createPost(objectCallback, postRequest, fields, data, null, null);
        } else {
            createPost(objectCallback, postRequest, null, null, fields, data);
        }
    }

    public void createPost(Callback<JsonObject> objectCallback, String postRequest,
                           @Nullable List<String> param_fields, @Nullable List<Object> param_data,
                           @Nullable List<String> body_fields, @Nullable List<Object> body_data) {

        if (!(checkFieldAndData(param_fields, param_data) && checkFieldAndData(body_fields, body_data))) {
            return;
        }

        Call<JsonObject> call = null;
        JsonObject paramsJson = new JsonObject();
        JsonObject bodyJson = new JsonObject();

        if (param_fields != null) {
            String paramsData = formatJson(param_fields, param_data);
            paramsJson = JsonParser.parseString(paramsData).getAsJsonObject();
        }

        if (body_fields != null) {
            String bodyData = formatJson(body_fields, body_data);
            bodyJson = JsonParser.parseString(bodyData).getAsJsonObject();
        }

        switch (postRequest) {
            case ALL_RUNNING_CAMERAS_ENDPOINT:
                call = watchFlowServerApiInterface.getAllCamerasIps(paramsJson, bodyJson);
                break;
            case USER_LOGIN_ENDPOINT:
                call = watchFlowServerApiInterface.userLogin(paramsJson, bodyJson);
                break;
            case USER_LOGOUT_ENDPOINT:
                call = watchFlowServerApiInterface.userLogout(paramsJson, bodyJson);
                break;
            case REGISTER_USER_ENDPOINT:
                call = watchFlowServerApiInterface.registerUser(paramsJson, bodyJson);
                break;
            case DELETE_USER_ENDPOINT:
                call = watchFlowServerApiInterface.deleteUser(paramsJson, bodyJson);
                break;
            case REGISTER_CAMERA_ENDPOINT:
                call = watchFlowServerApiInterface.registerCamera(paramsJson, bodyJson);
                break;
            case DELETE_CAMERA_ENDPOINT:
                call = watchFlowServerApiInterface.deleteCamera(paramsJson, bodyJson);
                break;
            case USERS_POSITIONS_ENDPOINT:
                call = watchFlowServerApiInterface.usersPositions(paramsJson, bodyJson);
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
