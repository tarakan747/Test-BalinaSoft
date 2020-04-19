package com.example.balinasofttest.data.list;


import android.util.Log;

import com.example.balinasofttest.data.dto.PageDto;
import com.example.balinasofttest.data.dto.PhotoDtoOut;
import com.example.balinasofttest.data.dto.PhotoTypeDtoOut;
import com.example.balinasofttest.data.provider.web.Api;


import java.io.File;
import java.util.ArrayList;
import java.util.List;


import io.reactivex.Completable;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Response;


public class RepositoryImpl implements Repository {
    private PageDto page;
    private Api api;
    private List<PhotoTypeDtoOut> list = new ArrayList<>();

    public RepositoryImpl(Api api) {
        this.api = api;
    }

    @Override
    public Completable getPhotoType(int numberPage) {
        return Completable.fromSingle(
                api.getPhotoTypes(numberPage)
                        .map(r -> {
                            page = r.body();
                            list = page.getList();
                            return Completable.complete();
                        }));
    }

    @Override
    public Completable uploadPhoto(PhotoTypeDtoOut p, File file) {
        RequestBody requestBodyName = RequestBody.create(MediaType.parse("multipart/form-data"), p.getName());
        RequestBody requestBodyFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part filePart = MultipartBody.Part.createFormData("photo", file.getName(), requestBodyFile);
        RequestBody requestBodyId = RequestBody.create(MediaType.parse("multipart/form-data"), p.getId().toString());
        return Completable.fromSingle(
                api.uploadPhoto(requestBodyName, filePart, requestBodyId).doOnSuccess(this::success)
        );
    }

    private void success(Response<PhotoDtoOut> response) {
        Log.d("MY_TAG", "success: " + response.body());
    }

    @Override
    public List<PhotoTypeDtoOut> getList() {
        return list;
    }

    @Override
    public boolean checkLastPage(int numberPage) {
        if (page == null || page.getTotalPages() > numberPage)
            return true;
        return false;
    }


}
