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
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.PopupMenu;

import com.example.awaylie.R;
import com.example.awaylie.ReleaseQuestionActivity;
import com.example.awaylie.adapter.VerifyRecyclerViewAdapter;
import com.example.awaylie.bean.VerifyBean;
import com.example.awaylie.database.AwayLieSQLiteOpenHelper;
import com.example.awaylie.interfaceClass.OnItemLongClickListener;
import com.xuexiang.xui.widget.button.shadowbutton.ShadowButton;

import java.util.List;

/**
     * 求证页的编写，主要是用于显示发布者发布的待求证的内容
     *
     * */
public class VerifyFragment extends Fragment {
    private ShadowButton releaseIBtn;
    private RecyclerView releaseVerifyRV;
    private AwayLieSQLiteOpenHelper mHelper;

    private SwipeRefreshLayout releaseRefresh;
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
        return inflater.inflate(R.layout.fragment_verify, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        releaseIBtn = view.findViewById(R.id.release_IB);
        releaseVerifyRV = view.findViewById(R.id.release_verify_rv);
        releaseRefresh = view.findViewById(R.id.release_verify_refresh);
        releaseRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                List<VerifyBean> verifyBeanList = mHelper.queryAllVerify();//查询数据库中的数据
                VerifyRecyclerViewAdapter verifyRecyclerViewAdapter = (VerifyRecyclerViewAdapter) releaseVerifyRV.getAdapter();
                verifyRecyclerViewAdapter.setVerifyBeanList(verifyBeanList,getActivity());
                verifyRecyclerViewAdapter.notifyDataSetChanged();
                //刷新完成后，隐藏进度指示器
                releaseRefresh.setRefreshing(false);
            }
        });
        releaseIBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ReleaseQuestionActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        // 获取保存的浏览位置
        SharedPreferences sharedPreferences = getActivity().getPreferences(Context.MODE_PRIVATE);
        int position = sharedPreferences.getInt("verifyPosition", 0);
        // 恢复浏览位置
        LinearLayoutManager layoutManager = (LinearLayoutManager) releaseVerifyRV.getLayoutManager();
        layoutManager.scrollToPositionWithOffset(position, 0);
    }

    @Override
    public void onPause() {
        super.onPause();
        // 获取当前浏览位置
        LinearLayoutManager layoutManager = (LinearLayoutManager) releaseVerifyRV.getLayoutManager();
        int position = layoutManager.findFirstVisibleItemPosition();
        // 保存当前浏览位置
        SharedPreferences sharedPreferences = getActivity().getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("verifyPosition", position);
        editor.apply();
    }


    //对recyclerView的初始化及数据展示
    private void initRecyclerView(){
        List<VerifyBean> verifyBeanList = mHelper.queryAllVerify();//查询数据库中的数据
//        Log.d("VerifyFragment", "initRecyclerView: "+verifyBeanList);
        //创建适配器对象
        VerifyRecyclerViewAdapter verifyAdapter = new VerifyRecyclerViewAdapter();
        verifyAdapter.setVerifyBeanList(verifyBeanList,getActivity());
        verifyAdapter.setmOnItemLongClickListener(new OnItemLongClickListener() {
            @Override
            public void onItemLongClick(View view, int position) {
                Log.d("verify", "onItemLongClick: "+position);
                //弹出菜单
                PopupMenu deletePop = new PopupMenu(getActivity(),view);
                deletePop.inflate(R.menu.verify_item_menu);
                deletePop.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        if (item.getItemId() == R.id.verify_item_delete){
                            VerifyBean verifyBean = verifyBeanList.get(position);
                            verifyBeanList.remove(position);
                            verifyAdapter.notifyItemRemoved(position);
                            mHelper.deleteVerifyItemById(verifyBean.getId());
                        }
                        return true;
                    }
                });
                deletePop.show();
            }
        });

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        releaseVerifyRV.setLayoutManager(linearLayoutManager);
        releaseVerifyRV.setAdapter(verifyAdapter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mHelper.closeDB();//关闭数据库
    }
}