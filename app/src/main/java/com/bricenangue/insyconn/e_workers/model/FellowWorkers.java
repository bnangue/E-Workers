package com.bricenangue.insyconn.e_workers.model;

/**
 * Created by bricenangue on 29.08.17.
 */

public class FellowWorkers {

    private String name;
    private String position;
    private int thumbnail;
    private String profilePicture;

    public FellowWorkers() {
    }

    public FellowWorkers(String name, String position, String profilePicture) {
        this.name = name;
        this.position = position;
        this.profilePicture = profilePicture;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }

    public int getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(int thumbnail) {
        this.thumbnail = thumbnail;
    }

}
