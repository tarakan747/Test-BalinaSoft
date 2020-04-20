package com.example.balinasofttest.data.list;
import com.example.balinasofttest.data.dto.PageDto;
import com.example.balinasofttest.data.dto.PhotoTypeDtoOut;
import com.example.balinasofttest.data.provider.web.Api;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import io.reactivex.Completable;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;


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
        String mT = "multipart/form-data";
        RequestBody requestName =
                RequestBody.create(MediaType.parse(mT), p.getName());

        RequestBody requestFile =
                RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part image = MultipartBody.Part.createFormData("photo ", file.getName(), requestFile);

        RequestBody requestId =
                RequestBody.create(MediaType.parse(mT), p.getId() + "");

         return Completable.fromSingle(
                api.uploadPhoto(requestName, image, requestId));
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
