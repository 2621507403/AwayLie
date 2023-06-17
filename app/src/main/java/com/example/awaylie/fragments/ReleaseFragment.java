package com.example.awaylie.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.example.awaylie.R;
import com.example.awaylie.ReleaseQuestionActivity;
import com.example.awaylie.fragments.releaseFragments.RumorFragment;
import com.example.awaylie.fragments.releaseFragments.TruthFragment;
import com.example.awaylie.fragments.releaseFragments.VerifyFragment;
import com.github.clans.fab.FloatingActionMenu;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.xuexiang.xui.widget.searchview.MaterialSearchView;

import java.util.ArrayList;
import java.util.List;


public class ReleaseFragment extends Fragment  {
    private ViewPager2 releaseVP;
    private TabLayout releaseTabLayout;
    private MaterialSearchView releaseSearchView;
    private MaterialToolbar releaseToolBar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_release, container, false);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.search_menu,menu);
        MenuItem menuItem = menu.findItem(R.id.release_search);
        releaseSearchView.setMenuItem(menuItem);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        releaseVP = view.findViewById(R.id.release_viewpager2);
        releaseTabLayout = view.findViewById(R.id.release_tablayout);
        releaseSearchView = view.findViewById(R.id.release_searchView);
        releaseToolBar = view.findViewById(R.id.release_toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(releaseToolBar);
        releaseToolBar.setTitle("发布");
        releaseToolBar.setTitleCentered(true);

        bindVTF();
    }
    //这个函数用于绑定ViewPager+TabLayout+Fragment
    private void bindVTF(){
        List<Fragment> fragments = new ArrayList<>();
        fragments.add(new VerifyFragment());
        fragments.add(new RumorFragment());
        fragments.add(new TruthFragment());
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
                    case 2:
                        tab.setText("真相");
                        break;
                    default:
                        break;
                }
            }
        }).attach();
    }
}