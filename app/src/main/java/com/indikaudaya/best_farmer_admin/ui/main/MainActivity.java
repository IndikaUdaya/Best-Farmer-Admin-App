package com.indikaudaya.best_farmer_admin.ui.main;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.indikaudaya.best_farmer_admin.R;
import com.indikaudaya.best_farmer_admin.model.LoginDetails;
import com.indikaudaya.best_farmer_admin.ui.auth.signing.SigningFragment;
import com.indikaudaya.best_farmer_admin.ui.buyer.BuyerFragment;
import com.indikaudaya.best_farmer_admin.ui.home.HomeFragment;
import com.indikaudaya.best_farmer_admin.ui.product.ProductFragment;
import com.indikaudaya.best_farmer_admin.ui.seller.SellerFragment;
import com.indikaudaya.best_farmer_admin.util.FragmentChangeCallback;
import com.indikaudaya.best_farmer_admin.util.SweetAlertDialogCustomize;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class MainActivity extends AppCompatActivity {

    static public BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setBackground(null);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.home1) {
                replaceFragment(new HomeFragment());
            } else if (item.getItemId() == R.id.product) {
                replaceFragment(new ProductFragment());
            } else if (item.getItemId() == R.id.seller) {
                replaceFragment(new SellerFragment());
            } else if (item.getItemId() == R.id.buyer) {
                replaceFragment(new BuyerFragment());
            } else if (item.getItemId() == R.id.signout) {
                signOut();
            }
            return true;
        });
        viewFirstFragment();
    }

    private void viewFirstFragment() {
        if (!LoginDetails.isLogin) {
            bottomNavigationView.setVisibility(View.GONE);
            replaceFragment(new SigningFragment());
        } else {
            bottomNavigationView.setVisibility(View.VISIBLE);
        }
    }

    private void signOut() {
        new SweetAlertDialogCustomize()
                .confirmationAlert(this, "Sign out and exit the app", "Sure")
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.dismiss();
                        FirebaseAuth auth = FirebaseAuth.getInstance();
                        auth.signOut();
                        LoginDetails.isLogin = false;
                        replaceFragment(new SigningFragment());
                    }
                }).show();
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }


}

