package com.example.refindproyecto.SplashScreen;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.refindproyecto.ActivityLogin;
import com.example.refindproyecto.R;

/*
 *
 * https://developer.android.com/reference/android/view/WindowManager.LayoutParams.html#FLAG_FULLSCREEN
 */

public class SplashScreen extends AppCompatActivity {
    ImageView ivTop,ivHeart,ivBeat,ivBottom;
    TextView textView;
    CharSequence charSequence;
    int index;
    long delay=200;
    Handler handler = new Handler();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        //Asignar Variables
        ivTop = findViewById(R.id.iv_top);
        ivHeart = findViewById(R.id.iv_heart);
        ivBeat = findViewById(R.id.iv_beat);
        ivBottom = findViewById(R.id.iv_bottom);
        textView = findViewById(R.id.text_view);

        MediaPlayer mp = MediaPlayer.create(this, R.raw.intro);
        mp.start();


        //TODO: mirar el FLAG_FULLSCREEN
        //Set full screen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN
                , WindowManager.LayoutParams.FLAG_FULLSCREEN);
        Animation animation1 = AnimationUtils.loadAnimation(this,R.anim.top_wave);

        //Start top animation
        ivTop.setAnimation(animation1);
        ObjectAnimator objectAnimator = ObjectAnimator.ofPropertyValuesHolder(
                ivHeart,
                PropertyValuesHolder.ofFloat("scaleX", 1.2f),
                PropertyValuesHolder.ofFloat("scaleY", 1.2f)
        );

        //Set duration
        objectAnimator.setDuration(500);
        //Set reat count
        objectAnimator.setRepeatCount(ValueAnimator.INFINITE);
        //Set repeat mode
        objectAnimator.setRepeatMode(ValueAnimator.REVERSE);
        //Start animator
        objectAnimator.start();
        animatText("REFIND");
        // Glide.with(this).load();
        Animation animation2 = AnimationUtils.loadAnimation(this, R.anim.bottom_wave);
        ivBottom.setAnimation(animation2);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(SplashScreen.this
                        , ActivityLogin.class)
                        .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                finish();
            }
        }, 4000);
    }
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            textView.setText(charSequence.subSequence(0, index++));
            if(index<=charSequence.length()){
                handler.postDelayed(runnable, delay);
            }
        }
    };
    public void animatText(CharSequence cs){
        charSequence = cs;
        index=0;
        handler.removeCallbacks(runnable);
        handler.postDelayed(runnable, delay);

    }

}