package com.example.socialmedia.Activity.chat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.socialmedia.Model.Post;
import com.example.socialmedia.Model.Users;
import com.example.socialmedia.Model.chatHistoryModel;
import com.example.socialmedia.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatList extends AppCompatActivity {
    View view;
    RecyclerView recyclerView;
    LinearLayoutManager llm;
    DatabaseReference userRef, fref;
    FirebaseRecyclerAdapter<chatHistoryModel, chatListingViewholdeers> firebaseRecyclerAdapter;
    FirebaseRecyclerOptions<chatHistoryModel> options;
    Context context;
    String uid;
    Post postItem  = null ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_list);

        uid = FirebaseAuth.getInstance().getUid();
        context = ChatList.this;
        recyclerView = findViewById(R.id.list);
        llm = new LinearLayoutManager(context);
        llm.setReverseLayout(true);
        llm.setStackFromEnd(true);
        recyclerView.setLayoutManager(llm);

            try {
                postItem = (Post) getIntent().getSerializableExtra("POST") ;
            }catch (Exception e){

            }

        findViewById(R.id.backBtn).setOnClickListener(view -> finish());

    }

    @Override
    protected void onResume() {
        loadData();
        super.onResume();
    }

    private void loadData() {

        DatabaseReference histroyref = FirebaseDatabase.getInstance().getReference("ChatHistory");

        Query query = histroyref.orderByChild("timestamp");

        fref = FirebaseDatabase.getInstance().getReference("Users");
        fref.keepSynced(true);


        histroyref.keepSynced(true);


        options = new FirebaseRecyclerOptions.Builder<chatHistoryModel>()
                .setQuery(query, chatHistoryModel.class)
                .build();


        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<chatHistoryModel, chatListingViewholdeers>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final chatListingViewholdeers viewholder, final int i, @NonNull final chatHistoryModel chatHistoryModel) {

                String lastMsg = chatHistoryModel.getLastMsg();

                if (lastMsg.isEmpty()) {
                    lastMsg = "Start Chat";
                }


                if (chatHistoryModel.getUser1().equals(uid)) {
                    //  user1 is me // user2 is my friend
                    // download the friend data
                    fref.child(chatHistoryModel.getUser2()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            Users model = dataSnapshot.getValue(Users.class);
                            viewholder.setNameTv(model.getName());
                            viewholder.setPp(model.getUser_image().toString(), context);


                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });


                    viewholder.setLastMsgTv(lastMsg);


                } else if (chatHistoryModel.getUser2().equals(uid)) {

                    //  user2  is me // user1 is my friend
                    // download the friend data
                    fref.child(chatHistoryModel.getUser1()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            Users model = dataSnapshot.getValue(Users.class);
                            viewholder.setNameTv(model.getName());
                            viewholder.setPp(model.getUser_image().toString(), context);


                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                    viewholder.setLastMsgTv(lastMsg);

                } else {
                    //this row not mine
                    viewholder.container.setVisibility(View.GONE);
                    ViewGroup.LayoutParams params = viewholder.container.getLayoutParams();
                    params.height = 0;
                    viewholder.container.setLayoutParams(params);

                }



                viewholder.setOnClickListener((view, position) -> {

                    String chatID = getItem(position).getMsg();


                    if (getItem(position).getUser1().equals(uid)) {

                        String friendUserID = getItem(position).getUser2();
                        Intent o = new Intent(context, ChatActivity.class);
                        o.putExtra("POST", postItem);
                        o.putExtra("chatID", chatID);
                        o.putExtra("otherUserID", friendUserID);
                        startActivity(o);


                    } else {
                        String friendUserID = getItem(position).getUser1();
                        Intent o = new Intent(context, ChatActivity.class);
                        o.putExtra("POST", postItem);
                        o.putExtra("chatID", chatID);
                        o.putExtra("otherUserID", friendUserID);
                     //   Toast.makeText(context , friendUserID , Toast.LENGTH_SHORT).show();
                        startActivity(o);
                    }


                });


            }

            @NonNull
            @Override
            public chatListingViewholdeers onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.row_for_chat_display, parent, false);

                final chatListingViewholdeers viewholder = new chatListingViewholdeers(view);


                return viewholder;
            }


        };

        recyclerView.setLayoutManager(llm);
        firebaseRecyclerAdapter.startListening();
        recyclerView.setAdapter(firebaseRecyclerAdapter);

    }
}


class chatListingViewholdeers extends RecyclerView.ViewHolder {

    View mView;
    public TextView nameTv, lastMsg;
    public CircleImageView pp;
    public ConstraintLayout container;


    public chatListingViewholdeers(@NonNull View itemView) {
        super(itemView);
        mView = itemView;
        container = mView.findViewById(R.id.full_layout);


        itemView.setOnClickListener(view -> mClickListener.onItemClick(view, getAbsoluteAdapterPosition()));

        container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mClickListener.onItemClick(view, getAbsoluteAdapterPosition());
            }
        });

    }

    public void setNameTv(String name) {
        container = mView.findViewById(R.id.full_layout);
        nameTv = (TextView) mView.findViewById(R.id.display_name);
        nameTv.setText(name);
    }

    public void setLastMsgTv(String lastMsgTv) {
        container = mView.findViewById(R.id.full_layout);
        lastMsg = (TextView) mView.findViewById(R.id.lastMsg);
        lastMsg.setText(lastMsgTv + "");
    }

    public void setPp(String link, Context context) {
        container = mView.findViewById(R.id.full_layout);
        pp = mView.findViewById(R.id.profile_image);

        Glide.with(context).load(link)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .error(R.drawable.placeholder)
                .into(pp);
    }





    private static chatListingViewholdeers.ClickListener mClickListener;


    public interface ClickListener {
        void onItemClick(View view, int position);

    }


    public void setOnClickListener(chatListingViewholdeers.ClickListener clickListener) {

        mClickListener = clickListener;

    }
}