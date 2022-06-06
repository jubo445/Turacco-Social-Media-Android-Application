package com.example.socialmedia.Activity.chat;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.socialmedia.Adapter.chatAdapter;
import com.example.socialmedia.HelperClass;
import com.example.socialmedia.Model.Chat;
import com.example.socialmedia.Model.Post;
import com.example.socialmedia.Model.Users;
import com.example.socialmedia.Model.chatMsgModel;
import com.example.socialmedia.R;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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

import de.hdodenhof.circleimageview.CircleImageView;
import kotlin.Unit;

public class ChatActivity extends AppCompatActivity {
    Intent intent;
    String uid;
    String otherUserId = "";
    TextView UserName;
    EditText messageBox;
    ImageView sendBtn;
    String name = "";
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    String currentUserId;
    EditText chatINput;
    DatabaseReference mref, histroyref;
    RecyclerView recyclerView;
    List<Chat> chatList;
    chatAdapter chatAdapter;
    String chatID;
    List<chatMsgModel> loadedChat = new ArrayList<>();
    LinearLayoutManager llm;
    Uri uri = null;
    CircleImageView userImage;
    TextView userName;

    ProgressDialog progressDialog;

    ActivityResultLauncher<Intent> someActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    // There are no request codes
                    Intent data = result.getData();

                    if (data != null) {

                        uri = data.getData();
                        uploadImage(uri);

                    } else {
                        Toast.makeText(getApplicationContext(), "Image No Found", Toast.LENGTH_SHORT).show();
                    }

                }
            });

    Post post = null;

    private void openImagePicker() {

        ImagePicker.with(this)
                .createIntent(intent -> {
                    someActivityResultLauncher.launch(intent);
                    return Unit.INSTANCE;
                });


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        progressDialog = HelperClass.createProgressDialog(ChatActivity.this, "Sending Image...");
        uid = FirebaseAuth.getInstance().getUid();
        intent = getIntent();
        chatID = intent.getStringExtra("chatID");

        try {
            post = (Post) getIntent().getSerializableExtra("POST") ;
        }catch (Exception e){

        }

        otherUserId = intent.getStringExtra("otherUserID");
        recyclerView = findViewById(R.id.list);
        chatINput = findViewById(R.id.message_input);
        sendBtn = findViewById(R.id.message_send_btn);

        userImage = findViewById(R.id.userImage);
        userName = findViewById(R.id.header_title);

        llm = new LinearLayoutManager(getApplicationContext());
        llm.setStackFromEnd(true);
        chatAdapter = new chatAdapter(getApplicationContext(), loadedChat);
        recyclerView.setAdapter(chatAdapter);

        findViewById(R.id.backBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        recyclerView.setHasFixedSize(true);

        recyclerView.setLayoutManager(llm);

        mref = FirebaseDatabase.getInstance().getReference("ChatHistory").child(chatID);

        findViewById(R.id.cameraBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                openImagePicker();
            }
        });

        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String msg = chatINput.getText().toString();

                if (!msg.isEmpty()) {
                    sendTheMsg(msg.trim());
                }
            }
        });

        if (post != null) {

            sendMediaMsg(post.getImageLink(), post.getPostText());
        }

    }


    @Override
    protected void onResume() {
        super.onResume();
        loadOtherUserData();
        downloadMsg();
    }

    private void downloadMsg() {

        loadedChat = chatAdapter.sendList();

        mref.child("Chats").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                loadedChat.clear();

                if (dataSnapshot.exists()) {
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        chatMsgModel chat = ds.getValue(chatMsgModel.class);

                        // Log.d("MSGESDSS" , chat.getMsg()  );
                        loadedChat.add(chat);
                    }
                    //

                    // set adapter
                    chatAdapter.notifyDataSetChanged();

                    try {
                        recyclerView.smoothScrollToPosition(loadedChat.size() - 1);
                    } catch (Exception exception) {

                    }
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {


            }
        });


    }


    private void sendTheMsg(@NonNull String msg) {

        String msg_ID = mref.push().getKey();
        // String msg , msg_id , uid , time   ;

        final chatMsgModel msgModel = new chatMsgModel(msg.trim(), msg_ID, uid, "null", "null", System.currentTimeMillis() / 1000);

        mref.child("Chats").child(msg_ID).setValue(msgModel).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                chatINput.setText("");
                writeChatHistory(msg);
            }
        });


    }

    private void sendMediaMsg(@NonNull String msg , String str) {

        String msg_ID = mref.push().getKey();
        // String msg , msg_id , uid , time   ;

        final chatMsgModel msgModel = new chatMsgModel( str, msg_ID, uid, "image",
                "" + msg, System.currentTimeMillis() / 1000);

        mref.child("Chats").child(msg_ID).setValue(msgModel).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                chatINput.setText("");
                writeChatHistory(msg);
            }
        });


    }

    private void writeChatHistory(String msg) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("lsatMsgSender", uid);
        map.put("lastMsg", msg);
        map.put("timestamp", System.currentTimeMillis() / 1000);

        mref.updateChildren(map);

    }

    private void uploadImage(Uri imageUri) {

        progressDialog.show();

        String fileId_local = uid + System.currentTimeMillis();
        StorageReference myStorage = FirebaseStorage.getInstance().getReference("Uploads").child(uid).child(fileId_local);

        myStorage.putFile(imageUri).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                myStorage.getDownloadUrl().addOnSuccessListener(uri -> {
                    String fileUrl = String.valueOf(uri);
                    progressDialog.dismiss();

                    sendMediaMsg(fileUrl, "");
                });
            }
        });


    }

    private void loadOtherUserData() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(otherUserId);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()) {
                    Users users = snapshot.getValue(Users.class);

                    name = users.getName().toString();

                    Glide.with(getApplicationContext())
                            .load(users.getUser_image().toString())
                            .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                            .error(R.drawable.placeholder)
                            .into(userImage);

                    userName.setText(name);

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }


}