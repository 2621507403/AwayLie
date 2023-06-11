package com.example.awaylie;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.TextView;

public class WebActivity extends AppCompatActivity {
    private WebView webView;
    private TextView webTitle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        webView = findViewById(R.id.webShow);

        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        //设置js可以直接打开窗口，如window.open()，默认为false
        webView.getSettings().setJavaScriptEnabled(true);
        //是否允许执行js，默认为false。设置true时，会提醒可能造成XSS漏洞
        webView.getSettings().setSupportZoom(true);
        //是否可以缩放，默认true
        webView.getSettings().setBuiltInZoomControls(true);
        // 是否显示缩放按钮，默认false
        webView.getSettings().setUseWideViewPort(true);
        // 设置此属性，可任意比例缩放。大视图模式
        webView.getSettings().setLoadWithOverviewMode(true);
        // 是否使用缓存
        webView.getSettings().setDomStorageEnabled(true);//DOM Storage


        webTitle = findViewById(R.id.webTitle);
        String url = getIntent().getStringExtra("url"); // 获取从Fragment传递过来的网页URL
        String title = getIntent().getStringExtra("title"); // 获取从Fragment传递过来的网页Title
        webTitle.setText(title);
        webView.loadUrl(url);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        webView.clearCache(true);
        webView.clearHistory();
    }

}