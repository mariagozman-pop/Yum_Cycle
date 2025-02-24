// OllamaService.java
package com.example.yumcycle.api;

import com.example.yumcycle.models.OllamaRequest;
import com.example.yumcycle.models.OllamaResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface OllamaService {
    @POST("api/generate")
    Call<String> generateRecipe(@Body OllamaRequest request);
}
