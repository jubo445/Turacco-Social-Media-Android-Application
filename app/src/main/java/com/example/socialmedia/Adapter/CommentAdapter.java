package com.example.socialmedia.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.socialmedia.Model.CommonModel;
import com.example.socialmedia.Model.Users;
import com.example.socialmedia.R;
import com.example.socialmedia.databinding.RowCommentBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.List;

/*** Created by Rahat Shovo on 5/14/2022 
 */
public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentViewholder> {

    private final Context context;
    private List<CommonModel> items;
    private ItemClickListener itemClickListener;
    private DatabaseReference mref = FirebaseDatabase.getInstance().getReference("Users");
    public CommentAdapter(List<CommonModel> items, Context context, ItemClickListener itemClickListener) {
        this.items = items;
        this.context = context;
        this.itemClickListener = itemClickListener;
    }

    @Override
    public @NotNull
    CommentViewholder onCreateViewHolder(@NonNull ViewGroup parent,
                                         int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_comment, parent, false);

        return new CommentViewholder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentViewholder holder, int position) {
        CommonModel item = items.get(position);
        RowCommentBinding binding = RowCommentBinding.bind(holder.itemView);

        binding.CommentView.setText(item.getText().toString());


        holder.itemView.setOnClickListener(v -> {
            itemClickListener.onItemClick(item);
        });

        mref.child(item.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()) {
                    Users user = snapshot.getValue(Users.class);
                    binding.userName.setText(user.getName().toString());
                    Glide.with(context)
                            .load(user.getUser_image().toString())
                            .error(R.drawable.placeholder_large_dark)
                            .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                            .into(binding.userProfile);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }


    @Override
    public int getItemCount() {
        if (items == null) {
            return 0;
        }
        return items.size();
    }

    class CommentViewholder extends RecyclerView.ViewHolder {

        CommentViewholder(@NonNull View itemView) {
            super(itemView);
        }


    }

    public interface ItemClickListener {
        void onItemClick(CommonModel model);
    }


}