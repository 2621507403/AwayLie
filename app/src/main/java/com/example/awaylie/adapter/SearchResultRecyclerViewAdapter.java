package com.example.awaylie.adapter;

import android.content.Context;
import android.content.Intent;
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
import com.example.awaylie.bean.RumorBean;
import com.example.awaylie.bean.TruthBean;
import com.example.awaylie.bean.VerifyBean;
import com.xuexiang.xui.widget.button.ButtonView;

import java.util.List;
import java.util.Objects;

public class SearchResultRecyclerViewAdapter extends RecyclerView.Adapter<SearchResultRecyclerViewAdapter.SearchResultViewHolder> {
    private List<VerifyBean> verifyBeanList;
    private List<RumorBean> rumorBeanList;
    private List<TruthBean> truthBeanList;
    private Context context;

    public SearchResultRecyclerViewAdapter(Context context) {
        this.context = context;
    }

    public void setVerifyBeanList(List<VerifyBean> verifyBeanList) {
        this.verifyBeanList = verifyBeanList;
    }

    public void setRumorBeanList(List<RumorBean> rumorBeanList) {
        this.rumorBeanList = rumorBeanList;
    }

    public void setTruthBeanList(List<TruthBean> truthBeanList) {
        this.truthBeanList = truthBeanList;
    }

    public SearchResultRecyclerViewAdapter(List<VerifyBean> verifyBeanList, List<RumorBean> rumorBeanList, List<TruthBean> truthBeanList, Context context) {
        this.verifyBeanList = verifyBeanList;
        this.rumorBeanList = rumorBeanList;
        this.truthBeanList = truthBeanList;
        this.context = context;
    }

    @NonNull
    @Override
    public SearchResultViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //根据不同的数据创建不同的viewHolder
        View view;
        if (viewType == 0) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.release_verify_item, parent, false);
            return new SearchResultViewHolder(view);
        } else if (viewType == 1) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.release_rumor_item, parent, false);
            return new SearchResultViewHolder(view);
        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.release_truth_item, parent, false);
            return new SearchResultViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull SearchResultViewHolder holder, int position) {
        //获取当前子类的类型和内容
        String type;//标记类型
        Object item;
        if (position < verifyBeanList.size()) {
            type = "verify";
            item = verifyBeanList.get(position);
        } else if (position < verifyBeanList.size() + rumorBeanList.size()) {
            type = "rumor";
            item = rumorBeanList.get(position - verifyBeanList.size());
        } else {
            type = "truth";
            item = truthBeanList.get(position - verifyBeanList.size() - rumorBeanList.size());
        }

        // 判断当前子项是否是某个块的第一个子项

        if (position == 0 || position == verifyBeanList.size() || position == verifyBeanList.size() + rumorBeanList.size()) {
            // 如果当前子项是列表中的第一个子项，或者是某个块的第一个子项，则它一定是某个块的第一个子项
            holder.isFirstItemInBlock = true;
        }
        // 根据当前子项的类型设置相应的视图内容
        if (type.equals("verify")) {
            VerifyBean verifyBean = (VerifyBean) item;
            holder.releaseVerifyPersonName.setText(verifyBean.getName());
            holder.releaseVerifyPersonTitle.setText(verifyBean.getTitle());
            holder.releaseVerifyKeyword.setText(verifyBean.getKeyword());
            holder.releaseVerifyTime.setText(verifyBean.getTime());
            holder.releaseVerifyDetail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, ReleaseVerifyActivity.class);
                    intent.putExtra("id", verifyBean.getId());//传过去后，在ReleaseVerifyActivity界面通过id获取到新闻消息，从而进行更完整的显示
                    context.startActivity(intent);
                }
            });
        } else if (type.equals("rumor")) {
            RumorBean rumorBean = (RumorBean) item;
            holder.releaseRumorPersonName.setText(rumorBean.getName());
            holder.releaseRumorPersonTitle.setText(rumorBean.getTitle());
            holder.releaseRumorTime.setText(rumorBean.getTime());
            holder.releaseRumorDetail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, ReleaseRumorActivity.class);
                    intent.putExtra("id", rumorBean.getId());//传过去后，在ReleaseRumorActivity界面通过id获取到新闻消息，从而进行更完整的显示
                    context.startActivity(intent);
                }
            });
        } else if (type.equals("truth")) {
            TruthBean truthBean = (TruthBean) item;
            holder.releaseTruthPersonName.setText(truthBean.getName());
            holder.releaseTruthPersonTitle.setText(truthBean.getTitle());
            holder.releaseTruthTime.setText(truthBean.getTime());
            holder.releaseTruthDetail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, ReleaseTruthActivity.class);
                    intent.putExtra("id", truthBean.getId());//传过去后，在ReleaseTruthActivity界面通过id获取到新闻消息，从而进行更完整的显示
                    context.startActivity(intent);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return verifyBeanList.size() + rumorBeanList.size() + truthBeanList.size();
    }

    public Object getItem(int position){
        if (position < verifyBeanList.size()) {
            return verifyBeanList.get(position);
        } else if (position < verifyBeanList.size() + rumorBeanList.size()) {
            return rumorBeanList.get(position - verifyBeanList.size());
        } else {
            return truthBeanList.get(position - verifyBeanList.size() - rumorBeanList.size());
        }

    }

    @Override
    public int getItemViewType(int position) {
        if (position < verifyBeanList.size()) {
            return 0;
        } else if (position < verifyBeanList.size() + rumorBeanList.size()) {
            return 1;
        } else {
            return 2;
        }
    }
    static class SearchResultViewHolder extends RecyclerView.ViewHolder {

        //verify类型视图
        private TextView releaseVerifyPersonName, releaseVerifyPersonTitle;
        private TextView releaseVerifyKeyword, releaseVerifyTime;
        private ButtonView releaseVerifyDetail;

        //rumor类型视图
        private TextView releaseRumorPersonName, releaseRumorPersonTitle;
        private TextView releaseRumorKeyword, releaseRumorTime;
        private ButtonView releaseRumorDetail;
        //truth类型视图
        private TextView releaseTruthPersonName, releaseTruthPersonTitle;
        private TextView releaseTruthKeyword, releaseTruthTime;
        private ButtonView releaseTruthDetail;

        public static boolean isFirstItemInBlock;

        public SearchResultViewHolder(@NonNull View itemView) {
            super(itemView);
            //verify
            releaseVerifyPersonName = itemView.findViewById(R.id.release_verify_person_name);
            releaseVerifyPersonTitle = itemView.findViewById(R.id.release_verify_person_title);
            releaseVerifyKeyword = itemView.findViewById(R.id.release_verify_keyword);
            releaseVerifyTime = itemView.findViewById(R.id.release_verify_time);
            releaseVerifyDetail = itemView.findViewById(R.id.release_verify_detail_btn);
            //rumor
            releaseRumorPersonName = itemView.findViewById(R.id.release_rumor_person_name);
            releaseRumorPersonTitle = itemView.findViewById(R.id.release_rumor_person_title);
            releaseRumorTime = itemView.findViewById(R.id.release_rumor_time);
            releaseRumorDetail = itemView.findViewById(R.id.release_rumor_detail_btn);

            //truth
            releaseTruthPersonName = itemView.findViewById(R.id.release_truth_person_name);
            releaseTruthPersonTitle = itemView.findViewById(R.id.release_truth_person_title);
            releaseTruthTime = itemView.findViewById(R.id.release_truth_time);
            releaseTruthDetail = itemView.findViewById(R.id.release_truth_detail_btn);
        }
    }

}
