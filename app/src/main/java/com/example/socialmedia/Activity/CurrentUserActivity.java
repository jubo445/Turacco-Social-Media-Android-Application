package com.example.socialmedia.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.socialmedia.Model.Users;
import com.example.socialmedia.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class CurrentUserActivity extends AppCompatActivity {

    TextView userName,mobile,email;
    TextView editProfile;

    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    String currentUserId;

    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_user);

        editProfile=findViewById(R.id.editProfile);
        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),ProfileEditActivity.class));
            }
        });

        databaseReference= FirebaseDatabase.getInstance().getReference("Users");

        firebaseAuth=FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        if(firebaseUser!=null){
            currentUserId=firebaseUser.getUid();
        }


        userName=findViewById(R.id.userName);
        mobile=findViewById(R.id.mobile);
        email=findViewById(R.id.email);

        databaseReference.child(currentUserId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                Users users=snapshot.getValue(Users.class);

                userName.setText(users.getName());
                mobile.setText(users.getMobile());
                email.setText(users.getEmail());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}