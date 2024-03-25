package com.indikaudaya.best_farmer_admin.ui.seller;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.indikaudaya.best_farmer_admin.R;
import com.indikaudaya.best_farmer_admin.adapter.BuyerAdapter;
import com.indikaudaya.best_farmer_admin.adapter.SellerAdapter;
import com.indikaudaya.best_farmer_admin.model.BuyerAdapterModel;
import com.indikaudaya.best_farmer_admin.model.SellerAdapterModel;
import com.indikaudaya.best_farmer_admin.ui.buyer.BuyerFragment;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SellerFragment extends Fragment {

    private static final String TAG = SellerFragment.class.getName();
    View root;
    EditText searchText;
    Button searchButton;
    FirebaseFirestore db;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root= inflater.inflate(R.layout.fragment_seller, container, false);
        initMethod();
        getAllUserFromFirebase();
        pressSearchButton();
        return root;
    }

    private void pressSearchButton() {

        searchButton.setOnClickListener(v -> {

            CollectionReference users = db.collection("users");

            users.whereGreaterThanOrEqualTo("email", String.valueOf(searchText.getText()))
                    .whereLessThanOrEqualTo("email", String.valueOf(searchText.getText()) + "\uf8ff")
                    .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            List<DocumentSnapshot> documents = queryDocumentSnapshots.getDocuments();

                            ArrayList<SellerAdapterModel> models = new ArrayList<>();

                            for (DocumentSnapshot ds : documents) {
                                Map<String, Object> data = ds.getData();
                                if (data.get("shopName") != null) {
                                    models.add(new SellerAdapterModel(
                                            String.valueOf(data.get("firstName")).concat(" ").concat(String.valueOf(data.get("lastName"))),
                                            String.valueOf(data.get("email")),
                                            String.valueOf(data.get("mobileNumber")),
                                            Boolean.parseBoolean(String.valueOf(data.get("status")))
                                    ));
                                }
                            }
                            initCartRecycler(models);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d(TAG, "onFailure: ");
                        }
                    });

        });


    }

    private void getAllUserFromFirebase() {
        CollectionReference users = db.collection("users");

        users.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                List<DocumentSnapshot> documents = queryDocumentSnapshots.getDocuments();

                ArrayList<SellerAdapterModel> models = new ArrayList<>();

                for (DocumentSnapshot ds : documents) {
                    Map<String, Object> data = ds.getData();
                    if (data.get("shopName") != null) {
                        models.add(new SellerAdapterModel(
                                String.valueOf(data.get("firstName")).concat(" ").concat(String.valueOf(data.get("lastName"))),
                                String.valueOf(data.get("email")),
                                String.valueOf(data.get("mobileNumber")),
                                Boolean.parseBoolean(String.valueOf(data.get("status")))
                        ));
                    }
                }
                initCartRecycler(models);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "onFailure: ");
            }
        });

    }

    private void initMethod() {
        searchButton = root.findViewById(R.id.button);
        searchText = root.findViewById(R.id.searchText);
        db = FirebaseFirestore.getInstance();
    }

    private void initCartRecycler(ArrayList<SellerAdapterModel> models) {
        RecyclerView recyclerView = root.findViewById(R.id.recyclerView);
        SellerAdapter cartAdapter = new SellerAdapter(models);
        recyclerView.setLayoutManager(new LinearLayoutManager(root.getContext(), LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(cartAdapter);
    }

}