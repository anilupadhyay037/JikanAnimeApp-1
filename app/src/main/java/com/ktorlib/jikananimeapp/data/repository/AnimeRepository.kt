package com.ktorlib.jikananimeapp.data.repository

import com.ktorlib.jikananimeapp.data.local.AnimeDao
import com.ktorlib.jikananimeapp.data.local.AnimeEntity
import com.ktorlib.jikananimeapp.data.remote.JikanService

class AnimeRepository(
    private val api: JikanService,
    private val dao: AnimeDao
) {

    val anime = dao.getAll()

    suspend fun refresh(): Result<Unit> {
        return try {
            val response =  api.getTopAnime()
            val list = response.data.map {
                AnimeEntity(
                    id = it.mal_id,
                    title = it.title,
                    episodes = it.episodes,
                    score = it.score,
                    poster = it.images.jpg.image_url
                )
            }
            dao.insertAll(list)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getDetails(id: Int) = api.getAnimeDetails(id).data
}