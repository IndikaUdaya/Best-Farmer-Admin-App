package com.indikaudaya.best_farmer_admin.ui.home;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.JsonObject;
import com.indikaudaya.best_farmer_admin.R;
import com.indikaudaya.best_farmer_admin.interceptor.RequestInterceptor;
import com.indikaudaya.best_farmer_admin.model.Auth;
import com.indikaudaya.best_farmer_admin.service.BestFarmerAdminApiService;
import com.indikaudaya.best_farmer_admin.ui.main.MainActivity;
import com.indikaudaya.best_farmer_admin.util.FragmentChangeCallback;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class HomeFragment extends Fragment {

    private static final double PROFIT_RATION = 0.15;
    View root;

    TextView sellerCount, productCount, buyerCount, profit;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_home, container, false);
        MainActivity.bottomNavigationView.setVisibility(View.VISIBLE);
        initMethod();
        loadTotalSeller("seller");
        loadTotalSeller("buyer");
        profitCalculate();
        loadTotalProfit();
        return root;
    }

    private void profitCalculate() {
        String token = getContext().getSharedPreferences(getContext().getString(R.string.security_admin_api_store), getContext().MODE_PRIVATE).getString("token", "");

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(getContext().getString(R.string.base_url))
                .addConverterFactory(GsonConverterFactory.create())
                .client(new OkHttpClient().newBuilder().addInterceptor(new RequestInterceptor(token)).build())
                .build();

        BestFarmerAdminApiService service = retrofit.create(BestFarmerAdminApiService.class);
        Call<Double> allProfitThisMonth = service.getAllProfitThisMonth();
        allProfitThisMonth.enqueue(new Callback<Double>() {
            @Override
            public void onResponse(Call<Double> call, Response<Double> response) {
                if(response.isSuccessful()){
                    Double body = response.body();
                    profit.setText("LKR "+ String.valueOf(body*PROFIT_RATION));
                }else{
                    profit.setText("0");
                }
            }

            @Override
            public void onFailure(Call<Double> call, Throwable t) {
            }
        });
    }

    private void loadTotalProfit() {
        String token = getContext().getSharedPreferences(getContext().getString(R.string.security_admin_api_store), getContext().MODE_PRIVATE).getString("token", "");

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(getContext().getString(R.string.base_url))
                .addConverterFactory(GsonConverterFactory.create())
                .client(new OkHttpClient().newBuilder().addInterceptor(new RequestInterceptor(token)).build())
                .build();

        BestFarmerAdminApiService service = retrofit.create(BestFarmerAdminApiService.class);
        Call<Integer> allProductCount = service.getAllProductCount();
        allProductCount.enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                if(response.isSuccessful()){
                    productCount.setText(String.valueOf(response.body()));
                }else{
                    productCount.setText("0");
                }
            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t) {

            }
        });


    }

    private void initMethod() {
        sellerCount = root.findViewById(R.id.sellerCount);
        productCount = root.findViewById(R.id.productCount);
        buyerCount = root.findViewById(R.id.buyerCount);
        profit = root.findViewById(R.id.profit);
    }

    private void loadTotalSeller(String userTypr) {

        String token = getContext().getSharedPreferences(getContext().getString(R.string.security_admin_api_store), getContext().MODE_PRIVATE).getString("token", "");

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(getContext().getString(R.string.base_url))
                .addConverterFactory(GsonConverterFactory.create())
                .client(new OkHttpClient().newBuilder().addInterceptor(new RequestInterceptor(token)).build())
                .build();

        BestFarmerAdminApiService service = retrofit.create(BestFarmerAdminApiService.class);
        Call<Integer> seller = service.getSellerCount(userTypr);
        seller.enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                if (response.isSuccessful()) {
                    if (userTypr.equalsIgnoreCase("seller")) {
                        sellerCount.setText(String.valueOf(response.body()));
                    } else if (userTypr.equalsIgnoreCase("buyer")) {
                        buyerCount.setText(String.valueOf(response.body()));
                    }
                } else {
                    if (userTypr.equalsIgnoreCase("seller")) {
                        sellerCount.setText("0");
                    } else if (userTypr.equalsIgnoreCase("buyer")) {
                        buyerCount.setText("0");
                    }
                }
            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t) {

            }
        });
    }
}