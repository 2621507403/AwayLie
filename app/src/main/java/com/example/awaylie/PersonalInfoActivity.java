package com.example.awaylie;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import com.xuexiang.xui.widget.button.ButtonView;

/**
 * 这个界面是点开设置界面后弹出的页面
 * 包括功能块：退出登录，
 * */
public class PersonalInfoActivity extends AppCompatActivity {
    private ButtonView loginOut;
    private SharedPreferences userInfoSP;
    private SharedPreferences.Editor userInfoEdit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pernal_info);
        userInfoSP = getSharedPreferences("userInfo",MODE_PRIVATE);
        userInfoEdit = userInfoSP.edit();
        loginOut = findViewById(R.id.login_out_btn);
        loginOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //退出登录，清除个人信息
                userInfoEdit.clear();
                userInfoEdit.apply();
                //跳转到登陆界面
                Intent intent = new Intent(PersonalInfoActivity.this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

    }
}