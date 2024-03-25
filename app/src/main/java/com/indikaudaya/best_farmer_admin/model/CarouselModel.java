package com.indikaudaya.best_farmer_admin.model;

import android.net.Uri;

public class CarouselModel {
    Uri imageUri;

    public CarouselModel(Uri imageUri) {
        this.imageUri = imageUri;
    }

    public Uri getImageUri() {
        return imageUri;
    }

    public void setImageUri(Uri imageUri) {
        this.imageUri = imageUri;
    }
}
