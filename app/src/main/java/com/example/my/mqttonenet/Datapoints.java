package com.example.my.mqttonenet;

/**
 * Created by Maibenben on 2020/3/27.
 */

public class Datapoints {
    private String at;
    private String value;


    public void setValue(String value) {
        this.value = value;
    }

    public String getAt() {
        return at;
    }

    public void setAt(String at) {
        this.at = at;
    }

    public String getValue() {
        return value;
    }
}
