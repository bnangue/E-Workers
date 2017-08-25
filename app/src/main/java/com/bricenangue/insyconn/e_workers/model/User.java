package com.bricenangue.insyconn.e_workers.model;

import com.facebook.accountkit.PhoneNumber;

/**
 * Created by bricenangue on 24.08.17.
 */

public class User {
    private String id;
    private String loginType;
    private String eWorkerID;

    public User() {
    }

    public User(String id, String loginType) {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLoginType() {
        return loginType;
    }

    public void setLoginType(String loginType) {
        this.loginType = loginType;
    }

    public String geteWorkerID() {
        return eWorkerID;
    }

    public void seteWorkerID(String eWorkerID) {
        this.eWorkerID = eWorkerID;
    }
}
