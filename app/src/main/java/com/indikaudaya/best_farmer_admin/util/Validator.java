package com.indikaudaya.best_farmer_admin.util;

import android.util.Patterns;

public class Validator {
    public static boolean isValidEmail(CharSequence target) {
        return (target != null && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }

    public static boolean isPhoneValidate(CharSequence sequence) {
        return (sequence != null && Patterns.PHONE.matcher(sequence).matches());
    }

    public static boolean isTextValidate(CharSequence sequence) {
        return (sequence != null && !String.valueOf(sequence).trim().isEmpty());
    }

    public static boolean isPasswordValidate(CharSequence sequence) {
        return (sequence != null && !String.valueOf(sequence).trim().isEmpty() && String.valueOf(sequence).trim().length() > 8);
    }
}
