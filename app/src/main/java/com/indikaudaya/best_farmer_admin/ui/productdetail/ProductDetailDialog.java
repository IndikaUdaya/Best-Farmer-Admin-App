package com.indikaudaya.best_farmer_admin.ui.productdetail;

import static android.R.color.transparent;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.carousel.CarouselLayoutManager;
import com.google.android.material.carousel.HeroCarouselStrategy;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.indikaudaya.best_farmer_admin.R;
import com.indikaudaya.best_farmer_admin.adapter.CarouselAdapter;
import com.indikaudaya.best_farmer_admin.dto.ProductImageDTO;
import com.indikaudaya.best_farmer_admin.dto.SellerReviewDTO;
import com.indikaudaya.best_farmer_admin.interceptor.RequestInterceptor;
import com.indikaudaya.best_farmer_admin.model.CarouselModel;
import com.indikaudaya.best_farmer_admin.model.PopularFood;
import com.indikaudaya.best_farmer_admin.service.BestFarmerAdminApiService;
import com.indikaudaya.best_farmer_admin.ui.sellerreview.SellerReviewListDialog;
import com.indikaudaya.best_farmer_admin.util.SweetAlertDialogCustomize;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ProductDetailDialog extends Dialog {

    private static final String TAG = ProductDetailDialog.class.getName();
    Context context;
    PopularFood popularFood;
    boolean isExistingOnWatchlist;
    boolean isExistingOnCart;
    long watchlistId;
    long cartId;

    Dialog dialog;

    public ProductDetailDialog(Context context, PopularFood popularFood) {
        super(context);
        this.context = context;
        this.popularFood = popularFood;
    }

    public void setDialog(Dialog dialog) {
        this.dialog = dialog;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.indikaudaya.best_farmer_admin.R.layout.dialog_product_detail);

        setValuesToFields(popularFood);
        clickReviewButton();
        backButton();
        getSellerReview(false);

    }

    private void clickReviewButton() {
        findViewById(R.id.imageView5).setOnClickListener(v -> {
                getSellerReview(true);
        });
    }

    private void getSellerReview(boolean click) {
        long sellerId = popularFood.getSeller().getId();
        String token = getContext().getSharedPreferences(getContext().getString(R.string.security_admin_api_store), getContext().MODE_PRIVATE).getString("token", "");

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(getContext().getString(R.string.base_url))
                .addConverterFactory(GsonConverterFactory.create())
                .client(new OkHttpClient().newBuilder().addInterceptor(new RequestInterceptor(token)).build())
                .build();

        BestFarmerAdminApiService apiService = retrofit.create(BestFarmerAdminApiService.class);
        Call<List<SellerReviewDTO>> review = apiService.getAllSellerReviewById(sellerId);

        review.enqueue(new Callback<List<SellerReviewDTO>>() {
            @Override
            public void onResponse(Call<List<SellerReviewDTO>> call, Response<List<SellerReviewDTO>> response) {
                if (response.isSuccessful()) {
                    List<SellerReviewDTO> body = response.body();

                    int totalReview = 0;
                    float ratingByBuyer = 0.0f;

                    for (SellerReviewDTO sr : body) {
                        totalReview++;
                        ratingByBuyer += sr.getRating();
                    }
                    if (totalReview != 0) {
                        ((TextView) findViewById(R.id.sellerRating)).setText(String.valueOf(ratingByBuyer / totalReview));
                    } else {
                        ((TextView) findViewById(R.id.sellerRating)).setText(String.valueOf(0));

                    }

                    ((TextView) findViewById(R.id.reviewCount)).setText(String.valueOf(totalReview));

                    if (click) {
                        viewSellerReviewDetails(body);
                    }
                }
            }

            @Override
            public void onFailure(Call<List<SellerReviewDTO>> call, Throwable throwable) {
                new SweetAlertDialogCustomize().errorAlert(getContext(), "no found seller review - " + throwable.getMessage());
            }
        });
    }

    private void viewSellerReviewDetails(List<SellerReviewDTO> reviewDTOS) {
        SellerReviewListDialog reviewSellerDialog = new SellerReviewListDialog(context, reviewDTOS);
        reviewSellerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(context.getResources().getColor(transparent, context.getTheme())));
        reviewSellerDialog.setCancelable(true);
        reviewSellerDialog.getWindow().setGravity(Gravity.CENTER);
        reviewSellerDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        reviewSellerDialog.show();
    }


    //! Other
    private void setValuesToFields(PopularFood popularFood) {
        if (popularFood != null) {
            ((TextView) findViewById(R.id.productTitle)).setText(popularFood.getTitle());
            ((TextView) findViewById(R.id.food_price)).setText("LKR ".concat(String.valueOf(BigDecimal.valueOf(popularFood.getPrice()).setScale(2, RoundingMode.HALF_UP).doubleValue())));
            ((TextView) findViewById(R.id.sellerRating)).setText(String.valueOf(popularFood.getRatingScore()));
            ((TextView) findViewById(R.id.reviewCount)).setText(String.valueOf(popularFood.getReviewCount()));
            ((TextView) findViewById(R.id.productDescription)).setText(popularFood.getDescription());
            ((TextView) findViewById(R.id.pQty)).setText(String.valueOf(popularFood.getQty()));

            loadImageFromServer(popularFood.getProductImageList());
        }
    }

    private void loadImageFromServer(List<ProductImageDTO> productImageList) {
        ArrayList<CarouselModel> carouselModels = new ArrayList<>();
        String[] split = productImageList.get(0).getPath().split("/");

        String newPath = split[0] + "/" + split[1];
        FirebaseStorage storage = FirebaseStorage.getInstance();
        storage.getReference("/product-image/" + newPath)
                .listAll().addOnCompleteListener(task -> {
                    for (StorageReference item : task.getResult().getItems()) {
                        item.getDownloadUrl()
                                .addOnSuccessListener(uri -> {
                                    carouselModels.add(new CarouselModel(uri));
                                    if (task.getResult().getItems().size() == carouselModels.size()) {
                                        setImageToCarousel(carouselModels);
                                    }
                                });
                    }
                });
    }

    private void setImageToCarousel(ArrayList<CarouselModel> productImageList) {
        RecyclerView recyclerView = findViewById(R.id.carousel_recycler_view);
        CarouselAdapter carouselAdapter = new CarouselAdapter(context, productImageList);
        recyclerView.setLayoutManager(new CarouselLayoutManager(new HeroCarouselStrategy(), CarouselLayoutManager.HORIZONTAL));
        recyclerView.setAdapter(carouselAdapter);
    }

    private void backButton() {
        ((ImageView) findViewById(R.id.imageView)).setOnClickListener(v -> {
            dismiss();
        });
    }

}