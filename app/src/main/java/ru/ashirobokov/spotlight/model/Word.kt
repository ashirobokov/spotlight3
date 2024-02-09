package ru.ashirobokov.spotlight.model

data class Word(
    val word: String,
    val translation: String,
    val category: String
) {
    override fun toString(): String {
        return "Word(word='$word', translation='$translation', category='$category')"
    }
}