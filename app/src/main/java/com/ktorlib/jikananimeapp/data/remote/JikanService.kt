package com.ktorlib.jikananimeapp.data.remote

import com.ktorlib.jikananimeapp.data.remote.models.AnimeDetailResponse
import com.ktorlib.jikananimeapp.data.remote.models.TopAnimeResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface JikanService {


    @GET("top/anime")
    suspend fun getTopAnime(): TopAnimeResponse

    @GET("anime/{id}")
    suspend fun getAnimeDetails(@Path("id") id: Int): AnimeDetailResponse
}
