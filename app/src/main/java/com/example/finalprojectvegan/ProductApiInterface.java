package com.example.finalprojectvegan;

import com.example.finalprojectvegan.Model.ProductItem;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ProductApiInterface {
    @GET("VeganProduct.php") // Base URL 이후 상세주소
    Call<ProductItem> getProductData();
}
