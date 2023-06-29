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
import com.example.awaylie.ReleaseTruthActivity;
import com.example.awaylie.bean.RumorBean;
import com.example.awaylie.bean.TruthBean;
import com.example.awaylie.interfaceClass.OnItemLongClickListener;
import com.xuexiang.xui.widget.button.ButtonView;

import java.util.List;

public class TruthRecyclerViewAdapter extends RecyclerView.Adapter<TruthRecyclerViewAdapter.MyVerifyViewHold> {
    private List<TruthBean> truthBeanList;
    private Context context;
    private OnItemLongClickListener mOnItemLongClickListener;

    public void setmOnItemLongClickListener(OnItemLongClickListener mOnItemLongClickListener) {
        this.mOnItemLongClickListener = mOnItemLongClickListener;
    }

    public void setVerifyBeanList(List<TruthBean> truthBeanList, Context context) {
        this.truthBeanList = truthBeanList;
        this.context = context;
    }

    @NonNull
    @Override
    public MyVerifyViewHold onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.release_truth_item,parent,false);
        return new MyVerifyViewHold(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyVerifyViewHold holder, int position) {
        TruthBean truthBean = truthBeanList.get(position);//通过list获取单个的对象
        holder.releaseTruthPersonName.setText(truthBean.getName());
        holder.releaseTruthPersonTitle.setText(truthBean.getTitle());
        holder.releaseTruthTime.setText(truthBean.getTime());
        holder.releaseTruthDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ReleaseTruthActivity.class);
                intent.putExtra("id",truthBean.getId());//传过去后，在ReleaseTruthActivity界面通过id获取到新闻消息，从而进行更完整的显示
                context.startActivity(intent);
            }
        });
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                int currentPosition = holder.getAdapterPosition();
                mOnItemLongClickListener.onItemLongClick(v,currentPosition);
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return truthBeanList.size();
    }

    class MyVerifyViewHold extends RecyclerView.ViewHolder{
        private TextView releaseTruthPersonName,releaseTruthPersonTitle;
        private TextView releaseTruthKeyword,releaseTruthTime;
        private ButtonView releaseTruthDetail;
        public MyVerifyViewHold(@NonNull View itemView) {
            super(itemView);
            releaseTruthPersonName = itemView.findViewById(R.id.release_truth_person_name);
            releaseTruthPersonTitle = itemView.findViewById(R.id.release_truth_person_title);
            releaseTruthTime = itemView.findViewById(R.id.release_truth_time);
            releaseTruthDetail = itemView.findViewById(R.id.release_truth_detail_btn);


        }
    }

}
