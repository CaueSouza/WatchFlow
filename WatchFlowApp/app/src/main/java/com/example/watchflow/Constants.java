package com.example.watchflow;

import java.util.ArrayList;
import java.util.List;

public class Constants {
    public static final Integer AUTO_REFRESH_SECONDS = 20;
    public static final String APP_PACKAGE = "com.example.watchflow";
    public static final String EMPTY_STRING = "";
    public static final String USER_TYPE = "userType";
    public static final Integer ADM_TYPE = 1;
    public static final String AUTHORIZATION = "authorization";
    public static final String OPERATION = "operation";
    public static final String IMAGE = "image";
    public static final String MARKER_TITLE = "marker_title";
    public static final int CREATE_USER_OPERATION = 1;
    public static final int DELETE_USER_OPERATION = 2;
    public static final int CREATE_CAM_OPERATION = 3;
    public static final int DELETE_CAM_OPERATION = 4;
    public static final int UPDATE_PHONE_OPERATION = 5;
    public static final int DASHBOARD_CONFIGURATION_CAM_LIMIT = 5;
    public static final int FIVE_MINUTES_IN_MILLIS = 300000;

    //ENDPOINTS
    public static final String BASE_URL = "http://192.168.0.12:5000";
    public static final String ALL_RUNNING_CAMERAS_ENDPOINT = "/allRunningCameras";
    public static final String USER_LOGIN_ENDPOINT = "/userLogin";
    public static final String USER_LOGOUT_ENDPOINT = "/userLogout";
    public static final String REGISTER_USER_ENDPOINT = "/registerUser";
    public static final String DELETE_USER_ENDPOINT = "/deleteUser";
    public static final String REGISTER_CAMERA_ENDPOINT = "/registerCamera";
    public static final String DELETE_CAMERA_ENDPOINT = "/deleteCamera";
    public static final String USERS_POSITIONS_ENDPOINT = "/usersPositions";
    public static final String CAMERA_INFORMATIONS_ENDPOINT = "/cameraInformations";
    public static final String USER_INFORMATIONS_ENDPOINT = "/userInformations";
    public static final String UPDATE_PHONE_ENDPOINT = "/updatePhone";
    public static final String MY_DASHBOARD_CAMS_ENDPOINT = "/myDashboardCameras";
    public static final String SAVE_DASHBOARD_SELECTED_IPS_ENDPOINT = "/saveDashboardSelectedIPs";
    public static final String DASHBOARD_INFORMATION_ENDPOINT = "/dashboardInformation";

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
    public static final String NEW_TELEPHONE = "newUserPhone";
    public static final String PHONE = "phone";
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
    public static final String RECOGNITIONS = "recognitions";
    public static final String ONLY_IPS = "onlyIps";
    public static final String IS_SELECTED = "isSelected";
    public static final String SELECTED_CAMERAS = "selectedCameras";
    public static final String ADDRESS = "address";
    public static final String HISTORIC = "historic";

    public static final String TIMESTAMP = "timestamp";
    public static final String INITIAL_TIMESTAMP = "initialTimestamp";
    public static final String FINAL_TIMESTAMP = "finalTimestamp";

    //RECOGNITIONS FIELDS
    public static final String TOTAL = "total";
    public static final String ARTICULATED_TRUCK = "articulated_truck";
    public static final String BICYCLE = "bicycle";
    public static final String BUS = "bus";
    public static final String CAR = "car";
    public static final String MOTORCYCLE = "motorcycle";
    public static final String MOTORIZED_VEHICLE = "motorized_vehicle";
    public static final String NON_MOTORIZED_VEHICLE = "non_motorized_vehicle";
    public static final String PEDESTRIAN = "pedestrian";
    public static final String PICKUP_TRUCK = "pickup_truck";
    public static final String SINGLE_UNIT_TRUCK = "single_unit_truck";
    public static final String WORK_VAN = "work_van";

    public static final List<String> RECOGNITION_FIELDS = new ArrayList<>() {{
        add(TOTAL);
        add(ARTICULATED_TRUCK);
        add(BICYCLE);
        add(BUS);
        add(CAR);
        add(MOTORCYCLE);
        add(MOTORIZED_VEHICLE);
        add(NON_MOTORIZED_VEHICLE);
        add(PEDESTRIAN);
        add(PICKUP_TRUCK);
        add(SINGLE_UNIT_TRUCK);
        add(WORK_VAN);
    }};


    public static final List<String> COMMON_HEADER_FIELDS = new ArrayList<>() {{
        add(REQUESTER_USER_ID);
        add(REQUESTER_PWD);
    }};

    public static final List<String> LOGIN_USER_HEADER_FIELDS = new ArrayList<>() {{
        add(USERNAME);
        add(PASSWORD);
    }};

    public static final List<String> LOGIN_USER_BODY_FIELDS = new ArrayList<>() {{
        add(LATITUDE);
        add(LONGITUDE);
    }};

    public static final List<String> REGISTER_USER_BODY_FIELDS = new ArrayList<>() {{
        add(NEW_USERNAME);
        add(NEW_USER_PWD);
        add(NEW_TELEPHONE);
        add(TYPE);
    }};

    public static final List<String> DELETE_USER_HEADER_FIELDS = new ArrayList<>() {{
        add(REQUESTER_USER_ID);
        add(REQUESTER_PWD);
        add(OLD_USERNAME);
    }};

    public static final List<String> REGISTER_CAM_BODY_FIELDS = new ArrayList<>() {{
        add(CAMERA_IP);
        add(STREET);
        add(NUMBER);
        add(NEIGHBORHOOD);
        add(CITY);
        add(COUNTRY);
    }};

    public static final List<String> GET_INFO_OR_DELETE_CAM_HEADER_FIELDS = new ArrayList<>() {{
        add(REQUESTER_USER_ID);
        add(REQUESTER_PWD);
        add(CAMERA_IP);
    }};

    public static final List<String> UPDATE_PHONE_BODY_FIELDS = new ArrayList<>() {{
        add(NEW_TELEPHONE);
    }};

    public static final List<String> GET_USER_INFO_CAM_HEADER_FIELDS = new ArrayList<>() {{
        add(REQUESTER_USER_ID);
        add(REQUESTER_PWD);
        add(USERNAME);
    }};

    public static final List<String> SAVE_DASHBOARD_SELECTED_CAMERAS_BODY_FIELD = new ArrayList<>() {{
        add(SELECTED_CAMERAS);
    }};

    public static final List<String> DASHBOARD_INFORMATION_HEADER_FIELDS = new ArrayList<>() {{
        add(REQUESTER_USER_ID);
        add(REQUESTER_PWD);
        add(INITIAL_TIMESTAMP);
        add(FINAL_TIMESTAMP);
    }};

    //RETRIEVE SPECIFIC VALUE FROM HISTORIC CONSTANTS
    public static final int RETRIEVE_HIGHEST_TOTAL = 1;
    public static final int RETRIEVE_HIGHEST_TIMESTAMP = 2;
    public static final int RETRIEVE_MINOR_TIMESTAMP = 3;
}
