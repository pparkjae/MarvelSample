package com.example.marvelsample.di

import com.marvel.data.repository.CharacterLocalRepositoryImpl
import com.marvel.data.repository.CharacterRemoteRepositoryImpl
import com.marvel.domain.repository.CharacterLocalRepository
import com.marvel.domain.repository.CharacterRemoteRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
abstract class RepositoryModule {

    @Singleton
    @Binds
    abstract fun bindCharacterRemoteRepository(characterRemoteRepositoryImpl: CharacterRemoteRepositoryImpl): CharacterRemoteRepository

    @Singleton
    @Binds
    abstract fun bindCharacterLocalRepository(characterLocalRepositoryImpl: CharacterLocalRepositoryImpl): CharacterLocalRepository
}