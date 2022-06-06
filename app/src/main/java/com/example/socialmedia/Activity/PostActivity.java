package com.example.socialmedia.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.socialmedia.Model.Post;
import com.example.socialmedia.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;

public class PostActivity extends AppCompatActivity {

    EditText postEdt;
    Button postSubmit;
    ImageView postImg;

    DatabaseReference databaseReference;
    StorageReference storageReference;

    FirebaseUser firebaseUser;
    String currentUserId;
    Uri uri;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        databaseReference= FirebaseDatabase.getInstance().getReference("Post");
        storageReference= FirebaseStorage.getInstance().getReference("Uploads");
        firebaseUser= FirebaseAuth.getInstance().getCurrentUser();

        if (firebaseUser!=null){
            currentUserId = firebaseUser.getUid();
        }

        postEdt=findViewById(R.id.postEdt);
        postSubmit=findViewById(R.id.postSubmit);
        postImg=findViewById(R.id.postImg);
        postImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent,101);
            }
        });

        postSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String post = postEdt.getText().toString();

                if (uri!=null){
                    postUpdateToServer(post,uri);
                }
                

            }
        });
    }

    private void postUpdateToServer(String postStr,Uri imageUri) {

        String fileId_local=currentUserId + System.currentTimeMillis();
        StorageReference myStorage = storageReference.child(currentUserId).child(fileId_local);

        myStorage.putFile(imageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                if (task.isSuccessful()){



                    myStorage.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {

                            String fileUrl= String.valueOf(uri);

                            dataUpdate(postStr,fileUrl);

                        }
                    });
                }
            }
        });


    }

    private void dataUpdate(String postStr, String fileUrl) {

        String postId_fb=databaseReference.push().getKey();

        //another local way to get postId


        //Using HashMap
        HashMap<String,Object> postMap = new HashMap<>();
        postMap.put("postText",postStr);
        postMap.put("userId",currentUserId);
        postMap.put("postId",postId_fb);
        postMap.put("imageLink",fileUrl);

        databaseReference.child(postId_fb).setValue(postMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    startActivity(new Intent(getApplicationContext(),MainActivity.class));
                    Toast.makeText(PostActivity.this, "Post Uploaded", Toast.LENGTH_SHORT).show();
                    finish();

                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode==101){
            if (resultCode==RESULT_OK){
                if (data!=null){
                    uri = data.getData();

                    postImg.setImageURI(uri);
                }
            }
        }
    }
}
