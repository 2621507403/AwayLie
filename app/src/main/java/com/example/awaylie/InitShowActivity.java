package com.example.awaylie;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;

/**
 * 此页面是app启动加载面
 * **/
public class InitShowActivity extends AppCompatActivity {
    private ImageView initShowIV;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_init_show);
        initShowIV = findViewById(R.id.init_show_pic);
        initShowIV.setImageResource(R.drawable.ic_launcher_background);
    }
}