package com.bricenangue.insyconn.e_workers.service;

import android.content.Context;
import android.content.SharedPreferences;

import com.bricenangue.insyconn.e_workers.model.User;

/**
 * Created by bricenangue on 19/11/2016.
 */

public class UserSharedPreference {
    Context context;
    public static final String SP_NAME="userDetails";
    private SharedPreferences userLocalDataBase;

    public UserSharedPreference(Context context){
        this.context=context;
        userLocalDataBase=context.getSharedPreferences(SP_NAME,0);
    }
    public void storeUserEWorkerID(String e_Worker_ID){
        SharedPreferences.Editor spEditor=userLocalDataBase.edit();
        spEditor.putString("e_Worker_ID",e_Worker_ID);
        spEditor.apply();
    }

    public String getUserEWorkerID(){

        return userLocalDataBase.getString("e_Worker_ID", "");

    }

    public void clearUserData(){
        SharedPreferences.Editor spEditor=userLocalDataBase.edit();
        spEditor.clear();
        spEditor.apply();
    }


    public void setEmailVerified(boolean verified){
        SharedPreferences.Editor spEditor=userLocalDataBase.edit();
        spEditor.putBoolean("verified",verified);

        spEditor.apply();
    }

    public boolean getEmailVerified(){
        return userLocalDataBase.getBoolean("verified", false);
    }


    public void setisArrived(boolean isarrived){
        SharedPreferences.Editor spEditor=userLocalDataBase.edit();
        spEditor.putBoolean("isarrived",isarrived);

        spEditor.apply();
    }





    public boolean getisArrived(){
        return userLocalDataBase.getBoolean("isarrived", false);
    }


    public void storeUserData(User user){
        SharedPreferences.Editor spEditor=userLocalDataBase.edit();
        spEditor.putString("account_id",user.getId());

        spEditor.putString("loginType",user.getLoginType());

        spEditor.apply();
    }

    public User getLoggedInUser(){
        String account_id=userLocalDataBase.getString("account_id", "");
        String loginType=userLocalDataBase.getString("loginType","");

        User user=new User();
        user.setId(account_id);
        user.setLoginType(loginType);


        return user;
    }


    //call with true if logged in
    public void setUserLoggedIn(boolean loggedIn){
        SharedPreferences.Editor spEditor=userLocalDataBase.edit();
        spEditor.putBoolean("loggedIn", loggedIn);
        spEditor.apply();

    }
    public boolean getUserLoggedIn(){
        return userLocalDataBase.getBoolean("loggedIn", false);
    }

    // if logged in and data not refresh work offline
    public void setUserDataRefreshed(boolean refreshed){
        SharedPreferences.Editor spEditor=userLocalDataBase.edit();
        spEditor.putBoolean("refresh_user_data", refreshed);
        spEditor.apply();

    }

    public boolean getUserDataRefreshed(){
        return userLocalDataBase.getBoolean("refresh_user_data", false);
    }

    /*













    public boolean getPhoneRequest(){
        return userLocalDataBase.getBoolean("phone_request", false);
    }



    //call with true if registered
    public void setUserRegisterd(boolean loggedIn){
        SharedPreferences.Editor spEditor=userLocalDataBase.edit();
        spEditor.putBoolean("registered", loggedIn);
        spEditor.apply();

    }

    public boolean getUserRegisterd(){
        return userLocalDataBase.getBoolean("registered", false);
    }


*/
}
