package com.example.socialmedia.Fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.socialmedia.Activity.CommentPage;
import com.example.socialmedia.Activity.Notification.PrefUtils;
import com.example.socialmedia.Activity.PostActivity;
import com.example.socialmedia.Activity.StoryPage;
import com.example.socialmedia.Activity.chat.ChatList;
import com.example.socialmedia.Adapter.PostAdapter;
import com.example.socialmedia.Adapter.StoryHorizontralAdapter;
import com.example.socialmedia.ConvertTime;
import com.example.socialmedia.HelperClass;
import com.example.socialmedia.Model.Post;
import com.example.socialmedia.Model.Story;
import com.example.socialmedia.Model.StoryResp;
import com.example.socialmedia.Model.Users;
import com.example.socialmedia.R;
import com.example.socialmedia.databinding.FragmentCreatePostBinding;
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

import kotlin.Unit;


public class FeedsFragment extends Fragment implements PostAdapter.ItemClickListener, StoryHorizontralAdapter.ItemClickListener {


    public FeedsFragment() {
        // Required empty public constructor
    }


    RecyclerView postRecyclerView, storyRecyclerView;
    List<Post> postList;
    List<Story> storyList = new ArrayList<>();
    PostAdapter adapter;
    DatabaseReference databaseReference;
    StorageReference storageReference;
    FirebaseUser firebaseUser;
    String currentUserId;
    Uri uri = null;
    FragmentCreatePostBinding binding;
    ProgressDialog dialog;

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
                        Toast.makeText(getContext(), "Image No Found", Toast.LENGTH_SHORT).show();
                    }

                }
            });

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_info, container, false);

        databaseReference = FirebaseDatabase.getInstance().getReference();

        postRecyclerView = view.findViewById(R.id.postRecyclerView);
        storyRecyclerView = view.findViewById(R.id.storyRecyclerView);
        postRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        storyRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false));
        postList = new ArrayList<>();

        dialog = HelperClass.createProgressDialog(getContext(), "Uploading Image...");
        readPostFromDatabase();


        return view;
    }

    private void readPostFromDatabase() {
        databaseReference.child("Post").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                postList.clear();

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Post post = dataSnapshot.getValue(Post.class);
                    postList.add(post);
                }
                adapter = new PostAdapter(getActivity(), postList, FeedsFragment.this);
                postRecyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        databaseReference.child("Stories").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {


                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Story post = dataSnapshot.getValue(Story.class);

                    if(ConvertTime.getTimeDifferance(post.getDate()) == 0){
                        storyList.add(post);
                    }


                }
                List<Story> newLIst = new ArrayList<>();

                newLIst.add(new Story());
                newLIst.addAll(storyList);
                StoryHorizontralAdapter adapter = new StoryHorizontralAdapter(newLIst, getContext(), FeedsFragment.this);

                storyRecyclerView.setAdapter(adapter);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }


    @Override
    public void onItemClick(Post model, int type) {

        if (type == 2) {
            /*
             share btn
             */
            shareThisPost(model);

        } else if (type == 1) {
            Intent p = new Intent(getContext(), CommentPage.class);
            p.putExtra("model", model);
            startActivity(p);
        }

    }

    private void shareThisPost(Post item) {

        //
        Intent intent = new Intent(getContext(), ChatList.class);
        intent.putExtra("POST" , item);
        startActivity(intent);


    }

    private void uploadImage(Uri imageUri) {
        dialog.show();
        currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid().toString();
        String fileId_local = currentUserId + System.currentTimeMillis();
        StorageReference myStorage = FirebaseStorage.getInstance().getReference("story").child(currentUserId).child(fileId_local);

        myStorage.putFile(imageUri).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                myStorage.getDownloadUrl().addOnSuccessListener(uri -> {
                    String fileUrl = String.valueOf(uri);

                    dataUpdate(fileUrl);
                });
            } else {
                dialog.dismiss();
            }
        });


    }

    private void dataUpdate(String fileUrl) {

        Users user = PrefUtils.loadUser(getContext());
        String name = "", user_image_link = "";
        if (user != null) {
            name = user.getName();
            user_image_link = user.getUser_image();

        }

        String postId_fb = databaseReference.push().getKey();
        DatabaseReference mref = FirebaseDatabase.getInstance().getReference("Stories");

        //another local way to get postId


        //Using HashMap
        HashMap<String, Object> postMap = new HashMap<>();

        postMap.put("userId", currentUserId);
        postMap.put("userName", name);
        postMap.put("userImage", user_image_link);
        postMap.put("postId", postId_fb);
        postMap.put("imageLink", fileUrl);
        postMap.put("date", HelperClass.getTodayDate());


        mref.child(postId_fb).setValue(postMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {

                    dialog.dismiss();
                }
            }
        });
    }

    @Override
    public void onItemClick(Story model, int position) {

        if (position != 0) {
            Intent intent = new Intent(getContext(), StoryPage.class);
            intent.putExtra("model", new StoryResp(storyList));
            intent.putExtra("pos", position - 1);
            startActivity(intent);
        } else {
            ImagePicker.with(this)
                    .cropSquare()
                    .galleryOnly()
                    .createIntent(intent -> {
                        someActivityResultLauncher.launch(intent);
                        return Unit.INSTANCE;
                    });
        }
    }
}

