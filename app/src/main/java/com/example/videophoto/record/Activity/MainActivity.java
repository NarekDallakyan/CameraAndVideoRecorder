package com.example.videophoto.record.Activity;


import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.TextureView;

import com.example.videophoto.record.Adapter.ViewPagerAdapter;
import com.example.videophoto.record.PhotoRecorder.PhotoRecorderFragment;
import com.example.videophoto.record.Utils.Permissions;
import com.example.videophoto.record.VideoRecorder.VideoRecorderFragment;
import com.example.yonguk.videorecord.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    public static final int REQUEST_CAMERA_PERMISSION_RESULT = 0;

    private List<Fragment> list;
    private ViewPagerAdapter adapter;
    private ViewPager viewPager;
    private Permissions permissions;
    private boolean b=false;
    int count=5;
    //***********************************************


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        permissions = new Permissions(this);
        connectCamera();
        viewPager = (ViewPager) findViewById(R.id.viewPagerID);
        isPermissionRequired();
        //**********************************************************



    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        isPermissionRequired();
    }

    private void connectCamera() {
        permissions.connectCamera();
    }

    private void addFragmentInViewPager() {
        list = new ArrayList<>();
        list.add(new PhotoRecorderFragment());
        list.add(new VideoRecorderFragment());
        adapter = new ViewPagerAdapter(getSupportFragmentManager(), list);
        viewPager.setAdapter(adapter);
    }

    private void isPermissionRequired() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED) {
            addFragmentInViewPager();

        }
    }



}

