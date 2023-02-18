package com.perco.interview.feature.main.repo

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class GamesResponse(@Json(name = "response") val games: List<GameResponse>)

@JsonClass(generateAdapter = true)
data class GameResponse(
    @Json(name = "WSCGameId") val wscGameId: String,
    @Json(name = "wscGame") val wscGame: WscGame?,
    @Json(name = "teams") val teams: Teams
)

@JsonClass(generateAdapter = true)
data class Teams(
    @Json(name = "home") val home: Team,
    @Json(name = "away") val away: Team
) {
    @JsonClass(generateAdapter = true)
    data class Team(@Json(name = "name") val name: String)
}

@JsonClass(generateAdapter = true)
data class WscGame(@Json(name = "primeStory") val primeStory: PrimeStory?) {

    @JsonClass(generateAdapter = true)
    data class PrimeStory(@Json(name = "pages") val pages: List<Page>?) {

        @JsonClass(generateAdapter = true)
        data class Page(
            @Json(name = "homeScore") val homeScore: Int?,
            @Json(name = "awayScore") val awayScore: Int?
        )
    }
}