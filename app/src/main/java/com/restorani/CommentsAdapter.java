package com.restorani;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;


public class CommentsAdapter extends RecyclerView.Adapter<CommentsAdapter.MyViewHolder> {

    private Context context;
    private ArrayList<CommentModel> comments = new ArrayList<>();
    private DatabaseReference database;
    private RestaurantModel restaurantModel;


    public CommentsAdapter(Context context, RestaurantModel restaurantModel) {
        this.context = context;
        this.restaurantModel = restaurantModel;
        if (restaurantModel.getComments() != null) {
            Iterator<String> hashMapKey = restaurantModel.getComments().keySet().iterator();
            while (hashMapKey.hasNext()) {
                String key = hashMapKey.next();
                restaurantModel.getComments().get(key).setId(key);
                this.comments.add(restaurantModel.getComments().get(key));
            }
        }

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_comment, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder viewHolder, final int position) {

        CommentModel comment = comments.get(position);
        int pos = position;

        viewHolder.user.setText(comment.getUser());
        viewHolder.comment.setText(comment.getText());
        viewHolder.date.setText(comment.getDate());
        viewHolder.rating.setText(String.valueOf(comment.getRating()));

        if (FirebaseAuth.getInstance().getCurrentUser().getUid().equals(comment.getUserId())) {
            viewHolder.delete.setVisibility(View.VISIBLE);
            viewHolder.delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    delete(restaurantModel.getId(), comment.getId(), pos);
                }
            });
        } else {
            viewHolder.delete.setVisibility(View.GONE);
            viewHolder.delete.setOnClickListener(null);
        }

    }

    @Override
    public int getItemCount() {

        return comments.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView user;
        TextView comment;
        TextView date;
        TextView rating;
        ImageButton delete;


        private MyViewHolder(View itemView) {
            super(itemView);
            user = itemView.findViewById(R.id.user);
            comment = itemView.findViewById(R.id.comment);
            date = itemView.findViewById(R.id.date);
            rating = itemView.findViewById(R.id.rating);
            delete = itemView.findViewById(R.id.delete);

        }

    }

    private void delete(String restaurantId, String commentId, int position) {
        database = FirebaseDatabase.getInstance().getReference();
        database.child("public").child("restaurants").child(restaurantId).child("comments").child(commentId).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                comments.remove(position);
                notifyItemRemoved(position);
            }
        });
    }


}