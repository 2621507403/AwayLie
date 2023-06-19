package com.example.awaylie;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.example.awaylie.bean.WeatherBean;
import com.example.awaylie.controller.WeatherController;
import com.example.awaylie.fragments.HomePagerFragment;
import com.example.awaylie.fragments.MessageFragment;
import com.example.awaylie.fragments.MineFragment;
import com.example.awaylie.fragments.ReleaseFragment;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;
import java.util.List;

/**
     * MainActivity中主要用于承载fragment
     *
     *
     * */
public class MainActivity extends AppCompatActivity {
    private TextView topBarWeatherTV;
    private ViewPager2 mainPagerVP2;
    private TabLayout mainPagerTabLayout;
    private List<Fragment> fragments;

    private static final int BACK_PRESS_INTERVAL = 2000; // 2秒内再次按下返回键才退出应用

    private long mLastBackPressTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        topBarWeatherTV = findViewById(R.id.main_Topbar_Weather);
        mainPagerVP2 = findViewById(R.id.main_pager_vp2);
        mainPagerTabLayout = findViewById(R.id.main_pager_tabLayout);
        initMainActivity();//对界面的初始化
        mainPagerVP2.setUserInputEnabled(false);
    }

    //初始化四个界面以及底部导航栏
    private void initMainActivity(){
        fragments = new ArrayList<>();
        fragments.add(new HomePagerFragment());
        fragments.add(new ReleaseFragment());
        fragments.add(new MessageFragment());
        fragments.add(new MineFragment());

        //对viewpager进行操作
        mainPagerVP2.setAdapter(new FragmentStateAdapter(MainActivity.this) {
            @NonNull
            @Override
            public Fragment createFragment(int position) {
                return fragments.get(position);
            }
            @Override
            public int getItemCount() {
                return fragments.size();
            }
        });

        new TabLayoutMediator(mainPagerTabLayout, mainPagerVP2, (tab, position) -> {//此处使用了lambda表达
                switch (position){
                    case 0:
                        tab.setText("首页");
                        tab.setIcon(R.mipmap.home_select);
                        break;
                    case 1:
                        tab.setText("发布");
                        tab.setIcon(R.mipmap.dynamic);
                        break;
                    case 2:
                        tab.setText("消息");
                        tab.setIcon(R.mipmap.message);
                        break;
                    case 3:
                        tab.setText("我的");
                        tab.setIcon(R.mipmap.mine);
                        break;
                }
        }).attach();

        //设置选中时，图标的改变
        mainPagerTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()){
                    case  0:
                        tab.setIcon(R.mipmap.home_select);
                        break;
                    case  1:
                        tab.setIcon(R.mipmap.dynamic_select);
                        break;
                    case  2:
                        tab.setIcon(R.mipmap.message_select);
                        break;
                    case  3:
                        tab.setIcon(R.mipmap.mine_select);
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                switch (tab.getPosition()){
                    case  0:
                        tab.setIcon(R.mipmap.home);
                        break;
                    case  1:
                        tab.setIcon(R.mipmap.dynamic);
                        break;
                    case  2:
                        tab.setIcon(R.mipmap.message);
                        break;
                    case  3:
                        tab.setIcon(R.mipmap.mine);
                        break;
                }
            }
            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
        //设置进入界面时的显示页
        mainPagerVP2.setCurrentItem(0);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    //这个函数用来获取到天气信息,位置信息从sharedPreference中获取，这里模拟一下
    //这个函数用于给变量weatherText进行初始化操作，同时能够给ui进行更新操作
    private void getWeather(String cityName){
        //创建子线程，实现对天气的获取
        HandlerThread weatherThread = new HandlerThread("weatherThread");
        weatherThread.start();
        Handler handler = new Handler(weatherThread.getLooper()){
            private String weatherText;
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                if (msg.what == 1){
                    weatherText = (String) msg.obj;
                }
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        //在主线程中更新ui
                        Log.d("MainActivity", "onCreate: "+weatherText);
                        topBarWeatherTV.setText(weatherText);
                    }
                });
            }
        };
        handler.post(new Runnable() {
            @Override
            public void run() {
                getWeather("合肥");//这里的合肥是模拟数据
                WeatherController weatherController = new WeatherController();
                String cityCode = weatherController.getCityCodeByName(cityName);
                List<WeatherBean> weatherBeanList = weatherController.getWeatherByCityCode(cityCode);
                Message m = Message.obtain();
                m.obj = weatherBeanList.get(0).getText()+","+weatherBeanList.get(0).getTemp()+"℃";;
                m.what = 1;
                handler.sendMessage(m);
            }
        });
    }

    @Override
    public void onBackPressed() {
        long currentTime = System.currentTimeMillis();
        if (currentTime - mLastBackPressTime < BACK_PRESS_INTERVAL) {
            // 2秒内再次按下返回键，退出应用
            super.onBackPressed();
        } else {
            // 弹出提示
            Toast.makeText(this, "再按一次返回键退出应用", Toast.LENGTH_SHORT).show();
            mLastBackPressTime = currentTime;
        }
    }

}