package com.example.awaylie;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.awaylie.bean.VerifyBean;
import com.example.awaylie.database.AwayLieSQLiteOpenHelper;
import com.xuexiang.xui.widget.actionbar.TitleBar;
import com.xuexiang.xui.widget.button.ButtonView;

/*
* 用于编写点击查看详情后的弹出的界面
* 主要显示求证的内容，同时包括底部的按钮，包括收藏，证明按钮，
* TODO:添加评论功能，待构思
* */
public class ReleaseVerifyActivity extends AppCompatActivity {
    private TextView releaseVerifyDetailTitle,releaseVerifyDetailName,releaseVerifyDetailKeyword,
            releaseVerifyDetailTime,releaseVerifyDetailContent;
    private TitleBar titleBar;
    private VerifyBean verifyBean;
    private ButtonView releaseVerifyDetailSupportBtn , releaseVerifyDetailVerifyBtn;
    private AwayLieSQLiteOpenHelper mHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_release_verify);
        initView();//初始化控件
        releaseVerifyDetailSupportBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ReleaseVerifyActivity.this,"点赞成功",Toast.LENGTH_SHORT).show();
            }
        });

        releaseVerifyDetailVerifyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ReleaseVerifyActivity.this,ReleaseV2RActivity.class);
                intent.putExtra("questionId",verifyBean.getId());

                startActivity(intent);
            }
        });

        titleBar.setLeftClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    //查询数据并且进行设置
    private void getData(){
        Intent intent = getIntent();
        int vId = intent.getIntExtra("id",1);
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
        titleBar = findViewById(R.id.release_verify_detail_title_bar);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mHelper.closeDB();
    }
}