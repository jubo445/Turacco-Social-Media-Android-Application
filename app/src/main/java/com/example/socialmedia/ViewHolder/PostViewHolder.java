package com.example.socialmedia.ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.socialmedia.R;

public class PostViewHolder extends RecyclerView.ViewHolder {

    public TextView postText, token, userName, timeTv, likeCount, commentCount;
    public ImageView postImage, userImage, commentBtn, reactBtn, shareBtn;

    public PostViewHolder(@NonNull View itemView) {
        super(itemView);

        postText = itemView.findViewById(R.id.postDesc);
        timeTv = itemView.findViewById(R.id.time_tv);
        likeCount = itemView.findViewById(R.id.likeNumber);
        commentCount = itemView.findViewById(R.id.commentNumber);
        userName = itemView.findViewById(R.id.userName);
        userImage = itemView.findViewById(R.id.profile_pic);
        postImage = itemView.findViewById(R.id.mediaViewer);
        commentBtn = itemView.findViewById(R.id.commentBtn);
        reactBtn = itemView.findViewById(R.id.reactBtn);
        shareBtn = itemView.findViewById(R.id.shareBtn);
        token = itemView.findViewById(R.id.token);
    }
}
