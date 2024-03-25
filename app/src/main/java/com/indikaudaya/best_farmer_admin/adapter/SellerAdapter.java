package com.indikaudaya.best_farmer_admin.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.indikaudaya.best_farmer_admin.R;
import com.indikaudaya.best_farmer_admin.interceptor.RequestInterceptor;
import com.indikaudaya.best_farmer_admin.model.ApiUserDetails;
import com.indikaudaya.best_farmer_admin.model.BuyerAdapterModel;
import com.indikaudaya.best_farmer_admin.model.SellerAdapterModel;
import com.indikaudaya.best_farmer_admin.service.BestFarmerAdminApiService;
import com.indikaudaya.best_farmer_admin.util.SweetAlertDialogCustomize;

import java.util.ArrayList;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SellerAdapter extends RecyclerView.Adapter<SellerAdapter.AdaptorViewHolder> {

    ArrayList<SellerAdapterModel> models;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth auth = FirebaseAuth.getInstance();

    Context context;

    public SellerAdapter(ArrayList<SellerAdapterModel> models) {
        this.models = models;
    }

    @NonNull
    @Override
    public AdaptorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View inflate = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.user_details_recycelr_layout, parent, false);
        return new AdaptorViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull AdaptorViewHolder holder, int position) {
        holder.name.setText(models.get(position).getName());
        holder.email.setText(models.get(position).getEmail());
        holder.mobile.setText(models.get(position).getMobile());
        holder.status.setText(models.get(position).isStatus() ? "Activated" : "Deactivated");

        holder.setStatusButton.setText(models.get(position).isStatus() ? "Deactivate" : "Activate");

        holder.setStatusButton.setOnClickListener(v -> {
            updateFireStore(holder, position);
        });

    }

    private void updateFireStore(AdaptorViewHolder holder, int position) {
        CollectionReference users = db.collection("users");
        boolean stat = false;

        if (!models.get(position).isStatus()) {
            stat = true;
        }

        users.document(models.get(position).getEmail())
                .update("status", stat)
                .addOnSuccessListener(command -> {
                    if (models.get(position).isStatus()) {
                        holder.status.setText("Deactivated");
                        holder.setStatusButton.setText("Activate");
                        new SweetAlertDialogCustomize().successAlert(context, "Seller Deactivated Successfully!.");
                    } else {
                        holder.status.setText("Activated");
                        holder.setStatusButton.setText("Deactivate");
                        new SweetAlertDialogCustomize().successAlert(context, "Seller Activated Successfully!.");
                    }
                    holder.setStatusButton.setEnabled(false);
                    updateProductStatus(models.get(position).isStatus(),models.get(position).getEmail());

                }).addOnFailureListener(command -> {
                    new SweetAlertDialogCustomize().errorAlert(context, "Seller Deactivated failed!.");
                });

    }

    private void updateProductStatus(boolean status,String email) {
        String to = context.getSharedPreferences(context.getString(R.string.security_admin_api_store), Context.MODE_PRIVATE).getString("token", "");

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(context.getString(R.string.base_url))
                .addConverterFactory(GsonConverterFactory.create())
                .client(new OkHttpClient.Builder().addInterceptor(new RequestInterceptor(to)).build())
                .build();

        BestFarmerAdminApiService service = retrofit.create(BestFarmerAdminApiService.class);

        Call<Boolean> booleanCall = service.deactivateProduct(email,!status);
        booleanCall.enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {

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

        public AdaptorViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.textView9);
            email = itemView.findViewById(R.id.textView10);
            mobile = itemView.findViewById(R.id.textView11);
            status = itemView.findViewById(R.id.textView12);
            setStatusButton = itemView.findViewById(R.id.button2);
        }
    }


}
