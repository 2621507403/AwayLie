package com.example.awaylie.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import com.example.awaylie.bean.VerifyBean;

import java.util.ArrayList;
import java.util.List;

public class AwayLieSQLiteOpenHelper extends SQLiteOpenHelper {

    private final static String DB_NAME = "awayLie.db";
    private final static int DB_VERSION = 1;
    private final static String TABLE_VERIFY_NAME = "verifyTB";
    private final static String TABLE_RUMOR_NAME = "rumorTB";
    private final static String TABLE_TRUTH_NAME = "truthTB";
    private static AwayLieSQLiteOpenHelper mHelper = null ;
    private SQLiteDatabase mRDB = null;
    private SQLiteDatabase mWDB = null;


    private AwayLieSQLiteOpenHelper(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }
    public static AwayLieSQLiteOpenHelper getInstance(Context context){
        if(mHelper == null){
            synchronized (AwayLieSQLiteOpenHelper.class){
                if (mHelper == null){
                    mHelper = new AwayLieSQLiteOpenHelper(context);
                }
            }
        }
        return mHelper;
    }

    //只在创建数据库时候被调用
    @Override
    public void onCreate(SQLiteDatabase db) {
        //创建verify表
        /**
         * name表示提问人的名字
         * time表示提问的时间
         * keyword表示发布问题时候选择的问题所属话题区
         * content表示提问时提交的内容
         * */
        String sql = "create table if not exists "+TABLE_VERIFY_NAME+" (" +
                "_id integer primary key autoincrement not null," +
                " name text not null," +
                " keyword text not null," +
                " title text not null," +
                " time real not null," +
                " content text not null);";
        db.execSQL(sql);
        //创建rumor表
        /**
         * name表示证明人的名字
         * time表示证明的时间
         * content表示证明所提交的内容
         * */
        sql = "create table if not exists "+TABLE_RUMOR_NAME+" (" +
                "_id integer primary key autoincrement not null," +
                " name text not null," +
                " verify_id integer not null," +
                " time real not null," +
                " content text not null," +
                " foreign key (verify_id) references "+TABLE_VERIFY_NAME+"(_id));";
        db.execSQL(sql);
        //创建truth表
        /**
         * name表示证明人的名字
         * time表示证明的时间
         * content表示证明所提交的内容
         * */
        sql = "create table if not exists "+TABLE_TRUTH_NAME+" (" +
                "_id integer primary key autoincrement not null," +
                " name text not null," +
                " verify_id integer not null," +
                " time real not null," +
                " content text not null," +
                " foreign key (verify_id) references "+TABLE_VERIFY_NAME+"(_id));";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    //打开数据库读权限
    public SQLiteDatabase openReadLink(){
        if (mRDB == null || !mRDB.isOpen()){
            mRDB = mHelper.getReadableDatabase();
        }
        return mRDB;
    }
    //打开数据库写权限（包括了读）
    public SQLiteDatabase openWriteLink(){
        if (mWDB == null || !mWDB.isOpen()){
            Log.d("SQLite", "insert2verify: "+mHelper);
            mWDB = mHelper.getWritableDatabase();
        }
        return mWDB;
    }

    //关闭连接数据库
    public void closeDB(){
        if (mRDB.isOpen() && mRDB != null){
            mRDB.close();
            mRDB = null;
        }
        if (mWDB.isOpen() && mWDB != null){
            mWDB.close();
            mWDB = null;
        }
    }

    //插入单条数据到verify表中
    public long insert2verify(VerifyBean verifyBean){
        ContentValues values = new ContentValues();
        values.put("name",verifyBean.getName());
        values.put("keyword",verifyBean.getKeyword());
        values.put("content",verifyBean.getContent());
        values.put("title",verifyBean.getTitle());
        values.put("time",verifyBean.getTime());
        Log.d("SQLite", "insert2verify: "+values);
        Log.d("SQLite", "insert2verify: "+mWDB);
        return mWDB.insert(TABLE_VERIFY_NAME,null,values);
    }

    //从verify表中查询数据并且交给recyclerView进行展示,通过list回传
    public List<VerifyBean> queryAllVerify(){
        List<VerifyBean> verifyBeanList = new ArrayList<>();
        Cursor cursor = mRDB.query(TABLE_VERIFY_NAME,null,null,null,null,null,null);
        while(cursor.moveToNext()){
            VerifyBean verifyBean = new VerifyBean();
            verifyBean.setId(cursor.getInt(0));
            verifyBean.setName(cursor.getString(1));
            verifyBean.setKeyword(cursor.getString(2));
            verifyBean.setTitle(cursor.getString(3));
            verifyBean.setTime(cursor.getString(4));
            verifyBean.setContent(cursor.getString(5));
            verifyBeanList.add(verifyBean);
        }
        cursor.close();
        return verifyBeanList;
    }

    //按照verify的id就行查找，找到就返回封装好的实体类对象
    public VerifyBean queryByVerifyById(int id){
        Log.d("Detail", "getData: ");
        Cursor cursor = mRDB.query(TABLE_VERIFY_NAME,null,"_id=?" , new String[]{String.valueOf(id)},null,null,null);
        Log.d("Detail", "getData: "+cursor);
        VerifyBean verifyBean = new VerifyBean();
        if (cursor.moveToFirst()){
            verifyBean.setId(cursor.getInt(0));
            verifyBean.setName(cursor.getString(1));
            verifyBean.setKeyword(cursor.getString(2));
            verifyBean.setTitle(cursor.getString(3));
            verifyBean.setTime(cursor.getString(4));
            verifyBean.setContent(cursor.getString(5));
            Log.d("Detail", "getData: "+verifyBean);
        }
        cursor.close();//关闭游标
        return verifyBean;

    }
}
