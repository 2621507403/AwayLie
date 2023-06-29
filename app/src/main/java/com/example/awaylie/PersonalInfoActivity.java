package com.example.awaylie;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.awaylie.bean.UserBean;
import com.lljjcoder.style.citylist.Toast.ToastUtils;
import com.xuexiang.xui.widget.actionbar.TitleBar;
import com.xuexiang.xui.widget.button.ButtonView;
import com.xuexiang.xui.widget.edittext.MultiLineEditText;

public class PersonalInfoActivity extends AppCompatActivity {

    private TextView name,signature,sex,city,birth;
    private ImageButton signatureBtn;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private TitleBar titleBar;

    private String signatureText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_info);
        initView();
        initData();
    }
    private void initView(){
        name = findViewById(R.id.personal_name_text);
        signature = findViewById(R.id.personal_signature_text);
        signatureBtn = findViewById(R.id.personal_signature_edit_btn);
        sex = findViewById(R.id.personal_sex_text);
        city = findViewById(R.id.personal_city_text);
        birth = findViewById(R.id.personal_birth_text);
        titleBar = findViewById(R.id.personal_info_title_bar);
        titleBar.setLeftClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        sharedPreferences = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();


        //点击按钮进弹出编辑框
        signatureBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //下面是弹出对话框的逻辑
                AlertDialog.Builder builder = new AlertDialog.Builder(PersonalInfoActivity.this);
                LayoutInflater inflater = getLayoutInflater();
                View dialogView = inflater.inflate(R.layout.signature_dialog_layout,null);
                builder.setView(dialogView);

                //获取到对话框中的数据
                MultiLineEditText multiLineEditText = dialogView.findViewById(R.id.editText);
                ButtonView cancelBtn = dialogView.findViewById(R.id.cancelButton);
                ButtonView saveBtn = dialogView.findViewById(R.id.saveButton);

                AlertDialog alertDialog = builder.create();

                cancelBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                    }
                });
                saveBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        signatureText = multiLineEditText.getContentText();
                        if (signatureText.length() == 0){
                            ToastUtils.showShortToast(PersonalInfoActivity.this,"签名不能为空");
                            return;
                        }
                        editor.putString("signature",signatureText);
                        editor.apply();
                        signature.setText(signatureText);
                        Intent resultIntent = new Intent();
                        resultIntent.putExtra("signatureText", signatureText);
                        Log.d("signatureText", "onActivityResult: "+signatureText);
                        setResult(RESULT_OK, resultIntent);
                        alertDialog.dismiss();
                    }
                });
                alertDialog.show();
            }
        });
    }

    //对数据进行初始化
    private void initData(){
        //获取数据
        name.setText(sharedPreferences.getString("name",""));
        signature.setText(sharedPreferences.getString("signature",""));
        city.setText(sharedPreferences.getString("city",""));
        birth.setText(sharedPreferences.getString("birth",""));
        sex.setText((sharedPreferences.getInt("sex",0)) == 0 ?  "男" : "女");
    }

}