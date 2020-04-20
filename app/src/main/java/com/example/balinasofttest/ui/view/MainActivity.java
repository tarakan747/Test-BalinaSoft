package com.example.balinasofttest.ui.view;
import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ProgressBar;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.arellomobile.mvp.MvpAppCompatActivity;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.example.balinasofttest.R;
import com.example.balinasofttest.data.dto.PhotoTypeDtoOut;
import com.example.balinasofttest.ui.presenter.ActivityPresenter;
import com.example.balinasofttest.ui.view.adapter.MyAdapter;
import java.util.List;


public class MainActivity extends MvpAppCompatActivity implements
        MainActivityView {

    @InjectPresenter
    ActivityPresenter presenter;
    private static final int MY_CAMERA_PERMISSION_CODE = 100;
    private static final int CAMERA_REQUEST = 1888;
    RecyclerView recyclerView;
    ProgressBar bar;
    MyAdapter myAdapter;
    LinearLayoutManager manager;
    boolean isScrolling = false;
    PhotoTypeDtoOut p;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        bar = findViewById(R.id.progressBar);
        recyclerView = findViewById(R.id.list);
        presenter.requestList();
    }


    @Override
    public void showList(List<PhotoTypeDtoOut> list) {
        myAdapter = new MyAdapter(list, this);
        manager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(myAdapter);
        recyclerView.setVisibility(View.VISIBLE);
        bar.setVisibility(View.GONE);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                    isScrolling = true;
                }
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int currentItems = manager.getChildCount();
                int totalItems = manager.getItemCount();
                int scrollOutItems = manager.findFirstVisibleItemPosition();


                if (isScrolling && (currentItems + scrollOutItems == totalItems)) {
                    isScrolling = false;
                    presenter.requestList();
                }
            }
        });
    }

    @Override
    public void updateAdapter(List<PhotoTypeDtoOut> list) {
        myAdapter.add(list);
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onClick(int position, PhotoTypeDtoOut p) {
        this.p = p;
        showCamera(position, p);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void showCamera(int position, PhotoTypeDtoOut p) {
        int cameraPerm = ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA);
        int fileRead = ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE);
        int fileSave = ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (cameraPerm != PackageManager.PERMISSION_GRANTED &&
                fileRead != PackageManager.PERMISSION_GRANTED &&
                fileSave != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.CAMERA,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE}, MY_CAMERA_PERMISSION_CODE);
        } else {
            Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(cameraIntent, CAMERA_REQUEST);
        }
    }

    @Override
    public void showError(String error) {
        AlertDialog alertDialog = new AlertDialog.Builder(this)
                .setTitle("Error")
                .setMessage(error)
                .setCancelable(false)
                .setPositiveButton("OK",null)
                .create();
        alertDialog.show();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_CAMERA_PERMISSION_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap photo = (Bitmap) extras.get("data");
            presenter.requestUploadPhoto(p, photo);
        }

    }
}
