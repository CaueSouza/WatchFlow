package com.example.watchflow;

import java.util.ArrayList;
import java.util.List;

public class UserIdPwd {
    private static UserIdPwd mInstance = null;

    private String userId;
    private String password;
    private Boolean isAdm;

    private UserIdPwd() {
        userId = "";
        password = "";
        isAdm = false;
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

    public Boolean getAdm() {
        return isAdm;
    }

    public void setAdm(Boolean adm) {
        isAdm = adm;
    }

    public List<Object> getUserIdPwdList() {
        List<Object> data = new ArrayList<>();
        data.add(getUserId());
        data.add(getPassword());
        return data;
    }
}
