package com.example.awaylie;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.awaylie.bean.UserBean;
import com.example.awaylie.database.AwayLieSQLiteOpenHelper;
import com.example.awaylie.utils.HashUtils;
import com.lljjcoder.style.citylist.Toast.ToastUtils;

public class LoginActivity extends AppCompatActivity {
    private TextView loginRegister;
    private AwayLieSQLiteOpenHelper mHelper;
    private EditText loginNumber,loginPassword;
    private Button loginInBtn;
    private SharedPreferences userInfoSP;
    private SharedPreferences.Editor userEdit;
    private String number;//这是存放输入的number
    private String password;//这是存放输入的密码的hash结果
    private UserBean loginUserBean;
    private boolean numberCorrect = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        userInfoSP = getSharedPreferences("userInfo",MODE_PRIVATE);
        userEdit = userInfoSP.edit();
        loginUserBean = new UserBean();
        initView();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mHelper = AwayLieSQLiteOpenHelper.getInstance(this);
        mHelper.openWriteLink();
        mHelper.openReadLink();
    }

    private void initView() {
        loginRegister = findViewById(R.id.tv_register);
        loginRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(intent);
            }
        });

        loginNumber = findViewById(R.id.login_number);
        loginPassword = findViewById(R.id.login_password);
        //设置只能输入11位数字
        loginNumber.setFilters(new InputFilter[] {
                new InputFilter.LengthFilter(11),
                new InputFilter() {
                    @Override
                    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                        if (source.equals("") || source.toString().matches("[0-9]+")) {
                            return null;
                        }
                        return "";
                    }
                }
        });
        //添加监听，如果输入的号码不存在，就弹出是否注册提示
        loginNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 11){
                    number = loginNumber.getText().toString();
                    if (mHelper.queryUserIsExist(number)==0){//数据库中没有，需要注册
                        numberCorrect = false;
                        //TODO：换成弹窗，有个跳转到注册界面的弹窗按钮
                        ToastUtils.showShortToast(LoginActivity.this,"未注册，请注册");
                    }else{
                        numberCorrect = true;
                    }
                }
            }
        });

        loginPassword = findViewById(R.id.login_password);

        loginInBtn = findViewById(R.id.login_in_btn);
        //登录
        loginInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!numberCorrect){
                    return;
                }
                //获取到输入的密码的hash值
                try {
                    password = HashUtils.hash(loginPassword.getText().toString());

                } catch (Exception e) {
                    throw new RuntimeException(e);
                }

                //首先是通过number获取到数据
                loginUserBean = mHelper.queryUserInfoByNumber(number);
                if (!loginUserBean.getPassword().equals(password)){
                    ToastUtils.showShortToast(LoginActivity.this,"密码错误，请重新输入");
                    loginPassword.setText("");//清空密码输入框
                }else { //这是登录成功的情况
                    //将数据存储到sp中，并且将登陆状态设置成true;
                    userEdit.putString("number", loginUserBean.getNumber());
                    userEdit.putString("name",loginUserBean.getName());
                    userEdit.putString("signature",loginUserBean.getSignature());
                    userEdit.putString("city",loginUserBean.getCity());
                    userEdit.putString("birth",loginUserBean.getBirth());
                    userEdit.putInt("sex",loginUserBean.getSex());
                    userEdit.putBoolean("isLogin",true);//设置是否登录状态标志位
                    userEdit.apply();//提交
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mHelper.closeDB();
    }
}