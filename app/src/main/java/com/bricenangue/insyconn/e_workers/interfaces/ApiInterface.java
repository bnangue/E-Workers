package com.bricenangue.insyconn.e_workers.interfaces;

import com.bricenangue.insyconn.e_workers.model.Message;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by bricenangue on 24.08.17.
 */

public interface ApiInterface {

    @GET("inbox.json")
    Call<List<Message>> getInbox() ;
}
