package tinkoff.fintech.lab.data.network.model

import com.google.gson.annotations.SerializedName

data class CurrentFilmResponse(

    @SerializedName("kinopoiskId")
    val filmId: Long,

    @SerializedName("nameRu")
    val nameRu: String? = null,

    @SerializedName("nameEn")
    val nameEn: String? = null,

    @SerializedName("year")
    val year: String? = null,

    @SerializedName("genres")
    val genres: List<GenreResponse> = emptyList(),

    @SerializedName("countries")
    val countries: List<CountriesResponse> = emptyList(),

    @SerializedName("ratingKinopoisk")
    val rating: String? = null,

    @SerializedName("posterUrlPreview")
    val posterUrlPreview: String?,

    @SerializedName("posterUrl")
    val posterUrl: String?,

    @SerializedName("filmLength")
    val filmLength: Int? = null,

    @SerializedName("description")
    val description: String? = null
)