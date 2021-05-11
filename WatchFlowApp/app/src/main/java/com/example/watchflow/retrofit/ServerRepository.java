package com.example.watchflow.retrofit;

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
    private WatchFlowServerApiInterface watchFlowServerApiInterface;

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

    public void createPost(Callback<JsonObject> objectCallback, String postRequest, List<String> fields, Object... data) {
        if (fields.size() != data.length) {
            return;
        }

        Call<JsonObject> call = null;
        String jsonData = formatJson(fields, data);
        JsonObject requestJson = JsonParser.parseString(jsonData).getAsJsonObject();

        switch (postRequest) {
            case ALL_RUNNING_CAMERAS_ENDPOINT:
                call = watchFlowServerApiInterface.getAllCamerasIps(requestJson);
                break;
            case USER_LOGIN_ENDPOINT:
                call = watchFlowServerApiInterface.userLogin(requestJson);
                break;
            case USER_LOGOUT_ENDPOINT:
                call = watchFlowServerApiInterface.userLogout(requestJson);
                break;
            case REGISTER_USER_ENDPOINT:
                call = watchFlowServerApiInterface.registerUser(requestJson);
                break;
            case DELETE_USER_ENDPOINT:
                call = watchFlowServerApiInterface.deleteUser(requestJson);
                break;
            case REGISTER_CAMERA_ENDPOINT:
                call = watchFlowServerApiInterface.registerCamera(requestJson);
                break;
            case DELETE_CAMERA_ENDPOINT:
                call = watchFlowServerApiInterface.deleteCamera(requestJson);
                break;
            case USERS_POSITIONS_ENDPOINT:
                call = watchFlowServerApiInterface.usersPositions(requestJson);
                break;
        }

        if (call != null) {
            call.enqueue(objectCallback);
        }
    }

    private String formatJson(List<String> fields, Object... data) {
        final JSONObject root = new JSONObject();
        ListIterator<String> fieldsIterator = fields.listIterator();

        try {
            for (Object fieldData : data) {
                root.put(fieldsIterator.next(), fieldData);
            }
            return root.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }
}
