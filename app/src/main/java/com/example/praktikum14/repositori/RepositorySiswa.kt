package com.example.praktikum14.repositori

import com.example.praktikum14.modeldata.Siswa
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

interface RepositorySiswa {
    suspend fun getDataSiswa(): List<Siswa>
    suspend fun postDataSiswa(siswa: Siswa)

    suspend fun deleteSiswa(siswa: Siswa)
    suspend fun getSiswaById(id: String): Siswa
}

class FirebaseRepositorySiswa : RepositorySiswa {
    private val db = FirebaseFirestore.getInstance()
    private val collection = db.collection("siswa")

    override suspend fun getDataSiswa(): List<Siswa> {
        return try {

            collection.get().await().documents.map { doc ->
                Siswa(
                    id = doc.id, // AMBIL ID DARI DOKUMEN (KOLOM TENGAH)
                    nama = doc.getString("nama") ?: "",
                    alamat = doc.getString("alamat") ?: "",
                    telpon = doc.getString("telpon") ?: ""
                )
            }
        } catch (e: Exception) {
            emptyList()
        }
    }

    override suspend fun getSiswaById(id: String): Siswa {
        return try {
            val doc = collection.document(id).get().await()
            Siswa(
                id = doc.id,
                nama = doc.getString("nama") ?: "",
                alamat = doc.getString("alamat") ?: "",
                telpon = doc.getString("telpon") ?: ""
            )
        } catch (e: Exception) {
            Siswa()
        }
    }

    override suspend fun postDataSiswa(siswa: Siswa) {

        val docRef = if (siswa.id.isEmpty()) {
            collection.document() // Buat dokumen baru dengan ID otomatis
        } else {
            collection.document(siswa.id) // Referensi ke dokumen yang sudah ada
        }

        val data = hashMapOf(

            "nama" to siswa.nama,
            "alamat" to siswa.alamat,
            "telpon" to siswa.telpon
        )

        docRef.set(data).await()
    }

    override suspend fun deleteSiswa(siswa: Siswa) {
        try {
            collection.document(siswa.id).delete().await()
        } catch (e: Exception) {
            throw Exception("Gagal menghapus data siswa: ${e.message}")
        }
    }
}