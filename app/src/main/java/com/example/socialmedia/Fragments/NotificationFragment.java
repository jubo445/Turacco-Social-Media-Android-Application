package com.example.socialmedia.Fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.socialmedia.Adapter.NotificationAdapter;
import com.example.socialmedia.Model.NotificationModel;
import com.example.socialmedia.Model.Users;
import com.example.socialmedia.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class NotificationFragment extends Fragment implements NotificationAdapter.ItemClickListener {


    public NotificationFragment() {
        // Required empty public constructor
    }
    FirebaseAuth mauth ;
    String uid;

    View view;
    Context mcontext;
    RecyclerView recyclerView;
    NotificationAdapter mAdapter;
    List<NotificationModel> notificationList = new ArrayList<>();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_rating, container, false);
        mcontext = view.getContext();
        mauth = FirebaseAuth.getInstance();
        uid = mauth.getUid();

        return view;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = view.findViewById(R.id.list);
        recyclerView.setLayoutManager(new LinearLayoutManager(mcontext));

        loadNotifications();
    }

    private void loadNotifications() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Notifications");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {

                    notificationList.clear();
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        NotificationModel notification = dataSnapshot.getValue(NotificationModel.class);

                        if(notification.getUser_id().toString().equals(uid)){
                            notificationList.add(notification);
                        }
                    }
                    mAdapter = new NotificationAdapter(notificationList, mcontext, NotificationFragment.this);
                    recyclerView.setAdapter(mAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void onItemClick(NotificationModel model) {

    }
}