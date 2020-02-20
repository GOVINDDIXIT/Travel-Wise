package com.example.needhelp.adapter;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.needhelp.Group;
import com.example.needhelp.Request;
import com.example.needhelp.fragment.ChatFragment;

public class TabAccessAdapter extends FragmentPagerAdapter {

    public TabAccessAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return new Request();
            case 1:
                return new ChatFragment();
            case 2:
                return new Group();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position){
            case 0:
                return "Request";
            case 1:
                return "Chat";
            case 2:
                return "Group";
            default:
                return null;
        }
    }
}
