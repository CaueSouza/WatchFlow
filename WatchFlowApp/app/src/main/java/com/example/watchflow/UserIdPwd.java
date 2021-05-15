package com.example.watchflow;

public class UserIdPwd {
    private static UserIdPwd mInstance = null;

    private String userId;
    private String password;

    private UserIdPwd() {
        userId = "";
        password = "";
    }

    public static synchronized UserIdPwd getInstance() {
        if (null == mInstance) {
            mInstance = new UserIdPwd();
        }
        return mInstance;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
