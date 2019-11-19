package org.techtown.kotlin_todolist

import org.techtown.kotlin_todolist.Retrofit.Request.LoginRequest
import org.techtown.kotlin_todolist.Retrofit.Request.RegisterRequest
import org.techtown.kotlin_todolist.Retrofit.Response.LoginResponse
import org.techtown.kotlin_todolist.Retrofit.Response.RegisterUserResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface RetrofitService {
    @Headers("Accept: application/json")
    @POST("/users/")    //사용자의 id,username을 반환
    fun registerUser(@Body user : RegisterRequest) : Call<RegisterUserResponse>

    @Headers("Accept: application/json")
    @POST("/api-token-auth/")
    fun login(@Body user : LoginRequest) : Call<LoginResponse>
}