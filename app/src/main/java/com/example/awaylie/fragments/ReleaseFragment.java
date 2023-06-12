package com.example.awaylie.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.awaylie.R;
import com.example.awaylie.fragments.releaseFragments.RumorFragment;
import com.example.awaylie.fragments.releaseFragments.VerifyFragment;
import com.github.clans.fab.FloatingActionMenu;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;
import java.util.List;


public class ReleaseFragment extends Fragment {
    private FloatingActionMenu releaseFAM;
    private ViewPager2 releaseVP;
    private TabLayout releaseTabLayout;

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
        releaseVP = view.findViewById(R.id.release_viewpager2);
        releaseTabLayout = view.findViewById(R.id.release_tablayout);
        bindVTF();

    }

    //这个函数用于绑定ViewPager+TabLayout+Fragment
    private void bindVTF(){
        List<Fragment> fragments = new ArrayList<>();
        fragments.add(new VerifyFragment());
        fragments.add(new RumorFragment());
        releaseVP.setAdapter(new FragmentStateAdapter(this) {
            @NonNull
            @Override
            public Fragment createFragment(int position) {
                return fragments.get(position);
            }
            @Override
            public int getItemCount() {
                return fragments.size();
            }
        });
        new TabLayoutMediator(releaseTabLayout, releaseVP, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                switch (position){
                    case 0:
                        tab.setText("求证");
                        break;
                    case 1:
                        tab.setText("谣言");
                        break;
                    default:
                        break;
                }
            }
        }).attach();



    }
    @Override
    public void onPause() {
        super.onPause();
        releaseFAM.close(true);//当界面不可交互时，收起按钮菜单
    }
}