package com.example.musicmedia.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.musicmedia.fragment.FragmentPlayerLyrics;
import com.example.musicmedia.fragment.FragmentPlayerInfo;

public class PlayerViewPagerAdapter extends FragmentStateAdapter {
    public PlayerViewPagerAdapter(@NonNull Fragment fragment) {
        super(fragment);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new FragmentPlayerInfo();
            case 1:
                return new FragmentPlayerLyrics();
            default:
                return new FragmentPlayerInfo();
        }
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
