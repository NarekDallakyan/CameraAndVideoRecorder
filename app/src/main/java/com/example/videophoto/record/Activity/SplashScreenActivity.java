package com.example.videophoto.record.Activity;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.example.yonguk.videorecord.R;

public class SplashScreenActivity extends AppCompatActivity {
    private ImageView imageView;
    private Animation animimage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        View view = getWindow().getDecorView();
        int uiOption = View.SYSTEM_UI_FLAG_FULLSCREEN;
        view.setSystemUiVisibility(uiOption);
        imageView = findViewById(R.id.ballon);
        animateImage();
    }

    private void animateImage() {
        animimage = AnimationUtils.loadAnimation(this, R.anim.animimage);
        imageView.setAnimation(animimage);
        final Intent intent = new Intent(this, MainActivity.class);
        new Handler().postDelayed(() -> startActivity(intent), 3000);
    }


}

