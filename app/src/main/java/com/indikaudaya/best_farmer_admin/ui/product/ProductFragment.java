package com.indikaudaya.best_farmer_admin.ui.product;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.indikaudaya.best_farmer_admin.R;
import com.indikaudaya.best_farmer_admin.adapter.BuyerAdapter;
import com.indikaudaya.best_farmer_admin.adapter.ProductAdapter;
import com.indikaudaya.best_farmer_admin.dto.ProductDTO;
import com.indikaudaya.best_farmer_admin.dto.ProductImageDTO;
import com.indikaudaya.best_farmer_admin.dto.SellerReviewDTO;
import com.indikaudaya.best_farmer_admin.dto.UserDTO;
import com.indikaudaya.best_farmer_admin.interceptor.RequestInterceptor;
import com.indikaudaya.best_farmer_admin.model.BuyerAdapterModel;
import com.indikaudaya.best_farmer_admin.model.PopularFood;
import com.indikaudaya.best_farmer_admin.model.ProductAdapterModel;
import com.indikaudaya.best_farmer_admin.model.SellerAdapterModel;
import com.indikaudaya.best_farmer_admin.service.BestFarmerAdminApiService;
import com.indikaudaya.best_farmer_admin.util.SweetAlertDialogCustomize;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ProductFragment extends Fragment {

    private static final String TAG = ProductFragment.class.getName();
    Spinner spinner;
    View root;

    EditText searchText;
    Button searchButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        root = inflater.inflate(R.layout.fragment_product, container, false);
        initMethod();
        setBooleanToSpinner();
        pressSearchButton();
        return root;
    }

    private void setBooleanToSpinner() {
        List<String> list = new ArrayList<>();
        list.add("Select Product status");
        list.add("Activated");
        list.add("Deactivated");

        ArrayAdapter adapter = new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_item, list);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }

    private void pressSearchButton() {
        loadAllProduct();
        searchButton.setOnClickListener(v -> {
            loadAllProduct();
        });
    }

    private void loadAllProduct() {
        String searchProductKeyword = String.valueOf(searchText.getText());
        boolean productStatus = String.valueOf(spinner.getSelectedItem()).equals("Activated");

        String token = getContext().getSharedPreferences(getContext().getString(R.string.security_admin_api_store), Context.MODE_PRIVATE).getString("token", "");

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(getContext().getString(R.string.base_url))
                .client(new OkHttpClient.Builder().addInterceptor(new RequestInterceptor(token)).build())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        BestFarmerAdminApiService apiService = retrofit.create(BestFarmerAdminApiService.class);

        Call<List<ProductDTO>> allProduct;

        if (!spinner.getSelectedItem().toString().equalsIgnoreCase("Select Product status") && !searchProductKeyword.isEmpty()) {
            allProduct = apiService.getAllProductsBySearching(searchProductKeyword, productStatus);
        } else if (!searchProductKeyword.isEmpty()) {
            allProduct = apiService.getAllProductsByProductName(searchProductKeyword);
        } else if (!spinner.getSelectedItem().toString().equalsIgnoreCase("Select Product status")) {
            allProduct = apiService.getAllProductsByStatus(productStatus);
        } else {
            allProduct = apiService.getAllProduct();
        }

        allProduct.enqueue(new Callback<List<ProductDTO>>() {
            @Override
            public void onResponse(Call<List<ProductDTO>> call, Response<List<ProductDTO>> response) {

                if (response.body() != null) {

                    List<ProductDTO> resProduct = response.body();
                    ArrayList<ProductAdapterModel> models = new ArrayList<>();

                    resProduct.forEach(product -> {
                        List<SellerReviewDTO> buyerReview = product.getSeller().getBuyerReview();

                        int sellerReviewCount = 0;
                        double sellerRanking = 0.0;

                        if (buyerReview != null && buyerReview.size() > 0) {
                            for (SellerReviewDTO sellerReview : buyerReview) {
                                sellerReviewCount++;
                                sellerRanking += sellerReview.getRating();
                            }
                        }

                        double SellerRankingScore = 0.0;
                        if (sellerReviewCount != 0) {
                            SellerRankingScore = new BigDecimal(sellerRanking / sellerReviewCount).setScale(2, RoundingMode.HALF_UP).doubleValue();
                        }

                        PopularFood popularFood = new PopularFood(
                                product.getId(),
                                product.getName(),
                                product.getDescription(),
                                BigDecimal.valueOf(product.getPrice()).setScale(2, RoundingMode.HALF_UP).doubleValue(),
                                sellerReviewCount,
                                SellerRankingScore,
                                product.getCartCount(),
                                product.getProductImages(),
                                new UserDTO(product.getSeller().getId()),
                                product.getQty());

                        ProductAdapterModel productAdapterModel = new ProductAdapterModel(
                                product.getName(), product.getSeller().getEmail(), product.getSeller().getMobile(), product.isStatus());
                        productAdapterModel.setPid(product.getId());
                        productAdapterModel.setProductImages(product.getProductImages());
                        productAdapterModel.setPopularFood(popularFood);
                        models.add(productAdapterModel);

                    });
                    initProductRecycler(models);
                }
            }

            @Override
            public void onFailure(Call<List<ProductDTO>> call, Throwable t) {
                Log.d(TAG, "onFailure: load all product-admin " + t.getMessage());
            }
        });

    }

//    private void loadProduct() {
//        String to = getContext().getSharedPreferences(getContext().getString(R.string.security_admin_api_store), Context.MODE_PRIVATE).getString("token", "");
//
//        Retrofit retrofit = new Retrofit.Builder()
//                .baseUrl(getContext().getString(R.string.base_url))
//                .addConverterFactory(GsonConverterFactory.create())
//                .client(new OkHttpClient.Builder().addInterceptor(new RequestInterceptor(to)).build())
//                .build();
//
//        BestFarmerAdminApiService service = retrofit.create(BestFarmerAdminApiService.class);
//
//        Call<List<ProductDTO>> allProduct = service.getAllProduct();
//        allProduct.enqueue(new Callback<List<ProductDTO>>() {
//            @Override
//            public void onResponse(Call<List<ProductDTO>> call, Response<List<ProductDTO>> response) {
//
//                if (response.body() != null) {
//
//                    List<ProductDTO> resProduct = response.body();
//                    ArrayList<ProductAdapterModel> models = new ArrayList<>();
//
//                    resProduct.forEach(product -> {
//                        List<SellerReviewDTO> buyerReview = product.getSeller().getBuyerReview();
//
//                        int sellerReviewCount = 0;
//                        double sellerRanking = 0.0;
//
//                        if (buyerReview != null && buyerReview.size() > 0) {
//                            for (SellerReviewDTO sellerReview : buyerReview) {
//                                sellerReviewCount++;
//                                sellerRanking += sellerReview.getRating();
//                            }
//                        }
//
//                        double SellerRankingScore = 0.0;
//                        if (sellerReviewCount != 0) {
//                            SellerRankingScore = new BigDecimal(sellerRanking / sellerReviewCount).setScale(2, RoundingMode.HALF_UP).doubleValue();
//                        }
//
//                        PopularFood popularFood = new PopularFood(
//                                product.getId(),
//                                product.getName(),
//                                product.getDescription(),
//                                BigDecimal.valueOf(product.getPrice()).setScale(2, RoundingMode.HALF_UP).doubleValue(),
//                                sellerReviewCount,
//                                SellerRankingScore,
//                                product.getCartCount(),
//                                product.getProductImages(),
//                                new UserDTO(product.getSeller().getId()),
//                                product.getQty());
//
//                        ProductAdapterModel productAdapterModel = new ProductAdapterModel(
//                                product.getName(), product.getSeller().getEmail(), product.getSeller().getMobile(), product.isStatus());
//                        productAdapterModel.setPid(product.getId());
//                        productAdapterModel.setProductImages(product.getProductImages());
//                        productAdapterModel.setPopularFood(popularFood);
//                        models.add(productAdapterModel);
//
//                    });
//                    initProductRecycler(models);
//                }
//            }
//
//            @Override
//            public void onFailure(Call<List<ProductDTO>> call, Throwable t) {
//                Log.d(TAG, "onFailure: load all product-admin " + t.getMessage());
//            }
//        });
//    }

    private void initProductRecycler(ArrayList<ProductAdapterModel> models) {
        RecyclerView recyclerView = root.findViewById(R.id.recyclerView);
        ProductAdapter productAdapter = new ProductAdapter(models);
        recyclerView.setLayoutManager(new LinearLayoutManager(root.getContext(), LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(productAdapter);
    }

    private void initMethod() {
        spinner = root.findViewById(R.id.spinner);
        searchText = root.findViewById(R.id.searchText);
        searchButton = root.findViewById(R.id.button);
    }
}