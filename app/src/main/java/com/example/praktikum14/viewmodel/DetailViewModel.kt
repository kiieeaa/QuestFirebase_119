package com.example.praktikum14.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.praktikum14.modeldata.Siswa
import com.example.praktikum14.repositori.RepositorySiswa
import com.example.praktikum14.view.route.DestinasiDetail
import kotlinx.coroutines.launch


sealed interface DetailUiState {
    data class Success(val siswa: Siswa) : DetailUiState
    object Error : DetailUiState
    object Loading : DetailUiState
}

class DetailViewModel(
    savedStateHandle: SavedStateHandle,
    private val repositorySiswa: RepositorySiswa
) : ViewModel() {

    private val _idSiswa: String = checkNotNull(savedStateHandle[DestinasiDetail.itemIdArg])
    var detailUiState: DetailUiState by mutableStateOf(DetailUiState.Loading)
        private set
    init {
        getSiswaById()
    }
    fun getSiswaById() {
        viewModelScope.launch {
            detailUiState = DetailUiState.Loading
            detailUiState = try {
                val siswa = repositorySiswa.getSiswaById(_idSiswa)
                DetailUiState.Success(siswa)
            } catch (e: Exception) {
                DetailUiState.Error
            }
        }
    }

}
