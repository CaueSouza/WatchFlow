package com.example.watchflow;

public class UserId {
    private static UserId mInstance = null;

    private String userId;
    private String password;

    protected UserId() {
    }

    public static synchronized UserId getInstance() {
        if (null == mInstance) {
            mInstance = new UserId();
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
