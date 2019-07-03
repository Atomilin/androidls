package com.example.loftmoney;

import com.google.gson.annotations.SerializedName;

class AuthResponse {

    private String status;
    private String id;
    private String auth;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @SerializedName("auth_token")
    private String authToken;

    public String getAuth() {
        return auth;
    }

    public void setAuth(String auth) {
        this.auth = auth;
    }
}
