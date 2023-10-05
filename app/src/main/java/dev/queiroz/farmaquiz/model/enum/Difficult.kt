package dev.queiroz.farmaquiz.model.enum

enum class Difficult(scoreValue: Int = 0) {
    easy(scoreValue = 5),
    medium(scoreValue = 10),
    hard(scoreValue = 20)
}