package com.example.awaylie;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.example.awaylie.bean.UserBean;
import com.lljjcoder.style.citylist.Toast.ToastUtils;
import com.xuexiang.xui.widget.actionbar.TitleBar;
import com.xuexiang.xui.widget.button.ButtonView;
import com.xuexiang.xui.widget.edittext.MultiLineEditText;
import com.xuexiang.xui.widget.imageview.photoview.PhotoView;

import de.hdodenhof.circleimageview.CircleImageView;

public class PersonalInfoActivity extends AppCompatActivity {

    private TextView name,signature,sex,city,birth;
    private ImageButton signatureBtn;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private CircleImageView personalHeadPic;
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
        personalHeadPic = findViewById(R.id.personal_head_pic);

        personalHeadPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //点击后弹出AlertDialog,并且弹出两个选项：查看大图和更换头像、
                AlertDialog.Builder builder = new AlertDialog.Builder(PersonalInfoActivity.this);
                builder.setTitle("选择操作");
                String[] items = {"查看大图","替换头像"};
                builder.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == 0){
                            //查看大图
                            LayoutInflater inflater = getLayoutInflater();
                            View dialogView = inflater.inflate(R.layout.header_big_pic,null);
                            PhotoView photoView = dialogView.findViewById(R.id.personal_header_big_pic);
                            photoView.setImageResource(R.drawable.xiaoxin);
                            photoView.setOnLongClickListener(new View.OnLongClickListener() {
                                @Override
                                public boolean onLongClick(View v) {
                                    ToastUtils.showShortToast(PersonalInfoActivity.this,"下载图片");
                                    return true;
                                }
                            });
                            ImageButton  closeBtn = dialogView.findViewById(R.id.personal_header_pic_close);
                            //创建一个对话框，弹出这个布局
                            AlertDialog.Builder bigPicBuilder = new AlertDialog.Builder(PersonalInfoActivity.this);
                            bigPicBuilder.setView(dialogView);
                            AlertDialog alertDialog = bigPicBuilder.create();
                            alertDialog.show();
                            closeBtn.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    alertDialog.dismiss();
                                }
                            });

                        }else {
                            //更换头像，从相册或者系统文件中获取到图片，选择图片后进入裁剪界面，将裁剪好的图片存放入本地文件夹中，并且将路径放到数据库里






                        }
                    }
                });
                builder.show();
            }
        });



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