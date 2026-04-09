package com.pipe.avi.network;

import com.pipe.avi.model.MisReportes;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;

public interface ReportesApi {
    @GET("misreportes")
    Call<List<MisReportes>> getMisReportes(
            @Header("Authorization") String token
    );
}
