package com.example.videophoto.record.PhotoRecorder;


import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.videophoto.record.Adapter.RecyclePhotoAdapter;
import com.example.yonguk.videorecord.R;

import java.io.File;
import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class SeeAllPhotoFragment extends Fragment {

    private View view;
    private Context context;
    private RecyclePhotoAdapter adapter;
    private RecyclerView recyclerView;
    private GridLayoutManager gridLayoutManager;
    public SeeAllPhotoFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.fragment_see_all_photo, container, false);
        context=getContext();
        recyclerView=view.findViewById(R.id.recycleViewPhotoID);
        gridLayoutManager=new GridLayoutManager(context,2);
        recyclerView.setLayoutManager(gridLayoutManager);
        adapter=new RecyclePhotoAdapter(context,getPhotos());
        recyclerView.setAdapter(adapter);

        return view;
    }
    private ArrayList<PhotoModel> getPhotos(){
        ArrayList<PhotoModel> list=new ArrayList<>();
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "PhotoFolder");
        PhotoModel model;
        if (mediaStorageDir.exists()){
            File[] files=mediaStorageDir.listFiles();

            for (int i = 0; i <files.length ; i++) {
                File file=files[i];
                model=new PhotoModel();
                model.setImageName(file.getName());
                model.setImagePath(Uri.fromFile(file));

                //add image in Array
                list.add(model);

            }
        }
        return list;
    }

}
