package com.app.server.models;


import com.app.server.http.utils.APPCrypt;

public class Session {

    String token = null;
    String userId = null;
    String username = null;

    public Session(User user) throws Exception{
        this.userId = user.userID;
        this.token = APPCrypt.encrypt(user.userID);
        this.username = user.username;
    }
}
