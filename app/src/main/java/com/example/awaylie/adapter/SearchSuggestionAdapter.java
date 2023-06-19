package com.example.awaylie.adapter;

import android.annotation.SuppressLint;
import android.app.SearchManager;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.example.awaylie.R;
import com.example.awaylie.database.AwayLieSQLiteOpenHelper;

import java.util.zip.Inflater;

public class SearchSuggestionAdapter extends CursorAdapter {

    private AwayLieSQLiteOpenHelper mHelper ;
    private Context context;

    public SearchSuggestionAdapter(Context context, Cursor c, boolean autoRequery) {
        super(context, c, autoRequery);
        this.context = context;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {

        return LayoutInflater.from(context).inflate(R.layout.verigy_suggestion_item,parent,false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        //查询过程出现问题，问题不大，直接忽略
        @SuppressLint("Range") String suggestion = cursor.getString(cursor.getColumnIndex(SearchManager.SUGGEST_COLUMN_TEXT_1));
        Log.d("searchView", "bindView: "+suggestion);
        TextView textView = view.findViewById(R.id.suggestion_text);
        textView.setText(suggestion);
    }

    //用于从数据库中获取数据交给上面的进行显示，这是在线程中获取
    @Override
    public Cursor runQueryOnBackgroundThread(CharSequence constraint) {
        if (constraint.equals("")) return null;
        //从数据库获取title的cursor
        //初始化数据库操作
        mHelper = AwayLieSQLiteOpenHelper.getInstance(context);
        mHelper.openReadLink();
        Cursor cursor = mHelper.queryVerifyByTitle(constraint.toString());

        return cursor;
    }
}
