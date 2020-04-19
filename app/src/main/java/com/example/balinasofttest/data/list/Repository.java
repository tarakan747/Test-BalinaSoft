package com.example.balinasofttest.data.list;

import com.example.balinasofttest.data.dto.PhotoTypeDtoOut;

import java.io.File;
import java.util.List;

import io.reactivex.Completable;

public interface Repository {

    Completable getPhotoType(int numberPage);

    Completable uploadPhoto(PhotoTypeDtoOut p, File file);

    List<PhotoTypeDtoOut> getList();

    boolean checkLastPage(int numberPage);
}
