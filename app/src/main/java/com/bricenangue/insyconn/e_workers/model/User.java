package com.bricenangue.insyconn.e_workers.model;

import com.facebook.accountkit.PhoneNumber;

/**
 * Created by bricenangue on 24.08.17.
 */

public class User {
    private String id;
    private String loginType;
    private String eWorkerID;// required to enter email or phone number
    private long last_logging;
    private String profilePicture;

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

    public long getLast_logging() {
        return last_logging;
    }

    public void setLast_logging(long last_logging) {
        this.last_logging = last_logging;
    }

    public String getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }
}
