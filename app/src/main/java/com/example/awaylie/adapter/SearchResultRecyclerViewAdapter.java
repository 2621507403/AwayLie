package com.example.awaylie.adapter;

import android.content.Context;
import android.content.Intent;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.awaylie.R;
import com.example.awaylie.ReleaseRumorActivity;
import com.example.awaylie.ReleaseTruthActivity;
import com.example.awaylie.ReleaseVerifyActivity;
import com.example.awaylie.bean.HeaderBean;
import com.example.awaylie.bean.RumorBean;
import com.example.awaylie.bean.TruthBean;
import com.example.awaylie.bean.VerifyBean;
import com.xuexiang.xui.widget.button.ButtonView;

import java.util.List;
import java.util.Objects;

public class SearchResultRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<Object> itemsList;
    private static final int TYPE_HEADER = 0;
    private static final int TYPE_VERIFY = 1;
    private static final int TYPE_RUMOR = 2;
    private static final int TYPE_TRUTH = 3;
    private Context context;

    public void setItemsList(List<Object> itemsList) {
        this.itemsList = itemsList;
    }

    @Override
    public int getItemViewType(int position) {
        if (itemsList.get(position) instanceof HeaderBean)
            return TYPE_HEADER;
        else if (itemsList.get(position) instanceof VerifyBean)
            return TYPE_VERIFY;
        else if (itemsList.get(position) instanceof RumorBean)
            return TYPE_RUMOR;
        else
            return TYPE_TRUTH;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        context = parent.getContext();
        if (viewType == TYPE_HEADER){
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_header_item,parent,false);
            return new HeaderViewHolder(view);
        }else if (viewType == TYPE_VERIFY){
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.release_verify_item,parent,false);
            return new VerifyViewHolder(view);
        }else if (viewType == TYPE_RUMOR){
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.release_rumor_item,parent,false);
            return new RumorViewHolder(view);
        }else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.release_truth_item,parent,false);
            return new RumorViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof HeaderViewHolder){
            HeaderBean headerBean = (HeaderBean) itemsList.get(position);
            ((HeaderViewHolder) holder).textView.setText(headerBean.getTitle());
        }else if (holder instanceof VerifyViewHolder){
            VerifyBean verifyBean = (VerifyBean) itemsList.get(position);
            ((VerifyViewHolder) holder).releaseVerifyPersonName.setText(verifyBean.getName());
            ((VerifyViewHolder) holder).releaseVerifyPersonTitle.setText(verifyBean.getTitle());
            ((VerifyViewHolder) holder).releaseVerifyKeyword.setText(verifyBean.getKeyword());
            ((VerifyViewHolder) holder).releaseVerifyTime.setText(verifyBean.getTime());
            ((VerifyViewHolder) holder).releaseVerifyDetail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, ReleaseVerifyActivity.class);
                    intent.putExtra("id",verifyBean.getId());//传过去后，在ReleaseVerifyActivity界面通过id获取到新闻消息，从而进行更完整的显示
                    context.startActivity(intent);
                }
            });
        }else if (holder instanceof RumorViewHolder){
            RumorBean rumorBean = (RumorBean) itemsList.get(position);
            ((RumorViewHolder) holder).releaseRumorPersonName.setText(rumorBean.getName());
            ((RumorViewHolder) holder).releaseRumorPersonTitle.setText(rumorBean.getTitle());
            ((RumorViewHolder) holder).releaseRumorTime.setText(rumorBean.getTime());
            ((RumorViewHolder) holder).releaseRumorDetail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, ReleaseRumorActivity.class);
                    intent.putExtra("id",rumorBean.getId());//传过去后，在ReleaseRumorActivity界面通过id获取到新闻消息，从而进行更完整的显示
                    context.startActivity(intent);
                }
            });
        } else if (holder instanceof TruthViewHolder){
            TruthBean truthBean = (TruthBean) itemsList.get(position);
            ((TruthViewHolder) holder).releaseTruthPersonName.setText(truthBean.getName());
            ((TruthViewHolder) holder).releaseTruthPersonTitle.setText(truthBean.getTitle());
            ((TruthViewHolder) holder).releaseTruthTime.setText(truthBean.getTime());
            ((TruthViewHolder) holder).releaseTruthDetail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, ReleaseTruthActivity.class);
                    intent.putExtra("id",truthBean.getId());//传过去后，在ReleaseTruthActivity界面通过id获取到新闻消息，从而进行更完整的显示
                    context.startActivity(intent);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return itemsList.size();
    }

    //创建四个viewHolder
    /**
     * 第一个是headerViewHolder
     * */
    static class HeaderViewHolder extends RecyclerView.ViewHolder{
        TextView textView ;
        public HeaderViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.search_header_tv);
        }
    }
    static class VerifyViewHolder extends RecyclerView.ViewHolder{
        private TextView releaseVerifyPersonName,releaseVerifyPersonTitle;
        private TextView releaseVerifyKeyword,releaseVerifyTime;
        private ButtonView releaseVerifyDetail;
        public VerifyViewHolder(@NonNull View itemView) {
            super(itemView);
            releaseVerifyPersonName = itemView.findViewById(R.id.release_verify_person_name);
            releaseVerifyPersonTitle = itemView.findViewById(R.id.release_verify_person_title);
            releaseVerifyKeyword = itemView.findViewById(R.id.release_verify_keyword);
            releaseVerifyTime = itemView.findViewById(R.id.release_verify_time);
            releaseVerifyDetail = itemView.findViewById(R.id.release_verify_detail_btn);
        }
    }
    static class RumorViewHolder extends RecyclerView.ViewHolder{
        private TextView releaseRumorPersonName,releaseRumorPersonTitle;
        private TextView releaseRumorKeyword,releaseRumorTime;
        private ButtonView releaseRumorDetail;
        public RumorViewHolder(@NonNull View itemView) {
            super(itemView);
            releaseRumorPersonName = itemView.findViewById(R.id.release_rumor_person_name);
            releaseRumorPersonTitle = itemView.findViewById(R.id.release_rumor_person_title);
            releaseRumorTime = itemView.findViewById(R.id.release_rumor_time);
            releaseRumorDetail = itemView.findViewById(R.id.release_rumor_detail_btn);
        }
    }
    static class TruthViewHolder extends RecyclerView.ViewHolder{
        private TextView releaseTruthPersonName,releaseTruthPersonTitle;
        private TextView releaseTruthKeyword,releaseTruthTime;
        private ButtonView releaseTruthDetail;
        public TruthViewHolder(@NonNull View itemView) {
            super(itemView);
            releaseTruthPersonName = itemView.findViewById(R.id.release_truth_person_name);
            releaseTruthPersonTitle = itemView.findViewById(R.id.release_truth_person_title);
            releaseTruthTime = itemView.findViewById(R.id.release_truth_time);
            releaseTruthDetail = itemView.findViewById(R.id.release_truth_detail_btn);
        }
    }
}
