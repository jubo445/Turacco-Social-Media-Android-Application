package com.example.socialmedia.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.socialmedia.Activity.Notification.PrefUtils;
import com.example.socialmedia.Model.Users;
import com.example.socialmedia.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    TextInputEditText nameEdt, numberEdt, emailEdt, passwordEdt;
    Button registerBtn;
    TextView oldUserTV;

    ProgressDialog progressDialog;
    public Map<String, Boolean> followers = new HashMap<>();
    public Map<String, Boolean> following = new HashMap<>();
    FirebaseAuth firebaseAuth;
    DatabaseReference databaseReference;

    FirebaseUser firebaseUser;
    String currentUID;
    String token = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        progressDialog = new ProgressDialog(this);

        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            Log.w("TAG", "Fetching FCM registration token failed", task.getException());
                            //return;
                        } else {
                            // Get new FCM registration token
                            token = task.getResult();
                        }


                    }
                });

        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("Users");

        nameEdt = findViewById(R.id.nameEdt);
        numberEdt = findViewById(R.id.numberEdt);
        emailEdt = findViewById(R.id.emailEdt);
        passwordEdt = findViewById(R.id.passwordEdt);
        registerBtn = findViewById(R.id.registerBtn);
        oldUserTV = findViewById(R.id.oldUserTV);

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.setTitle("creating account...");
                progressDialog.setMessage("Please wait...");
                progressDialog.show();

                String nameStr = nameEdt.getText().toString().trim();
                String numberStr = numberEdt.getText().toString().trim();
                String emailStr = emailEdt.getText().toString().trim();
                String passwordStr = passwordEdt.getText().toString().trim();

                if (nameStr.equals("")) {

                    nameEdt.setError("Please Enter Your Name!!!");
                    progressDialog.dismiss();
                } else if (numberStr.equals("")) {

                    numberEdt.setError("Please Enter Your Number!!!");
                    progressDialog.dismiss();
                } else if (numberStr.length() < 11) {

                    numberEdt.setError("Enter The Correct Phone Number!!!");
                    progressDialog.dismiss();
                } else if (numberStr.length() > 11) {

                    numberEdt.setError("Enter The Correct Phone Number!!!");
                    progressDialog.dismiss();
                } else if (emailStr.equals("")) {

                    emailEdt.setError("Please Enter Your Email!!!");
                    progressDialog.dismiss();
                } else if (passwordStr.equals("")) {

                    passwordEdt.setError("Please Enter Your Password...");
                    progressDialog.dismiss();
                } else if (passwordStr.length() < 6) {

                    passwordEdt.setError("Password need to be more than 6 digits!");
                    progressDialog.dismiss();
                } else {
                    firebaseAuth.createUserWithEmailAndPassword(emailStr, passwordStr).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if (task.isSuccessful()) {

                                firebaseUser = firebaseAuth.getCurrentUser();
                                if (firebaseUser != null) {
                                    currentUID = firebaseUser.getUid();

                                }

                                HashMap<String, String> dataMap = new HashMap<>();
                                dataMap.put("name", nameStr);
                                dataMap.put("mobile", numberStr);
                                dataMap.put("email", emailStr);
                                dataMap.put("password", passwordStr);
                                dataMap.put("user_id", currentUID);

                                databaseReference.child(currentUID).setValue(dataMap);
                                String email, mobile, name, password, user_id, user_image = "", user_cover_image = "";
                                String n_token = "";
                                Users users = new Users(
                                        emailStr, numberStr, nameStr, passwordStr, currentUID, "", "", token
                                        , followers
                                        , following
                                );

                                PrefUtils.saveUser(users, getApplicationContext());


                                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                                finish();
                                progressDialog.dismiss();

                            } else {
                                progressDialog.dismiss();
                                Toast.makeText(RegisterActivity.this, "Error" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }


            }
        });
        oldUserTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                finish();
            }
        });
    }
}