package com.example.socialmedia.Adapter;

import android.content.Context;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.socialmedia.ConvertTime;
import com.example.socialmedia.HelperClass;
import com.example.socialmedia.Model.chatMsgModel;
import com.example.socialmedia.R;
import com.google.firebase.auth.FirebaseAuth;
import com.joooonho.SelectableRoundedImageView;


import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class chatAdapter extends RecyclerView.Adapter<chatAdapter.myHolder> {

    List<chatMsgModel> chatList;
    private Context contextt;
    private static final int MSG_TYPE_RIGHT = 1;
    private static final int MSG_TYPE_LEFT = 0;
    private static final int IMAGE_MSG_TYPE_RIGHT = 5;
    private static final int IMAGE_MSG_TYPE_LEFT = 6;
    String uid = FirebaseAuth.getInstance().getUid();


    public chatAdapter(Context context, List<chatMsgModel> chatList) {
        this.contextt = context;
        this.chatList = chatList;

    }

    @NonNull
    @Override
    public myHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


        View view = null;

        if (viewType == MSG_TYPE_LEFT) {
            view = LayoutInflater
                    .from(parent.getContext())
                    .inflate(R.layout.row_for_left_chat, parent, false);
        } else if (viewType == MSG_TYPE_RIGHT) {
            view = LayoutInflater
                    .from(parent.getContext())
                    .inflate(R.layout.row_for_right_chat, parent, false);
        } else if (viewType == IMAGE_MSG_TYPE_LEFT) {
            view = LayoutInflater
                    .from(parent.getContext())
                    .inflate(R.layout.row_for_image_left, parent, false);
        } else if (viewType == IMAGE_MSG_TYPE_RIGHT) {
            view = LayoutInflater
                    .from(parent.getContext())
                    .inflate(R.layout.row_for_image_right, parent, false);
        }


        return new myHolder(view);
    }

    public List<chatMsgModel> sendList(){
        return chatList ;
    }


    @Override
    public void onBindViewHolder(@NonNull myHolder holder,  int position) {

        String msg = chatList.get(position).getMsg();
        holder.msgView.setText(msg);

        holder.date.setText(ConvertTime.getTimeAgo(new Date(chatList.get(position).getTime() * 1000 )));

        if (!chatList.get(position).getContent_type().equals("null") && !chatList.get(position).getContent_link().equals("null")) {

            Glide.with(contextt).load(chatList.get(position).getContent_link()).into(holder.imageView);
            //holder.imageView.setOnClickListener(v -> Toast.makeText(contextt, chatList.get(position).getContent_link() + "", Toast.LENGTH_LONG).show());

        }


    }

    @Override
    public int getItemViewType(int position) {


        if (chatList.get(position).getUid().equals(uid) && chatList.get(position).getContent_type().equals("null")) {

            return MSG_TYPE_RIGHT;

        } else if (chatList.get(position).getUid().equals(uid) && !chatList.get(position).getContent_type().equals("null")) {

            return IMAGE_MSG_TYPE_RIGHT;

        } else if (!chatList.get(position).getUid().equals(uid) && chatList.get(position).getContent_type().equals("null")) {
            return MSG_TYPE_LEFT;
        } else {
            return IMAGE_MSG_TYPE_LEFT;
        }


    }

    @Override
    public int getItemCount() {
        return chatList.size();
    }


    class myHolder extends RecyclerView.ViewHolder {

        public TextView msgView, date;
        SelectableRoundedImageView imageView;


        public myHolder(@NonNull View itemView) {
            super(itemView);


            msgView = itemView.findViewById(R.id.msgTv);
            date = itemView.findViewById(R.id.dateview);
            imageView = itemView.findViewById(R.id.image);


        }

        private void setImageView(String url, Context context) {
            imageView = itemView.findViewById(R.id.image);
            Glide.with(context).load(url).into(imageView);
        }


    }


}





