package com.example.awaylie;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;

import com.example.awaylie.bean.UserBean;
import com.example.awaylie.database.AwayLieSQLiteOpenHelper;
import com.example.awaylie.utils.HashUtils;
import com.lljjcoder.Interface.OnCityItemClickListener;
import com.lljjcoder.bean.CityBean;
import com.lljjcoder.bean.DistrictBean;
import com.lljjcoder.bean.ProvinceBean;
import com.lljjcoder.citywheel.CityConfig;
import com.lljjcoder.style.citylist.Toast.ToastUtils;
import com.lljjcoder.style.citypickerview.CityPickerView;
import com.xuexiang.xui.utils.ResUtils;
import com.xuexiang.xui.widget.actionbar.TitleBar;
import com.xuexiang.xui.widget.alpha.XUIAlphaButton;
import com.xuexiang.xui.widget.button.ButtonView;
import com.xuexiang.xui.widget.edittext.PasswordEditText;
import com.xuexiang.xui.widget.picker.widget.OptionsPickerView;
import com.xuexiang.xui.widget.picker.widget.TimePickerView;
import com.xuexiang.xui.widget.picker.widget.builder.OptionsPickerBuilder;
import com.xuexiang.xui.widget.picker.widget.builder.TimePickerBuilder;
import com.xuexiang.xui.widget.picker.widget.listener.OnTimeSelectListener;

import java.text.SimpleDateFormat;
import java.util.Date;

public class RegisterActivity extends AppCompatActivity {
    private XUIAlphaButton registerSexPickerBtn;
    private ButtonView registerCityPickerBtn, registerBirthPickerBtn, registerBtn;
    private String[] mSexOption;
    private TitleBar titleBar;
    private int sexSelectOption = 0;
    private AwayLieSQLiteOpenHelper mHelper;
    private UserBean registerBean;
    private EditText registerNumber, registerName;
    private PasswordEditText registerPassword,registerPasswordSure;



    private CityPickerView mPicker = new CityPickerView();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mPicker.init(this);
        registerBean = new UserBean();//对这个进行初始化
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


        registerNumber = findViewById(R.id.register_number);
        registerName = findViewById(R.id.register_name);
        registerPassword = findViewById(R.id.register_password);
        registerPasswordSure = findViewById(R.id.register_password_sure);
        titleBar = findViewById(R.id.register_back_btn);
        titleBar.setLeftClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        //设置只能输入11位数字
        registerNumber.setFilters(new InputFilter[] {
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

        //输入number时判断数据库中是否存在该number
        registerNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }


            @Override
            public void afterTextChanged(Editable s) {
                if (s.length()==11){
                    int raw = mHelper.queryUserIsExist(s.toString());
                    if (raw > 0 ) {
                        ToastUtils.showShortToast(RegisterActivity.this,"已经存在，请更换手机号");
                        registerNumber.setText("");//清空
                        return;
                    }else{
                        registerBean.setNumber(s.toString());
                        ToastUtils.showShortToast(RegisterActivity.this,"该号码可以使用");
                        return;
                    }
                }
            }
        });
        //对输入的密码进行确认

        registerPasswordSure.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String password = registerPassword.getText().toString();//输入的密码
                if (!password.equals(registerPasswordSure.getText().toString())) registerPasswordSure.setTextColor(Color.RED);
                else {//确认密码成功，将其加密后存入数据库
                    registerPasswordSure.setTextColor(Color.GREEN);
                    try {
                        String passwordSure = HashUtils.hash(password);
                        registerBean.setPassword(passwordSure);//存储密码
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }
        });



        registerSexPickerBtn = findViewById(R.id.register_sex_btn);
        registerSexPickerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSexPickerView();
            }
        });

        registerCityPickerBtn = findViewById(R.id.register_city_btn);
        registerCityPickerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//添加默认的配置，不需要自己定义，当然也可以自定义相关熟悉，详细属性请看demo
                CityConfig cityConfig = new CityConfig.Builder().build();
                mPicker.setConfig(cityConfig);

//监听选择点击事件及返回结果
                mPicker.setOnCityItemClickListener(new OnCityItemClickListener() {
                    @Override
                    public void onSelected(ProvinceBean province, CityBean city, DistrictBean district) {

                        //省份province
                        //城市city
                        //地区district
                        String addr = province.getName() + "-" + city.getName() + "-" + district.getName();
                        registerCityPickerBtn.setText(addr);
                        registerBean.setCity(addr);
                    }

                    @Override
                    public void onCancel() {
                        ToastUtils.showLongToast(RegisterActivity.this, "已取消");
                    }
                });

                //显示
                mPicker.showCityPicker();
            }
        });

        registerBirthPickerBtn = findViewById(R.id.register_birth_btn);
        registerBirthPickerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //日期选择器
                TimePickerView pvTime = new TimePickerBuilder(RegisterActivity.this, new OnTimeSelectListener() {
                    @Override
                    public void onTimeSelected(Date date, View v) {
                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                        String formattedDate = dateFormat.format(date);
                        registerBirthPickerBtn.setText(formattedDate);
                        registerBean.setBirth(formattedDate);//设置生日
                    }
                }).setType(new boolean[]{true, true, true, false, false, false}).build();
                // pvTime.setDate(Calendar.getInstance());//注：根据需求来决定是否使用该方法（一般是精确到秒的情况），此项可以在弹出选择器的时候重新设置当前时间，避免在初始化之后由于时间已经设定，导致选中时间与当前时间不匹配的问题。
                pvTime.show();
            }
        });

        //提交按钮
        registerBtn = findViewById(R.id.register_btn);
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerBean.setName(registerName.getText().toString());//设置用户名
                if (registerBean.getNumber()==null || registerBean.getPassword()==null || registerBean.getName() == null
                || registerBean.getCity() == null || registerBean.getBirth() == null ){
                    ToastUtils.showShortToast(RegisterActivity.this,"不能有空信息");
                    return;
                }else {
                    if (mHelper.insert2user(registerBean) > 0) {
                        ToastUtils.showShortToast(RegisterActivity.this, "注册成功");
                        finish();
                    } else {
                        ToastUtils.showShortToast(RegisterActivity.this, "注册失败");
                    }
                }
            }
        });

    }

    private void showSexPickerView() {
        mSexOption = ResUtils.getStringArray(this, R.array.sex_option);
        OptionsPickerView pvOptions = new OptionsPickerBuilder(RegisterActivity.this, (v, options1, options2, options3) -> {
            registerSexPickerBtn.setText(mSexOption[options1]);
            sexSelectOption = options1;
            registerBean.setSex(sexSelectOption);//设置性别，0-男，1-女
            return false;
        })
                .setTitleText("性别选择")
                .setSelectOptions(sexSelectOption)
                .build();
        pvOptions.setPicker(mSexOption);
        pvOptions.show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mHelper.closeDB();
    }
}