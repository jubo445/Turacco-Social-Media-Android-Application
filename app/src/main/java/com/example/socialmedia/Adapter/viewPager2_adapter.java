package com.example.socialmedia.Adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.socialmedia.Fragments.CreatePostFragment;
import com.example.socialmedia.Fragments.FeedsFragment;
import com.example.socialmedia.Fragments.FriendsFragment;
import com.example.socialmedia.Fragments.MyProfileFragment;
import com.example.socialmedia.Fragments.NotificationFragment;

public class viewPager2_adapter extends FragmentStateAdapter {


    public viewPager2_adapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }


    @NonNull
    @Override
    public Fragment createFragment(int position) {


        switch (position) {
            case 0:
                return new FeedsFragment();
            case 1:
                return new FriendsFragment();
            case 2:
                return new CreatePostFragment();

            case 3:
                return new NotificationFragment();
            case 4:
                return new MyProfileFragment();

        }

        return new FeedsFragment();
    }


    @Override
    public int getItemCount() {
        return 5;
    }

}
