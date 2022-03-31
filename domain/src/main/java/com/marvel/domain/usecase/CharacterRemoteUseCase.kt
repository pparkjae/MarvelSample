package com.marvel.domain.usecase

import com.marvel.domain.repository.CharacterRemoteRepository
import javax.inject.Inject

class CharacterRemoteUseCase @Inject constructor(private val characterRemoteRepository: CharacterRemoteRepository) {
    suspend fun getCharacter(offset: Int, limit: Int) = characterRemoteRepository.getCharacter(offset, limit)
    suspend fun getCharacterId(id: Int) = characterRemoteRepository.getCharacterId(id)
}