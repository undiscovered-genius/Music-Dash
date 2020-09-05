package com.example.musictab;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class PagerController extends FragmentPagerAdapter {
    int tabcounts;

    public PagerController(@NonNull FragmentManager fm, int tabcounts) {
        super(fm);
        this.tabcounts = tabcounts;
    }

    @NonNull
    @Override
    public Fragment getItem(int i) {
        switch (i) {
            case 0:
                return new AllMusic();

            case 1:
                return new Albums();
            case 2:
                return new Playlists();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return tabcounts;
    }
}
