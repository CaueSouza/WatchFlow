package com.example.watchflow;

import java.util.ArrayList;
import java.util.List;

public class Constants {
    public static final Integer AUTO_REFRESH_SECONDS = 20;
    public static final String APP_PACKAGE = "com.example.watchflow";
    public static final String EMPTY_STRING = "";
    public static final String USER_TYPE = "userType";
    public static final Integer ADM_TYPE = 1;
    public static final String AUTHORIZATION = "authentication";
    public static final String OPERATION = "operation";
    public static final String IMAGE = "image";
    public static final int CREATE_USER_OPERATION = 1;
    public static final int DELETE_USER_OPERATION = 2;
    public static final int CREATE_CAM_OPERATION = 3;
    public static final int DELETE_CAM_OPERATION = 4;

    //ENDPOINTS
    public static final String BASE_URL = "http://172.29.160.1:5000";
    public static final String ALL_RUNNING_CAMERAS_ENDPOINT = "/allRunningCameras";
    public static final String USER_LOGIN_ENDPOINT = "/userLogin";
    public static final String USER_LOGOUT_ENDPOINT = "/userLogout";
    public static final String REGISTER_USER_ENDPOINT = "/registerUser";
    public static final String DELETE_USER_ENDPOINT = "/deleteUser";
    public static final String REGISTER_CAMERA_ENDPOINT = "/registerCamera";
    public static final String DELETE_CAMERA_ENDPOINT = "/deleteCamera";
    public static final String USERS_POSITIONS_ENDPOINT = "/usersPositions";
    public static final String CAMERA_INFORMATIONS_ENDPOINT = "/cameraInformations";
    public static final int RETROFIT_TIME_OUT_LIMIT = 50;

    //REQUEST FIELDS
    public static final String USERNAME = "userName";
    public static final String PASSWORD = "pwd";
    public static final String IP = "ip";
    public static final String LATITUDE = "latitude";
    public static final String LONGITUDE = "longitude";
    public static final String REQUESTER_USER_ID = "requesterUserId";
    public static final String REQUESTER_PWD = "requesterPwd";
    public static final String TYPE = "type";
    public static final String NEW_USERNAME = "newUserName";
    public static final String NEW_USER_PWD = "newUserPwd";
    public static final String OLD_USERNAME = "oldUserName";
    public static final String USER_ID = "userId";
    public static final String CAMERAS = "cameras";
    public static final String MESSAGE = "message";
    public static final String LOCATIONS = "locations";
    public static final String CAMERA_IP = "cameraIp";
    public static final String STREET = "street";
    public static final String NUMBER = "number";
    public static final String NEIGHBORHOOD = "neighborhood";
    public static final String CITY = "city";
    public static final String COUNTRY = "country";

    public static final List<String> COMMON_HEADER_FIELDS = new ArrayList<String>() {{
        add(REQUESTER_USER_ID);
        add(REQUESTER_PWD);
    }};

    public static final List<String> LOGIN_USER_HEADER_FIELDS = new ArrayList<String>() {{
        add(USERNAME);
        add(PASSWORD);
    }};

    public static final List<String> LOGIN_USER_BODY_FIELDS = new ArrayList<String>() {{
        add(LATITUDE);
        add(LONGITUDE);
    }};

    public static final List<String> REGISTER_USER_BODY_FIELDS = new ArrayList<String>() {{
        add(NEW_USERNAME);
        add(NEW_USER_PWD);
        add(TYPE);
    }};

    public static final List<String> DELETE_USER_HEADER_FIELDS = new ArrayList<String>() {{
        add(REQUESTER_USER_ID);
        add(REQUESTER_PWD);
        add(OLD_USERNAME);
    }};

    public static final List<String> REGISTER_CAM_BODY_FIELDS = new ArrayList<String>() {{
        add(CAMERA_IP);
        add(STREET);
        add(NUMBER);
        add(NEIGHBORHOOD);
        add(CITY);
        add(COUNTRY);
    }};

    public static final List<String> GET_INFO_OR_DELETE_CAM_HEADER_FIELDS = new ArrayList<String>() {{
        add(REQUESTER_USER_ID);
        add(REQUESTER_PWD);
        add(CAMERA_IP);
    }};
}
