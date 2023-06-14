package com.example.awaylie;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.awaylie.bean.VerifyBean;
import com.example.awaylie.database.AwayLieSQLiteOpenHelper;

/*
* 用于编写点击查看详情后的弹出的界面
* 主要显示求证的内容，同时包括底部的按钮，包括收藏，实锤按钮，
* TODO:添加评论功能，待构思
* */
public class ReleaseVerifyActivity extends AppCompatActivity {
    private TextView releaseVerifyDetailTitle,releaseVerifyDetailName,releaseVerifyDetailKeyword,
            releaseVerifyDetailTime,releaseVerifyDetailContent;
    private VerifyBean verifyBean;
    private ImageButton releaseVerifyDetailSupportBtn , releaseVerifyDetailVerifyBtn;
    private AwayLieSQLiteOpenHelper mHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_release_verify);
        initView();//初始化控件


    }

    //查询数据并且进行设置
    private void getData(){
        Intent intent = getIntent();
        int vId = intent.getIntExtra("id",1);
        Log.d("Detail", "getData: "+vId);
        verifyBean = mHelper.queryByVerifyById(vId);//按照id进行查询
        releaseVerifyDetailTitle.setText(verifyBean.getTitle());
        releaseVerifyDetailName.setText(verifyBean.getName());
        releaseVerifyDetailKeyword.setText(verifyBean.getKeyword());
        releaseVerifyDetailTime.setText(verifyBean.getTime());
        releaseVerifyDetailContent.setText(verifyBean.getContent());
    }

    @Override
    protected void onStart() {
        super.onStart();
        mHelper = AwayLieSQLiteOpenHelper.getInstance(this);
        mHelper.openWriteLink();
        mHelper.openReadLink();
        getData();//获取数据
    }

    private void initView(){
        releaseVerifyDetailTitle = findViewById(R.id.release_verify_detail_title);
        releaseVerifyDetailName = findViewById(R.id.release_verify_detail_name);
        releaseVerifyDetailKeyword = findViewById(R.id.release_verify_detail_keyword);
        releaseVerifyDetailTime = findViewById(R.id.release_verify_detail_time);
        releaseVerifyDetailContent = findViewById(R.id.release_verify_detail_content);
        releaseVerifyDetailSupportBtn = findViewById(R.id.release_verify_detail_support);
        releaseVerifyDetailVerifyBtn = findViewById(R.id.release_verify_detail_verify);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mHelper.closeDB();
    }
}