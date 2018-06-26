package com.example.videophoto.record.Adapter;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.yonguk.videorecord.R;
import com.example.videophoto.record.VideoRecorder.VideoViewFragment;

import java.io.File;
import java.util.List;

public class RecycleVideosAdapter extends RecyclerView.Adapter<RecycleVideosAdapter.MyViewHelper> {

    private List<File> list;
    private Context context;

    public RecycleVideosAdapter(List<File> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public MyViewHelper onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.video_item,parent,false);
        return new MyViewHelper(view);
    }

    @Override
    public void onBindViewHolder(MyViewHelper holder, final int position) {
     holder.textView.setText(list.get(position).getName());
     holder.textView.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
             VideoViewFragment videoView = new VideoViewFragment();
             videoView.setFilePath(list.get(position).getPath());
             AppCompatActivity activity = (AppCompatActivity) v.getContext();
             activity
                     .getSupportFragmentManager()
                     .beginTransaction()
                     .addToBackStack("")
                     .replace(R.id.main_Container, videoView)
                     .commit();
         }
     });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHelper extends RecyclerView.ViewHolder {
        TextView textView;

        public MyViewHelper(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.videoNameId);
        }
    }
}
