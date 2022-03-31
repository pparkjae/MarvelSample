package com.marvel.domain.repository

import com.marvel.domain.entity.MarvelCharacter
import com.marvel.domain.entity.ResultStatus
import kotlinx.coroutines.flow.Flow

interface CharacterRemoteRepository {
    suspend fun getCharacter(offset: Int, limit: Int) : Flow<ResultStatus<MarvelCharacter>>
    suspend fun getCharacterId(id: Int) : Flow<ResultStatus<MarvelCharacter>>
}