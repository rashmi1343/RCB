package com.app.rcb.response


import com.google.gson.annotations.SerializedName


data class HolidaysResponse(

    var status: Int? ,
  var message: String? ,
   var holiday: ArrayList<Holiday>

)

data class Holidaytype(

    var ht_id: Int?,
    var ht_name: String? ,
    var ht_color: String? ,
    var ht_status: Int? ,
    var created_at: String?,
    var updated_at: String?

)


data class Holiday(

  var id: Int ,
  var program_id: String? ,
   var holiday_name: String? ,
   var holiday_type_id: Int?,
   var holidays_date: String?,
   var holidays_status: Int? ,
   var add_by: Int? ,
  var holidaytype: Holidaytype?

)