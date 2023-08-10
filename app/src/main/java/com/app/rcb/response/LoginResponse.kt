package com.app.rcb.response

import com.google.gson.annotations.SerializedName

data class LoginResponse(

    /*@SerializedName("token")
    public var mtoken:String,*/

    @SerializedName("status")
    public var mstatus :String,

    @SerializedName("message")
    public var msg:String,

    /*@SerializedName("otp")
    public var otp:String,
*/
    @SerializedName("studentid")
    public var studentid:String?

    )

