package com.example.carypark.models.Service

import com.example.carypark.models.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

interface RetrofitApi {

//lokza taa NormalUser

    @POST("api/user/login")
    fun userLogin(
        @Body user: UserResetPassword
    ):Call<User>


    @Multipart
    @POST("api/user/signup")
    fun userSingup(
        @PartMap data : LinkedHashMap<String, RequestBody>,
        @Part profilePicture: MultipartBody.Part
    ):Call<User>

    @GET("api/user/{id}")
    fun GetAllUser(@Path("id")id:String): Call<List<User>>

    @DELETE("api/user/{id}")
    fun deleteuser(@Path("id")id:String): Call<User>

    @POST("/api/user/forgotPassword")
    fun sendResetCode(
        @Body user: UserReset
    ):Call<UserResetResponse>

    @PUT("/api/user/editPassword")
    fun changePasswordReset(
        @Body user:UserResetPassword):Call<User>

    @PUT("/api/user/editProfile")
    fun updateProfile(
        @PartMap data : LinkedHashMap<String, RequestBody>,
        ):Call<User>
    @Multipart
    @PUT("/api/user/editProfilePic")
    fun updateProfilepic(
        @Part profilePicture: MultipartBody.Part
    ):Call<User>

   //********************* ParkOwner
    @GET("api/parking/park/{id}")
    fun GetMyParkings(@Path("id")id:String): Call<List<Parking>>

    @GET("api/parking")
    fun GetAllParkings(): Call<List<Parking>>

   @GET("/api/reservation/ownermy/{id}")
   fun incoming(@Path("id") id: String):Call<List<Reservation>>



    @POST("api/parking/")
    fun addpark(
        @Body parking: Parking
    ):Call<Parking>




    //*************************user reservation
    @GET("api/reservation/{id}")
    fun GetAllReservation(@Path("id")id:String): Call<List<Reservation>>

    @POST("api/reservation/")
    fun addReservation(
        @Body reservation: ReservationParking
    ):Call<Reservation>
    @DELETE("/api/reservation/delete/{id}")
    fun delete(
        @Path("id")id:String
    ):Call<Reservation>

    companion object {
        fun create() : RetrofitApi {
            val retrofit = Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl("https://carypark-backend.herokuapp.com/")
                .build()
            return retrofit.create(RetrofitApi::class.java)

        }
    }

}