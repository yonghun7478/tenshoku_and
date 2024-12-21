package com.example.tokitoki.utils

import android.content.ContentResolver
import android.net.Uri
import java.io.File
import javax.inject.Inject

class FileManager @Inject constructor(
    private val contentResolver: ContentResolver,
    private val cacheDir: File,
    private val externalFilesDir: File?
) {
    /**
     * 내부 저장소(cacheDir)에 Uri로부터 파일 저장
     */
    fun saveUriToInternalCache(uri: Uri): String? {
        return saveUriToFile(uri, cacheDir)
    }

    /**
     * 외부 저장소(externalFilesDir)에 Uri로부터 파일 저장
     */
    fun saveUriToExternalStorage(uri: Uri): String? {
        return externalFilesDir?.let { dir -> saveUriToFile(uri, dir) }
    }

    /**
     * Uri를 지정된 디렉터리에 파일로 저장
     */
    private fun saveUriToFile(uri: Uri, directory: File): String? {
        return try {
            val tempFile = File(directory, "profile_image_${System.currentTimeMillis()}.jpg")
            val inputStream = contentResolver.openInputStream(uri)
            inputStream?.use { stream ->
                tempFile.outputStream().use { output ->
                    stream.copyTo(output)
                }
            }
            tempFile.absolutePath
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}