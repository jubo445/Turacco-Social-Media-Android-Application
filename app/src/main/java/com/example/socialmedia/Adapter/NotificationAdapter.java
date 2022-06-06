package com.example.socialmedia.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.socialmedia.Model.NotificationModel;
import com.example.socialmedia.Model.Users;
import com.example.socialmedia.R;
import com.example.socialmedia.databinding.RowForNotificationBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.List;

/*** Created by Rahat Shovo on 5/15/2022 
 */
public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.viewholder> {

    private final Context context;
    private List<NotificationModel> items;
    private ItemClickListener itemClickListener;
    private  DatabaseReference mref = FirebaseDatabase.getInstance().getReference("Users");
    public NotificationAdapter(List<NotificationModel> items, Context context, ItemClickListener itemClickListener) {
        this.items = items;
        this.context = context;
        this.itemClickListener = itemClickListener;
    }

    @Override
    public @NotNull
    viewholder onCreateViewHolder(@NonNull ViewGroup parent,
                                  int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_for_notification, parent, false);
        return new viewholder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull viewholder holder, int position) {
        NotificationModel item = items.get(position);
        RowForNotificationBinding binding = RowForNotificationBinding.bind(holder.itemView);

        holder.itemView.setOnClickListener(v -> {
            itemClickListener.onItemClick(item);
        });
        binding.desc.setText( item.getMsg() + "");
        mref.child(item.getOther_user_id()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()) {
                    Users user = snapshot.getValue(Users.class);
                    binding.title.setText(user.getName().toString());
                    Glide.with(context)
                            .load(user.getUser_image().toString())
                            .error(R.drawable.placeholder_large_dark)
                            .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                            .into(binding.image);
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

    class viewholder extends RecyclerView.ViewHolder {

        viewholder(@NonNull View itemView) {
            super(itemView);
        }


    }

    public interface ItemClickListener {
        void onItemClick(NotificationModel model);
    }


}