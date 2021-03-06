package com.koreatech.naeilro.ui.restraunt.adapater;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.koreatech.core.recyclerview.RecyclerViewClickListener;
import com.koreatech.naeilro.R;
import com.koreatech.naeilro.network.entity.facility.Facility;
import com.koreatech.naeilro.network.entity.restaurant.RestaurantInfo;
import com.koreatech.naeilro.ui.main.MainActivity;

import java.util.ArrayList;
import java.util.List;

public class RestaurantInfoRecyclerViewAdapter extends RecyclerView.Adapter<RestaurantInfoRecyclerViewAdapter.ViewHolder> {
    NavController navController;
    private List<RestaurantInfo> restaurantInfoList;
    private Context context;

    public RestaurantInfoRecyclerViewAdapter(Context context) {
        this.context = context;
        restaurantInfoList = new ArrayList<>();
        navController = Navigation.findNavController((MainActivity) context, R.id.nav_host_fragment);
    }
    public void addItem(List<RestaurantInfo> item){
        for(int i=0;i<item.size();i++){
            restaurantInfoList.add(item.get(i));
        }
        notifyDataSetChanged();
    }

    public void clearItem(){
        restaurantInfoList.clear();
    }
    @NonNull
    @Override
    public RestaurantInfoRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_restaurant_info, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RestaurantInfoRecyclerViewAdapter.ViewHolder holder, int position) {
        RestaurantInfo restaurantInfo = restaurantInfoList.get(position);
        holder.setIsRecyclable(false);
        if (restaurantInfo.getTitle() != null)
            holder.restaurantTitleTextView.setText(restaurantInfo.getTitle());
        else
            holder.restaurantTitleTextView.setText("식당명이 등록되지 않았습니다.");
        if (restaurantInfo.getAddress() != null)
            holder.restaurantAddressTextView.setText(restaurantInfo.getAddress());
        else
            holder.restaurantAddressTextView.setText("주소가 등록되지 않았습니다.");
        if (restaurantInfo.getTelephoneNumber() != null)
            holder.restaurantTelTextView.setText(restaurantInfo.getTelephoneNumber());
        else
            holder.restaurantTelTextView.setText("");

        if (restaurantInfo.getFirstImage() != null) {
            Glide.with(holder.restaurantImageView)
                    .load(restaurantInfo.getFirstImage())
                    .into(holder.restaurantImageView);
        } else
            Glide.with(holder.restaurantImageView)
                    .load(R.drawable.ic_no_image)
                    .thumbnail(0.05f)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .diskCacheStrategy(DiskCacheStrategy.DATA)
                    .error(R.drawable.ic_no_image)
                    .into(holder.restaurantImageView);
        holder.view.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putInt("contentId", restaurantInfo.getContentID());
                Log.e("adapter", Integer.toString(restaurantInfo.getContentID()));
                bundle.putString("title", restaurantInfo.getTitle());
                navController.navigate(R.id.action_navigation_restraunt_to_navigation_restraunt_detail, bundle);
            }
        });
    }

    @Override
    public int getItemCount() {
        return restaurantInfoList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View view;
        public TextView restaurantTitleTextView;
        public TextView restaurantAddressTextView;
        public TextView restaurantTelTextView;
        public ImageView restaurantImageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            view = itemView;
            restaurantTitleTextView = itemView.findViewById(R.id.restaurant_title);
            restaurantAddressTextView = itemView.findViewById(R.id.restaurant_address);
            restaurantTelTextView = itemView.findViewById(R.id.restaurant_tel);
            restaurantImageView = itemView.findViewById(R.id.restaurant_image_view);
        }

    }
}
