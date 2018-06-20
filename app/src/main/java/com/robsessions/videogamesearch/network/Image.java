package com.robsessions.videogamesearch.network;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Model for Image object
 */
public class Image {

    @SerializedName("super_url")
    @Expose
    private String imageUrl;

    public String getImageUrl() {
        return this.imageUrl;
    }
}
