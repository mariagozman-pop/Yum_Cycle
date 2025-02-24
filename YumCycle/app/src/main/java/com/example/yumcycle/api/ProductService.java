package com.example.yumcycle.api;

import com.example.yumcycle.models.Product;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ProductService {
    @GET("/api/products/{barcode}")
    Call<Product> getProductByBarcode(@Path("barcode") String barcode);
}
