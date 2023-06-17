package com.example.awaylie.fragments.releaseFragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.example.awaylie.R;
import com.example.awaylie.ReleaseQuestionActivity;
import com.example.awaylie.adapter.RumorRecyclerViewAdapter;
import com.example.awaylie.adapter.TruthRecyclerViewAdapter;
import com.example.awaylie.adapter.VerifyRecyclerViewAdapter;
import com.example.awaylie.bean.RumorBean;
import com.example.awaylie.bean.TruthBean;
import com.example.awaylie.bean.VerifyBean;
import com.example.awaylie.database.AwayLieSQLiteOpenHelper;

import java.util.List;

/**
 * 证实页的编写，主要是用于显示发布者发布的待求证的内容
 *
 * */
public class TruthFragment extends Fragment {
    private RecyclerView releaseTruthRV;
    private AwayLieSQLiteOpenHelper mHelper;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onStart() {
        super.onStart();
        mHelper = AwayLieSQLiteOpenHelper.getInstance(getActivity());//对数据库操作权限的开启
        mHelper.openReadLink();
        mHelper.openWriteLink();
        initRecyclerView();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_truth, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        releaseTruthRV = view.findViewById(R.id.release_truthRV);
    }
    @Override
    public void onResume() {
        super.onResume();
        // 获取保存的浏览位置
        SharedPreferences sharedPreferences = getActivity().getPreferences(Context.MODE_PRIVATE);
        int position = sharedPreferences.getInt("truthPosition", 0);
        // 恢复浏览位置
        LinearLayoutManager layoutManager = (LinearLayoutManager) releaseTruthRV.getLayoutManager();
        layoutManager.scrollToPositionWithOffset(position, 0);
    }
    @Override
    public void onPause() {
        super.onPause();
        // 获取当前浏览位置
        LinearLayoutManager layoutManager = (LinearLayoutManager) releaseTruthRV.getLayoutManager();
        int position = layoutManager.findFirstVisibleItemPosition();
        // 保存当前浏览位置
        SharedPreferences sharedPreferences = getActivity().getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("truthPosition", position);
        editor.apply();
    }
    //对recyclerView的初始化及数据展示
    private void initRecyclerView(){
        List<TruthBean> truthBeanList = mHelper.queryAllTruth();//查询数据库中的数据
        //创建适配器对象
        TruthRecyclerViewAdapter truthAdapter = new TruthRecyclerViewAdapter();
        truthAdapter.setVerifyBeanList(truthBeanList,getActivity());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        releaseTruthRV.setLayoutManager(linearLayoutManager);
        releaseTruthRV.setAdapter(truthAdapter);
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        mHelper.closeDB();//关闭数据库
    }
}