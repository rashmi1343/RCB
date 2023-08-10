package com.app.rcb.api

import com.app.rcb.response.*
import com.google.gson.JsonObject
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*
import okhttp3.ResponseBody

import retrofit2.http.POST

import retrofit2.http.Multipart


interface ApiInterface {


    @Headers("Content-Type: application/json")
    @POST("login")
    fun getaccesstoken(
        @Query("email") email: String?,
        @Query("password") password: String?
    ): Call<RequestAccess>

    @POST("student")
    @FormUrlEncoded
    fun getotp(
        @Header("Authorization") mToken: String,
        @Field("student_username") sphone: String?,
        @Field("methodname") mname: String?
    ): Call<LoginResponse>


    /* @FormUrlEncoded
       @POST("data")
         @Headers("Content-Type: application/json")
      fun getauthenticate(@Header("Authorization")mToken:String,@Body requestBody:RequestBody) : Call<String>
 */
    @POST("student")
    @FormUrlEncoded
    fun getverifyotp(
        @Header("Authorization") mToken: String,
        @Field("methodname") methodname: String,
        @Field("studentid") sid: String?,
        @Field("otpentered") otp: String?
    ): Call<VerifyotpResponse>

    @POST("student")
    @FormUrlEncoded
    fun getdropdownvalue(
        @Header("Authorization") mToken: String,
        @Field("methodname") methodname: String
    ): Call<getdropdownvalue>

    @POST("student")
    @FormUrlEncoded
    fun getstudentbranch(
        @Header("Authorization") mToken: String,
        @Field("methodname") methodname: String,
        @Field("pm_center_id") centerid: String,
        @Field("bm_program_id") programid: String
    ): Call<getstudentbranch>


    @POST("student")
    @FormUrlEncoded
    fun getacadamicyear(
        @Header("Authorization") mToken: String,
        @Field("methodname") methodname: String
    ): Call<getacademicyear>

    @POST("student")
    @FormUrlEncoded
    fun getstudentprogram(
        @Header("Authorization") mToken: String,
        @Field("methodname") methodname: String,
        @Field("pm_center_id") centerid: String
    ): Call<getstudentprogram>

    @POST("student")
    @FormUrlEncoded
    fun getstudentcenter(
        @Header("Authorization") mToken: String,
        @Field("methodname") methodname: String
    ): Call<getstudentcenter>

    @POST("student")
    @FormUrlEncoded
    fun getstudentprofiledetail(
        @Header("Authorization") mToken: String,
        @Field("methodname") methodname: String,
        @Field("student_id") sid: String?
    ): Call<ProfileResponse>


    @POST("student")
    @FormUrlEncoded
    fun getstudentacademiccalendar(
        @Header("Authorization") mToken: String,
        @Field("methodname") methodname: String,
        @Field("student_id") sid: String?,
        @Field("semester_id") semid: Int,
        @Field("institution_id") insid: String?,
        @Field("program_id") prgmid: String?,
        @Field("academic_year_id") acadid: String?,
        @Field("session_id") sess_id: String?
    ): Call<getstudentcalendar>

    @POST("student")
    @FormUrlEncoded
    fun getstudentfees(
        @Header("Authorization") mToken: String,
        @Field("student_id") stdid: String,
        @Field("methodname") methodname: String
    ): Call<getfeedetail>

    @POST("student")
    @FormUrlEncoded
    fun getstudentassignment(
        @Header("Authorization") mToken: String,
        @Field("student_id") stdid: String,
        @Field("methodname") methodname: String,
        @Field("student_session_id") sess_id: Int
    ): Call<getassignment>

    @POST("student")
    @FormUrlEncoded
    fun getdownloadassignment(
        @Header("Authorization") mToken: String,
        @Field("methodname") methodname: String,
        @Field("student_id") stdid: String,
        @Field("student_session_id") sess_id: String
    ): Call<getdownloadassignment>


    @POST("student")
    @FormUrlEncoded
    fun getstates(
        @Header("Authorization") mToken: String,
        @Field("methodname") methodname: String
    ): Call<strstateresponse>

    @POST("student")
    @FormUrlEncoded
    fun getseason(
        @Header("Authorization") mToken: String,
        @Field("methodname") methodname: String
    ): Call<SeasonResponse>


    @POST("student")
    @FormUrlEncoded
    fun getsp(
        @Header("Authorization") mToken: String,
        @Field("methodname") methodname: String,
        @Field("sm_program_id") programid: String,
        @Field("sm_center_id") centerid: String,
        @Field("sm_branch_id") branchid: String
    ): Call<getspecialization>

    @POST("beta/api/student")
    @FormUrlEncoded
    fun uploadimg(
        @Header("Authorization") mToken: String,
        @Field("filename") filename: String,
        @Field("strbase64dta") strbase64: String,
        @Field("methodname") mname: String
    ): Call<String>


    @Multipart
    @POST("student")
    //fun calluploadfile(@Part filePart:MultipartBody.Part) : Call<String>
            /* fun calluploadfile(@Header("Authorization")mToken:String, @Field("type") type:String,@Field("methodname")methodname:String, @Field("filename")filename:String,
             @Part("file")file:RequestBody)
     */


    fun calluploadfile(
        @Header("Authorization") mToken: String,
        @Part("type") type: RequestBody,
        @Part("methodname") methodname: RequestBody,
        @Part("filename") filename: RequestBody,
        @Part image: MultipartBody.Part
    ): Call<String>


    @Multipart
    @POST("student")
    fun upldfile(
        @Query("Authorization") token: String?, @Query("type") type: String,
        @Query("methodname") methodname: String?, @Part file: MultipartBody.Part
    ): Call<ResponseBody?>?


    @POST("beta/api/student")
    public fun upload_image(@Body json: JsonObject): Call<String>

    data class CommonParam(val methodname: String)
    data class CommonParamOther(val methodname: String, val student_program: List<Int>)

    @Headers("Content-Type: application/json")
    @POST("student")
    suspend fun getstatesnew(
        @Header("Authorization") mToken: String,
        @Body request: CommonParam
    ): strstateresponse

    @POST("student")
    suspend fun getdropdownnew(
        @Header("Authorization") mToken: String,
        @Body request: CommonParam
    ): getdropdownvalue

    @POST("student")
    suspend fun getseasonnew(
        @Header("Authorization") mToken: String,
        @Body request: CommonParam
    ): SeasonResponse

    @POST("student")
    suspend fun getprogramnew(
        @Header("Authorization") mToken: String,
        @Body request: CommonParam
    ): getstudentprogram

    @POST("student")
    suspend fun getcenternew(
        @Header("Authorization") mToken: String,
        @Body request: CommonParam
    ): getstudentcenter

    @POST("student")
    suspend fun getbranchnew(
        @Header("Authorization") mToken: String,
        @Body request: CommonParam
    ): getstudentbranch

    @POST("student")
    suspend fun getaynew(
        @Header("Authorization") mToken: String,
        @Body request: CommonParam
    ): getacademicyear


    @POST("student")
    suspend fun getsp(
        @Header("Authorization") mToken: String,
        @Body request: CommonParam
    ): getspecialization


    @POST("student")
    suspend fun getholidayslist(
        @Header("Authorization") mToken: String,
        @Body request: CommonParamOther
    ): HolidaysResponse
}

data class param(val methodname: String, val student_phone: String)
