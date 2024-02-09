package ru.ashirobokov.spotlight.ui

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import ru.ashirobokov.spotlight.game.MAX_NO_OF_WORDS
import ru.ashirobokov.spotlight.game.SCORE_INCREASE
import ru.ashirobokov.spotlight.model.Dictionary
import ru.ashirobokov.spotlight.model.Word

class DictionaryFragmentViewModel(application: Application) : AndroidViewModel(application) {
    private val TAG: String? = DictionaryFragmentViewModel::class.simpleName
    private val fileInString: String
        get() {
            return getApplication<Application>().applicationContext.assets.open("words by groups.json")
                .bufferedReader().use { it.readText() }
        }
    private lateinit var dictionary: Dictionary
    private lateinit var words: List<Word>

    private lateinit var currentWord: Word
    private var wordsList: MutableList<Word> = mutableListOf()

    private val _currentRussianWord = MutableLiveData<String>()
    val currentRussianWord: LiveData<String>
        get() = _currentRussianWord

    private val _currentWordCount = MutableLiveData<Int>(0)
    val currentWordCount: LiveData<Int>
        get() = _currentWordCount

    private val _score = MutableLiveData<Int>(0)
    val score: LiveData<Int>
        get() = _score

    private fun getNextWord() {
        currentWord = words.random()
        if (wordsList.contains(currentWord)) {
            getNextWord()
        } else {
            _currentRussianWord.value = currentWord.translation
            _currentWordCount.value = (_currentWordCount.value)?.inc()
            wordsList.add(currentWord)
        }
    }

    init {
        Log.d(TAG, "[MainActivityViewModel init] started ... ")
        Log.d(TAG, "[MainActivityViewModel init] File=" + fileInString)
        val moshi = Moshi.Builder()
            .addLast(KotlinJsonAdapterFactory())
            .build()
        val adapter: JsonAdapter<Dictionary> = moshi.adapter(Dictionary::class.java)
        this.dictionary = adapter.fromJson(fileInString)!!
        this.words = dictionary.words
        Log.d(TAG, "[getWords-from-dictionary] : " + words.toString())
        getNextWord()
    }

    private fun increaseScore() {
        _score.value = _score.value?.plus(SCORE_INCREASE)
    }

    fun nextWord(): Boolean {
        return if (_currentWordCount.value!! < MAX_NO_OF_WORDS) {
            getNextWord()
            true
        } else false
    }

    fun isUserWordCorrect(englishWord: String): Boolean {
        Log.d(TAG, "[fun isUserWordCorrect] englishWord = $englishWord")
        var wordCheck: Boolean = false

        if (currentWord.word.contains(",")) {
                val words: List<String> = currentWord.word.split(",").map { it.trim() }
                Log.d(TAG, "Количество слов в списке = ${words.size}")
                words.forEach({
                    if (englishWord.trim().lowercase().equals(it)) {
                        wordCheck = true
                    }
                }
            )
        } else {
            if (englishWord.trim().lowercase().equals(currentWord.word.lowercase())) {
                wordCheck = true
            }
        }
        if (wordCheck) {
            increaseScore()
        }
        return wordCheck
    }

    fun reinitializeData() {
        _score.value = 0
        _currentWordCount.value = 0
        wordsList.clear()
        getNextWord()
    }

    override fun onCleared() {
        super.onCleared()
        Log.d(TAG, "View Model destroyed")
    }

}