package com.example.socialmedia.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.socialmedia.Activity.Notification.PrefUtils;
import com.example.socialmedia.HelperClass;
import com.example.socialmedia.Model.Users;
import com.example.socialmedia.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class ProfileEditActivity extends AppCompatActivity {


    TextInputEditText userName, mobile, email;

    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    String currentUserId;

    DatabaseReference databaseReference;

    Button updatebtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_edit);


        databaseReference = FirebaseDatabase.getInstance().getReference("Users");

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        if (firebaseUser != null) {
            currentUserId = firebaseUser.getUid();
        }


        userName = findViewById(R.id.userName);
        mobile = findViewById(R.id.mobile);


        updatebtn = findViewById(R.id.updateBtn);

        databaseReference.child(currentUserId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                Users users = snapshot.getValue(Users.class);

                userName.setText(users.getName());
                mobile.setText(users.getMobile());
           //     email.setText(users.getEmail());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        updatebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String usernameStr = userName.getText().toString().trim();
                String mobileStr = mobile.getText().toString().trim();
                // String emailStr=email.getText().toString().trim();

                if (usernameStr.isEmpty()) {
                    userName.setError("Can't Be Empty");
                }

                if (mobileStr.isEmpty()) {
                    mobile.setError("Can't Be Empty");
                }

                updateMyProfile(usernameStr, mobileStr);

            }
        });
    }

    private void updateMyProfile(String usernameStr, String mobileStr) {
        ProgressDialog progressDialog = HelperClass.createProgressDialog(ProfileEditActivity.this, "Updating Profile");
        progressDialog.show();
        HashMap<String, Object> updateMap = new HashMap<>();

        updateMap.put("name", usernameStr);
        updateMap.put("mobile", mobileStr);

        Users user = PrefUtils.loadUser(getApplicationContext());


        databaseReference.child(currentUserId).updateChildren(updateMap).addOnCompleteListener(task -> {

            if (task.isSuccessful()) {
                user.setName(usernameStr);
                user.setMobile(mobileStr);
                PrefUtils.saveUser(user, getApplicationContext());
                progressDialog.dismiss();
                Toast.makeText(ProfileEditActivity.this, "Profile Updated", Toast.LENGTH_SHORT).show();
            }

        });
    }
}