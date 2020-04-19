package com.example.balinasofttest.data.dto;

import com.google.gson.annotations.SerializedName;

public class PhotoDtoOut {
    @SerializedName("id")
    String id;

    public PhotoDtoOut() {
    }

    public PhotoDtoOut(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    @Override
    public String toString() {
        return "PhotoDtoOut{" +
                "id='" + id + '\'' +
                '}';
    }
}
