package com.example.awaylie.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.awaylie.LoginActivity;
import com.example.awaylie.MinePersonalInfoActivity;
import com.example.awaylie.PersonalInfoActivity;
import com.example.awaylie.R;
import com.example.awaylie.RegisterActivity;
import com.example.awaylie.bean.UserBean;

public class MineFragment extends Fragment {

    private View minePersonalInfoBtn;

    private SharedPreferences personalInfoSP;
    private UserBean personalInfo;
    private TextView minePersonalName,minePersonalSignature;
    private Toolbar toolbar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.mine_toolbar_setting,menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId()==R.id.mine_toolbar_setting_btn){
            Intent intent = new Intent(getActivity(), PersonalInfoActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_mine, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initInfo();
        initView(view);

    }
    private void initView(View view){
        minePersonalInfoBtn = view.findViewById(R.id.mine_personal_view_btn);
        minePersonalName = view.findViewById(R.id.mine_personal_username);
        minePersonalSignature = view.findViewById(R.id.mine_personal_signature);
        minePersonalName.setText(personalInfo.getName());
        minePersonalSignature.setText(personalInfo.getSignature());
        toolbar = view.findViewById(R.id.mine_toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
    }
    //用于获取数据，并存放在userBean实体类中
    private void initInfo() {
        personalInfo = new UserBean();
        personalInfoSP = getActivity().getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        //获取数据
        personalInfo.setNumber(personalInfoSP.getString("number",""));
        personalInfo.setName(personalInfoSP.getString("name",""));
        personalInfo.setSignature(personalInfoSP.getString("signature",""));
        personalInfo.setCity(personalInfoSP.getString("city",""));
        personalInfo.setBirth(personalInfoSP.getString("birth",""));
        personalInfo.setSex(personalInfoSP.getInt("sex",0));//默认为男
    }
}