package com.marvel.presentation.view.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.marvel.domain.entity.Result
import com.marvel.domain.entity.ResultStatus
import com.marvel.domain.usecase.CharacterRemoteUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class CharacterDetailViewModel @Inject constructor(
    private val characterRemoteUseCase: CharacterRemoteUseCase
) : ViewModel() {
    private val resultStatePrivate =
        MutableStateFlow<ResultStatus<Result>>(ResultStatus.Loading())
    val resultState = resultStatePrivate.asStateFlow()

    private val errorLiveDataPrivate = MutableLiveData<String>()
    val errorLiveData: LiveData<String> get() = errorLiveDataPrivate

    suspend fun requestCharactersId(id: Int) =
        characterRemoteUseCase.getCharacterId(id).stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = ResultStatus.Loading()
        ).collectLatest {
            when (it) {
                is ResultStatus.Success -> {
                    resultStatePrivate.value = ResultStatus.Success(it.data?.data?.results!![0])
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

    fun getImagePath(): String? {
        return if (resultState.value is ResultStatus.Success<*>) {
            resultState.value.data?.thumbnail?.path + "." + resultState.value.data?.thumbnail?.extension
        } else {
            null
        }
    }
}