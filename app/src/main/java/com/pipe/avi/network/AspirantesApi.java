package com.pipe.avi.network;

import com.pipe.avi.model.Aspirante;
import com.pipe.avi.model.LoginResponse;
import com.pipe.avi.model.LoginRequest;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface AspirantesApi {

    @GET("aspirantes")
    Call<List<Aspirante>> getAspirantes();

    @POST("registeraspirante")
    Call<Aspirante> registrarAspirante(@Body Aspirante aspirante);

    @POST("loginaspirante")
    Call<LoginResponse> loginAspirante(@Body LoginRequest loginRequest);
}




