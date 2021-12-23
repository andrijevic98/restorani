package com.restorani;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;

import okhttp3.OkHttpClient;
import okhttp3.Protocol;


public class RestaurantsAdapter extends RecyclerView.Adapter<RestaurantsAdapter.MyViewHolder> {

    private Context context;
    private ArrayList<RestaurantModel> restaurants = new ArrayList<>();


    public RestaurantsAdapter(Context context, ArrayList<RestaurantModel> restaurants) {
        this.context = context;
        this.restaurants.addAll(restaurants);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_restaurant, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder viewHolder, final int position) {

        RestaurantModel restaurant = restaurants.get(position);

        viewHolder.name.setText(restaurant.getName());
        viewHolder.address.setText(restaurant.getAddress());
        viewHolder.phone.setText(restaurant.getPhone());
        if (restaurant.getComments() != null)
            viewHolder.comments.setText(String.valueOf(restaurant.getComments().size()));
        else
            viewHolder.comments.setText("0");


        Picasso.get().load(restaurant.getImage()).into(viewHolder.image);

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, CommentsActivity.class);
                intent.putExtra("RESTAURANT", restaurant);
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {

        return restaurants.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView name;
        TextView address;
        TextView phone;
        TextView comments;
        ImageView image;


        private MyViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            address = itemView.findViewById(R.id.address);
            phone = itemView.findViewById(R.id.phone);
            comments = itemView.findViewById(R.id.comments);
            image = itemView.findViewById(R.id.image);

        }

    }


}