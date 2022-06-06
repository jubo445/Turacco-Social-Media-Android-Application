package com.example.socialmedia.Activity;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.socialmedia.Activity.chat.ChatActivity;
import com.example.socialmedia.Activity.chat.ChatList;
import com.example.socialmedia.Adapter.PostAdapter;
import com.example.socialmedia.HelperClass;
import com.example.socialmedia.Model.Post;
import com.example.socialmedia.Model.Users;
import com.example.socialmedia.Model.chatHistoryModel;
import com.example.socialmedia.R;
import com.example.socialmedia.databinding.ActivityProfileBinding;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import kotlin.Unit;

public class ProfileActivity extends AppCompatActivity implements PostAdapter.ItemClickListener {

    Intent intent;

    List<Post> postList = new ArrayList<>();
    PostAdapter adapter;
    String userId;
    StorageReference storageReference;
    TextView userName, mobile, email;
    TextView editProfile;
    int i = 0;
    DatabaseReference databaseReference;
    String uid = "";
    Uri uri = null;
    ProgressDialog progressDialog;

    Button ChatBtn;
    ActivityProfileBinding profileBinding;
    ActivityResultLauncher<Intent> someActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    // There are no request codes
                    Intent data = result.getData();

                    if (data != null) {

                        uri = data.getData();
                        updateImage(uri);
                    } else {
                        Toast.makeText(getApplicationContext(), "Image No Found", Toast.LENGTH_SHORT).show();
                    }

                }
            });


    private void openImagePicker() {

        ImagePicker.with(this)
                .createIntent(intent -> {
                    someActivityResultLauncher.launch(intent);
                    return Unit.INSTANCE;
                });


    }

    private void updateImage(Uri uri) {
        if (i == 1) {
            profileBinding.coverPhoto.setImageURI(uri);
        } else {

            profileBinding.profileImg.setImageURI(uri);
        }

        uploadImage(uri);

    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        profileBinding = ActivityProfileBinding.inflate(getLayoutInflater());
        setContentView(profileBinding.getRoot());
        progressDialog = HelperClass.createProgressDialog(ProfileActivity.this, "Uploading Image");
        uid = FirebaseAuth.getInstance().getUid();
        storageReference = FirebaseStorage.getInstance().getReference("Uploads");

        intent = getIntent();
        userId = intent.getStringExtra("userId");
        databaseReference = FirebaseDatabase.getInstance().getReference("Users");

        ChatBtn = findViewById(R.id.ChatBtn);


        userName = findViewById(R.id.userName);
        mobile = findViewById(R.id.mobile);
        email = findViewById(R.id.email);



        databaseReference.child(userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()) {
                    Users users = snapshot.getValue(Users.class);

                    userName.setText(users.getName());
                    mobile.setText(users.getMobile());
                    email.setText(users.getEmail());

                    Glide.with(getApplicationContext())
                            .load(users.getUser_image().toString())
                            .error(R.color.grey_600)
                            .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                            .into(profileBinding.profileImg);


                    Glide.with(getApplicationContext())
                            .load(users.getUser_cover_image().toString())
                            .error(R.color.grey_600)
                            .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                            .into(profileBinding.coverPhoto);


                    if (users.getFollowers() != null) {

                        if (users.getFollowers().containsKey(uid)) {

                            profileBinding.FollowBtn.setText("Following");
                        }

                    }


                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        profileBinding.FollowBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // equals
                // unfollow
                //follow
                addAction(!profileBinding.FollowBtn.getText().equals("Following"));


            }
        });

        ChatBtn.setOnClickListener(v -> {
            searchChatHistory();
        });


        if (!userId.equals(uid)) {
            // other profile
            profileBinding.changeCover.setVisibility(View.GONE);
            profileBinding.changePP.setVisibility(View.GONE);


        } else {
            profileBinding.container.setVisibility(View.GONE);
        }

        profileBinding.changePP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                i = 0;
                openImagePicker();
            }
        });

        profileBinding.changeCover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                i = 1;
                openImagePicker();
            }
        });

        loadMyPosts();

    }

    private void addAction(boolean isfollow) {
        if (isfollow) {
            databaseReference.child(uid).child("following").child(userId).setValue(true);
            databaseReference.child(userId).child("followers").child(uid).setValue(true);
            profileBinding.FollowBtn.setText("Following");
        } else {
            databaseReference.child(uid).child("following").child(userId).removeValue();
            databaseReference.child(userId).child("followers").child(uid).removeValue();
            profileBinding.FollowBtn.setText("Follow");
        }
    }

    private void uploadImage(Uri imageUri) {

        progressDialog.show();

        String fileId_local = uid + System.currentTimeMillis();
        StorageReference myStorage = storageReference.child(uid).child(fileId_local);

        myStorage.putFile(imageUri).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                myStorage.getDownloadUrl().addOnSuccessListener(uri -> {
                    String fileUrl = String.valueOf(uri);

                    dataUpdate(fileUrl);
                });
            }
        });


    }

    private void dataUpdate(String fileUrl) {
        //another local way to get postId
        //Using HashMap
        HashMap<String, Object> postMap = new HashMap<>();
        if (i == 0) {
            postMap.put("user_image", fileUrl);
        } else {
            postMap.put("user_cover_image", fileUrl);
        }

        databaseReference.child(userId).updateChildren(postMap).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {

                progressDialog.dismiss();

            }
        });
    }


    private void searchChatHistory(){
        DatabaseReference ref  = FirebaseDatabase.getInstance().getReference("ChatHistory");
        ref.child(userId+uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    // found the data
                    GotoChatPage(userId+uid);
                }else {
                    ref.child(uid+userId).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(snapshot.exists()){
                                // found the chat
                                GotoChatPage(uid+userId);
                            }else {
                                // no chat have to create

                                createChat(uid+userId);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void createChat(String key) {
        //user1 , user2  , lsatMsgSender , lastMsg ;
        //    long timestamp  ;
        chatHistoryModel historyModel = new chatHistoryModel(
                uid , userId , "", "" , key ,
                System.currentTimeMillis() / 1000

        );

        DatabaseReference ref  = FirebaseDatabase.getInstance().getReference("ChatHistory");

        ref.child(key).setValue(historyModel).addOnCompleteListener(
                task -> {
                    if(task.isSuccessful()){

                        GotoChatPage(key);

                    }
                }
        );

    }


    private void GotoChatPage(String key) {
        Intent intent = new Intent(ProfileActivity.this, ChatActivity.class);
        intent.putExtra("chatID", key);
        intent.putExtra("otherUserID" , userId);
        startActivity(intent);
    }

    private  void loadMyPosts(){
        DatabaseReference mref = FirebaseDatabase.getInstance().getReference("Post");
        mref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                postList.clear();

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Post post = dataSnapshot.getValue(Post.class);
                    if(post.getUserId().toString().equals(userId)){
                        postList.add(post);
                    }

                }
                adapter = new PostAdapter(ProfileActivity.this, postList, ProfileActivity.this);
                profileBinding.myPostList.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                profileBinding.myPostList.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    @Override
    public void onItemClick(Post model, int type) {

        if (type == 2) {
            /*
             share btn
             */
            shareThisPost(model);

        }else if (type == 1 ){
            Intent p = new Intent(getApplicationContext(), CommentPage.class);
            p.putExtra("model" , model) ;
            startActivity(p);
        }

    }

    private void shareThisPost(Post item) {

        //
        Intent intent = new Intent(getApplicationContext(), ChatList.class);
        intent.putExtra("POST" , item);
        startActivity(intent);


    }
}