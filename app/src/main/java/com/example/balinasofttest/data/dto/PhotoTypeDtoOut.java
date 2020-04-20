package com.example.balinasofttest.data.dto;
import com.google.gson.annotations.SerializedName;

public class PhotoTypeDtoOut {
    @SerializedName("id")
    Integer id;
    @SerializedName("name")
    String name;
    @SerializedName("image")
    String image;

    public PhotoTypeDtoOut() {
    }

    public PhotoTypeDtoOut(Integer id, String name, String image) {
        this.id = id;
        this.name = name;
        this.image = image;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getImage() {
        return image;
    }

    @Override
    public String toString() {
        return "PhotoTypeDtoOut{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", image='" + image + '\'' +
                '}';
    }
}
