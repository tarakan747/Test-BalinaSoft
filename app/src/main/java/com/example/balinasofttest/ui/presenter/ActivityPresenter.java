package com.example.balinasofttest.ui.presenter;



import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.example.balinasofttest.App;
import com.example.balinasofttest.data.dto.PhotoTypeDtoOut;
import com.example.balinasofttest.data.list.Repository;
import com.example.balinasofttest.di.list.ListModule;
import com.example.balinasofttest.ui.view.MainActivityView;

import java.io.File;
import java.io.IOException;
import java.util.List;


import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;


@InjectViewState
public class ActivityPresenter extends MvpPresenter<MainActivityView> {
    Disposable disposable;

    public static int numberPage = 0;

    @Inject
    Repository repository;

    public ActivityPresenter() {
        App.get().plus(new ListModule()).inject(this);
    }

    public void requestList() {
        if (repository.checkLastPage(numberPage)) {
            disposable = repository.getPhotoType(numberPage)
                    .subscribeOn(Schedulers.computation())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this::onSuccess, this::thatAll);
        }
    }

    public void requestUploadPhoto(PhotoTypeDtoOut p, File file) {
        disposable = repository.uploadPhoto(p,file)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe();
    }


    private void thatAll(Throwable throwable) {
        return;
    }

    private List<PhotoTypeDtoOut> getList() {
        numberPage++;
        return repository.getList();
    }

    public void onSuccess() {
        if (numberPage == 0)
            getViewState().showList(getList());
        else
            getViewState().updateAdapter(getList());

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (disposable != null) {
            disposable.dispose();
        }
        App.get().clearListComponent();
    }
}
