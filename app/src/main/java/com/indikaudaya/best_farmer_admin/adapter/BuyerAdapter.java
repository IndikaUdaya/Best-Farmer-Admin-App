package com.indikaudaya.best_farmer_admin.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.indikaudaya.best_farmer_admin.R;
import com.indikaudaya.best_farmer_admin.model.BuyerAdapterModel;
import com.indikaudaya.best_farmer_admin.util.SweetAlertDialogCustomize;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BuyerAdapter extends RecyclerView.Adapter<BuyerAdapter.AdaptorViewHolder> {

    ArrayList<BuyerAdapterModel> models;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth auth = FirebaseAuth.getInstance();

    Context context;

    public BuyerAdapter(ArrayList<BuyerAdapterModel> models) {
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
                        new SweetAlertDialogCustomize().successAlert(context, "Buyer Deactivated Successfully!.");
                    } else {
                        holder.status.setText("Activated");
                        holder.setStatusButton.setText("Deactivate");
                        new SweetAlertDialogCustomize().successAlert(context, "Buyer Activated Successfully!.");

                    }
                    holder.setStatusButton.setEnabled(false);

                }).addOnFailureListener(command -> {
                    new SweetAlertDialogCustomize().errorAlert(context, "Buyer Deactivated failed!.");
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
