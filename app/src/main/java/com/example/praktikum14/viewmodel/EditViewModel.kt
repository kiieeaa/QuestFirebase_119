package com.example.praktikum14.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.praktikum14.modeldata.DetailSiswa
import com.example.praktikum14.modeldata.UIStateSiswa
import com.example.praktikum14.modeldata.toDataSiswa
import com.example.praktikum14.modeldata.toUiStateSiswa
import com.example.praktikum14.repositori.RepositorySiswa
import com.example.praktikum14.view.route.DestinasiEdit
import kotlinx.coroutines.launch


class EditViewModel(
    savedStateHandle: SavedStateHandle,
    private val repositorySiswa: RepositorySiswa
) : ViewModel() {
    var uiStateSiswa by mutableStateOf(UIStateSiswa())
        private set
    private val itemId: String = checkNotNull(savedStateHandle[DestinasiEdit.itemIdArg])
    init {
        viewModelScope.launch {
            uiStateSiswa = repositorySiswa.getSiswaById(itemId).toUiStateSiswa(true)
        }
    }
    fun updateUiState(detailSiswa: DetailSiswa) {
        uiStateSiswa = UIStateSiswa(
            detailSiswa = detailSiswa,
            isEntryValid = validasiInput(detailSiswa)
        )
    }
    private fun validasiInput(uiState: DetailSiswa = uiStateSiswa.detailSiswa): Boolean {
        return with(uiState) {
            nama.isNotBlank() && alamat.isNotBlank() && telpon.isNotBlank()
        }
    }

    fun updateSiswa() {
        viewModelScope.launch {
            if (validasiInput(uiStateSiswa.detailSiswa)) {

                repositorySiswa.postDataSiswa(uiStateSiswa.detailSiswa.toDataSiswa())
            }
        }
    }
}


