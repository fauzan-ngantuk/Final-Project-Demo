package com.rangermerah.recyletor;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {

    String checkWelcome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        Thread timerThread = new Thread(){
            public void run(){
                try{
                    sleep(2000);
                }catch(InterruptedException e){
                    e.printStackTrace();
                }finally{
                    SharedPreferences sharedPref = getSharedPreferences("welcome", Context.MODE_PRIVATE);
                    checkWelcome = sharedPref.getString("sudahlihat", "");

                    if (!checkWelcome.equals("true")) {
                        startActivity(new Intent(SplashActivity.this, WelcomeScreen.class));
                        // overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                    }
                    else {
                        Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                        startActivity(intent);
                    }

                }
            }
        };
        timerThread.start();

    }
    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        finish();
    }
}
