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
import com.example.awaylie.bean.VerifyBean;
import com.example.awaylie.database.AwayLieSQLiteOpenHelper;
import com.xuexiang.xui.widget.actionbar.TitleBar;
import com.xuexiang.xui.widget.button.ButtonView;

/*
 * 用于编写点击查看详情后的弹出的界面
 * 主要显示谣言的内容，同时包括底部的按钮，包括支持，反对按钮。
 * TODO:添加评论功能，待构思
 * */
public class ReleaseRumorActivity extends AppCompatActivity {
    private TextView releaseRumorDetailTitle,releaseRumorDetailName,releaseRumorDetailKeyword,
            releaseRumorDetailTime,releaseRumorDetailContent;
    private TitleBar titleBar;
    private RumorBean rumorBean;
    private ButtonView releaseRumorDetailSupportBtn , releaseRumorDetailRefuseBtn;
    private AwayLieSQLiteOpenHelper mHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_release_rumor);
        initView();//初始化控件
        releaseRumorDetailSupportBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ReleaseRumorActivity.this,"赞成",Toast.LENGTH_SHORT).show();
            }
        });
        releaseRumorDetailRefuseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ReleaseRumorActivity.this,"反对",Toast.LENGTH_SHORT).show();
            }
        });


    }

    //查询数据并且进行设置
    private void getData(){
        Intent intent = getIntent();
        int vId = intent.getIntExtra("id",0);
        rumorBean = mHelper.queryByRumorById(vId);//按照id进行查询
        releaseRumorDetailTitle.setText(rumorBean.getTitle());
        releaseRumorDetailName.setText(rumorBean.getName());
        releaseRumorDetailTime.setText(rumorBean.getTime());
        releaseRumorDetailContent.setText(rumorBean.getContent());
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
        releaseRumorDetailTitle = findViewById(R.id.release_rumor_detail_title);
        releaseRumorDetailName = findViewById(R.id.release_rumor_detail_name);
        releaseRumorDetailTime = findViewById(R.id.release_rumor_detail_time);
        releaseRumorDetailContent = findViewById(R.id.release_rumor_detail_content);
        releaseRumorDetailRefuseBtn = findViewById(R.id.release_rumor_detail_refuse);
        releaseRumorDetailSupportBtn = findViewById(R.id.release_rumor_detail_support);
        titleBar = findViewById(R.id.release_rumor_detail_title_bar);

        titleBar.setLeftClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }

    @Override
    protected void onPause() {
        super.onPause();
        mHelper.closeDB();
    }
}