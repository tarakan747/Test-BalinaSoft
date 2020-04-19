package com.example.balinasofttest.data.provider.web;

import com.example.balinasofttest.data.dto.PageDto;
import com.example.balinasofttest.data.dto.PhotoDtoOut;


import io.reactivex.Single;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;

import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

import retrofit2.http.Query;

public interface Api {

    @GET("api/v2/photo/type")
    Single<Response<PageDto>> getPhotoTypes(@Query("page") Integer numberPage);

    @Multipart
    @POST("api/v2/photo")
    Single<Response<PhotoDtoOut>> uploadPhoto(@Part("name") RequestBody name,
                                              @Part MultipartBody.Part image,
                                              @Part("typeId") RequestBody id);
}
