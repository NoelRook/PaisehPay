package com.example.paisehpay.tabBar;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import java.util.ArrayList;

public class ViewPagerAdapter extends FragmentStateAdapter {
    private final ArrayList<Fragment> fragmentList = new ArrayList<>();
    private final ArrayList<String> fragmentTitleList = new ArrayList<>();

    // constructor
    public ViewPagerAdapter(@NonNull FragmentActivity fa) {
        super(fa);
    }

    // getter
    @Override
    public int getItemCount() {
        return fragmentList.size();
    }
    public String getTitle(int position) {
        return fragmentTitleList.get(position);
    }

    // functions
    public void addFragment(Fragment fragment, String title) {
        fragmentList.add(fragment);
        fragmentTitleList.add(title);
    }
    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return fragmentList.get(position); // THIS MUST return the fragment with the correct Bundle
    }

}


