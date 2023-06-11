package com.example.awaylie.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.awaylie.R;
import com.github.clans.fab.FloatingActionMenu;


public class ReleaseFragment extends Fragment {
    private FloatingActionMenu releaseFAM;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_release, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        releaseFAM = view.findViewById(R.id.release_FAM);

        //对菜单按钮设置监听事件


    }
    @Override
    public void onPause() {
        super.onPause();
        releaseFAM.close(true);
    }
}