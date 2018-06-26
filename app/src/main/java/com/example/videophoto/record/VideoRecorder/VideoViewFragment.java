package com.example.videophoto.record.VideoRecorder;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.VideoView;

import com.example.yonguk.videorecord.R;

public class VideoViewFragment extends Fragment {
    String filePath="";
    private View view;
    private Context context;
    private VideoView videoView;
    private android.widget.MediaController mediaController;


    public VideoViewFragment() {
        // Required empty public constructor
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.fragment_video_view, container, false);
        context=getContext();
        findViews();
        startVideoView();
        return view;
    }
    private void findViews(){
        videoView= (VideoView) view.findViewById(R.id.videoViewID);
    }
    private void startVideoView(){
        videoView.setVideoPath(filePath);
        mediaController=new android.widget.MediaController(context);
        mediaController.setAnchorView(videoView);
        videoView.setMediaController(mediaController);
        videoView.start();
    }

}
