package com.app.rcb.response

import com.app.rcb.db.entity.Season

data class strSeasonResponse(var season_id:Int, var season_name:String, var season_description:String, var season_start_month:String, var season_end_month:String, var season_status:Int, var season_duration:String)

data class SeasonResponse(var status:Int, var message:String, var Season:ArrayList<Season>)

