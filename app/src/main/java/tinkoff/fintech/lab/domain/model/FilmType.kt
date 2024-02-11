package tinkoff.fintech.lab.domain.model

enum class FilmType {
    POPULAR, FAVORITE;

    companion object {
        infix fun getByName(value: String): FilmType =
            entries.firstOrNull { it.name.lowercase() == value.lowercase() }!!
    }
}