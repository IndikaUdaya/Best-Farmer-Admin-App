package com.indikaudaya.best_farmer_admin.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.GranularRoundedCorners;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.indikaudaya.best_farmer_admin.R;
import com.indikaudaya.best_farmer_admin.interceptor.RequestInterceptor;
import com.indikaudaya.best_farmer_admin.model.PopularFood;
import com.indikaudaya.best_farmer_admin.model.ProductAdapterModel;
import com.indikaudaya.best_farmer_admin.service.BestFarmerAdminApiService;
import com.indikaudaya.best_farmer_admin.ui.productdetail.ProductDetailDialog;
import com.indikaudaya.best_farmer_admin.util.SweetAlertDialogCustomize;

import java.util.ArrayList;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.AdaptorViewHolder> {

    ArrayList<ProductAdapterModel> models;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth auth = FirebaseAuth.getInstance();

    Context context;

    public ProductAdapter(ArrayList<ProductAdapterModel> models) {
        this.models = models;
    }

    @NonNull
    @Override
    public AdaptorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View inflate = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.product_recycler_layout, parent, false);
        return new AdaptorViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull AdaptorViewHolder holder, int position) {
        holder.name.setText(models.get(position).getName());
        holder.email.setText(models.get(position).getEmail());
        holder.mobile.setText(models.get(position).getMobile());
        holder.status.setText(models.get(position).isStatus() ? "Product is activated" : "Product is deactivated");

        FirebaseStorage storage = FirebaseStorage.getInstance();
        storage.getReference("product-image/" + models.get(position).getProductImages().get(0).getPath())
                .getDownloadUrl()
                .addOnSuccessListener(uri -> {
                    Glide.with(holder.itemView.getContext())
                            .load(uri)
                            .transform(new GranularRoundedCorners(35, 35, 0, 0))
                            .into(holder.productImage);
                });

        holder.setStatusButton.setText(models.get(position).isStatus() ? "Deactivate" : "Activate");

        checkSellerStatusFromFirebase(holder, position);
        holder.setStatusButton.setOnClickListener(v -> {
            updateProductStatus(holder, position, models.get(position).isStatus());
        });

        holder.productImage.setOnClickListener(v -> {
            loadProductDetails( models.get(position).getPopularFood());
        });

    }

    private void loadProductDetails(PopularFood popularFood) {
        ProductDetailDialog productDetailDialog = new ProductDetailDialog(context,popularFood);
        productDetailDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        productDetailDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        productDetailDialog.getWindow().setGravity(Gravity.CENTER);
        productDetailDialog.setCancelable(false);
        productDetailDialog.show();
    }

    private void checkSellerStatusFromFirebase(AdaptorViewHolder holder, int position) {
        CollectionReference users = db.collection("users");

        users.document(models.get(position).getEmail())
                .get()
                .addOnSuccessListener(command -> {
                    boolean status = Boolean.parseBoolean(String.valueOf(command.get("status")));
                    if (!status) {
                        holder.setStatusButton.setText("This seller is deactivated!.");
                        holder.setStatusButton.setEnabled(false);
                    }
                }).addOnFailureListener(command -> {
                    new SweetAlertDialogCustomize().errorAlert(context, "Get seller account details failed!.");
                });
    }

    private void updateProductStatus(AdaptorViewHolder holder, int position, boolean status) {
        String to = context.getSharedPreferences(context.getString(R.string.security_admin_api_store), Context.MODE_PRIVATE).getString("token", "");

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(context.getString(R.string.base_url))
                .addConverterFactory(GsonConverterFactory.create())
                .client(new OkHttpClient.Builder().addInterceptor(new RequestInterceptor(to)).build())
                .build();

        BestFarmerAdminApiService service = retrofit.create(BestFarmerAdminApiService.class);

        Call<Boolean> booleanCall = service.deactivateProductById(models.get(position).getPid(), !status);
        booleanCall.enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                if (status) {
                    holder.status.setText("Deactivated");
                    holder.setStatusButton.setText("Activate");
                    new SweetAlertDialogCustomize().successAlert(context, "Product Deactivated Successfully!.");
                } else {
                    holder.status.setText("Activated");
                    holder.setStatusButton.setText("Deactivate");
                    new SweetAlertDialogCustomize().successAlert(context, "Product Activated Successfully!.");
                }
                holder.setStatusButton.setEnabled(false);
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {

            }
        });

    }


    @Override
    public int getItemCount() {
        return models.size();
    }

    class AdaptorViewHolder extends RecyclerView.ViewHolder {

        TextView name, email, mobile, status;
        Button setStatusButton;
        ImageView productImage;

        public AdaptorViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.textView9);
            email = itemView.findViewById(R.id.textView10);
            mobile = itemView.findViewById(R.id.textView11);
            status = itemView.findViewById(R.id.textView12);
            setStatusButton = itemView.findViewById(R.id.button2);
            productImage = itemView.findViewById(R.id.imageView3);
        }
    }


}
