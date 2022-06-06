package com.example.socialmedia.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {

    TextInputEditText emailEdt, passwordEdt;
    Button loginBtn;
    TextView newUserTV;

    ProgressDialog progressDialog;

    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        progressDialog = new ProgressDialog(this);

        firebaseAuth = FirebaseAuth.getInstance();

        emailEdt = findViewById(R.id.emailEdt);
        passwordEdt = findViewById(R.id.passwordEdt);
        loginBtn = findViewById(R.id.loginBtn);
        newUserTV = findViewById(R.id.newUserTV);

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.setTitle("Login in...");
                progressDialog.setMessage("Please Wait...");
                progressDialog.show();

                String emailStr = emailEdt.getText().toString().trim();
                String passwordStr = passwordEdt.getText().toString().trim();

                if (emailStr.equals("")) {
                    emailEdt.setError("Email Field Can't be Empty...");
                    progressDialog.dismiss();
                } else if (passwordStr.equals("")) {
                    passwordEdt.setError("Give Password...");
                    progressDialog.dismiss();
                } else if (passwordStr.length() < 6) {
                    passwordEdt.setError("Password should be more than 6 ...");
                    progressDialog.dismiss();
                } else {
                    firebaseAuth.signInWithEmailAndPassword(emailStr, passwordStr).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                String uid = firebaseAuth.getCurrentUser().getUid();
                                loadUserData(progressDialog, uid.toString() + "");
                            } else {
                                progressDialog.dismiss();

                                Toast.makeText(LoginActivity.this, "Error" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }

                        }
                    });
                }


            }
        });

        newUserTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), RegisterActivity.class));
                finish();
            }
        });
    }

    private void loadUserData(ProgressDialog progressDialog, String uid) {

        DatabaseReference mref = FirebaseDatabase.getInstance().getReference("Users");
        mref.child(uid)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            Users user = snapshot.getValue(Users.class);
                            PrefUtils.saveUser(user, getApplicationContext());

                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                            finish();
                            progressDialog.dismiss();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

    }
}