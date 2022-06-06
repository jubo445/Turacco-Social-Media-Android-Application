package com.example.socialmedia.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;

import com.example.socialmedia.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;

public class SplashActivity extends AppCompatActivity {

    CountDownTimer countDownTimer;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        firebaseAuth=FirebaseAuth.getInstance();
        firebaseUser=firebaseAuth.getCurrentUser();

        countDownTimer=new CountDownTimer(3000,100) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                if(firebaseUser!=null){
                   RegisterForToken();
                }else{
                    startActivity(new Intent(getApplicationContext(),LoginActivity.class));
                    finish();
                }


            }
        }.start();
    }

    private void RegisterForToken() {
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            Log.w("TAG", "Fetching FCM registration token failed", task.getException());
                            //return;
                        }else {
                            // Get new FCM registration token
                            String token = task.getResult();
                            UploadToken(token);
                        }


                    }
                });
    }

    private void UploadToken(String token) {
        DatabaseReference mref = FirebaseDatabase.getInstance().getReference("Users");

        mref.child(firebaseUser.getUid()).child("n_token").setValue(token).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

              Intent intent =   new Intent(getApplicationContext(), MainActivity.class) ;
              Bundle  p = getIntent().getExtras() ;
                StringBuilder k = new StringBuilder();
                if(p != null){
                    Log.d("TAG", "onComplete: " + p.toString());
                    for (String key : p.keySet()) {
                        Log.e("TAG", key + " : " + (p.get(key) != null ? p.get(key) : "NULL"));
                        k.insert(0, key);

                    }
                }

                if (k.toString().contains("message_id")) {
                    Log.d("TAG", "onComplete: zzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzz");
                    intent.putExtra("isNotification" , true) ;
                }
                startActivity(intent);
                finish();
            }
        });
    }
}