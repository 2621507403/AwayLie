package com.example.awaylie;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;

import com.example.awaylie.bean.RumorBean;
import com.example.awaylie.bean.TruthBean;
import com.example.awaylie.bean.VerifyBean;
import com.example.awaylie.database.AwayLieSQLiteOpenHelper;
import com.example.awaylie.utils.TimeGetUtil;
import com.xuexiang.xui.widget.actionbar.TitleBar;
import com.xuexiang.xui.widget.button.ButtonView;
import com.xuexiang.xui.widget.edittext.MultiLineEditText;
import com.xuexiang.xui.widget.tabbar.TabControlView;
import com.xuexiang.xui.widget.textview.badge.BadgeView;

/**
 * 这个界面是证实界面， 证实结果为谣言或者真相
 * */
public class ReleaseV2RActivity extends AppCompatActivity {
    private TitleBar titleBar;
    private TabControlView detailVorRSelect;
    private AwayLieSQLiteOpenHelper mHelper;
    private ButtonView detailVorRSubmit;
    private VerifyBean verifyBean;//用于存放查询到的待证实信息
    private MultiLineEditText detailEditContent;
    private int rumorNum;
    private int truthNum;
    private SharedPreferences userNameSP;
    private String userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_release_v2_ractivity);
        //获取用户信息
        userNameSP = getSharedPreferences("userInfo",MODE_PRIVATE);
        userName = userNameSP.getString("name","");

        initView();
        titleBar.setLeftClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //设置响应情况，分别存储在不同的数据库中
        detailVorRSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (detailVorRSelect.getChecked() == "0"){
                    //插入到rumor
                    RumorBean rumorBean = new RumorBean();
                    rumorBean.setVerifyId(verifyBean.getId());
                    rumorBean.setTime(TimeGetUtil.getTime());
                    rumorBean.setName(userName);
                    rumorBean.setTitle(verifyBean.getTitle());
                    rumorBean.setContent(detailEditContent.getContentText());
                    if (mHelper.insert2rumor(rumorBean) > 0){
                        Toast.makeText(ReleaseV2RActivity.this,"提交成功",Toast.LENGTH_SHORT).show();
                        finish();
                    }else
                        Toast.makeText(ReleaseV2RActivity.this,"提交失败",Toast.LENGTH_SHORT).show();

                }else {
                    //插入到truth
                    TruthBean truthBean = new TruthBean();
                    truthBean.setVerifyId(verifyBean.getId());
                    truthBean.setTime(TimeGetUtil.getTime());
                    truthBean.setName(userName);
                    truthBean.setTitle(verifyBean.getTitle());
                    truthBean.setContent(detailEditContent.getContentText());
                    if (mHelper.insert2truth(truthBean) > 0){
                        Toast.makeText(ReleaseV2RActivity.this,"提交成功",Toast.LENGTH_SHORT).show();
                        finish();
                    }else
                        Toast.makeText(ReleaseV2RActivity.this,"提交失败",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        mHelper = AwayLieSQLiteOpenHelper.getInstance(ReleaseV2RActivity.this);
        mHelper.openReadLink();
        mHelper.openWriteLink();
        showTitle();


    }
    //显示顶部bar的标题
    private void showTitle(){
        Intent verifyIdIntent = getIntent();
        int verifyId = verifyIdIntent.getIntExtra("questionId",0);
        //在数据库查询数据，并且放于标题栏中
        verifyBean = mHelper.queryByVerifyById(verifyId);
        titleBar.setTitle(verifyBean.getTitle());
        titleBar.setSubTitle(verifyBean.getName());
        rumorNum = mHelper.queryRawByIdRumor(verifyBean.getId());
        truthNum = mHelper.queryRawByIdTruth(verifyBean.getId());
        detailVorRSelect.setOnTabSelectionChangedListener(new TabControlView.OnTabSelectionChangedListener() {
            @Override
            public void newSelection(String title, String value) {
                switch (value){
                    case "0":
                        new BadgeView(ReleaseV2RActivity.this).bindTarget(detailVorRSelect)
                                .setBadgeGravity(Gravity.START|Gravity.TOP).setBadgeNumber(rumorNum);
                        break;
                    case "1":
                        new BadgeView(ReleaseV2RActivity.this).bindTarget(detailVorRSelect).setBadgeNumber(truthNum);
                        break;
                }
            }
        });
    }
    //对ui界面进行初始化
    private void initView()  {
        titleBar = findViewById(R.id.verify_title_bar);
        detailVorRSelect = findViewById(R.id.detail_v2r_select);
        detailVorRSubmit = findViewById(R.id.detail_v2r_submit);
        detailEditContent = findViewById(R.id.detail_v2r_edit_content);
        String[] selectItems = {"谣言","事实"};
        String[] valueItems = {"0","1"};
        try {
            detailVorRSelect.setItems(selectItems,valueItems,0);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }


    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mHelper.closeDB();
    }
}