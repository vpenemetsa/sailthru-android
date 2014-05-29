package com.sailthru.android.sdk.impl.response;

/**
 * Created by Vijay Penemetsa on 5/14/14.
 *
 * Reponse class matching response from API for registration request
 */
public class UserRegisterAppResponse extends BaseResponse {

    private String hid;

    private String email;

    public String getHid() {
        return hid;
    }

    public void setHid(String hid) {
        this.hid = hid;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}