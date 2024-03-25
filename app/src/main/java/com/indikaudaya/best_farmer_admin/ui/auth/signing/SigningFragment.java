package com.indikaudaya.best_farmer_admin.ui.auth.signing;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.material.datepicker.MaterialTextInputPicker;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.JsonObject;
import com.indikaudaya.best_farmer_admin.R;
import com.indikaudaya.best_farmer_admin.interceptor.RequestInterceptor;
import com.indikaudaya.best_farmer_admin.model.AdminModel;
import com.indikaudaya.best_farmer_admin.model.ApiUserDetails;
import com.indikaudaya.best_farmer_admin.model.Auth;
import com.indikaudaya.best_farmer_admin.model.LoginDetails;
import com.indikaudaya.best_farmer_admin.service.BestFarmerAdminApiService;
import com.indikaudaya.best_farmer_admin.ui.auth.signup.SignupFragment;
import com.indikaudaya.best_farmer_admin.ui.home.HomeFragment;
import com.indikaudaya.best_farmer_admin.ui.main.MainActivity;
import com.indikaudaya.best_farmer_admin.util.SweetAlertDialogCustomize;
import com.indikaudaya.best_farmer_admin.util.Validator;

import cn.pedant.SweetAlert.SweetAlertDialog;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SigningFragment extends Fragment {

    private static final String USERTYPE = "admin";
    private static final String TAG = SigningFragment.class.getName();

    SweetAlertDialog sweetAlertDialog;

    FirebaseAuth mAuth;
    FirebaseFirestore db;

    View root;

    Button signinButton;
    EditText emailText, passeordText;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_signing, container, false);
        initMethod();
        changeToSignup();
        clickSigningButton();
        emailText.setText("hakisense@gmail.com");
        passeordText.setText("zxcvbnm,./");
        return root;
    }

    private void initMethod() {
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        signinButton = root.findViewById(R.id.btnSignIn);
        emailText = root.findViewById(R.id.email);
        passeordText = root.findViewById(R.id.password);
    }

    private void clickSigningButton() {
        signinButton.setOnClickListener(v -> {
            checkInputFiled();
        });
    }

    private void checkInputFiled() {

        if (!Validator.isValidEmail(emailText.getText())) {
            new SweetAlertDialogCustomize().errorAlert(getContext(), "Please enter email address");
        } else if (String.valueOf(passeordText.getText()).isEmpty()) {
            new SweetAlertDialogCustomize().errorAlert(getContext(), "Please enter password ");
        } else {
            checkOnDb();
        }
    }

    private void checkOnDb() {
        String email = String.valueOf(this.emailText.getText());
        String password = String.valueOf(this.passeordText.getText());

        pleaseWait(true);
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(command -> {
                    if (command.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        if (user != null && user.isEmailVerified()) {
                            getUserFromFireStore();
                        } else {
                            pleaseWait(false);
                            user.sendEmailVerification().addOnSuccessListener(command1 -> {
                                new SweetAlertDialogCustomize().successAlert(getContext(), "We sent a verification email to your email address, Please check!.");
                                buttonCountDOwn();
                            });
                        }
                    } else {
                        pleaseWait(false);
                        new SweetAlertDialogCustomize().errorAlert(getContext(), "Please check email address and password");
                    }
                });

    }

    private void getUserFromFireStore() {
        db.collection(USERTYPE)
                .document(String.valueOf(emailText.getText()))
                .get().addOnSuccessListener(documentSnapshot -> {
                    AdminModel adminModel = documentSnapshot.toObject(AdminModel.class);
                    Log.d(TAG, "getUserFromFireStore: - " + adminModel.toString());
                    apiCalling(adminModel);

                }).addOnFailureListener(e -> {
                    new SweetAlertDialogCustomize().errorAlert(getContext(), e.getMessage());
                });
    }

    private void buttonCountDOwn() {
        signinButton.setEnabled(false);
        signinButton.setText("waiting for 2mins..");
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            signinButton.setText("Sign-in");
            signinButton.setEnabled(true);
        }, 2 * 60 * 1000);

    }

    private void apiCalling(AdminModel dataModel) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(getContext().getString(R.string.base_url))
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        BestFarmerAdminApiService service = retrofit.create(BestFarmerAdminApiService.class);
        Call<JsonObject> request = service.auth(new Auth(dataModel.getEmail(),dataModel.getMobile(),dataModel.getPassword(),true,"admin"));

        request.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(@NonNull Call<JsonObject> call, @NonNull Response<JsonObject> response) {

                if (response.isSuccessful()) {
                    String to = response.body().get("jwtToken").getAsString();

                    SharedPreferences.Editor securityApi = getContext().getSharedPreferences(getContext().getString(R.string.security_admin_api_store), Context.MODE_PRIVATE).edit();
                    securityApi.putString("token", to);
                    securityApi.apply();

                    LoginDetails.adminModel = dataModel;

                    getLoginUserDetailFromApi(to);
                } else {
                    pleaseWait(false);
                    new SweetAlertDialogCustomize().errorAlert(getContext(), "Admin not found");
                }
            }

            private void getLoginUserDetailFromApi(String to) {
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(getContext().getString(R.string.base_url))
                        .addConverterFactory(GsonConverterFactory.create())
                        .client(new OkHttpClient.Builder().addInterceptor(new RequestInterceptor(to)).build())
                        .build();

                BestFarmerAdminApiService service = retrofit.create(BestFarmerAdminApiService.class);

                Call<ApiUserDetails> userDetail = service.getAdminDetail(dataModel.getEmail());

                userDetail.enqueue(new Callback<ApiUserDetails>() {
                    @Override
                    public void onResponse(Call<ApiUserDetails> call, Response<ApiUserDetails> response) {
                        if (response.isSuccessful()) {
                            ApiUserDetails u = response.body();
                            LoginDetails.isLogin = true;
                            LoginDetails.apiAdminDetails = u;

                            pleaseWait(false);
                            new SweetAlertDialogCustomize().successAlert(getContext(), "Login Successfully..");
                            changeFragment(new HomeFragment());
                        } else {
                            Log.i(TAG, "onResponse: no admin data found from api");
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<ApiUserDetails> call, @NonNull Throwable t) {
                        Log.e(TAG, "onFailure API calling admin login : " + t.getMessage());
                    }
                });
            }

            @Override
            public void onFailure(@NonNull Call<JsonObject> call, @NonNull Throwable error) {
                Log.e(TAG, "Error :" + error.getMessage());
                if (mAuth != null) {
                    mAuth.signOut();
                    new SweetAlertDialogCustomize().errorAlert(getContext(), "Service currently unavailable!");
                }
            }
        });
    }

    private void pleaseWait(boolean visibility) {
        if (sweetAlertDialog == null) {
            sweetAlertDialog = new SweetAlertDialogCustomize().loadingAlert(getContext(), false);
        }
        if (visibility) {
            sweetAlertDialog.show();
        } else {
            sweetAlertDialog.dismiss();
        }
    }

    private void changeToSignup() {
        root.findViewById(R.id.signup).setOnClickListener(v -> {
            changeFragment( new SignupFragment());
        });
    }

    private void changeFragment(Fragment fragment) {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout,fragment);
        fragmentTransaction.commit();
    }
}