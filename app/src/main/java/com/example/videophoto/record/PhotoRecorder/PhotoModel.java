package com.example.videophoto.record.PhotoRecorder;

import android.net.Uri;

public class PhotoModel {
    private Uri imagePath;
    private String imageName;


    public PhotoModel() {
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public Uri getImagePath() {
        return imagePath;
    }

    public void setImagePath(Uri imagePath) {
        this.imagePath = imagePath;
    }
}
