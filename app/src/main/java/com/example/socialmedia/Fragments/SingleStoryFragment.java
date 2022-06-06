
package com.example.socialmedia.Fragments;

import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.socialmedia.Activity.StoryPage;
import com.example.socialmedia.Model.Story;
import com.example.socialmedia.R;


public class SingleStoryFragment extends Fragment {


    View view;
    ImageView imageView , pp ;
    TextView name ;

    Story story ;

    StoryPage storyPage ;
    public SingleStoryFragment(StoryPage storyPage, Story item) {
        // Required empty public constructor
        this.storyPage = storyPage ;
        this.story = item ;
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        view = inflater.inflate(R.layout.fragment_single_story, container, false);
        imageView = view.findViewById(R.id.image);
        pp = view.findViewById(R.id.pp);
        name = view.findViewById(R.id.name);


        Glide.with(view.getContext())
                .load(story.getImageLink())
                .into(imageView);
        Glide.with(view.getContext())
                .load(story.getUserImage())
                .into(pp);

        name.setText(story.getUserName());



        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);








    }


    @Override
    public void onDetach() {

        super.onDetach();

    }

    @Override
    public void onStop() {
        Log.d("TAG", "onStop: ");

        super.onStop();
    }


}