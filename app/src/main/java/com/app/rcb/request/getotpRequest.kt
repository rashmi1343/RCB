package com.app.rcb.request

import com.app.comissionflow.Activity.Response.DocumentResponse
import com.app.rcb.response.LoginResponse
import com.google.gson.JsonObject
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*


interface getotpRequest
{
    @Headers("Content-Type: application/json")
    @POST("student")
    public fun getotp(@Body json: JsonObject): Call<LoginResponse>
}


interface AllParamRequest
{

    @Multipart
    @Headers("Content-Type: application/json")
    @POST("upload")
    public fun uploadcertificate(@Header("Authorization")mToken: String, @Part("methodname")methodname:String, @Part("type")mDocType: String,
                              @Part file: MultipartBody.Part):Call<String>


    @POST("student")
    fun uploadMultiFile(@Body file: RequestBody?): Call<ResponseBody?>?


    @Multipart
    @POST("student")
    fun uploadkycdoc(@Header("Authorization") token: String?, @Part file: MultipartBody.Part?, @PartMap partMap: Map<String?, RequestBody?>?): Call<String?>?





}