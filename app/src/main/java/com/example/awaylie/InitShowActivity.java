package com.example.awaylie;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

/**
 * 此页面是app启动加载面
 * **/
public class InitShowActivity extends AppCompatActivity {
    private ImageView initShowIV;
    private Button skipShowPic;
    final long threeSecond = 3000;//设置总的倒计时时间
    final long countInterval = 1000;//倒计时的间隔时间
    private SharedPreferences loginStateSP;//判断是否已经登陆
    private Boolean isLogin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_init_show);

        initShowIV = findViewById(R.id.init_show_pic);
        initShowIV.setImageResource(R.drawable.init_show_pic);
        skipShowPic = findViewById(R.id.skipShowPic);
        loginStateSP = getSharedPreferences("userInfo",MODE_PRIVATE);
        /**加载界面跳过按钮**/
//        skipShowPic.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Log.d("InitShowActivity", "Handler postDelayed run");
//                nextPage(InitShowActivity.this,MainActivity.class);
//            }
//        });

        /**
         * 实现了默认三秒后跳转
         * */
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                isLogin = loginStateSP.getBoolean("isLogin",false);
                if (isLogin)
                    nextPage(InitShowActivity.this,MainActivity.class);
                else{
                    nextPage(InitShowActivity.this, LoginActivity.class);
                }
            }
        },3000);


        /**
         * 使用CountDownTimer 类来实现每倒计时一次，更新一次ui操作
         *两个参数，第一个是总时间，第二个是间隔，内部方法为每次更新都会实现
         * */
        new CountDownTimer(threeSecond, countInterval) {
            @Override
            public void onTick(long millisUntilFinished) {
                //用于更新按钮
                skipShowPic.setText("跳过"+millisUntilFinished/1000+"s");
            }
            @Override
            public void onFinish() {
                //用于更新按钮
                skipShowPic.setText("跳过");
            }
        }.start();
    }

    /**跳转函数*/
    private void nextPage(Context context,Class className){
        Intent intent = new Intent( context , className);
        startActivity(intent);
        finish();
    }
}