package com.hkm.datamodel;

import android.net.Uri;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by hesk on 5/31/2014.
 */
public class PhotoViewData implements Serializable {
    @SerializedName("description")
    public String description = "";
    @SerializedName("local_uri")
    public Uri uri;
    @SerializedName("pre_tag")
    public String tag = "";

    public PhotoViewData(String description, Uri uri) {
        this.description = description;
        this.uri = uri;
    }

    public void setlabel(String string) {
        this.description = string;
    }
    public void setTag(String s){this.tag = s;}
}
