package com.example.awaylie;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import com.example.awaylie.adapter.SearchResultRecyclerViewAdapter;
import com.example.awaylie.bean.RumorBean;
import com.example.awaylie.bean.TruthBean;
import com.example.awaylie.bean.VerifyBean;
import com.example.awaylie.database.AwayLieSQLiteOpenHelper;

import java.util.List;

public class SearchResultActivity extends AppCompatActivity {

    private RecyclerView searchResultRV;
    private List<VerifyBean> verifyBeanList ;
    private List<RumorBean> rumorBeanList ;
    private List<TruthBean> truthBeanList ;
    //数据库操作
    private AwayLieSQLiteOpenHelper mHelper;
    //适配器
    private SearchResultRecyclerViewAdapter recyclerViewAdapter;

    private String searchTitle;

    @Override
    protected void onStart() {
        super.onStart();
        mHelper = AwayLieSQLiteOpenHelper.getInstance(this);
        mHelper.openReadLink();
        //创建子线程，获取三类数据值,这里未使用handler
        new Thread(new Runnable() {
            @Override
            public void run() {
                verifyBeanList = mHelper.queryAllVerifyByTitle(searchTitle);
                rumorBeanList = mHelper.queryAllRumorByTitle(searchTitle);
                truthBeanList = mHelper.queryAllTruthByTitle(searchTitle);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        recyclerViewAdapter.setVerifyBeanList(verifyBeanList);
                        recyclerViewAdapter.setRumorBeanList(rumorBeanList);
                        recyclerViewAdapter.setTruthBeanList(truthBeanList);
                        recyclerViewAdapter.notifyDataSetChanged();
                    }
                });
            }
        }).start();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);
        searchResultRV = findViewById(R.id.search_result_rv);
        //获取标题title
        Intent intent = getIntent();
        searchTitle = intent.getStringExtra("title");
        initRV();
    }

    private void initRV(){
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerViewAdapter = new SearchResultRecyclerViewAdapter(this);
        searchResultRV.setLayoutManager(linearLayoutManager);
        searchResultRV.setAdapter(recyclerViewAdapter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mHelper.closeDB();
    }
}