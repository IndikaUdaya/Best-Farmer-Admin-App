package com.indikaudaya.best_farmer_admin.ui.auth.signup;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.JsonObject;
import com.indikaudaya.best_farmer_admin.R;
import com.indikaudaya.best_farmer_admin.model.AdminModel;
import com.indikaudaya.best_farmer_admin.model.Auth;
import com.indikaudaya.best_farmer_admin.service.BestFarmerAdminApiService;
import com.indikaudaya.best_farmer_admin.ui.auth.signing.SigningFragment;
import com.indikaudaya.best_farmer_admin.util.SweetAlertDialogCustomize;
import com.indikaudaya.best_farmer_admin.util.Validator;

import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SignupFragment extends Fragment {

    private static final String TAG = SigningFragment.class.getName();
    private static final String COLLECTION = "admin";
    View root;
    EditText firstName, lastName, mobile, email, password, confirmPassword, address;
    SweetAlertDialog sweetAlertDialog;

    FirebaseFirestore db;
    FirebaseAuth mAuth;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_signup, container, false);
        changeToSignIn();
        initMethod();
        clickSignupBtn();
       apiRegister("hakisense@gmail.com", "0754215456", "zxcvbnm,./");
        return root;
    }

    private void clickSignupBtn() {
        root.findViewById(R.id.btnSignUp).setOnClickListener(v -> {
            pleaseWait(true);
            checkValidation();
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

    private void checkValidation() {
        if (!Validator.isTextValidate(String.valueOf(firstName.getText()))) {
            new SweetAlertDialogCustomize().errorAlert(getContext(), "Please enter your first name.");
        } else if (!Validator.isTextValidate(String.valueOf(lastName.getText()))) {
            new SweetAlertDialogCustomize().errorAlert(getContext(), "Please enter your last name.");
        } else if (!Validator.isPhoneValidate(String.valueOf(mobile.getText()))) {
            new SweetAlertDialogCustomize().errorAlert(getContext(), "Please check your mobile number.");
        } else if (!Validator.isValidEmail(String.valueOf(email.getText()))) {
            new SweetAlertDialogCustomize().errorAlert(getContext(), "Please enter your email.");
        } else if (!Validator.isPasswordValidate(String.valueOf(password.getText()))) {
            new SweetAlertDialogCustomize().errorAlert(getContext(), "Please enter password greater than 8 characters.");
        } else if (!Validator.isPasswordValidate(String.valueOf(confirmPassword.getText()))) {
            new SweetAlertDialogCustomize().errorAlert(getContext(), "Please enter confirm password.");
        } else if (!String.valueOf(password.getText()).equals(String.valueOf(confirmPassword.getText()))) {
            new SweetAlertDialogCustomize().errorAlert(getContext(), "Please check password and confirm password.");
        } else {
            saveDataOnFirebase();
        }
    }

    private void saveDataOnFirebase() {

        AdminModel adminModel = new AdminModel();
        adminModel.setAddress(String.valueOf(address.getText()));
        adminModel.setEmail(String.valueOf(email.getText()));
        adminModel.setFirstName(String.valueOf(firstName.getText()));
        adminModel.setLastName(String.valueOf(lastName.getText()));
        adminModel.setMobile(String.valueOf(mobile.getText()));
        adminModel.setStatus(true);
        adminModel.setPassword(String.valueOf(confirmPassword.getText()));

        db.collection(COLLECTION)
                .document(String.valueOf(email.getText()))
                .set(adminModel)
                .addOnSuccessListener(documentReference -> {
                    sendVerificationEmail();
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        new SweetAlertDialogCustomize().errorAlert(getContext(), "Error adding document");
                    }
                });
    }

    private void sendVerificationEmail() {
        String email = String.valueOf(this.email.getText());
        String password = String.valueOf(this.confirmPassword.getText());
        String mobile = String.valueOf(this.mobile.getText());

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        FirebaseUser user = mAuth.getCurrentUser();
                        if (user != null) {
                            user.sendEmailVerification();
                            //! Register for API access
                            apiRegister(email, mobile, password);
                        }
                    } else {
                        Log.w(TAG, "signInWithEmail:failure", task.getException());
                        new SweetAlertDialogCustomize().errorAlert(getContext(), task.getException().getMessage());
                    }
                });
    }

    private void apiRegister(String email, String mobile, String password) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(getContext().getString(R.string.base_url))
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        BestFarmerAdminApiService service = retrofit.create(BestFarmerAdminApiService.class);

        Auth auth = new Auth(email,mobile,password,true,"admin");

        Call<JsonObject> signUp = service.signUp(auth);
        signUp.request().newBuilder().header("Content-Type", "application/json").build();

        signUp.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(@NonNull Call<JsonObject> call, @NonNull Response<JsonObject> response) {
                if (response.isSuccessful()) {
                    new SweetAlertDialog(getContext(), SweetAlertDialog.SUCCESS_TYPE)
                            .setTitleText("We will send verification link into your email address. Please check it!")
                            .setConfirmButton("Ok", new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    sweetAlertDialog.dismiss();
                                    changeFragment();
                                }
                            })
                            .showCancelButton(false)
                            .show();
                } else {
                    new SweetAlertDialogCustomize().errorAlert(getContext(), "Admin Register Failed!");
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.e(TAG, "Error :" + t.getMessage());
            }
        });
    }

    private void initMethod() {
        firstName = root.findViewById(R.id.firstName);
        lastName = root.findViewById(R.id.lastName);
        mobile = root.findViewById(R.id.mobile);
        email = root.findViewById(R.id.email);
        password = root.findViewById(R.id.password);
        confirmPassword = root.findViewById(R.id.confirmPassword);
        address = root.findViewById(R.id.address);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
    }

    private void changeToSignIn() {
        root.findViewById(R.id.signing).setOnClickListener(v -> {
            changeFragment();
        });

    }

    private void changeFragment() {
        pleaseWait(false);
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, new SigningFragment());
        fragmentTransaction.commit();
    }

}