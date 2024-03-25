package com.indikaudaya.best_farmer_admin.service;

import android.hardware.lights.LightState;

import com.google.gson.JsonObject;
import com.indikaudaya.best_farmer_admin.dto.ProductDTO;
import com.indikaudaya.best_farmer_admin.dto.SellerReviewDTO;
import com.indikaudaya.best_farmer_admin.model.AdminModel;
import com.indikaudaya.best_farmer_admin.model.ApiUserDetails;
import com.indikaudaya.best_farmer_admin.model.Auth;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface BestFarmerAdminApiService {

    @POST("authenticate")
    Call<JsonObject> auth(@Body Auth authRequest);

    @POST("sign-up")
    Call<JsonObject> signUp(@Body Auth authRequest);

    @GET("api/user/{email}")
    Call<ApiUserDetails> getAdminDetail(@Path("email") String email);

    @GET("api/admin/count/{seller-type}")
    Call<Integer> getSellerCount(@Path("seller-type") String sellerType);

    @GET("api/admin/count")
    Call<Integer> getAllProductCount();

    @GET("api/admin/product")
    Call<Double> getAllProfitThisMonth();

    @PUT("api/admin/seller-id/{email}/{status}")
    Call<Boolean> deactivateProduct(@Path("email") String email, @Path("status") boolean status);

    @GET("api/admin/product/all-product")
    Call<List<ProductDTO>> getAllProduct();

    @PUT("api/admin/product/product-id/{id}/{status}")
    Call<Boolean> deactivateProductById(@Path("id") Long pId, @Path("status") boolean status);

    @GET("api/admin/seller-review/review-all/{id}")
    Call<List<SellerReviewDTO>> getAllSellerReviewById(@Path("id") long id);

    @GET("api/admin/product/search/{searchText}/{productStatus}")
    Call<List<ProductDTO>> getAllProductsBySearching(@Path("searchText") String searchText, @Path("productStatus") boolean productStatus);

    @GET("api/admin/product/search/search-word/{searchText}")
    Call<List<ProductDTO>> getAllProductsByProductName(@Path("searchText") String searchText);

    @GET("api/admin/product/search/status/{productStatus}")
    Call<List<ProductDTO>> getAllProductsByStatus(@Path("productStatus") boolean productStatus);

}
