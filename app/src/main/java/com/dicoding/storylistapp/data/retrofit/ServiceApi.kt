package com.dicoding.storylistapp.data.retrofit

import com.dicoding.storylistapp.data.response.LoginResponse
import com.dicoding.storylistapp.data.response.RegisterResponse
import com.dicoding.storylistapp.data.response.StoriesResponse
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface ServiceApi {
    @FormUrlEncoded
    @POST("register")
    suspend fun resgister(
        @Field("name") name : String,
        @Field("email") email : String,
        @Field("password") password: String
    ): RegisterResponse

    @FormUrlEncoded
    @POST("login")
    suspend fun Login(
        @Field("email") email: String,
        @Field("password") password: String
    ): LoginResponse


    @GET("stories")
    suspend fun getStories(
        @Header("Authorization") token: String
    ): StoriesResponse

    @Multipart
    @POST("stories")
    suspend fun postStories(
        @Header("Authorization") token: String
        @Part file: Multiplayer.Part,
        @Part("description") description: RequestBody,
        @Part("lat") lat: RequestBody? = null,
        @Part("lon") lon: RequestBody? = null
    ): AddResponse
}