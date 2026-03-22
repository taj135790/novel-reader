package com.novel.reader

import android.content.Context
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.InputStreamReader

class NovelViewModel : ViewModel() {
    
    private val _novelData = MutableLiveData<Novel?>()
    val novelData: LiveData<Novel?> = _novelData
    
    private val _toastMessage = MutableLiveData<String?>()
    val toastMessage: LiveData<String?> = _toastMessage
    
    var currentPage = 0
        private set
    
    var isTtsPlaying = false
        private set
    
    private var fontSize = 16f
    private var lineSpacing = 1.5f
    private var letterSpacing = 0.05f
    
    suspend fun importNovelFromUri(context: Context, uri: Uri) {
        try {
            val content = withContext(Dispatchers.IO) {
                context.contentResolver.openInputStream(uri)?.use { inputStream ->
                    BufferedReader(InputStreamReader(inputStream, "UTF-8")).use { reader ->
                        reader.readText()
                    }
                }
            }
            
            content?.let { text ->
                val novel = NovelParser.parseText(text)
                _novelData.postValue(novel)
                _toastMessage.postValue(context.getString(R.string.msg_import_success))
            } ?: run {
                _toastMessage.postValue(context.getString(R.string.msg_import_failed))
            }
        } catch (e: Exception) {
            e.printStackTrace()
            _toastMessage.postValue(context.getString(R.string.msg_import_failed))
        }
    }
    
    fun setCurrentPage(page: Int) {
        currentPage = page
    }
    
    fun getCurrentChapter(): Chapter? {
        return _novelData.value?.chapters?.getOrNull(currentPage)
    }
    
    fun setTtsPlaying(playing: Boolean) {
        isTtsPlaying = playing
    }
    
    fun updateReadingSettings(fontSize: Float, lineSpacing: Float, letterSpacing: Float) {
        this.fontSize = fontSize
        this.lineSpacing = lineSpacing
        this.letterSpacing = letterSpacing
    }
    
    fun getReadingSettings(): ReadingSettings {
        return ReadingSettings(fontSize, lineSpacing, letterSpacing)
    }
    
    fun clearToastMessage() {
        _toastMessage.value = null
    }
}

data class Novel(
    val title: String,
    val chapters: List<Chapter>
)

data class Chapter(
    val title: String,
    val content: String
)

data class ReadingSettings(
    val fontSize: Float,
    val lineSpacing: Float,
    val letterSpacing: Float
)