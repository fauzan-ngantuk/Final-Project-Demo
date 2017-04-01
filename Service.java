package com.rangermerah.recyletor;

/**
 * Created by NajibSaurus on 1/5/17.
 */

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;


interface Service {

    @Multipart
    @POST("/api/photo")
    Call<Response> postImage(@Part MultipartBody.Part img, @Part("image") RequestBody image);

    @Multipart
    @POST("/api/photos")
    Call<Response> uploadImage(@Part MultipartBody.Part img, @Part("image") RequestBody image);

    @Multipart
    @POST("/api/classify")
    Call<Response> submitImage(@Part MultipartBody.Part img, @Part("images_file") RequestBody image);

    @Multipart
    @POST("/api/photos")
    Call<Response> upload(
            @Part("image\"; filename=\"image.png\" ") ProgressRequestBody file,
            @Part("description") String description);


}
