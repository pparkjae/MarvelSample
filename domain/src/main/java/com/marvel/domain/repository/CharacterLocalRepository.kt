package com.marvel.domain.repository

import com.marvel.domain.entity.MarvelCharacterLocal
import com.marvel.domain.entity.ResultStatus
import kotlinx.coroutines.flow.Flow

interface CharacterLocalRepository {
    fun getBookMark() : Flow<ResultStatus<List<MarvelCharacterLocal>>>
    suspend fun insertBookMark(marvelCharacterLocal: MarvelCharacterLocal) : Long
    suspend fun deleteBookMark(id: Int) : Int
}