package com.example.socialmedia.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SearchView;

import com.example.socialmedia.Adapter.UserAdapter;
import com.example.socialmedia.Activity.LoginActivity;
import com.example.socialmedia.Model.CommonModel;
import com.example.socialmedia.Model.Post;
import com.example.socialmedia.Model.Users;
import com.example.socialmedia.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;


public class FriendsFragment extends Fragment {


    public FriendsFragment() {
        // Required empty public constructor
    }


    RecyclerView recyclerView;

    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    String currentUserId;
    List<String> followersList = new ArrayList<>();
    List<Users> usersList;
    UserAdapter userAdapter;
    DatabaseReference databaseReference;
    SearchView searchView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        usersList = new ArrayList<>();
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        searchView = view.findViewById(R.id.searchView);
        recyclerView = view.findViewById(R.id.userRecycler);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(userAdapter);


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                List<Users> fillterList = new ArrayList<>();
                for (Users user : usersList) {
                    Log.d("TAG", "onQueryTextSubmit: " + user.getName());

                    if (user.getName().toLowerCase(Locale.ROOT).contains(s)) {
                        fillterList.add(user);

                    }
                }
                userAdapter = new UserAdapter(getContext(), fillterList);

                recyclerView.setAdapter(userAdapter);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });

        currentUserId = firebaseUser.getUid();


        databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(currentUserId);


        userAdapter = new UserAdapter(getContext(), usersList);

        recyclerView.setAdapter(userAdapter);
        //data getting from firebase.

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                Users user = snapshot.getValue(Users.class);

                Map<String, Boolean> followers = new HashMap<>();
                followers = user.getFollowers();

                followersList = new ArrayList<String>(followers.keySet());

                Log.d("TAG", "onDataChange: " + followersList.toString());

                loadAllTheUsers(followersList);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return view;
    }

    private void loadAllTheUsers(List<String> followersList) {
        databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    usersList = userAdapter.getList();
                    usersList.clear();
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        Users user = dataSnapshot.getValue(Users.class);

                        for (String followe : followersList) {
                            if (user.getUser_id().equals(followe)) {
                                usersList.add(user);

                            }
                        }
                        userAdapter.notifyDataSetChanged();
                    }


//                    for(Users user : usersList){
//                        for(String followe : followersList){
//                            if(user.getUser_id().equals(followe)){
//
//                            }
//                        }
//                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}