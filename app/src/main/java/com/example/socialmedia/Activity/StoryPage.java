package com.example.socialmedia.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import com.example.socialmedia.Activity.story.StoryPager;
import com.example.socialmedia.ConvertTime;
import com.example.socialmedia.Fragments.SingleStoryFragment;
import com.example.socialmedia.Model.Story;
import com.example.socialmedia.Model.StoryResp;
import com.example.socialmedia.R;

import java.util.ArrayList;
import java.util.List;

import pt.tornelas.segmentedprogressbar.SegmentedProgressBar;

public class StoryPage extends AppCompatActivity {
    List<SingleStoryFragment> views = new ArrayList<>();
    ViewPager2 vpPager;
    private SegmentedProgressBar storiesProgressView;
    private Handler headerHandler = new Handler();
    List<Story> storyList = new ArrayList<>();
    StoryResp modelResp;
    int pos = 0  ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_story_page);

        modelResp = (StoryResp) getIntent().getSerializableExtra("model");
        pos = getIntent().getIntExtra("pos" , 0 );

        storyList = modelResp.getStories();

        for (Story item : storyList) {


            if(ConvertTime.getTimeDifferance(item.getDate()) == 0){
                views.add(new SingleStoryFragment(this, item));
            }



        }
        storiesProgressView = (SegmentedProgressBar) findViewById(R.id.spb);

        vpPager = findViewById(R.id.pager);
        StoryPager adapterViewPager = new StoryPager(StoryPage.this, views);
        vpPager.setUserInputEnabled(true);
        vpPager.setAdapter(adapterViewPager);

        vpPager.setCurrentItem(pos, false);

        vpPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                headerHandler.removeCallbacks(headerRunnable);
                headerHandler.postDelayed(headerRunnable, 5000); // Slide duration 3 seconds
                if (position == (views.size() - 1)) {
                    vpPager.setCurrentItem(0);
                }
                storiesProgressView.reset();
                storiesProgressView.start();

            }
        });

        findViewById(R.id.cross).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private Runnable headerRunnable = new Runnable() {
        @Override
        public void run() {
            vpPager.setCurrentItem(vpPager.getCurrentItem() + 1, true);
            storiesProgressView.reset();
            storiesProgressView.start();
        }
    };

    @Override
    protected void onPause() {
        super.onPause();
        headerHandler.removeCallbacks(headerRunnable);
    }

    @Override
    protected void onResume() {
        super.onResume();
        storiesProgressView.start();
        headerHandler.postDelayed(headerRunnable, 5000); // Slide duration 3 seconds
    }

}