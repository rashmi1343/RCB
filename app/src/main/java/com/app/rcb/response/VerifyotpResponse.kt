package com.app.rcb.response

import com.google.gson.annotations.SerializedName


data class VerifyotpResponse(

    @SerializedName("status")
    public var mstatus :String,

    @SerializedName("message")
    public var msg:String
)


data class customresponse(
    @SerializedName("status")
    public var mstatus :String,

    @SerializedName("message")
    public var msg:String
)