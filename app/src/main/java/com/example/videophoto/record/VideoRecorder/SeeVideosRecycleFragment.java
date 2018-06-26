package com.example.videophoto.record.VideoRecorder;


import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.videophoto.record.Adapter.RecycleVideosAdapter;
import com.example.yonguk.videorecord.R;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;

/**
 * A simple {@link Fragment} subclass.
 */
public class SeeVideosRecycleFragment extends Fragment {

    private View view;
    private Context context;
    private ArrayList list;
    private LinearLayoutManager layoutManager;
    private RecyclerView recyclerView;
    private RecycleVideosAdapter recycleViewAdapter;

    public SeeVideosRecycleFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_see_videos, container, false);
        context = getContext();
        recyclerView = (RecyclerView) view.findViewById(R.id.videoRecycleViewID);
        getFiles();
        layoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManager);
        recycleViewAdapter = new RecycleVideosAdapter(list, context);
        recyclerView.setAdapter(recycleViewAdapter);
        return view;
    }

    private void getFiles() {
        File videoFile = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "mapiary");
        list = new ArrayList<>();
        Collections.addAll(list, videoFile.listFiles());
    }

}
