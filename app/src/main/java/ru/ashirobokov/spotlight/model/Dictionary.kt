package ru.ashirobokov.spotlight.model

data class Dictionary(
    val groups: Int? = null,
    val words: List<Word>
) {
    override fun toString(): String {
        return "Dictionary(groups=$groups, words=$words)"
    }
}

