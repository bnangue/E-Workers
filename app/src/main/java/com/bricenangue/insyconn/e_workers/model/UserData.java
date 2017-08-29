package com.bricenangue.insyconn.e_workers.model;

/**
 * Created by bricenangue on 30.08.17.
 */

public class UserData {
    private String City, PostalCode, Street, StreetNumber,name,profilePicture;

    public UserData() {

    }

    public String getCity() {
        return City;
    }

    public void setCity(String city) {
        City = city;
    }

    public String getPostalCode() {
        return PostalCode;
    }

    public void setPostalCode(String postalCode) {
        PostalCode = postalCode;
    }

    public String getStreet() {
        return Street;
    }

    public void setStreet(String street) {
        Street = street;
    }

    public String getStreetNumber() {
        return StreetNumber;
    }

    public void setStreetNumber(String streetNumber) {
        StreetNumber = streetNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }

    public String getfullStreet(){
        return Street + " " + StreetNumber;
    }

    public String getzipAndCity(){
        return PostalCode + ", " + City;
    }
}
