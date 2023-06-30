package com.example.awaylie.adapter;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.awaylie.R;
import com.example.awaylie.ReleaseVerifyActivity;
import com.example.awaylie.bean.VerifyBean;
import com.example.awaylie.database.AwayLieSQLiteOpenHelper;
import com.example.awaylie.interfaceClass.OnItemLongClickListener;
import com.xuexiang.xui.adapter.simple.ViewHolder;
import com.xuexiang.xui.widget.button.ButtonView;

import java.util.List;
import java.util.zip.Inflater;

public class VerifyRecyclerViewAdapter extends RecyclerView.Adapter<VerifyRecyclerViewAdapter.MyVerifyViewHold> {
    private List<VerifyBean> verifyBeanList;
    private Context context;

    private OnItemLongClickListener mOnItemLongClickListener;

    public void setmOnItemLongClickListener(OnItemLongClickListener mOnItemLongClickListener) {
        this.mOnItemLongClickListener = mOnItemLongClickListener;
    }

    public void setVerifyBeanList(List<VerifyBean> verifyBeanList, Context context) {
        this.verifyBeanList = verifyBeanList;
        this.context = context;
    }

    @NonNull
    @Override
    public MyVerifyViewHold onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.release_verify_item, parent, false);
        return new MyVerifyViewHold(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyVerifyViewHold holder, int position) {
        VerifyBean verifyBean = verifyBeanList.get(position);//通过list获取单个的对象
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
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (mOnItemLongClickListener != null) {
                    int currentPosition = holder.getAdapterPosition();
                    mOnItemLongClickListener.onItemLongClick(holder.itemView, currentPosition);
                }
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return verifyBeanList.size();
    }

    class MyVerifyViewHold extends RecyclerView.ViewHolder {
        private TextView releaseVerifyPersonName, releaseVerifyPersonTitle;
        private TextView releaseVerifyKeyword, releaseVerifyTime;
        private ButtonView releaseVerifyDetail;

        public MyVerifyViewHold(@NonNull View itemView) {
            super(itemView);
            releaseVerifyPersonName = itemView.findViewById(R.id.release_verify_person_name);
            releaseVerifyPersonTitle = itemView.findViewById(R.id.release_verify_person_title);
            releaseVerifyKeyword = itemView.findViewById(R.id.release_verify_keyword);
            releaseVerifyTime = itemView.findViewById(R.id.release_verify_time);
            releaseVerifyDetail = itemView.findViewById(R.id.release_verify_detail_btn);

        }
    }

}
