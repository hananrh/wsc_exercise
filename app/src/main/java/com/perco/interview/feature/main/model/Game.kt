package com.perco.interview.feature.main.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Games(val gameList: List<Game>)

@JsonClass(generateAdapter = true)
data class Game(
    @Json(name = "id") val id: String,
    @Json(name = "homeTeam") val homeTeam: String,
    @Json(name = "awayTeam") val awayTeam: String,
    @Json(name = "homeScore") val homeScore: Int,
    @Json(name = "awayScore") val awayScore: Int
)