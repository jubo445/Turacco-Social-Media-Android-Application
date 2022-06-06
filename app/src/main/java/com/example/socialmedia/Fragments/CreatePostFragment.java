package com.example.socialmedia.Fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.socialmedia.Activity.Notification.PrefUtils;
import com.example.socialmedia.HelperClass;
import com.example.socialmedia.Model.Users;
import com.example.socialmedia.databinding.FragmentCreatePostBinding;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;

import kotlin.Unit;

public class CreatePostFragment extends Fragment {

    public CreatePostFragment() {
        // Required empty public constructor
    }

    DatabaseReference databaseReference;
    StorageReference storageReference;
    ProgressDialog dialog;

    FirebaseUser firebaseUser;
    String currentUserId;
    Uri uri = null;
    FragmentCreatePostBinding binding;

    ActivityResultLauncher<Intent> someActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    // There are no request codes
                    Intent data = result.getData();

                    if (data != null) {

                        uri = data.getData();
                        updateImage(uri);
                    } else {
                        Toast.makeText(getContext(), "Image No Found", Toast.LENGTH_SHORT).show();
                    }

                }
            });

    private void updateImage(Uri uri) {
        binding.imagePreView.setImageURI(uri);
        binding.imagePreView.setVisibility(View.VISIBLE);
    }

    private void openImagePicker() {

        ImagePicker.with(this)
                .createIntent(intent -> {
                    someActivityResultLauncher.launch(intent);
                    return Unit.INSTANCE;
                });
    }


    private void uploadImage(Uri imageUri) {
        dialog.show();

        String fileId_local = currentUserId + System.currentTimeMillis();
        StorageReference myStorage = storageReference.child(currentUserId).child(fileId_local);

        myStorage.putFile(imageUri).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                myStorage.getDownloadUrl().addOnSuccessListener(uri -> {
                    String fileUrl = String.valueOf(uri);

                    dataUpdate(fileUrl);
                });
            }
        });


    }

    private void dataUpdate(String fileUrl) {

        String postId_fb = databaseReference.push().getKey();


        //another local way to get postId


        //Using HashMap
        HashMap<String, Object> postMap = new HashMap<>();
        postMap.put("postText", binding.content.getText().toString());
        postMap.put("userId", currentUserId);
        postMap.put("postId", postId_fb);
        postMap.put("imageLink", fileUrl);
        postMap.put("date", HelperClass.getTodayDate());


        databaseReference.child(postId_fb).setValue(postMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    dialog.dismiss();
                }
            }
        });
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentCreatePostBinding.inflate(inflater, container, false);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        databaseReference = FirebaseDatabase.getInstance().getReference("Post");
        storageReference = FirebaseStorage.getInstance().getReference("Uploads");
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        dialog = HelperClass.createProgressDialog(getContext(), "Uploading Image...");
        if (firebaseUser != null) {
            currentUserId = firebaseUser.getUid();
        }


        binding.addImage.setOnClickListener((View.OnClickListener) view1 -> openImagePicker());
        binding.imagePreView.setOnClickListener(view12 -> openImagePicker());
        binding.createPostAction.setOnClickListener(
                view13 -> {
                    String content = binding.content.getText().toString();
                    if (content.isEmpty()) {
                        HelperClass.showMsg(getContext(), "Status Can't be empty");
                    } else {
                        if (uri != null) {
                            uploadImage(uri);
                        } else {
                            dataUpdate("");
                        }
                    }
                }
        );

        Users user = PrefUtils.loadUser(getContext());

        if (user != null) {
            binding.userName.setText(user.getName() + "");
            Glide.with(getContext())
                    .load(user.getUser_image() + "")
                    .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                    .into(binding.profilePic);
        }

    }


}