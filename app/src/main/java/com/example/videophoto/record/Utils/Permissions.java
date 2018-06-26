package com.example.videophoto.record.Utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

public class Permissions{
    private Activity activity;
    private Context context;

    public Permissions(Context context) {
        this.context = context;
        activity= (Activity) context;
    }

    public void connectCamera(){
        int REQUEST_CAMERA_PERMISSION_RESULT = 0;
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {


                } else {
                    if (activity.shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)) {
                        //          Toast.makeText(getContext(), "Video app required to camera", Toast.LENGTH_SHORT).show();
                    }
                    activity.requestPermissions(new String[]{Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.RECORD_AUDIO}, REQUEST_CAMERA_PERMISSION_RESULT);
                }

            }
            if (ContextCompat.checkSelfPermission(activity, Manifest.permission.CAMERA)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(activity, new String[]{ android.Manifest.permission.CAMERA}, 1);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
