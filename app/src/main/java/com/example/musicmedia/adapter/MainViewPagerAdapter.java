package com.example.musicmedia.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.musicmedia.activity.MainActivity;
import com.example.musicmedia.fragment.FragmentFavorite;
import com.example.musicmedia.fragment.FragmentOffline;
import com.example.musicmedia.fragment.FragmentOnline;
import com.example.musicmedia.fragment.FragmentPermission;


public class MainViewPagerAdapter extends FragmentStateAdapter {

    public MainViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new FragmentOnline();
            case 1:
                return new FragmentOffline();
            case 2:
                return new FragmentFavorite();
            default:
                return new FragmentOnline();
        }
    }

    @Override
    public int getItemCount() {
        return 3;
    }

}
