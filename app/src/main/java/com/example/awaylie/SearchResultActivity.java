package com.example.awaylie;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import com.example.awaylie.adapter.SearchResultRecyclerViewAdapter;
import com.example.awaylie.bean.HeaderBean;
import com.example.awaylie.bean.RumorBean;
import com.example.awaylie.bean.TruthBean;
import com.example.awaylie.bean.VerifyBean;
import com.example.awaylie.database.AwayLieSQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class SearchResultActivity extends AppCompatActivity {

    private RecyclerView searchResultRV;
    private List<Object> allDataList = new ArrayList<>();
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

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //数据库初始化操作
        mHelper = AwayLieSQLiteOpenHelper.getInstance(this);
        mHelper.openReadLink();

        setContentView(R.layout.activity_search_result);
        searchResultRV = findViewById(R.id.search_result_rv);
        //获取标题title
        Intent intent = getIntent();
        searchTitle = intent.getStringExtra("title");
        initRV();


        //从数据库中获取数据,开启子线程进行操作
        new Thread(new Runnable() {
            @Override
            public void run() {
                verifyBeanList = mHelper.queryAllVerifyByTitle(searchTitle);
                rumorBeanList = mHelper.queryAllRumorByTitle(searchTitle);
                truthBeanList = mHelper.queryAllTruthByTitle(searchTitle);
                allDataList.add(new HeaderBean("求证"));
                allDataList.addAll(verifyBeanList);
                allDataList.add(new HeaderBean("谣言"));
                allDataList.addAll(rumorBeanList);
                allDataList.add(new HeaderBean("真相"));
                allDataList.addAll(truthBeanList);
                //数据创建完毕，对适配器进行赋值并且刷新
                recyclerViewAdapter.setItemsList(allDataList);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        recyclerViewAdapter.notifyDataSetChanged();
                    }
                });

            }
        }).start();
    }

    private void initRV(){
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        searchResultRV.setLayoutManager(linearLayoutManager);
        recyclerViewAdapter = new SearchResultRecyclerViewAdapter();
        searchResultRV.setAdapter(recyclerViewAdapter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mHelper.closeDB();
    }
}