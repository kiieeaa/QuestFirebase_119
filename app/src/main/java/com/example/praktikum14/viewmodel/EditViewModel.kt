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

}