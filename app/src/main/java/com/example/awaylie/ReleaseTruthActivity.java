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

import com.example.awaylie.bean.RumorBean;
import com.example.awaylie.bean.TruthBean;
import com.example.awaylie.bean.VerifyBean;
import com.example.awaylie.database.AwayLieSQLiteOpenHelper;
import com.xuexiang.xui.widget.actionbar.TitleBar;
import com.xuexiang.xui.widget.button.ButtonView;

/*
 * 用于编写点击查看详情后的弹出的界面
 * 主要显示谣言的内容，同时包括底部的按钮，包括支持，反对按钮。
 * TODO:添加评论功能，待构思
 * */
public class ReleaseTruthActivity extends AppCompatActivity {
    private TextView releaseTruthDetailTitle,releaseTruthDetailName,releaseTruthDetailKeyword,
            releaseTruthDetailTime,releaseTruthDetailContent;
    private TitleBar titleBar;
    private TruthBean truthBean;
    private ButtonView releaseTruthDetailSupportBtn , releaseTruthDetailRefuseBtn;
    private AwayLieSQLiteOpenHelper mHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_release_truth);
        initView();//初始化控件
        releaseTruthDetailSupportBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ReleaseTruthActivity.this,"赞成",Toast.LENGTH_SHORT).show();
            }
        });

        titleBar.setLeftClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        releaseTruthDetailRefuseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ReleaseTruthActivity.this,"反对",Toast.LENGTH_SHORT).show();
            }
        });
    }

    //查询数据并且进行设置
    private void getData(){
        Intent intent = getIntent();
        int vId = intent.getIntExtra("id",0);
        truthBean = mHelper.queryByTruthById(vId);//按照id进行查询
        releaseTruthDetailTitle.setText(truthBean.getTitle());
        releaseTruthDetailName.setText(truthBean.getName());
        releaseTruthDetailTime.setText(truthBean.getTime());
        releaseTruthDetailContent.setText(truthBean.getContent());
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
        releaseTruthDetailTitle = findViewById(R.id.release_truth_detail_title);
        releaseTruthDetailName = findViewById(R.id.release_truth_detail_name);
        releaseTruthDetailTime = findViewById(R.id.release_truth_detail_time);
        releaseTruthDetailContent = findViewById(R.id.release_truth_detail_content);
        releaseTruthDetailRefuseBtn = findViewById(R.id.release_truth_detail_refuse);
        releaseTruthDetailSupportBtn = findViewById(R.id.release_truth_detail_support);
        titleBar = findViewById(R.id.release_truth_detail_title_bar);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mHelper.closeDB();
    }
}