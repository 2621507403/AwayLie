package com.example.awaylie;

import androidx.appcompat.app.AppCompatActivity;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.awaylie.bean.VerifyBean;
import com.example.awaylie.database.AwayLieSQLiteOpenHelper;

import java.util.Calendar;


//主要是点击发布后，弹出的填入提出问题的表单界面
public class ReleaseQuestionActivity extends AppCompatActivity {
    private EditText releaseTitle , releaseKeyword , releaseContent;
    private Spinner releaseChoose;
    private Button releaseBtn;
    private RadioButton releaseCheck;
    //数据库操作
    private AwayLieSQLiteOpenHelper mHelper;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_release_question);
        initView();//对view进行初始化
        initSpinner();//对releaseChoose进行初始化
        releaseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (releaseCheck.isChecked()){
                    //按钮点击事件，用于负责将数据传入数据库中
                    VerifyBean verifyBean = new VerifyBean();
                    verifyBean.setTitle(releaseTitle.getText().toString());
                    verifyBean.setName("user01");//应当从sp中获取，这里模拟一下
                    verifyBean.setKeyword(releaseKeyword.getText().toString());
                    verifyBean.setTime(getTime());
                    verifyBean.setContent(releaseContent.getText().toString());
                    if (mHelper.insert2verify(verifyBean) > 0 )
                        Toast.makeText(ReleaseQuestionActivity.this,"插入成功",Toast.LENGTH_SHORT).show();
                    else Toast.makeText(ReleaseQuestionActivity.this,"插入失败",Toast.LENGTH_SHORT).show();
                    finish();
                }else {
                    Toast.makeText(ReleaseQuestionActivity.this,"请先勾选",Toast.LENGTH_SHORT).show();

                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        mHelper = AwayLieSQLiteOpenHelper.getInstance(this);
        Log.d("SQLite", "insert2verify: "+mHelper);
        mHelper.openWriteLink();
        mHelper.openReadLink();
    }

    private void initSpinner() {
        String[] items = new String[]{"","校园暴力","社会秩序","男女对立"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item,items);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        releaseChoose.setAdapter(adapter);
        releaseChoose.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String releaseChooseItem = items[position];
                releaseKeyword.append(releaseChooseItem);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });




    }

    //对view进行初始化
    private void initView(){
        releaseTitle = findViewById(R.id.release_question_title);
        releaseKeyword = findViewById(R.id.release_question_keyword);
        releaseContent = findViewById(R.id.release_question_content);
        releaseChoose = findViewById(R.id.release_question_keyword_spinner);
        releaseBtn = findViewById(R.id.release_question_submitBtn);
        releaseCheck = findViewById(R.id.release_question_check);
    }


    //获取当前时间
    private String getTime(){
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        return year + "-" + month + "-" + day + "  " + hour + ":" + minute + ":00";

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mHelper.closeDB();
    }
}