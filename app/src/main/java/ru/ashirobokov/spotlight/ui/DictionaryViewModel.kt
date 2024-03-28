package ru.ashirobokov.spotlight.ui

import android.app.Application
import android.util.Log
import android.view.View
import android.widget.CheckBox
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import ru.ashirobokov.spotlight.R
import ru.ashirobokov.spotlight.game.MAX_NO_OF_WORDS
import ru.ashirobokov.spotlight.game.SCORE_INCREASE
import ru.ashirobokov.spotlight.model.Dictionary
import ru.ashirobokov.spotlight.model.Word
import kotlin.math.roundToInt

class DictionaryViewModel(application: Application) : AndroidViewModel(application) {
    private val TAG: String? = DictionaryViewModel::class.simpleName
    private val appContext = getApplication<Application>().applicationContext

    private val fileInString: String
        get() {
            return getApplication<Application>().applicationContext.assets.open("words by groups.json")
                .bufferedReader().use { it.readText() }
        }
    private lateinit var dictionary: Dictionary
    private lateinit var words: List<Word>

    private lateinit var currentWord: Word

    private var wordsList: MutableList<Word> = mutableListOf()

    private var categoryFilterList: MutableList<String> = mutableListOf()

    private val _currentRussianWord = MutableLiveData<String>()
    val currentRussianWord: LiveData<String>
        get() = _currentRussianWord

    private val _currentWordCount = MutableLiveData<Int>(0)
    val currentWordCount: LiveData<Int>
        get() = _currentWordCount

    private val _score = MutableLiveData<Int>(0)
    val score: LiveData<Int>
        get() = _score

    private val _attempts = MutableLiveData<Int>(0)
    val attempts: LiveData<Int>
        get() = _attempts

    /**
     * Block of check box states
     */
    private val _all = MutableLiveData<Boolean>(false)
    val all: LiveData<Boolean>
        get() = _all

    private val _verb = MutableLiveData<Boolean>(false)
    val verb: LiveData<Boolean>
        get() = _verb

    private val _cottage = MutableLiveData<Boolean>(false)
    val cottage: LiveData<Boolean>
        get() = _cottage

    private val _day_of_week = MutableLiveData<Boolean>(false)
    val day_of_week: LiveData<Boolean>
        get() = _day_of_week

    private val _home = MutableLiveData<Boolean>(false)
    val home: LiveData<Boolean>
        get() = _home

    private val _food = MutableLiveData<Boolean>(false)
    val food: LiveData<Boolean>
        get() = _food

    private val _animal = MutableLiveData<Boolean>(false)
    val animal: LiveData<Boolean>
        get() = _animal

    private val _toy = MutableLiveData<Boolean>(false)
    val toy: LiveData<Boolean>
        get() = _toy

    private val _cloth = MutableLiveData<Boolean>(false)
    val cloth: LiveData<Boolean>
        get() = _cloth

    private val _single_many = MutableLiveData<Boolean>(false)
    val single_many: LiveData<Boolean>
        get() = _single_many

    private val _season_weather = MutableLiveData<Boolean>(false)
    val season_weather: LiveData<Boolean>
        get() = _season_weather

    private val _family = MutableLiveData<Boolean>(false)
    val family: LiveData<Boolean>
        get() = _family

    private val _color = MutableLiveData<Boolean>(false)
    val color: LiveData<Boolean>
        get() = _color

    private val _speech_part = MutableLiveData<Boolean>(false)
    val speech_part: LiveData<Boolean>
        get() = _speech_part

    private val _body = MutableLiveData<Boolean>(false)
    val body: LiveData<Boolean>
        get() = _body

    private val _number = MutableLiveData<Boolean>(false)
    val number: LiveData<Boolean>
        get() = _number

    private val _school = MutableLiveData<Boolean>(false)
    val school: LiveData<Boolean>
        get() = _school

    private val _school_subj = MutableLiveData<Boolean>(false)
    val school_subj: LiveData<Boolean>
        get() = _school_subj

    /**
     *  End of block check boxes states
     */

    fun processCategory(category: String, check: Boolean) {
        var res = false
        if (check) {
            if (category.equals("all")) {
                categoryFilterList.clear()
                _all.value = true; _verb.value = false; _cottage.value = false; _day_of_week.value = false;
                _home.value = false; _food.value = false; _animal.value = false; _toy.value = false;
                _cloth.value = false; _single_many.value = false; _season_weather.value = false;
                _family.value = false; _color.value = false; _speech_part.value =false; _body.value = false;
                _number.value = false; _school.value = false; _school_subj.value =false;
            } else {
                res = categoryFilterList.add(category)
                when (category) {
                    appContext.resources.getString(R.string.verb_category) -> {
                        _verb.value = true
                        _all.value = false
                        Log.d(TAG, "_verb set to true")
                    }

                    appContext.resources.getString(R.string.cottage_category) -> {
                        _cottage.value = true
                        _all.value = false
                        Log.d(TAG, "_cottage set to true")
                    }

                    appContext.resources.getString(R.string.day_of_week_category) -> {
                        _day_of_week.value = true
                        _all.value = false
                        Log.d(TAG, "_day_of_week set to true")
                    }

                    appContext.resources.getString(R.string.home_category) -> {
                        _home.value = true
                        _all.value = false
                        Log.d(TAG, "_home set to true")
                    }

                    appContext.resources.getString(R.string.food_category) -> {
                        _food.value = true
                        _all.value = false
                        Log.d(TAG, "_food set to true")
                    }

                    appContext.resources.getString(R.string.animal_category) -> {
                        _animal.value = true
                        _all.value = false
                        Log.d(TAG, "_animal set to true")
                    }

                    appContext.resources.getString(R.string.toy_category) -> {
                        _toy.value = true
                        _all.value = false
                        Log.d(TAG, "_toy set to true")
                    }

                    appContext.resources.getString(R.string.cloth_category) -> {
                        _cloth.value = true
                        _all.value = false
                        Log.d(TAG, "_cloth set to true")
                    }

                    appContext.resources.getString(R.string.single_many_category) -> {
                        _single_many.value = true
                        _all.value = false
                        Log.d(TAG, "_single_many set to true")
                    }

                    appContext.resources.getString(R.string.season_weather_category) -> {
                        _season_weather.value = true
                        _all.value = false
                        Log.d(TAG, "_season_weather set to true")
                    }

                    appContext.resources.getString(R.string.family_category) -> {
                        _family.value = true
                        _all.value = false
                        Log.d(TAG, "_family set to true")
                    }

                    appContext.resources.getString(R.string.color_category) -> {
                        _color.value = true
                        _all.value = false
                        Log.d(TAG, "_color set to true")
                    }

                    appContext.resources.getString(R.string.speech_part_category) -> {
                        _speech_part.value = true
                        _all.value = false
                        Log.d(TAG, "_speech_part set to true")
                    }

                    appContext.resources.getString(R.string.body_category) -> {
                        _body.value = true
                        _all.value = false
                        Log.d(TAG, "_body set to true")
                    }
                    appContext.resources.getString(R.string.number_category) -> {
                        _number.value = true
                        _all.value = false
                        Log.d(TAG, "_number set to true")
                    }
                    appContext.resources.getString(R.string.school_category) -> {
                        _school.value = true
                        _all.value = false
                        Log.d(TAG, "_school set to true")
                    }

                    appContext.resources.getString(R.string.school_subj_category) -> {
                        _school_subj.value = true
                        _all.value = false
                        Log.d(TAG, "_school_subj set to true")
                    }

                }
            }
        } else {
            res = categoryFilterList.remove(category)
            when (category) {
                appContext.resources.getString(R.string.verb_category) -> {
                    _verb.value = false
                    Log.d(TAG, "_verb set to false")
                }

                appContext.resources.getString(R.string.cottage_category) -> {
                    _cottage.value = false
                    Log.d(TAG, "_cottage set to false")
                }

                appContext.resources.getString(R.string.day_of_week_category) -> {
                    _day_of_week.value = false
                    Log.d(TAG, "_day_of_week set to false")
                }

                appContext.resources.getString(R.string.home_category) -> {
                    _home.value = false
                    Log.d(TAG, "_home set to false")
                }

                appContext.resources.getString(R.string.food_category) -> {
                    _food.value = false
                    Log.d(TAG, "_food set to false")
                }

                appContext.resources.getString(R.string.animal_category) -> {
                    _animal.value = false
                    Log.d(TAG, "_animal set to false")
                }

                appContext.resources.getString(R.string.toy_category) -> {
                    _toy.value = false
                    Log.d(TAG, "_toy set to false")
                }

                appContext.resources.getString(R.string.cloth_category) -> {
                    _cloth.value = false
                    Log.d(TAG, "_cloth set to false")
                }

                appContext.resources.getString(R.string.single_many_category) -> {
                    _single_many.value = false
                    Log.d(TAG, "_single_many set to true")
                }

                appContext.resources.getString(R.string.season_weather_category) -> {
                    _season_weather.value = false
                    Log.d(TAG, "_season_weather set to false")
                }

                appContext.resources.getString(R.string.family_category) -> {
                    _family.value = false
                    Log.d(TAG, "_family set to false")
                }

                appContext.resources.getString(R.string.color_category) -> {
                    _color.value = false
                    Log.d(TAG, "_color set to false")
                }

                appContext.resources.getString(R.string.speech_part_category) -> {
                    _speech_part.value = false
                    Log.d(TAG, "_speech_part set to false")
                }

                appContext.resources.getString(R.string.body_category) -> {
                    _body.value = false
                    Log.d(TAG, "_body set to false")
                }
                appContext.resources.getString(R.string.number_category) -> {
                    _number.value = false
                    Log.d(TAG, "_number set to false")
                }
                appContext.resources.getString(R.string.school_category) -> {
                    _school.value = false
                    Log.d(TAG, "_school set to false")
                }

                appContext.resources.getString(R.string.school_subj_category) -> {
                    _school_subj.value = false
                    Log.d(TAG, "_school_subj set to false")
                }

            }
        }
        Log.d(
            TAG, "completedFilter = " + categoryFilterList.toString() +
                    " Result : " + res
        )
    }

    fun clearFilter() {
        categoryFilterList.clear()
        Log.d(TAG, "Filter cleared" + categoryFilterList.toString())
    }

    fun applyFilter() {
        if (categoryFilterList.isNotEmpty()) {
//            categoryFilterList.let {
            this.words = dictionary.words.filter { categoryFilterList.contains(it.category) }
            Log.d(TAG, "Applying filter = " + categoryFilterList.toString())
        } else {
            this.words = dictionary.words
            Log.d(TAG, "Using whole dictionary")
        }
    }

    private fun getNextWord() {
        currentWord = words.random()
        Log.d(TAG, "getNextWord started word = " + currentWord)
        if (wordsList.contains(currentWord)) {
            getNextWord()
        } else {
            _currentRussianWord.value = currentWord.translation
            _currentWordCount.value = (_currentWordCount.value)?.inc()
            _attempts.value = 0
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
        _attempts.value = (_attempts.value)?.inc()
    }

    private fun recalcScore(wordCheck: Boolean) {
        var k: Double = 1.00

        Log.d(TAG, "[recalcScore] score=${_score.value}, atempts=${_attempts.value} ")

        if (wordCheck) {
            if (_attempts.value!! > 0) {
                k = k - ((_attempts.value!!) * 0.2)
            }
            _score.value = _score.value!! + (SCORE_INCREASE * k).roundToInt()
            Log.d(TAG, "[recalcScore] recalc=${_score.value}")
        } else {
            if (_attempts.value!! > 4) {
                Toast.makeText(
                    appContext, "Количество попыток исчерпано," +
                            " Нажмите \"Пропустить\"", Toast.LENGTH_LONG
                ).show()
            } else {
                _attempts.value = (_attempts.value!!).inc()
            }
        }

        /*
                if (wordCheck) {
        //            _currentWordCount.value = (_currentWordCount.value)?.inc()
                    recalc =
                        (((_score.value!!.toDouble() + SCORE_INCREASE) / attempts.value!!) * _currentWordCount.value!!).roundToInt()
                    Log.d(
                        TAG,
                        "[recalcScore] wordCheck=TRUE, translated=${_currentWordCount.value}, recalc = $recalc"
                    )
                } else {
                    recalc =
                        ((_score.value!!.toDouble() / attempts.value!!) * _currentWordCount.value!!).roundToInt()
                    Log.d(
                        TAG,
                        "[recalcScore] wordCheck=FALSE, translated=${_currentWordCount.value}, recalc = $recalc"
                    )
                }
                _score.value = recalc

        */

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
            words.forEach {
                if (englishWord.trim().lowercase().equals(it)) {
                    wordCheck = true
                }
            }
        } else {
            if (englishWord.trim().lowercase().equals(currentWord.word.lowercase())) {
                wordCheck = true
            }
        }

        /*
                if (wordCheck) {
                    increaseScore()
                } else {
                    _attempts.value = (_attempts.value)?.inc()
                }
        */

        recalcScore(wordCheck)

        return wordCheck
    }

    fun reinitializeData() {
        applyFilter()
        _score.value = 0
        _currentWordCount.value = 0
        _attempts.value = 0
        wordsList.clear()
        getNextWord()
    }

    override fun onCleared() {
        super.onCleared()
        Log.d(TAG, "View Model destroyed")
    }

}