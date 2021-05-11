package com.example.watchflow;

import java.util.ArrayList;
import java.util.List;

public class Constants {
    //ENDPOINTS
    public static final String BASE_URL = "http://192.168.0.13:5000";
    public static final String ALL_RUNNING_CAMERAS_ENDPOINT = "/allRunningCameras";
    public static final String USER_LOGIN_ENDPOINT = "/userLogin";
    public static final String USER_LOGOUT_ENDPOINT = "/userLogout";
    public static final String REGISTER_USER_ENDPOINT = "/registerUser";
    public static final String DELETE_USER_ENDPOINT = "/deleteUser";
    public static final String REGISTER_CAMERA_ENDPOINT = "/registerCamera";
    public static final String DELETE_CAMERA_ENDPOINT = "/deleteCamera";
    public static final String USERS_POSITIONS_ENDPOINT = "/usersPositions";
    public static final int RETROFIT_TIME_OUT_LIMIT = 50;

    //REQUEST FIELDS
    public static final String USERNAME = "userName";
    public static final String PASSWORD = "pwd";
    public static final String LATITUDE = "latitude";
    public static final String LONGITUDE = "longitude";
    public static final String REQUESTER_USER_ID = "requesterUserId";
    public static final String REQUESTER_PWD = "requesterPwd";
    public static final String TYPE = "type";
    public static final String NEW_USERNAME = "newUserName";
    public static final String NEW_USER_PWD = "newUserPwd";
    public static final String CAMERA_IP = "cameraIp";
    public static final String OLD_USERNAME = "oldUserName";
    public static final String USER_ID = "userId";

    public static final List<String> LOGIN_USER_FIELDS = new ArrayList<String>() {{
        add(USERNAME);
        add(PASSWORD);
        add(LATITUDE);
        add(LONGITUDE);
    }};

    public static final List<String> ALL_RUNNING_CAMERAS_FIELDS = new ArrayList<String>() {{
        add(REQUESTER_USER_ID);
        add(REQUESTER_PWD);
    }};
}
