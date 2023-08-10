package com.app.rcb.response

import com.google.gson.annotations.SerializedName


data class RequestAccess (

    @SerializedName("status")
    public var status:String,

    @SerializedName("message")
    public var message:String,

    @SerializedName("access_token")
    public var access_token:String


)



