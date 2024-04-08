package com.example.greengrove.Adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.greengrove.Fragment.FruitFragment;
import com.example.greengrove.Fragment.HomeFragment;
import com.example.greengrove.Fragment.MeatsFragment;
import com.example.greengrove.Fragment.RecyclerViewFruitFragment;
import com.example.greengrove.Fragment.VegetableFragment;

public class ViewPager2Adapter extends FragmentStateAdapter {
    public ViewPager2Adapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 0:
                return new RecyclerViewFruitFragment();
            case 1:
                return new FruitFragment();
            case 2:
                return new VegetableFragment();
            case 3:
                return new MeatsFragment();
            default: return new HomeFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 4;
    }
}
