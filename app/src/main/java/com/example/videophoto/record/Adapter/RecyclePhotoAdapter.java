package com.example.videophoto.record.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.videophoto.record.PhotoRecorder.PhotoModel;
import com.example.yonguk.videorecord.R;

import java.util.ArrayList;

public class RecyclePhotoAdapter  extends RecyclerView.Adapter<RecyclePhotoAdapter.MyPhotoViewHolder>{
   private Context context;
   private ArrayList<PhotoModel> list;

    public RecyclePhotoAdapter(Context context, ArrayList<PhotoModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public MyPhotoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.recycle_photo_item,parent,false);
        return new MyPhotoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyPhotoViewHolder holder, int position) {
        holder.imageName.setText(list.get(position).getImageName());
        holder.imageView.setImageURI(list.get(position).getImagePath());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyPhotoViewHolder extends RecyclerView.ViewHolder{
        TextView imageName;
        ImageView imageView;
        public MyPhotoViewHolder(View itemView) {
            super(itemView);
            imageName=itemView.findViewById(R.id.photoNameID);
            imageView=itemView.findViewById(R.id.photoImageID);
        }
    }
}
