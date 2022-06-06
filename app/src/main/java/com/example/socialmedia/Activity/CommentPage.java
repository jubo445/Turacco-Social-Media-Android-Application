package com.example.socialmedia.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.NotificationManager;
import android.os.Bundle;
import android.util.Log;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.socialmedia.Activity.Notification.NotificationHelper;
import com.example.socialmedia.Activity.Notification.PrefUtils;
import com.example.socialmedia.Adapter.CommentAdapter;
import com.example.socialmedia.HelperClass;
import com.example.socialmedia.Model.CommonModel;
import com.example.socialmedia.Model.Post;
import com.example.socialmedia.Model.Users;
import com.example.socialmedia.R;
import com.example.socialmedia.databinding.ActivityCommentPageBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CommentPage extends AppCompatActivity implements CommentAdapter.ItemClickListener {
    Post post;
    ActivityCommentPageBinding binding;
    CommentAdapter commentAdapter;
    List<CommonModel> commentList = new ArrayList<>();
    DatabaseReference databaseReference;
    FirebaseAuth firebaseAuth;
    String userToken = "" ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCommentPageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        firebaseAuth = FirebaseAuth.getInstance();
        post = (Post) getIntent().getSerializableExtra("model");
        databaseReference = FirebaseDatabase.getInstance().getReference("Post").child("" + post.getPostId()).child("comments");

        binding.commentList.setLayoutManager(new LinearLayoutManager(this));
        if (post.getComments() != null) {

         Map<String, CommonModel>   commonMap = post.getComments();

            commentList = new ArrayList<CommonModel>(commonMap.values());


        }

        commentAdapter = new CommentAdapter(commentList, this, this);
        binding.commentList.setAdapter(commentAdapter);

        binding.sendBtn.setOnClickListener(
                view -> {
                    String comment = binding.commentEdit.getText().toString();
                    if (comment.isEmpty()) {
                        HelperClass.showMsg(this, "Can't Be Empty");
                    } else {
                        AddComment(comment);
                    }
                }
        );

    }

    private void AddComment(String comment) {
        String key = databaseReference.push().getKey().toString();
        CommonModel model = new CommonModel(
                key,
                firebaseAuth.getUid().toString(),
                comment

        );
        databaseReference.child(key).setValue(model).addOnCompleteListener(task -> {

            commentList.add(model);
            commentAdapter.notifyItemInserted(commentList.size()-1);
            binding.commentEdit.setText("");
            Users users = PrefUtils.loadUser(getApplicationContext());

            new Thread(new Runnable() {
                @Override
                public void run() {
            /*
            // Run operation here
            */

                    NotificationHelper.createNotifcation("Has Commented On Your Post",
                            post.getUserId(),firebaseAuth.getUid().toString()
                            , "POST" , post.getPostId().toString() , "" ) ;

                    NotificationHelper.send(userToken , users.getName()+" Has Commented On Your Post");
                    // After getting the result
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            // Post the result to the main thread
                        }
                    });
                }
            }).start();


        });



    }

    @Override
    public void onItemClick(CommonModel model) {

    }

    @Override
    protected void onResume() {
        super.onResume();
        Users user = PrefUtils.loadUser(getApplicationContext());

        if (user != null) {

            Glide.with(getApplicationContext())
                    .load(user.getUser_image() + "")
                    .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                    .into(binding.profilePic);
        }

        DatabaseReference mref = FirebaseDatabase.getInstance().getReference("Users");
        mref.child(post.getUserId()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()) {
                    Users user = snapshot.getValue(Users.class);
                    userToken = user.getN_token();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}