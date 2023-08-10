package com.app.comissionflow.Activity.Response

import com.google.gson.annotations.SerializedName

class DocumentResponse
{
    @SerializedName("data")
    public val mdata:dataobject? = null




   /* @SerializedName("documentId")
    public val mDocumentId:String? = null*/
}


class dataobject {

    @SerializedName("success")
    public val msucess:String? = null

    @SerializedName("message")
    public val msg:String? = null


}


