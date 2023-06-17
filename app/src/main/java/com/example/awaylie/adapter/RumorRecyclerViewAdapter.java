package com.example.awaylie.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.awaylie.R;
import com.example.awaylie.ReleaseRumorActivity;
import com.example.awaylie.ReleaseVerifyActivity;
import com.example.awaylie.bean.RumorBean;
import com.example.awaylie.bean.VerifyBean;
import com.xuexiang.xui.widget.button.ButtonView;

import java.util.List;

public class RumorRecyclerViewAdapter extends RecyclerView.Adapter<RumorRecyclerViewAdapter.MyVerifyViewHold> {
    private List<RumorBean> rumorBeanList;
    private Context context;

    public void setVerifyBeanList(List<RumorBean> rumorBeanList,Context context) {
        this.rumorBeanList = rumorBeanList;
        this.context = context;
    }

    @NonNull
    @Override
    public MyVerifyViewHold onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.release_rumor_item,parent,false);
        return new MyVerifyViewHold(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyVerifyViewHold holder, int position) {
        RumorBean rumorBean = rumorBeanList.get(position);//通过list获取单个的对象
        holder.releaseRumorPersonName.setText(rumorBean.getName());
        holder.releaseRumorPersonTitle.setText(rumorBean.getTitle());
        holder.releaseRumorTime.setText(rumorBean.getTime());
        holder.releaseRumorDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ReleaseRumorActivity.class);
                intent.putExtra("id",rumorBean.getId());//传过去后，在ReleaseRumorActivity界面通过id获取到新闻消息，从而进行更完整的显示
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return rumorBeanList.size();
    }

    class MyVerifyViewHold extends RecyclerView.ViewHolder{
        private TextView releaseRumorPersonName,releaseRumorPersonTitle;
        private TextView releaseRumorKeyword,releaseRumorTime;
        private ButtonView releaseRumorDetail;
        public MyVerifyViewHold(@NonNull View itemView) {
            super(itemView);
            releaseRumorPersonName = itemView.findViewById(R.id.release_rumor_person_name);
            releaseRumorPersonTitle = itemView.findViewById(R.id.release_rumor_person_title);
            releaseRumorTime = itemView.findViewById(R.id.release_rumor_time);
            releaseRumorDetail = itemView.findViewById(R.id.release_rumor_detail_btn);


        }
    }

}
