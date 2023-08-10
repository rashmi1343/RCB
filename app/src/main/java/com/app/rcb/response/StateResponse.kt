package com.app.rcb.response

import com.app.rcb.db.entity.State


data class strstateresponse(var status:Int, var message:String, var States:List<State>) {}


data class StateResponse(var id:Int, var name:String, var country_id:Int) {
}


