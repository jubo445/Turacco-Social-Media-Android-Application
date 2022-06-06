package com.example.socialmedia.ViewHolder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.socialmedia.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatViewHolder extends RecyclerView.ViewHolder {

    public CircleImageView circleImageView;
    public TextView textMessage;

    public ChatViewHolder(@NonNull View itemView) {
        super(itemView);

        circleImageView=itemView.findViewById(R.id.userProfile);
        textMessage=itemView.findViewById(R.id.textMessage);
    }
}
