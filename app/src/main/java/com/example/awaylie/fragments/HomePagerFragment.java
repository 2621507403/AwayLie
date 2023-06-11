package com.example.awaylie.fragments;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.example.awaylie.MainActivity;
import com.example.awaylie.R;
import com.example.awaylie.WebActivity;
import com.example.awaylie.adapter.NewsRecyclerViewAdapter;
import com.example.awaylie.bean.HomePagerNewsDataBean;
import com.example.awaylie.utils.NewsDataUtil;
import com.youth.banner.Banner;
import com.youth.banner.adapter.BannerImageAdapter;
import com.youth.banner.holder.BannerImageHolder;
import com.youth.banner.indicator.CircleIndicator;
import com.youth.banner.listener.OnBannerListener;

import java.util.ArrayList;
import java.util.List;


public class HomePagerFragment extends Fragment {
    private Banner homaPagerBanner;
    private List<String> images = new ArrayList<>();//轮播图图片
    private List<String> urls = new ArrayList<>();//轮播图对应链接
    private List<String> titles = new ArrayList<>();//轮播图主题
    private RecyclerView newsDataRV;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home_pager, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initBannerData();//对图片数据源进行获取
        initBanner(view);//对轮播进行设置
        showData(view);

    }



    /**
     * 对新闻消息的显示
     * */
    private void showData(View view){
        //对newsData进行设置
        List<HomePagerNewsDataBean> newsDataBeanList ;//存放消息的list列表
        newsDataRV = view.findViewById(R.id.homePager_newsData);
        //设置布局方向
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        newsDataRV.setLayoutManager(layoutManager);
        //设置适配器
        NewsRecyclerViewAdapter newsAdapter = new NewsRecyclerViewAdapter();
        //通过子线程完成网络请求
        HandlerThread newsHandlerThread = new HandlerThread("newsData");
        newsHandlerThread.start();
        Handler newsHandler = new Handler(newsHandlerThread.getLooper()){
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                final List<HomePagerNewsDataBean> list ;
                list = (List<HomePagerNewsDataBean>) msg.obj;
                Log.d("awaylie", "handleMessage: "+list);
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        newsAdapter.setHomePagerNewsDataBean(list);
                        newsAdapter.setContext(getContext());
                        newsDataRV.setAdapter(newsAdapter);
                    }
                });
            }
        };
        newsHandler.post(new Runnable() {
            @Override
            public void run() {
                List<HomePagerNewsDataBean> list = NewsDataUtil.getNewsData();
                Log.d("awaylie", "run: "+list);
                Message msg = Message.obtain();
                msg.obj = list;
                newsHandler.sendMessage(msg);
            }
        });

    }

    /**
     * 对轮播图进行初始化
     * */
    private void initBanner(View view){
        homaPagerBanner = view.findViewById(R.id.homePager_banner);
        //设置图片
        homaPagerBanner.setAdapter(new BannerImageAdapter(images) {
            @Override
            public void onBindView(Object holder, Object data, int position, int size) {
                BannerImageHolder bannerImageHolder = (BannerImageHolder) holder;
                Glide.with(bannerImageHolder.itemView)
                        .load(data)
                        .into(bannerImageHolder.imageView);
            }
        }).addBannerLifecycleObserver(this)//添加生命周期观察者
            .setIndicator(new CircleIndicator(getActivity()));

        //设置监听事件
        homaPagerBanner.setOnBannerListener(new OnBannerListener() {
            @Override
            public void OnBannerClick(Object data, int position) {
                // 在这里处理图片点击事件
                // 例如，打开一个网页
                String url = urls.get(position);
                Intent intent = new Intent(getActivity(), WebActivity.class);
                intent.putExtra("url",url);
                intent.putExtra("title",titles.get(position));
                startActivity(intent);
            }
        });
    }
    private void initBannerData() {
        images.add("https://www.piyao.org.cn/2023pyyxzp/img/banner.jpg");
        images.add("https://www.piyao.org.cn/yybgt/index/images/0426/images_01.jpg");
        images.add("https://www.piyao.org.cn/ylzt/images2022/banner_ylzp.png");
        urls.add("https://www.piyao.org.cn/2022zp/index.html");
        urls.add("https://www.piyao.org.cn/yybgt/index.htm");
        urls.add("https://www.piyao.org.cn/ylzt/index.htm");
        titles.add("辟谣优秀作品发布");
        titles.add("网络谣言曝光台");
        titles.add("打击整顿养老诈骗");
    }
}