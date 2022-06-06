package com.example.socialmedia.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.socialmedia.Model.Story;
import com.example.socialmedia.R;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/*** Created by Rahat Shovo on 5/19/2022 
 */
public class StoryHorizontralAdapter extends RecyclerView.Adapter {

    private final Context context;
    private List<Story> items;
    private ItemClickListener itemClickListener;
    private int ownType = 0;
    private int otherType = 1;

    public StoryHorizontralAdapter(List<Story> items, Context context, ItemClickListener itemClickListener) {
        this.items = items;
        this.context = context;
        this.itemClickListener = itemClickListener;
    }

    @Override
    public @NotNull
    RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent,
                                  int viewType) {

        if(viewType == ownType){
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.story_own, parent, false);
            return new ownViewholder(v);

        }else {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.story_item, parent, false);
            return new viewholder(v);

        }



    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Story item = items.get(position);

        holder.itemView.setOnClickListener(v -> {
            itemClickListener.onItemClick(item , position);
        });

        if(position == 0){
            ((ownViewholder)holder)
                    .setViews(item);
        }else {
            ((viewholder)holder)
                    .setViews(item);
        }

    }


    @Override
    public int getItemCount() {
        if (items == null) {
            return 0;
        }
        return items.size();
    }

    class viewholder extends RecyclerView.ViewHolder {

        public  CircleImageView pp ;
        public  ImageView  imageView;
        public  TextView name ;

        viewholder(@NonNull View itemView) {
            super(itemView);
            pp = itemView.findViewById(R.id.pp) ;
            imageView = itemView.findViewById(R.id.image) ;
            name = itemView.findViewById(R.id.name) ;
        }

        void setViews(Story item){
            Glide.with(context)
                    .load(item.getImageLink())
                    .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                    .into(imageView);
            Glide.with(context)
                    .load(item.getUserImage()+"")
                    .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                    .into(pp);
            name.setText(item.getUserName());
        }

    }

    class ownViewholder extends RecyclerView.ViewHolder {

        public  CircleImageView pp ;
        public  ImageView  imageView;
        public  TextView name ;

        ownViewholder(@NonNull View itemView) {
            super(itemView);

            pp = itemView.findViewById(R.id.pp) ;


        }

        void setViews(Story item){

            Glide.with(context)
                    .load(item.getUserImage()+"")
                    .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                    .into(pp);

        }


    }


    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return ownType;
        } else {
            return otherType;
        }
    }

    public interface ItemClickListener {
        void onItemClick(Story model , int postision);
    }


}