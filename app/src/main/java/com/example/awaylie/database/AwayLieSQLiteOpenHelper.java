package com.example.awaylie.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import com.example.awaylie.bean.RumorBean;
import com.example.awaylie.bean.TruthBean;
import com.example.awaylie.bean.UserBean;
import com.example.awaylie.bean.VerifyBean;

import java.util.ArrayList;
import java.util.List;

public class AwayLieSQLiteOpenHelper extends SQLiteOpenHelper {

    private final static String DB_NAME = "awayLie.db";
    private final static int DB_VERSION = 1;
    private final static String TABLE_VERIFY_NAME = "verifyTB";
    private final static String TABLE_RUMOR_NAME = "rumorTB";
    private final static String TABLE_TRUTH_NAME = "truthTB";
    private final static String TABLE_USER_NAME = "userTB";
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
                " title text not null," +
                " content text not null," +
                " foreign key (verify_id) references "+TABLE_VERIFY_NAME+"(_id) ON DELETE CASCADE);";
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
                " title text not null," +
                " content text not null," +
                " foreign key (verify_id) references "+TABLE_VERIFY_NAME+"(_id) ON DELETE CASCADE);";
        db.execSQL(sql);

        //创建用户表，存放用户信息，包括用户uid=unumber,所以就只使用unumber作为唯一登录依据，uname,usignature,ucity,ubirth,usex(0是男，1是女),以及登录的账号unumber和密码upassword
        //TODO:添加图片路径，使得上传图像能够成功，使用本地作为一个服务器，图片文件放于自创的文件夹中
        sql = "create table if not exists "+TABLE_USER_NAME+" (" +
                " unumber text primary key not null," +
                " upassword text not null,"+
                " uname text not null," +
                " usignature text ," +
                " ucity text ," +
                " ubirth real ," +
                " usex integer );";

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
        if ( mRDB != null && mRDB.isOpen()){
            mRDB.close();
            mRDB = null;
        }
        if ( mWDB != null && mWDB.isOpen()){
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

    //插入单条数据到rumor
    public long insert2rumor(RumorBean rumorBean){
        ContentValues values = new ContentValues();
        values.put("name",rumorBean.getName());
        values.put("verify_id",rumorBean.getVerifyId());
        values.put("content",rumorBean.getContent());
        values.put("title",rumorBean.getTitle());
        values.put("time",rumorBean.getTime());
        return mWDB.insert(TABLE_RUMOR_NAME,null,values);
    }

    //插入单条数据到truth
    public long insert2truth(TruthBean truthBean){
        ContentValues values = new ContentValues();
        values.put("name",truthBean.getName());
        values.put("verify_id",truthBean.getVerifyId());
        values.put("content",truthBean.getContent());
        values.put("title",truthBean.getTitle());
        values.put("time",truthBean.getTime());
        return mWDB.insert(TABLE_TRUTH_NAME,null,values);
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
            verifyBeanList.add(0,verifyBean);
        }
        cursor.close();
        return verifyBeanList;
    }


    //从rumor表中查询数据并且交给recyclerView进行展示,通过list回传
    public List<RumorBean> queryAllRumor(){
        List<RumorBean> rumorBeanList = new ArrayList<>();
        Cursor cursor = mRDB.query(TABLE_RUMOR_NAME,null,null,null,null,null,null);
        while(cursor.moveToNext()){
            RumorBean rumorBean = new RumorBean();
            rumorBean.setId(cursor.getInt(0));
            rumorBean.setName(cursor.getString(1));
            rumorBean.setVerifyId(cursor.getInt(2));
            rumorBean.setTime(cursor.getString(3));
            rumorBean.setTitle(cursor.getString(4));
            rumorBean.setContent(cursor.getString(5));
            rumorBeanList.add(0,rumorBean);
        }
        cursor.close();
        return rumorBeanList;
    }

    //从truth表中查询数据并且交给recyclerView进行展示,通过list回传
    public List<TruthBean> queryAllTruth(){
        List<TruthBean> truthBeanList = new ArrayList<>();
        Cursor cursor = mRDB.query(TABLE_TRUTH_NAME,null,null,null,null,null,null);
        while(cursor.moveToNext()){
            TruthBean truthBean = new TruthBean();
            truthBean.setId(cursor.getInt(0));
            truthBean.setName(cursor.getString(1));
            truthBean.setVerifyId(cursor.getInt(2));
            truthBean.setTime(cursor.getString(3));
            truthBean.setTitle(cursor.getString(4));
            truthBean.setContent(cursor.getString(5));
            truthBeanList.add(0,truthBean);
        }
        cursor.close();
        return truthBeanList;
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

    //按照rumor的id就行查找，找到就返回封装好的实体类对象
    public RumorBean queryByRumorById(int id){
        Cursor cursor = mRDB.query(TABLE_RUMOR_NAME,null,"_id=?" , new String[]{String.valueOf(id)},null,null,null);
        RumorBean rumorBean = new RumorBean();
        if (cursor.moveToFirst()){
            rumorBean.setId(cursor.getInt(0));
            rumorBean.setName(cursor.getString(1));
            rumorBean.setVerifyId(cursor.getInt(2));
            rumorBean.setTime(cursor.getString(3));
            rumorBean.setTitle(cursor.getString(4));
            rumorBean.setContent(cursor.getString(5));
        }
        cursor.close();//关闭游标
        return rumorBean;
    }

    //按照rumor的id就行查找，找到就返回封装好的实体类对象
    public TruthBean queryByTruthById(int id){
        Cursor cursor = mRDB.query(TABLE_TRUTH_NAME,null,"_id=?" , new String[]{String.valueOf(id)},null,null,null);
        TruthBean truthBean = new TruthBean();
        if (cursor.moveToFirst()){
            truthBean.setId(cursor.getInt(0));
            truthBean.setName(cursor.getString(1));
            truthBean.setVerifyId(cursor.getInt(2));
            truthBean.setTime(cursor.getString(3));
            truthBean.setTitle(cursor.getString(4));
            truthBean.setContent(cursor.getString(5));
        }
        cursor.close();//关闭游标
        return truthBean;
    }


    //按照verify_id获取表rumor中对其引用的次数
    public int queryRawByIdRumor(int verifyId){
        String sql = "SELECT COUNT(*) as reference_count FROM "+TABLE_RUMOR_NAME +" WHERE verify_id = ?";
        Cursor cursor = mRDB.rawQuery(sql,new String[]{String.valueOf(verifyId)});
        if (cursor.moveToFirst()){
            return cursor.getInt(cursor.getColumnIndexOrThrow("reference_count"));
        }
        return 0;
    }

    //按照verify_id获取表truth中对其引用的次数
    public int queryRawByIdTruth(int verifyId){
        String sql = "SELECT COUNT(*) as reference_count FROM "+TABLE_TRUTH_NAME +" WHERE verify_id = ?";
        Cursor cursor = mRDB.rawQuery(sql,new String[]{String.valueOf(verifyId)});
        if (cursor.moveToFirst()){
            return cursor.getInt(cursor.getColumnIndexOrThrow("reference_count"));
        }
        return 0;

    }


    /**
     * 对用户账号的操作
     * */

    //这个函数用于将合法的user信息插入数据库，signature通常为空
    public long insert2user(UserBean userBean){
        ContentValues values = new ContentValues();
        values.put("unumber",userBean.getNumber());
        values.put("upassword",userBean.getPassword());
        values.put("uname",userBean.getName());
        values.put("ucity",userBean.getCity());
        values.put("ubirth",userBean.getBirth());
        values.put("usex",userBean.getSex());
        return mWDB.insert(TABLE_USER_NAME,null,values);
    }

    //通过unumber来确定user并且更新其内容
    public int updateUserInfo(UserBean userBean){
        ContentValues values = new ContentValues();
        values.put("uname",userBean.getName());
        values.put("usignature",userBean.getSignature());
        values.put("ucity",userBean.getCity());
        values.put("ubirth",userBean.getBirth());
        values.put("usex",userBean.getSex());
        return mWDB.update(TABLE_USER_NAME,values,"unumber = ?",new String[]{String.valueOf(userBean.getNumber())});
    }

    //通过unumber来确定user，向其中添加个性签名signature
    public int updateUserSignature(int number,String signature){
        ContentValues values = new ContentValues();
        values.put("usignature",signature);
        return mWDB.update(TABLE_USER_NAME,values,"number = ?",new String[]{signature});
    }

    //通过输入的手机号，来检测这个手机号有没有被注册过
    //返回0表示没有被注册过。返回1表示被注册过
    public int queryUserIsExist(String userNumber){
        String sql = "SELECT COUNT(*) as reference_count FROM "+TABLE_USER_NAME +" WHERE unumber = ?";
        Cursor cursor = mRDB.rawQuery(sql,new String[]{String.valueOf(userNumber)});
        if (cursor.moveToFirst()){
            return cursor.getInt(cursor.getColumnIndexOrThrow("reference_count"));
        }
        return 0;
    }
}
