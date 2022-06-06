package com.example.socialmedia.Model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class StoryResp implements Serializable {

    public StoryResp(List<Story> stories) {
        this.stories = stories;
    }

    public List<Story> getStories() {
        return stories;
    }

    public void setStories(List<Story> stories) {
        this.stories = stories;
    }

    List<Story> stories = new ArrayList<>();
}
