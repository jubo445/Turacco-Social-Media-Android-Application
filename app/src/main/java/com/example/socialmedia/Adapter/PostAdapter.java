package com.example.socialmedia.Adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;


import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.socialmedia.Activity.Notification.NotificationHelper;
import com.example.socialmedia.Activity.Notification.PrefUtils;
import com.example.socialmedia.Activity.ProfileActivity;
import com.example.socialmedia.ConvertTime;
import com.example.socialmedia.Model.Post;
import com.example.socialmedia.Model.Users;
import com.example.socialmedia.R;
import com.example.socialmedia.ViewHolder.PostViewHolder;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class PostAdapter extends RecyclerView.Adapter<PostViewHolder> {
    Context context;
    List<Post> postList;
    DatabaseReference mref = FirebaseDatabase.getInstance().getReference("Users");
    String uid = FirebaseAuth.getInstance().getUid();
    private ItemClickListener itemClickListener;

    public PostAdapter(Context context, List<Post> postList, ItemClickListener itemClickListener) {
        this.context = context;
        this.postList = postList;
        this.itemClickListener = itemClickListener;
    }

    public PostAdapter() {
    }

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.item_post_new, parent, false);

        return new PostViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {

        Post post = postList.get(position);
        holder.postText.setText(post.getPostText());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                itemClickListener.onItemClick(post, 0);
            }
        });

        holder.commentBtn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        itemClickListener.onItemClick(post, 1);
                    }
                }
        );


        holder.shareBtn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        itemClickListener.onItemClick(post, 2);
                    }
                }
        );


        if (post.getImageLink().equals("null") || post.getImageLink().isEmpty()) {
            holder.postImage.setVisibility(View.GONE);
        } else {
            holder.postImage.setVisibility(View.VISIBLE);
            Glide
                    .with(context)
                    .load(post.getImageLink())
                    .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                    .into(holder.postImage);
        }

        if (post.getLikes() != null) {
            holder.likeCount.setText(post.getLikes().size() + "");
            if (post.getLikes().containsKey(uid)) {
                holder.reactBtn.setBackground(ContextCompat.getDrawable(
                        context,
                        R.drawable.ic_heart
                ));
            } else {

                holder.reactBtn.setBackground(ContextCompat.getDrawable(
                        context,
                        R.drawable.ic_heart_shape_outline
                ));

            }


        } else {
            holder.likeCount.setText("0");
            holder.reactBtn.setBackground(ContextCompat.getDrawable(
                    context,
                    R.drawable.ic_heart_shape_outline
            ));
        }

        if (post.getComments() != null) {
            Log.d("TAG", "onCreate: " + post.getComments().toString());
            holder.commentCount.setText(post.getComments().size() + "");
        } else {
            holder.commentCount.setText("0");
        }


        if (post.getDate() != null) {

            SimpleDateFormat df = new SimpleDateFormat(ConvertTime.TIME_FORMAT);
            try {
                Date date = df.parse(post.getDate());
                holder.timeTv.setText(ConvertTime.getTimeAgo(date));
            } catch (ParseException e) {
                e.printStackTrace();
                holder.timeTv.setText(post.getDate() + "");
            }

        }

        holder.reactBtn.setOnClickListener(view -> {
            DatabaseReference mref = FirebaseDatabase.getInstance().getReference("Post").child(post.getPostId()).child("likes");
            Map<String, Boolean> map = post.getLikes();
            if (post.getLikes() != null) {

                if (map.containsKey(uid)) {
                    // remove like
                    mref.child(uid).removeValue();
                    post.getLikes().remove(uid);
                    holder.reactBtn.setBackground(ContextCompat.getDrawable(
                            context,
                            R.drawable.ic_heart_shape_outline
                    ));

                } else {
                    // add likes
                    createNotification(post, holder.token.getText().toString());
                    mref.child(uid).setValue(true);
                    post.getLikes().put(uid, true);
                    holder.reactBtn.setBackground(ContextCompat.getDrawable(
                            context,
                            R.drawable.ic_heart
                    ));
                }
            } else {
                // add likes

                createNotification(post, holder.token.getText().toString());
                mref.child(uid).setValue(true);
                post.getLikes().put(uid, true);
                holder.reactBtn.setBackground(ContextCompat.getDrawable(
                        context,
                        R.drawable.ic_heart
                ));


            }

            holder.likeCount.setText(post.getLikes().size() + "");

        });

        mref.child(post.getUserId()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()) {
                    Users user = snapshot.getValue(Users.class);
                    holder.userName.setText(user.getName().toString());
                    Glide.with(context)
                            .load(user.getUser_image().toString())
                            .error(R.drawable.placeholder_large_dark)
                            .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                            .into(holder.userImage);
                    holder.token.setText(user.getN_token().toString() + "");
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        holder.userName.setOnClickListener(
                view -> {
                    Intent p = new Intent(context, ProfileActivity.class);
                    p.putExtra("userId", post.getUserId().toString());
                    context.startActivity(p);
                }
        );
        holder.userImage.setOnClickListener(
                view -> {
                    Intent p = new Intent(context, ProfileActivity.class);
                    p.putExtra("userId", post.getUserId().toString());
                    context.startActivity(p);
                }
        );

    }

    private void createNotification(Post post, String token) {

        Users users = PrefUtils.loadUser(context);


        new Thread(() -> {
        /*
        // Run operation here
        */
            NotificationHelper.createNotifcation("Has Liked Your Post",
                    post.getUserId(), uid
                    , "POST", post.getPostId().toString(), "");

            NotificationHelper.send(token, users.getName()+" Has Liked Your Post");
            // After getting the result
            ((AppCompatActivity) context).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    // Post the result to the main thread

                }
            });
        }).start();

    }

    public interface ItemClickListener {
        void onItemClick(Post model, int type);
    }


    @Override
    public int getItemCount() {
        return postList.size();
    }
}
