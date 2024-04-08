package com.example.greengrove.serviecs;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class HttpRequest {
    private ApiServices requestInterface;

    //Hàm tạo

    public HttpRequest() {
        //Create retrofit
        requestInterface = new Retrofit.Builder()
                .baseUrl(ApiServices.BASE_URL) // Sử dụng BASE_URL từ interface ApiServices
                .addConverterFactory(GsonConverterFactory.create()) // Thêm Converter Factory cho Gson
                .build()
                .create(ApiServices.class);
    }

    public ApiServices callAPI(){
        return requestInterface;
    }
}
