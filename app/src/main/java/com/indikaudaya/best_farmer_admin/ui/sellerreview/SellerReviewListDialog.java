package com.indikaudaya.best_farmer_admin.ui.sellerreview;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.indikaudaya.best_farmer_admin.R;
import com.indikaudaya.best_farmer_admin.adapter.SellerReviewAdapter;
import com.indikaudaya.best_farmer_admin.dto.SellerReviewDTO;
import com.indikaudaya.best_farmer_admin.model.SellerReviewModel;
import com.skydoves.progressview.ProgressView;

import java.util.ArrayList;
import java.util.List;

public class SellerReviewListDialog extends Dialog {

    private static final String TAG = SellerReviewListDialog.class.getName();
    Context context;
    List<SellerReviewDTO> review;

    private TextView totalReviewCount, farmerName;
    private RatingBar ratingBar;
    private ProgressView progressView5, progressView4, progressView3, progressView2, progressView1;

    public SellerReviewListDialog(@NonNull Context context, List<SellerReviewDTO> sellerReview) {
        super(context);
        this.context = context;
        this.review = sellerReview;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.seller_review_list_layout);
        initMethod();
        rateViewGenerate();
        clickDown();
    }

    private void initMethod() {
        totalReviewCount = findViewById(R.id.textView51);
        farmerName = findViewById(R.id.textView50);
        ratingBar = findViewById(R.id.ratingBar);
        progressView5 = findViewById(R.id.progressView5);
        progressView4 = findViewById(R.id.progressView4);
        progressView3 = findViewById(R.id.progressView3);
        progressView2 = findViewById(R.id.progressView2);
        progressView1 = findViewById(R.id.progressView1);
    }

    private void rateViewGenerate() {
        if (review != null) {
            ArrayList<SellerReviewModel> sellerReviews = new ArrayList<>();

            int totalReview = 0;
            float ratingByBuyer = 0.0f;

            float rate5 = 0.0f;
            float rate4 = 0.0f;
            float rate3 = 0.0f;
            float rate2 = 0.0f;
            float rate1 = 0.0f;

            for (SellerReviewDTO sr : review) {
                Log.d(TAG, "rateViewGenerate: " + sr.toString());
                totalReview++;
                ratingByBuyer += sr.getRating();
                farmerName.setText(sr.getSeller().getEmail());

                if (sr.getRating() > 4.0 && sr.getRating() <= 5.0) {
                    rate5 += 1; //sr.getRating();
                } else if (sr.getRating() > 3.0 && sr.getRating() <= 4.0) {
                    rate4 += 1;// sr.getRating();
                } else if (sr.getRating() > 2.0 && sr.getRating() <= 3.0) {
                    rate3 += 1;//sr.getRating();
                } else if (sr.getRating() > 1.0 && sr.getRating() <= 2.0) {
                    rate2 += 1;// sr.getRating();
                } else if (sr.getRating() >= 0.0 && sr.getRating() <= 1.0) {
                    rate1 += 1;// sr.getRating();
                }

                sellerReviews.add(new SellerReviewModel(
                        sr.getBuyer().getEmail(),
                        sr.getRating(),
                        sr.getComment(),
                        sr.getReviewDate(),
                        "https://media.istockphoto.com/id/1434212178/photo/middle-eastern-lady-using-laptop-working-online-sitting-in-office.jpg?s=1024x1024&w=is&k=20&c=H640-Mts2rHSHLTkCd04WFd_VhcHMwX8kAGVXW4ddJY="
                ));
            }

            if (totalReview == 0) {
                totalReview++;
            }

            initSellerRecycleView(sellerReviews);

            float totalCountReview = ratingByBuyer / totalReview;

            totalReviewCount.setText(String.valueOf(totalCountReview));
            ratingBar.setRating(totalCountReview);

            float rating5 = (rate5 / totalReview) * 100;
            float rating4 = (rate4 / totalReview) * 100;
            float rating3 = (rate3 / totalReview) * 100;
            float rating2 = (rate2 / totalReview) * 100;
            float rating1 = (rate1 / totalReview) * 100;

            progressView5.setAutoAnimate(true);
            progressView5.setLabelText(" 5 * - " + rating5 + "%");
            progressView5.setProgress(rating5);

            progressView4.setAutoAnimate(true);
            progressView4.setLabelText(" 4 * - " + rating4 + "%");
            progressView4.setProgress(rating4);

            progressView3.setAutoAnimate(true);
            progressView3.setLabelText(" 3 * - " + rating3 + "%");
            progressView3.setProgress(rating3);

            progressView2.setAutoAnimate(true);
            progressView2.setLabelText(" 2 * - " + rating2 + "%");
            progressView2.setProgress(rating2);

            progressView1.setAutoAnimate(true);
            progressView1.setLabelText(" 1 * - " + rating1 + "%");
            progressView1.setProgress(rating1);
        }
    }

    private void clickDown() {
        findViewById(R.id.back).setOnClickListener(v -> {
            this.dismiss();
        });
    }

    private void initSellerRecycleView(ArrayList<SellerReviewModel> sellerReviews) {
        RecyclerView recyclerView = findViewById(R.id.seller_recycle_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setAdapter(new SellerReviewAdapter(sellerReviews));
    }

}

