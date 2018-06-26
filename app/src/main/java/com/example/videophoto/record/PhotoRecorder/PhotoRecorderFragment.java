package com.example.videophoto.record.PhotoRecorder;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.example.yonguk.videorecord.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import droidninja.filepicker.FilePickerBuilder;
import droidninja.filepicker.FilePickerConst;


public class PhotoRecorderFragment extends Fragment {
    View view;
    Context context;
    public static final int MEDIA_TYPE_IMAGE = 1;
    public static final int MEDIA_TYPE_VIDEO = 2;
    private Camera mCamera;
    private CameraPreview mPreview;
    private FrameLayout preview;
    private FragmentTransaction fragmentTransaction;
    ArrayList<String> filePaths = new ArrayList<>();
    private Camera.PictureCallback mPicture = new Camera.PictureCallback() {

        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            File pictureFile = getOutputMediaFile(MEDIA_TYPE_IMAGE);

            if (pictureFile == null) {
                return;
            }

            try {
                FileOutputStream fos = new FileOutputStream(pictureFile);
                fos.write(data);
                fos.close();
                mCamera.startPreview();


            } catch (FileNotFoundException e) {
                e.getMessage();
            } catch (IOException e) {
                e.getMessage();
            }

        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_photo_recorder, container, false);
        context = getContext();
        mCamera = getCameraInstance();
        if (checkCameraHardware(context)) {
            Toast.makeText(context, "true", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "false", Toast.LENGTH_SHORT).show();
        }

        mPreview = new CameraPreview(context, mCamera);
        preview = (FrameLayout) view.findViewById(R.id.camera_preview11);
        preview.addView(mPreview);

        Button captureButton = (Button) view.findViewById(R.id.button_capture11);
        captureButton.setOnClickListener(
                v -> {
                    System.gc();
                    mCamera.setPreviewCallback(null);
                    mCamera.setOneShotPreviewCallback(null);
                    mCamera.takePicture(null, null, mPicture);


                }
        );
        Button seePhotosBtn = (Button) view.findViewById(R.id.seeAllPhotoIID);
        seePhotosBtn.setOnClickListener(v -> {
            mCamera.lock();
            fragmentTransaction = getFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.main_Container, new SeeAllPhotoFragment()).addToBackStack("");
            fragmentTransaction.commit();
        });
        Button getGalleryBtn = (Button) view.findViewById(R.id.photoGaleryID);
        getGalleryBtn.setOnClickListener(v -> {
            filePaths.clear();
            FilePickerBuilder.getInstance().setMaxCount(2)
                    .setSelectedFiles(filePaths)
                    .setActivityTheme(R.style.AppTheme)
                    .pickPhoto(this);
        });

        return view;
    }

    private static File getOutputMediaFile(int type) {
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "PhotoFolder");
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d("MyCameraApp", "failed to create directory");
                return null;
            }
        }

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                    "IMG_" + timeStamp + ".jpg");
        } else if (type == MEDIA_TYPE_VIDEO) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                    "VID_" + timeStamp + ".mp4");
        } else {
            return null;
        }

        return mediaFile;
    }

    private boolean checkCameraHardware(Context context) {
        if (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
            // this device has a camera
            return true;
        } else {
            return false;
        }
    }

    public static Camera getCameraInstance() {
        Camera c = null;
        try {
            c = Camera.open(1); // attempt to get a Camera instance
            c.setDisplayOrientation(90);
        } catch (Exception e) {
            e.getMessage();
        }
        return c;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) throws NullPointerException {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 233:
                if (data!=null) {
                    try {
                        filePaths = data.getStringArrayListExtra(FilePickerConst.KEY_SELECTED_MEDIA);
                        if (filePaths != null) {
                            moveImages(filePaths);
                        } else {
                            return;
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
        }
    }

    private void moveImages(ArrayList<String> list) throws IOException {
        createFolder();
        InputStream inStream = null;
        OutputStream outStream = null;
        String timeStamp="";
        int indexZero=0;
        String tempImagePath="";
        String generalImagePath="";
        File afile=null;
        ArrayList<File> filesList = new ArrayList<>();
        for (int i = 0; i < filePaths.size(); i++) {
            timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            indexZero = filePaths.get(i).indexOf('0');
            tempImagePath = filePaths.get(i).substring(indexZero + 2);
            generalImagePath = "/sdcard/" + tempImagePath;
            afile = new File(generalImagePath);
            filesList.add(afile);


            File bfile = new File("/sdcard/Pictures/PhotoFolder/From_Gallery_" + timeStamp + ".jpg");

            inStream = new FileInputStream(filesList.get(i));
            outStream = new FileOutputStream(bfile);

            byte[] buffer = new byte[1024];

            int length;
            while ((length = inStream.read(buffer)) > 0) {

                outStream.write(buffer, 0, length);

            }
            inStream.close();
            outStream.close();
        }
        Toast.makeText(context, ""+filesList.size(), Toast.LENGTH_SHORT).show();




    }
    private void createFolder(){
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "PhotoFolder");
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d("PhotoFolder", "failed to create directory");
            }
        }
    }


}
