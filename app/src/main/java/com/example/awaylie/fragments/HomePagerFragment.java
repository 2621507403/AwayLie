package com.example.awaylie.fragments;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.example.awaylie.R;
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
    private List<String> titles = new ArrayList<>();//轮播图标题
    private List<String> urls = new ArrayList<>();//轮播图对应链接

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
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(url));
                startActivity(intent);
            }
        });
    }
    private void initBannerData() {
        images.add("https://www.piyao.org.cn/2023pyyxzp/img/banner.jpg");
        images.add("https://www.piyao.org.cn/yybgt/index/images/0426/images_01.jpg");
        images.add("https://www.piyao.org.cn/ylzt/images2022/banner_ylzp.png");
        titles.add("Image 1");
        titles.add("Image 2");
        titles.add("Image 3");
        urls.add("https://www.piyao.org.cn/2022zp/index.html");
        urls.add("https://www.piyao.org.cn/yybgt/index.htm");
        urls.add("https://www.piyao.org.cn/ylzt/index.htm");
    }


}