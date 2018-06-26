package com.example.videophoto.record.VideoRecorder;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.Toast;

import com.example.yonguk.videorecord.R;

import java.io.File;
import java.io.IOException;
import java.util.List;


public class VideoRecorderFragment extends Fragment implements View.OnClickListener, TextureView.SurfaceTextureListener {
    private View view;
    private Context context;
    private Camera mCamera;
    private TextureView mPreview;
    private Button btnCapture;
    private Button seeVieosBtn;
    private Chronometer chronometer;
    private MediaRecorder mMediaRecorder;
    private File mOutputFile;
    private ViewPager viewPager;

    private boolean isRecording = false;
    private static final String TAG = "Recorder";

    private FragmentTransaction fragmentTransaction;
    private CountDownTimer countDownTimer;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_video_recorder, container, false);
        context = getContext();
        chronometer = (Chronometer) view.findViewById(R.id.chronometerID);
        mPreview = new TextureView(context);
        mPreview.setSurfaceTextureListener(this);
        mPreview = (TextureView) view.findViewById(R.id.texture_view);
        mPreview.setSurfaceTextureListener(this);
        btnCapture = (Button) view.findViewById(R.id.btn_capture);
        seeVieosBtn = (Button) view.findViewById(R.id.seeAllVideoBtnID);
        btnCapture.setOnClickListener(this);

        seeVieosBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCamera = null;
                Toast.makeText(context, "onClick", Toast.LENGTH_SHORT).show();
                seeVideoFragmentTransaction();
            }
        });
        return view;
    }

    @Override
    public void onStop() {
        super.onStop();
        if (isRecording) {
            chronometer.stop();
            btnCapture.setText("CAPTURE");
            chronometer.setVisibility(View.INVISIBLE);
            countDownTimer.cancel();
        }
    }

    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surfaceTexture, int i, int i1) {
        mCamera = Camera.open();
        mCamera.setDisplayOrientation(90);

        try {
            mCamera.setPreviewTexture(surfaceTexture);
            mCamera.startPreview();
        } catch (IOException ioe) {

        }
    }

    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surfaceTexture, int i, int i1) {

    }

    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surfaceTexture) {
        if (mCamera != null) {
            mCamera.stopPreview();
            mCamera.release();
            return true;
        }
        return false;
    }

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surfaceTexture) {

    }


    @Override
    public void onClick(View v) {

        if (isRecording) {
            chronometer.stop();
            chronometer.setVisibility(View.INVISIBLE);
            stopRecordingAndSaveVideo();

        } else {

            new MediaPrepareTask().execute(null, null, null);
            chronometer.setBase(SystemClock.elapsedRealtime());
            chronometer.setVisibility(View.VISIBLE);
            chronometer.start();

            countDownTimer = new CountDownTimer(21 * 1000, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                }

                @Override
                public void onFinish() {
                    chronometer.stop();
                    chronometer.setVisibility(View.INVISIBLE);
                    stopRecordingAndSaveVideo();
                }
            };
            countDownTimer.start();
        }
    }

    private void releaseMediaRecorder() {
        if (mMediaRecorder != null && mCamera!=null) {
            mMediaRecorder.reset();
            mMediaRecorder.release();
            mMediaRecorder = null;
            mCamera.lock();
        }
    }


    private void releaseCamera() {
        if (mCamera != null) {
            mCamera.release();
            mCamera = null;
        }
    }

    private boolean prepareVideoRecorder() {
        mCamera = CameraHelper.getDefaultCameraInstance();
        mCamera.setDisplayOrientation(90);
        Camera.Parameters parameters = mCamera.getParameters();
        List<Camera.Size> mSupportedPreviewSizes = parameters.getSupportedPreviewSizes();
        List<Camera.Size> mSupportedVideoSizes = parameters.getSupportedVideoSizes();
        Camera.Size optimalSize = CameraHelper.getOptimalVideoSize(mSupportedVideoSizes,
                mSupportedPreviewSizes, mPreview.getWidth(), mPreview.getHeight());

        CamcorderProfile profile = CamcorderProfile.get(CamcorderProfile.QUALITY_HIGH);
        profile.videoFrameWidth = optimalSize.width;
        profile.videoFrameHeight = optimalSize.height;


        parameters.setPreviewSize(profile.videoFrameWidth, profile.videoFrameHeight);
        mCamera.setParameters(parameters);
        try {
            mCamera.setPreviewTexture(mPreview.getSurfaceTexture());
        } catch (IOException e) {
            return false;
        }


        mMediaRecorder = new MediaRecorder();


        mCamera.unlock();
        mMediaRecorder.setCamera(mCamera);
        mMediaRecorder.setOrientationHint(90);

        mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.DEFAULT);
        mMediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);


        mMediaRecorder.setProfile(profile);


        mOutputFile = CameraHelper.getOutputMediaFile(CameraHelper.MEDIA_TYPE_VIDEO);
        if (mOutputFile == null) {
            return false;
        }
        mMediaRecorder.setOutputFile(mOutputFile.getPath());

        try {
            mMediaRecorder.prepare();
        } catch (IllegalStateException e) {
            Log.d(TAG, "IllegalStateException preparing MediaRecorder: " + e.getMessage());
            releaseMediaRecorder();
            return false;
        } catch (IOException e) {
            Log.d(TAG, "IOException preparing MediaRecorder: " + e.getMessage());
            releaseMediaRecorder();
            return false;
        }
        return true;
    }


    class MediaPrepareTask extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Void... voids) {

            if (prepareVideoRecorder()) {

                mMediaRecorder.start();

                isRecording = true;
            } else {

                releaseMediaRecorder();
                return false;
            }

            return true;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            if (!result) {
                getActivity().finish();
            }

            btnCapture.setText("Stop");

        }
    }

    private void seeVideoFragmentTransaction() {
        fragmentTransaction = getFragmentManager().beginTransaction().addToBackStack("");
        fragmentTransaction.replace(R.id.main_Container, new SeeVideosRecycleFragment());
        fragmentTransaction.commit();
    }

    private void stopRecordingAndSaveVideo() {
        try {
            mMediaRecorder.stop();
        } catch (RuntimeException e) {
            mOutputFile.delete();
        }
        releaseMediaRecorder();
        mCamera.lock();
        btnCapture.setText("Capture");
        isRecording = false;
        releaseCamera();

        context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE
                , Uri.parse("file://" + mOutputFile.getPath())));
    }

    @Override
    public void onPause() {
        super.onPause();
    }
}
