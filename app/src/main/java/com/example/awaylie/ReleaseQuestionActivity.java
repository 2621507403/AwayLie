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
import com.example.awaylie.utils.TimeGetUtil;
import com.xuexiang.xui.widget.button.ButtonView;
import com.xuexiang.xui.widget.edittext.MultiLineEditText;
import com.xuexiang.xui.widget.spinner.materialspinner.MaterialSpinner;

import java.text.BreakIterator;
import java.util.Calendar;


//主要是点击发布后，弹出的填入提出问题的表单界面
public class ReleaseQuestionActivity extends AppCompatActivity {
    private EditText releaseTitle , releaseKeyword ;
    private MultiLineEditText releaseContent;
    private MaterialSpinner releaseChoose;
    private ButtonView releaseBtn;
    private RadioButton releaseCheck;
    //数据库操作
    private AwayLieSQLiteOpenHelper mHelper;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_release_question);
        initView();//对view进行初始化
        releaseChoose.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
                releaseKeyword.append((CharSequence) item);
            }
        });

        releaseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (releaseCheck.isChecked()){
                    //先判断是否有空，有空的弹出提示
                    if (releaseTitle.getText().toString().equals("") || releaseKeyword.getText().toString().equals("")||releaseContent.getContentText().equals("")){
                        Toast.makeText(ReleaseQuestionActivity.this,"不能有空数据",Toast.LENGTH_SHORT).show();
                        return;
                    }
                    //按钮点击事件，用于负责将数据传入数据库中
                    VerifyBean verifyBean = new VerifyBean();
                    verifyBean.setTitle(releaseTitle.getText().toString());
                    verifyBean.setName("user01");//应当从sp中获取，这里模拟一下
                    verifyBean.setKeyword(releaseKeyword.getText().toString());
                    verifyBean.setTime(TimeGetUtil.getTime());
                    verifyBean.setContent(releaseContent.getContentText());
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


    //对view进行初始化
    private void initView(){
        releaseTitle = findViewById(R.id.release_question_title);
        releaseKeyword = findViewById(R.id.release_question_keyword);
        releaseContent = findViewById(R.id.release_question_content);
        releaseChoose = findViewById(R.id.release_question_keyword_spinner);
        releaseBtn = findViewById(R.id.release_question_submitBtn);
        releaseCheck = findViewById(R.id.release_question_check);
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        mHelper.closeDB();
    }
}