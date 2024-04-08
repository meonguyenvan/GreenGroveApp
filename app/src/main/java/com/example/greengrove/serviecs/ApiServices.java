package com.example.greengrove.serviecs;

import com.example.greengrove.Model.Cart;
import com.example.greengrove.Model.Categor;
import com.example.greengrove.Model.Favourite;
import com.example.greengrove.Model.Fruit;
import com.example.greengrove.Model.Review;
import com.example.greengrove.Model.Users;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
//import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

import com.example.greengrove.Model.Response;

import java.util.ArrayList;

public interface ApiServices {
    public static String BASE_URL = "http://192.168.1.2:3000/api/";
    //192.168.1.3
    @Multipart
    @POST("register-send-email")
    Call<Response<Users>> register(@Part("username")RequestBody username,
                                   @Part("password")RequestBody password,
                                   @Part("email")RequestBody email,
                                   @Part("name")RequestBody name,
                                   @Part MultipartBody.Part avatar);
    @POST("login")
    Call<Response<Users>> login(@Body Users users);

    @GET("get-list-fruit")
    Call<Response<ArrayList<Fruit>>> getListFruit(@Header("Authorization") String token);
    @GET("get-list-category")
    Call<Response<ArrayList<Categor>>> getListCate();
    @GET("get-list-fruits/{id_category}")
    Call<Response<ArrayList<Fruit>>> getListML(@Path("id_category") String id_category);
    @GET("get-favourite/{id_user}")
    Call<Response<ArrayList<Favourite>>> getListFV(@Path("id_user") String id_user);
//    @PUT("put-favourite/{id_user}/{id_fruit}/{isActivated}")
//    Call<Response<Favourite>> updateActivated(@Path("id_user") String id_user,@Path("id_fruit") String id_fruit,@Path("isActivated") String isActivated);
        //Nếu chúng ta cập nhật nhiều trạng thái thì ngay @Body chúng ta nên truyền vào đối tượng

    @DELETE("remove-favourite/{id_user}/{id_fruit}")
    Call<Response<Favourite>> deleteFavourite(@Path("id_user") String id_user,@Path("id_fruit") String id_fruit);

    @POST("user-products")
    Call<Response<Favourite>> addFavourite(@Body RequestBody requestBody);

    @GET("get-list-fruit-have-name")
    Call<Response<ArrayList<Fruit>>> getListSearch(@Query("searchTerm") String searchTerm);

    @GET("get-reviews/{id_fruit}")
    Call<Response<Review>> getReviews(@Path("id_fruit") String id_fruit);

    @GET("get-list-fruit-in-prices")
    Call<Response<ArrayList<Fruit>>> getListSearchPrice(@Query("price_start") String price_start,@Query("price_end") String price_end);

    @GET("get-user/{id_user}")
    Call<Response<Users>> getUser(@Path("id_user") String id_user);

    @GET("get-cart/{id_user}")
    Call<Response<ArrayList<Cart>>> getListCart(@Path("id_user") String id_user);

    @POST("add-cart")
    Call<Response<Cart>> addCart(@Body RequestBody requestBody);

    @DELETE("delete-cart")//"/delete-cart" => Lỗi 404
    Call<Response<Cart>> deleteCart(@Query("id_user") String id_user, @Query("id_fruit") String id_fruit);

    @PUT("update-cart")
    Call<Response<Cart>> updateCart(@Body RequestBody requestBody);

    @GET("get-favourite/{id_user}")
    Call<Response<ArrayList<Favourite>>> findFavourite(@Path("id_user") String id_user,@Query("searchTerm") String searchTerm);

    @Multipart
    @POST("update-user/{userId}")
    Call<Response<Users>> updateUser(
            @Path("userId") String userId,
            @Part("username") RequestBody username,
            @Part("email") RequestBody email,
            @Part("name") RequestBody name,
            @Part MultipartBody.Part avatar
    );

    @POST("login-with-google")
    Call<Response<Users>> loginGoogle(@Query("picture") String picture,
                                      @Query("email") String email,
                                      @Query("name") String name);
}
