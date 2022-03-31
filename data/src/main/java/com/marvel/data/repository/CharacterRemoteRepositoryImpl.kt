package com.marvel.data.repository

import com.marvel.data.api.MarvelApi
import com.marvel.data.exception.NetworkException
import com.marvel.data.mapper.CharacterEntityMapper
import com.marvel.domain.entity.ResultStatus
import com.marvel.domain.repository.CharacterRemoteRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class CharacterRemoteRepositoryImpl @Inject constructor(
    private val marvelApi: MarvelApi
) : CharacterRemoteRepository {

    override suspend fun getCharacter(offset: Int, limit: Int) = flow {
        val response = marvelApi.getCharacter(offset = (offset * limit), limit = limit)

        if (response.code == 200 &&  "Ok" == response.status) {
            emit(ResultStatus.Success(CharacterEntityMapper.mapperToCharacter(response)))
        } else {
            emit(ResultStatus.Error(NetworkException()))
        }
    }.catch { e ->
        emit(ResultStatus.Error(e))
    }.flowOn(Dispatchers.IO)

    override suspend fun getCharacterId(id: Int) = flow {
        val response = marvelApi.getCharacterId(id)

        if (response.code == 200 &&  "Ok" == response.status) {
            emit(ResultStatus.Success(CharacterEntityMapper.mapperToCharacter(response)))
        } else {
            emit(ResultStatus.Error(NetworkException()))
        }
    }.catch { e ->
        emit(ResultStatus.Error(e))
    }.flowOn(Dispatchers.IO)
}