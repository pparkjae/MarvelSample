package com.marvel.presentation.view.character

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.marvel.domain.entity.MarvelCharacterLocal
import com.marvel.domain.entity.Result
import com.marvel.domain.entity.ResultStatus
import com.marvel.domain.usecase.CharacterLocalUseCase
import com.marvel.domain.usecase.CharacterRemoteUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class CharacterViewModel @Inject constructor(
    private val characterRemoteUseCase: CharacterRemoteUseCase,
    private val characterLocalUseCase: CharacterLocalUseCase
) : ViewModel() {
    private val characterData = mutableListOf<Result>()

    private val resultStatePrivate =
        MutableStateFlow<ResultStatus<List<Result>>>(ResultStatus.Loading())
    val resultState = resultStatePrivate.asStateFlow()

    private val isEndPrivate = MutableLiveData(false)
    val isEnd: LiveData<Boolean> get() = isEndPrivate

    suspend fun getCharacterLocal() = characterLocalUseCase.getCharacter().stateIn(
        scope = viewModelScope
    )

    private val errorLiveDataPrivate = MutableLiveData<String>()
    val errorLiveData: LiveData<String> get() = errorLiveDataPrivate

    suspend fun requestCharacters(offset: Int, limit: Int = 20) =
        characterRemoteUseCase.getCharacter(offset, limit).stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = ResultStatus.Loading()
        ).collectLatest {
            when (it) {
                is ResultStatus.Success -> {
                    isEndPrivate.value = it.data?.data?.offset!! >= it.data?.data?.total!!

                    resultStatePrivate.value = ResultStatus.Success(it.data.run {
                        characterData.also { data ->

                            for (localCharacter in getCharacterLocal().value.data!!) {
                                for (result in this?.data?.results!!) {
                                    if (localCharacter.id == result.id) {
                                        result.isBookMark = true
                                        break
                                    }
                                }
                            }

                            data.addAll(this?.data?.results!!)
                        }
                    })
                }

                is ResultStatus.Loading -> {
                    resultStatePrivate.value = ResultStatus.Loading()
                }

                is ResultStatus.Error -> {
                    resultStatePrivate.value = ResultStatus.Error(it.throwable)
                    errorLiveDataPrivate.value = it.throwable?.message
                }
                else -> {}
            }
        }

    suspend fun addBookMark(result: Result) =
        characterLocalUseCase.insertBookMark(
            MarvelCharacterLocal(
                result.id,
                result.name,
                result.thumbnail.path + "." + result.thumbnail.extension
            )
        )


    suspend fun deleteBookMark(id: Int) {
        characterLocalUseCase.deleteBookMark(id)

        for (result in characterData) {
            if (id == result.id) {
                result.isBookMark = false
                break
            }
        }

        resultStatePrivate.value = ResultStatus.Success(characterData)
    }

}