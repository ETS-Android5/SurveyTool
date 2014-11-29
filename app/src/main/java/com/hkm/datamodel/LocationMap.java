package com.hkm.datamodel;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Hesk on 9/6/2014.
 */
public class LocationMap implements Serializable {
    @SerializedName("ID")
    private int id;
    @SerializedName("guid")
    private String url_raw;

    public LocationMap() {
    }

    public LocationMap(int id, String raw) {
        this.id = id;
        this.url_raw = raw;
    }

    public String get_url() {
        return url_raw;
    }

    public int get_attachment_id() {
        return id;
    }

}
