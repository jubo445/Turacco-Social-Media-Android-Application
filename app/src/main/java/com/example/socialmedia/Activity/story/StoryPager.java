package com.example.socialmedia.Activity.story;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.socialmedia.Fragments.CreatePostFragment;
import com.example.socialmedia.Fragments.SingleStoryFragment;

import java.util.ArrayList;
import java.util.List;

public class StoryPager extends FragmentStateAdapter {

    List<SingleStoryFragment> views = new ArrayList<>();

    public StoryPager(@NonNull FragmentActivity fragmentActivity, List<SingleStoryFragment> viewss) {
        super(fragmentActivity);
        views = viewss;

    }


    @NonNull
    @Override
    public Fragment createFragment(int position) {

        return views.get(position);

    }


    @Override
    public int getItemCount() {
        return views.size();

    }
}