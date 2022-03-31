package com.marvel.data.repository

import com.marvel.data.mapper.CharacterEntityMapper
import com.marvel.data.model.CharacterDao
import com.marvel.domain.entity.MarvelCharacterLocal
import com.marvel.domain.entity.ResultStatus
import com.marvel.domain.repository.CharacterLocalRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class CharacterLocalRepositoryImpl @Inject constructor(
    private val characterDao: CharacterDao
) : CharacterLocalRepository {

    override fun getBookMark() = flow {
        try {
            val result = characterDao.getCharacter()
            emit(ResultStatus.Success(CharacterEntityMapper.mapperToCharacterLocal(result)))
        } catch (e: Exception) {
            emit(ResultStatus.Error(e))
        }
    }.catch { e ->
        emit(ResultStatus.Error(e))
    }.flowOn(Dispatchers.IO)

    override suspend fun insertBookMark(marvelCharacterLocal: MarvelCharacterLocal) =
        characterDao.insert(CharacterEntityMapper.mapperToCharacterTable(marvelCharacterLocal))

    override suspend fun deleteBookMark(id: Int) = characterDao.delete(id)
}