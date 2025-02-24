package com.example.yumcycle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class ViewPagerAdapter extends FragmentStateAdapter {

    private final int PAGE_COUNT = 2;

    public ViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if (position == 0) {
            // Inflate the first page directly (activity_main.xml)
            return new StaticLayoutFragment(R.layout.activity_main);
        } else {
            // Use SecondFragment for the second page
            return new SecondFragment();
        }
    }

    @Override
    public int getItemCount() {
        return PAGE_COUNT; // Two pages
    }
}
