package com.zx.lockscreen.bean;

import java.io.Serializable;

/**
 * Created by liuyuchuan on 2018/5/26.
 */

public class BleKeyInfo implements Serializable {

    private String ble_token;
    private int id;
    private String secret;

    public String getBle_token() {
        return ble_token;
    }

    public void setBle_token(String ble_token) {
        this.ble_token = ble_token;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }
}
