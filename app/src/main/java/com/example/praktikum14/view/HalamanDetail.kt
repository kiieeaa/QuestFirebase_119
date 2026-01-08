package com.example.praktikum14.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.praktikum14.R
import com.example.praktikum14.modeldata.Siswa
import com.example.praktikum14.view.route.DestinasiDetail
import com.example.praktikum14.viewmodel.DetailUiState
import com.example.praktikum14.viewmodel.DetailViewModel
import com.example.praktikum14.viewmodel.PenyediaViewModel

// DetailSiswaScreen merupakan composable utama untuk menampilkan detail data siswa
// serta menyediakan navigasi kembali dan tombol edit data.
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailSiswaScreen(
    navigateBack: () -> Unit,
    navigateToEditItem: (String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: DetailViewModel = viewModel(factory = PenyediaViewModel.Factory)
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    Scaffold(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            SiswaTopAppBar(
                title = stringResource(DestinasiDetail.titleRes),
                canNavigateBack = true,
                scrollBehavior = scrollBehavior,
                navigateUp = navigateBack
            )
        },
        floatingActionButton = {
            // FloatingActionButton digunakan untuk navigasi ke halaman edit siswa
            FloatingActionButton(
                onClick = {
                    val state = viewModel.detailUiState
                    if (state is DetailUiState.Success) {
                        navigateToEditItem(state.siswa.id)
                    }
                },
                shape = MaterialTheme.shapes.medium,
                modifier = Modifier.padding(dimensionResource(id = R.dimen.padding_large))
            ) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = stringResource(R.string.edit_siswa)
                )
            }
        }
    ) { innerPadding ->
        DetailBody(
            detailUiState = viewModel.detailUiState,
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxWidth(),
            onDelete = {
                viewModel.deleteSiswa()
                navigateBack()
            }
        )
    }
}

// DetailBody menampilkan isi halaman berdasarkan state data (Loading, Error, atau Success)
@Composable
fun DetailBody(
    detailUiState: DetailUiState,
    modifier: Modifier = Modifier,
    onDelete: () -> Unit
) {
    Column(
        modifier = modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        when (detailUiState) {
            is DetailUiState.Loading -> Text("Loading...")
            is DetailUiState.Error -> Text("Error data tidak ditemukan")
            is DetailUiState.Success -> {
                ItemDetailSiswa(
                    siswa = detailUiState.siswa,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.padding(8.dp))

                // State untuk mengontrol dialog konfirmasi hapus data
                var deleteConfirmationRequired by rememberSaveable { mutableStateOf(false) }

                OutlinedButton(
                    onClick = { deleteConfirmationRequired = true },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(stringResource(R.string.delete))
                }

                if (deleteConfirmationRequired) {
                    DeleteConfirmationDialog(
                        onDeleteConfirm = {
                            deleteConfirmationRequired = false
                            onDelete()
                        },
                        onDeleteCancel = { deleteConfirmationRequired = false }
                    )
                }
            }
        }
    }
}

// ItemDetailSiswa berfungsi menampilkan informasi detail siswa dalam bentuk Card
@Composable
fun ItemDetailSiswa(
    siswa: Siswa,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            contentColor = MaterialTheme.colorScheme.onPrimaryContainer
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(dimensionResource(id = R.dimen.padding_medium)),
            verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.padding_medium))
        ) {
            ComponentDetailSiswa(label = stringResource(R.string.nama), value = siswa.nama)
            ComponentDetailSiswa(label = stringResource(R.string.alamat), value = siswa.alamat)
            ComponentDetailSiswa(label = stringResource(R.string.telpon), value = siswa.telpon)
        }
    }
}

// ComponentDetailSiswa digunakan sebagai komponen reusable untuk menampilkan label dan nilai data siswa
@Composable
fun ComponentDetailSiswa(
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
private fun DeleteConfirmationDialog(
    onDeleteConfirm: () -> Unit,
    onDeleteCancel: () -> Unit,
    modifier: Modifier = Modifier
) {
    // Dialog konfirmasi untuk memastikan pengguna yakin sebelum menghapus data siswa
    AlertDialog(
        onDismissRequest = { /* Do nothing */ },
        title = { Text(stringResource(R.string.attention)) },
        text = { Text(stringResource(R.string.delete)) },
        modifier = modifier,
        dismissButton = {
            TextButton(onClick = onDeleteCancel) {
                Text(text = stringResource(R.string.no))
            }
        },
        confirmButton = {
            TextButton(onClick = onDeleteConfirm) {
                Text(text = stringResource(R.string.yes))
            }
        }
    )
}
