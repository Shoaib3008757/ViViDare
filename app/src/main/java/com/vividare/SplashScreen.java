package com.vividare;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.LinearLayout;

public class SplashScreen extends AppCompatActivity {

   /* private int timer = 2;
    Handler mHandler;
   */
   LinearLayout ll_let_start;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        ll_let_start = (LinearLayout) findViewById(R.id.ll_let_start);
        ll_let_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SplashScreen.this, LoginActivity.class));
                finish();
            }
        });
       /* mHandler = new Handler();
        useHandler();*/

    }

    /*//Thread for starting mainActivity
    private Runnable mRunnableStartMainActivity = new Runnable() {
        @Override
        public void run() {
            Log.d("Handler", " Calls");
            timer--;
            mHandler = new Handler();
            mHandler.postDelayed(this, 1000);

            if (timer == 2) {
                //loading.setText("Loading...");
            }
            if (timer == 1) {
                //loading.setText("Loading.");
            }
            if (timer == 0) {
                //loading.setText("Loading..");

                *//*Intent i = new Intent(SplashScreen.this, SignInSignUpActivity.class);*//*

                //Intent i = new Intent(SplashScreen.this, MainActivity.class);
                Intent i = new Intent(SplashScreen.this, LoginActivity.class);

                startActivity(i);
                finish();
            }
        }
    };*/


    /*//handler for the starign activity
    Handler newHandler;
    public void useHandler(){

        newHandler = new Handler();
        newHandler.postDelayed(mRunnableStartMainActivity, 1000);

    }*/

    @Override
    protected void onDestroy() {
        super.onDestroy();
       // mHandler.removeCallbacks(mRunnableStartMainActivity);
    }

}
