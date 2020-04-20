package com.example.balinasofttest.ui.presenter;
import android.graphics.Bitmap;
import android.os.Environment;
import android.util.Log;
import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.example.balinasofttest.App;
import com.example.balinasofttest.data.dto.PhotoTypeDtoOut;
import com.example.balinasofttest.data.list.Repository;
import com.example.balinasofttest.di.list.ListModule;
import com.example.balinasofttest.ui.view.MainActivityView;
import java.io.File;
import java.io.FileOutputStream;
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

    private File bitMapToFile(Bitmap bm) {
        String root = Environment.getExternalStorageDirectory().toString();
        File myDir = new File(root);
        myDir.mkdirs();
        String fname = "Image-" + "image_name" + ".jpg";
        File file = new File(myDir, fname);
        if (file.exists()) file.delete();
        Log.i("TAG", root + fname);
        try {
            FileOutputStream out = new FileOutputStream(file);
            bm.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return file;
    }

    public void requestUploadPhoto(PhotoTypeDtoOut p, Bitmap bm) {
        File file = bitMapToFile(bm);
        disposable = repository.uploadPhoto(p, file)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> file.delete(), this::onError);
    }

    private void onError(Throwable throwable) {
        getViewState().showError(throwable.getMessage());
    }

    private void thatAll(Throwable throwable) {
        getViewState().showError("Нет подключения к интернету!");
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
