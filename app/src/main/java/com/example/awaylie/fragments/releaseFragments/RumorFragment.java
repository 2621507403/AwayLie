package com.example.awaylie.fragments.releaseFragments;

import android.content.Context;
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
import android.widget.PopupMenu;

import com.example.awaylie.R;
import com.example.awaylie.adapter.RumorRecyclerViewAdapter;
import com.example.awaylie.bean.RumorBean;
import com.example.awaylie.database.AwayLieSQLiteOpenHelper;
import com.example.awaylie.interfaceClass.OnItemLongClickListener;

import java.util.List;

/**
 * 谣言页的编写，主要是用于显示用户发布的已经被证实的谣言
 * */
public class RumorFragment extends Fragment {

    private RecyclerView releaseRumorRV;
    private AwayLieSQLiteOpenHelper mHelper;
    private SwipeRefreshLayout releaseRumorRefresh;
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
        return inflater.inflate(R.layout.fragment_rumor, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        releaseRumorRV = view.findViewById(R.id.release_rumorRV);
        releaseRumorRefresh = view.findViewById(R.id.release_rumor_refresh);
        releaseRumorRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                List<RumorBean> rumorBeanList = mHelper.queryAllRumor();//查询数据库中的数据
                RumorRecyclerViewAdapter adapter = (RumorRecyclerViewAdapter) releaseRumorRV.getAdapter();
                adapter.setVerifyBeanList(rumorBeanList,getActivity());
                adapter.notifyDataSetChanged();
                //隐藏
                releaseRumorRefresh.setRefreshing(false);
            }
        });
    }
    @Override
    public void onResume() {
        super.onResume();
        // 获取保存的浏览位置
        SharedPreferences sharedPreferences = getActivity().getPreferences(Context.MODE_PRIVATE);
        int position = sharedPreferences.getInt("rumorPosition", 0);
        // 恢复浏览位置
        LinearLayoutManager layoutManager = (LinearLayoutManager) releaseRumorRV.getLayoutManager();
        layoutManager.scrollToPositionWithOffset(position, 0);
    }
    @Override
    public void onPause() {
        super.onPause();
        // 获取当前浏览位置
        LinearLayoutManager layoutManager = (LinearLayoutManager) releaseRumorRV.getLayoutManager();
        int position = layoutManager.findFirstVisibleItemPosition();
        // 保存当前浏览位置
        SharedPreferences sharedPreferences = getActivity().getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("rumorPosition", position);
        editor.apply();
    }

    //对recyclerView的初始化及数据展示
    private void initRecyclerView(){
        List<RumorBean> rumorBeanList = mHelper.queryAllRumor();//查询数据库中的数据
        Log.d("VerifyFragment", "initRecyclerView: "+rumorBeanList);
        //创建适配器对象
        RumorRecyclerViewAdapter rumorAdapter = new RumorRecyclerViewAdapter();
        rumorAdapter.setVerifyBeanList(rumorBeanList,getActivity());
        rumorAdapter.setItemLongClickListener(new OnItemLongClickListener() {
            @Override
            public void onItemLongClick(View view, int position) {
                PopupMenu deletePop = new PopupMenu(getActivity(),view);
                deletePop.inflate(R.menu.verify_item_menu);
                deletePop.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        if (item.getItemId() == R.id.verify_item_delete){
                            RumorBean rumorBean = rumorBeanList.get(position);
                            rumorBeanList.remove(position);
                            rumorAdapter.notifyItemRemoved(position);
                            mHelper.deleteRumorItemById(rumorBean.getId());
                        }
                        return true;
                    }
                });

                deletePop.show();
            }
        });



        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        releaseRumorRV.setLayoutManager(linearLayoutManager);
        releaseRumorRV.setAdapter(rumorAdapter);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mHelper.closeDB();//关闭数据库
    }
}