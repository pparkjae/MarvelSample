package com.marvel.data.api

import com.marvel.data.model.MarvelCharacterResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MarvelApi {

    @GET("/v1/public/characters")
    suspend fun getCharacter(
        @Query("offset") offset: Int = 0,
        @Query("limit") limit: Int = 20
    ): MarvelCharacterResponse

    @GET("/v1/public/characters/{id}")
    suspend fun getCharacterId(
        @Path("id") id: Int
    ): MarvelCharacterResponse
}