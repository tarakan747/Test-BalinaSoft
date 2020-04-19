package com.example.balinasofttest.ui.view;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ProgressBar;
import android.widget.Toast;

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

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;
import java.util.Objects;
import java.util.Random;

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
    public void showProgress() {
        bar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
        bar.setVisibility(View.GONE);
    }

    @Override
    public void showList(List<PhotoTypeDtoOut> list) {
        myAdapter = new MyAdapter(list, this);
        manager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(myAdapter);
        recyclerView.setVisibility(View.VISIBLE);

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
        showCamera(position, p);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void showCamera(int position, PhotoTypeDtoOut p) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.CAMERA}, MY_CAMERA_PERMISSION_CODE);
        } else {
            Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
            cameraIntent.putExtra("ID", p.getId());
            cameraIntent.putExtra("NAME", p.getName());
            startActivityForResult(cameraIntent, CAMERA_REQUEST);
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_CAMERA_PERMISSION_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                cameraIntent.putExtra("ID", p.getId());
                cameraIntent.putExtra("NAME", p.getName());
                startActivityForResult(cameraIntent, CAMERA_REQUEST);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
            Bitmap photo = (Bitmap) Objects.requireNonNull(data.getExtras()).get("data");
            try {
                FileOutputStream out = openFileOutput("image", MODE_PRIVATE);
                photo.compress(Bitmap.CompressFormat.PNG, 100, out);
                out.close();
            } catch (Exception ignored) {
            }

            photo.recycle();
            File file = getFileStreamPath("image.png");

            Log.d("TAG", "onActivityResult: " + file.getAbsolutePath());

            presenter.requestUploadPhoto(new PhotoTypeDtoOut(
                    data.getIntExtra("ID", 0),
                    null,
                    data.getStringExtra("NAME")), file);
        }
    }
}
