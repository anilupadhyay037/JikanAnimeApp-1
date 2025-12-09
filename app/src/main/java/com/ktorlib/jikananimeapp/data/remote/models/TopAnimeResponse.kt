package com.ktorlib.jikananimeapp.data.remote.models

data class TopAnimeResponse(val data: List<AnimeDto>)

data class AnimeDto(
    val mal_id: Int,
    val title: String,
    val episodes: Int?,
    val score: Double?,
    val images: Images
)

data class Images(val jpg: Jpg)
data class Jpg(val image_url: String)

data class AnimeDetailResponse(val data: AnimeDetailDto)

data class AnimeDetailDto(
    val title: String,
    val synopsis: String?,
    val episodes: Int?,
    val score: Double?,
    val genres: List<Genre>,
    val trailer: Trailer?
)

data class Genre(val name: String)
data class Trailer(val url: String?)