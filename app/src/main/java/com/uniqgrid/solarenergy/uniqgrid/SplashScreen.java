package com.uniqgrid.solarenergy.uniqgrid;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;

public class SplashScreen extends AppCompatActivity {

    private static String DEFAULT_TOKEN="abcd";
    Animation anim;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
//
//         anim = AnimationUtils.loadAnimation(this, R.anim.alpha_splash_screen);
//        anim.reset();
//        CoordinatorLayout l = (CoordinatorLayout) findViewById(R.id.splash_layout);
//        l.clearAnimation();
//        l.startAnimation(anim);

//        anim = AnimationUtils.loadAnimation(this, R.anim.alpha_splash_screen);
//        anim.reset();
//        ImageView iv = (ImageView) findViewById(R.id.splash_img);
//        iv.clearAnimation();
//        iv.startAnimation(anim);


        Handler mHandlerLogin = new Handler() {
            public void handleMessage(android.os.Message msg) {

                //change this to login activity
                Intent intent = new Intent(SplashScreen.this, LoginActivityNew.class);
                startActivity(intent);
                finish();
            }

            ;

        };


        Handler mHandlerHome = new Handler() {
            public void handleMessage(android.os.Message msg) {
                Intent intent = new Intent(SplashScreen.this, WelcomeActivityNew.class);
                startActivity(intent);
                finish();
            }

            ;

        };


        SharedPreferences app_preferences = PreferenceManager.getDefaultSharedPreferences(SplashScreen.this);
        String token = app_preferences.getString("token", DEFAULT_TOKEN);


        if (token.equals(DEFAULT_TOKEN)) {

            mHandlerLogin.sendEmptyMessageDelayed(0, 2000);

        } else {

//            JWT jwt = new JWT(token);
//            boolean isExpired = jwt.isExpired(10);
//
//            if (isExpired) {
//                Toast.makeText(SplashScreen.this, "Session expired, please login again", Toast.LENGTH_LONG).show();
//                mHandlerLogin.sendEmptyMessageDelayed(0, 2000);
//                Log.i("uniqgrid", "expired");
//            } else {
                mHandlerHome.sendEmptyMessageDelayed(0, 2000);
                Log.i("login", "success");

        }
    }

}

