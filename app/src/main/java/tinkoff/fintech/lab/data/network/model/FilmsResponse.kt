package tinkoff.fintech.lab.data.network.model

import com.google.gson.annotations.SerializedName

data class FilmsResponse(

    @SerializedName("films") val films: List<FilmPreviewResponse>
)

data class FilmPreviewResponse(

    @SerializedName("filmId")
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

    @SerializedName("rating")
    val rating: String? = null,

    @SerializedName("posterUrlPreview")
    val posterUrlPreview: String?,

    @SerializedName("posterUrl")
    val posterUrl: String?,
)

data class GenreResponse(
    @SerializedName("genre")
    val genre: String? = null
)

data class CountriesResponse(
    @SerializedName("country")
    val country: String? = null
)