package com.indikaudaya.best_farmer_admin.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.indikaudaya.best_farmer_admin.R;
import com.indikaudaya.best_farmer_admin.model.CarouselModel;

import java.util.ArrayList;

public class CarouselAdapter extends RecyclerView.Adapter<CarouselAdapter.ItemViewHolder> {

    Context context;
    private ArrayList<CarouselModel> imageUri;

    public CarouselAdapter(Context context, ArrayList<CarouselModel> imageUri) {
        this.context = context;
        this.imageUri = imageUri;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(context).inflate(R.layout.carousel_layout, parent, false);
        return new ItemViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        CarouselModel carouselModel = imageUri.get(position);
        Glide.with(context)
                .load(carouselModel.getImageUri())
                .into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return imageUri.size();
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.carousel_image_view);
        }
    }
}
