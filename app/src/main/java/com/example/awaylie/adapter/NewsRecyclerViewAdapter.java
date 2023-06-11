package com.example.awaylie.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.awaylie.R;
import com.example.awaylie.WebActivity;
import com.example.awaylie.bean.HomePagerNewsDataBean;

import java.util.List;

public class NewsRecyclerViewAdapter extends RecyclerView.Adapter<NewsRecyclerViewAdapter.MyViewHolder> {
    private List<HomePagerNewsDataBean> homePagerNewsDataBean;
    private Context context;

    public void setHomePagerNewsDataBean(List<HomePagerNewsDataBean> homePagerNewsDataBean) {
        this.homePagerNewsDataBean = homePagerNewsDataBean;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public NewsRecyclerViewAdapter() {
    }

    public NewsRecyclerViewAdapter(List<HomePagerNewsDataBean> homePagerNewsDataBean) {
        this.homePagerNewsDataBean = homePagerNewsDataBean;
    }
    //这个方法用于将item使用的layout布局与recyclerView相关联
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_data_layout_item,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        HomePagerNewsDataBean newsDataBean = homePagerNewsDataBean.get(position);//获取HomePagerNewsDataBean对象
        Glide.with(context)
                .load(newsDataBean.getImageUrl())
                .into(holder.newsImg);
        holder.newsTitle.setText(newsDataBean.getText());
        holder.newsSource.setText(newsDataBean.getNewsSource());
        holder.newsLook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = newsDataBean.getNewsUrl();
                Intent intent = new Intent(context, WebActivity.class);
                intent.putExtra("url",url);
                intent.putExtra("title",newsDataBean.getText());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return homePagerNewsDataBean.size();
    }

    //这个方法类似于初始化操作，就是找到每个item中的组件
    class MyViewHolder extends RecyclerView.ViewHolder{
        public ImageView newsImg;
        public TextView newsTitle,newsSource;
        public ImageButton newsLook;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            newsImg = itemView.findViewById(R.id.newsDataIV);
            newsTitle = itemView.findViewById(R.id.newsDataTitleTV);
            newsSource = itemView.findViewById(R.id.newsDataSourceTV);
            newsLook = itemView.findViewById(R.id.newsDataDetailBtn);
        }
    }
}
