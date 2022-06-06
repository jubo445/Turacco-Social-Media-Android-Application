package com.example.socialmedia.ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.socialmedia.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserViewHolder extends RecyclerView.ViewHolder {

    public CircleImageView userProfile;
    public TextView userName,mobile,email;

    public ImageView chatIcon;



    public UserViewHolder(@NonNull View itemView) {
        super(itemView);
        userProfile=itemView.findViewById(R.id.userProfile);
        userName=itemView.findViewById(R.id.userName);
        mobile=itemView.findViewById(R.id.mobile);
        email=itemView.findViewById(R.id.email);
        chatIcon=itemView.findViewById(R.id.chatIcon);
    }
}
