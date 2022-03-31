package com.marvel.domain.usecase

import com.marvel.domain.entity.MarvelCharacterLocal
import com.marvel.domain.repository.CharacterLocalRepository
import com.marvel.domain.repository.CharacterRemoteRepository
import javax.inject.Inject

class CharacterLocalUseCase @Inject constructor(private val characterLocalRepository: CharacterLocalRepository) {
    fun getCharacter() = characterLocalRepository.getBookMark()
    suspend fun insertBookMark(marvelCharacterLocal: MarvelCharacterLocal) = characterLocalRepository.insertBookMark(marvelCharacterLocal)
    suspend fun deleteBookMark(id: Int) = characterLocalRepository.deleteBookMark(id)
}