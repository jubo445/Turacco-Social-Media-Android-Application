package com.example.socialmedia.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.socialmedia.Activity.chat.ChatActivity;
import com.example.socialmedia.Model.Users;
import com.example.socialmedia.Activity.ProfileActivity;
import com.example.socialmedia.R;
import com.example.socialmedia.ViewHolder.UserViewHolder;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserViewHolder> {
    Context context;
    List<Users> usersList;

    public UserAdapter(Context context, List<Users> usersList) {
        this.context = context;
        this.usersList = usersList;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_user,parent,false);
        return new UserViewHolder(view);
    }

    public List<Users> getList(){
        return usersList ;
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {

        Users users=usersList.get(position);
        holder.email.setText(users.getEmail());
        holder.mobile.setText(users.getMobile());
        holder.userName.setText(users.getName());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, ProfileActivity.class);
                intent.putExtra("userId", users.getUser_id());
                context.startActivity(intent);
            }
        });
        holder.chatIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(context, "Chat With "+ users.getName(), Toast.LENGTH_SHORT).show();

                Intent intent=new Intent(context, ChatActivity.class);
                intent.putExtra("userId", users.getUser_id());
                context.startActivity(intent);

            }
        });

    }

    @Override
    public int getItemCount() {
        return usersList.size();
    }
}
