package com.indikaudaya.best_farmer_admin.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.GranularRoundedCorners;
import com.indikaudaya.best_farmer_admin.R;
import com.indikaudaya.best_farmer_admin.model.SellerReviewModel;

import java.util.ArrayList;

public class SellerReviewAdapter extends RecyclerView.Adapter<SellerReviewAdapter.ViewHolder> {

    Context context;
    ArrayList<SellerReviewModel> sellerReviews;

    public SellerReviewAdapter(ArrayList<SellerReviewModel> sellerReviews) {
        this.sellerReviews = sellerReviews;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.seller_review_layout, parent, false);
        return new ViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.buyerName.setText(sellerReviews.get(position).getFirstName());
        holder.score.setText(context.getString(R.string.score_2_5).concat(" ").concat(String.valueOf(sellerReviews.get(position).getScore())));
        holder.comment.setText(sellerReviews.get(position).getReviewComment());
        holder.reviewDate.setText(sellerReviews.get(position).getReviewDate());

        Glide.with(holder.itemView.getContext())
                .load(sellerReviews.get(position).getProfileImageUrl())
                .placeholder(R.drawable.buyer_icon)
                .transform(new GranularRoundedCorners(35, 35, 0, 0))
                .into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return sellerReviews.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView buyerName, score, reviewDate, comment;
        ImageView imageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            buyerName = itemView.findViewById(R.id.textView53);
            score = itemView.findViewById(R.id.textView54);
            reviewDate = itemView.findViewById(R.id.textView56);
            comment = itemView.findViewById(R.id.textView55);
            imageView = itemView.findViewById(R.id.imageView25);
        }
    }
}
