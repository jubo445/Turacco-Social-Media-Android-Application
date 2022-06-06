package com.example.socialmedia.Activity;


import static com.example.socialmedia.Activity.Notification.PrefUtils.USER_DETAIL;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.socialmedia.Activity.Notification.PrefUtils;
import com.example.socialmedia.Activity.chat.ChatList;
import com.example.socialmedia.Adapter.viewPager2_adapter;
import com.example.socialmedia.Model.Users;
import com.example.socialmedia.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    BottomNavigationView bottomNav;
    ViewPager2 fragmentManager;
    CircleImageView topProfile;
    TextView topUserName;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    String currentUserId;
    DatabaseReference databaseReference;
    viewPager2_adapter viewPager2Adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        FirebaseMessaging.getInstance().subscribeToTopic("NOTIFICATION");
        viewPager2Adapter = new viewPager2_adapter(this);

        requestForPermission();


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        if (firebaseUser != null) {
            currentUserId = firebaseUser.getUid();
        }

        fragmentManager = findViewById(R.id.fragContainer);
        topProfile = findViewById(R.id.topProfile);
        topUserName = findViewById(R.id.topUserName);


        databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        databaseReference.child(currentUserId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Users users = snapshot.getValue(Users.class);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        topProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), CurrentUserActivity.class));
            }
        });

        drawerLayout = findViewById(R.id.drawerLayout);
        navigationView = findViewById(R.id.navView);
        bottomNav = findViewById(R.id.bottomNavigationView);
        bottomNav.setBackground(null);
        //   bottomNav.getMenu().getItem(2).setEnabled(false);


        findViewById(R.id.menu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawState();
            }
        });


        fragmentManager.setAdapter(viewPager2Adapter);
        fragmentManager.setUserInputEnabled(false);


        fragmentManager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        bottomNav.getMenu().findItem(R.id.feeds).setChecked(true);
                        break;
                    case 1:
                        bottomNav.getMenu().findItem(R.id.followers).setChecked(true);
                        break;
                    case 2:

                        bottomNav.getMenu().findItem(R.id.chip3).setChecked(true);
                        break;

                    case 3:
                        bottomNav.getMenu().findItem(R.id.notification).setChecked(true);
                        break;
                    case 4:
                        bottomNav.getMenu().findItem(R.id.profile).setChecked(true);
                        break;

                }

                super.onPageSelected(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                super.onPageScrollStateChanged(state);
            }
        });


        if (getIntent().getBooleanExtra("isNotification", false)) {

            fragmentManager.setCurrentItem(3, false);

        }

        findViewById(R.id.fab).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragmentManager.setCurrentItem(2, false);
            }
        });


        bottomNav.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {

                case R.id.feeds:
                    fragmentManager.setCurrentItem(0, false);
                    break;
                case R.id.followers:
                    fragmentManager.setCurrentItem(1, false);
                    break;
                case R.id.profile:

                    fragmentManager.setCurrentItem(4, false);
                    break;

                case R.id.notification:

                    fragmentManager.setCurrentItem(3, false);
                    break;

            }


            return false;
        });
        navigationView.setNavigationItemSelectedListener(item -> {

            drawState();

            if (item.getItemId() == R.id.log_out) {
                PrefUtils.removeFromPrefs(getApplicationContext(), USER_DETAIL);
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();

            } else if (item.getItemId() == R.id.my_profile) {
                fragmentManager.setCurrentItem(4);
            } else if (item.getItemId() == R.id.my_msg) {
                Intent intent = new Intent(getApplicationContext(), ChatList.class);
                startActivity(intent);

            } else if (item.getItemId() == R.id.my_notificaiton) {
                fragmentManager.setCurrentItem(3);
            }


            return false;
        });


    }

    private void requestForPermission() {


    }

    @Override
    protected void onResume() {
        super.onResume();

        Users user = PrefUtils.loadUser(getApplicationContext());

        if (user != null) {

            setupSideMenu(user);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.top_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.postMenu) {
            startActivity(new Intent(getApplicationContext(), ChatList.class));

        }

        return true;
    }

    private void drawState() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(Gravity.LEFT); //CLOSE Nav Drawer!
        } else {
            drawerLayout.openDrawer(Gravity.LEFT); //OPEN Nav Drawer!
        }
    }

    private void setupSideMenu(Users user) {
        TextView nname = navigationView.getHeaderView(0).findViewById(R.id.name);
        ImageView iamge = navigationView.getHeaderView(0).findViewById(R.id.profile_image);

        Glide.with(getApplicationContext())
                .load(user.getUser_image() + "")
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .into(iamge);

        nname.setText(user.getName() + "");
    }

}