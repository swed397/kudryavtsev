package tinkoff.fintech.lab.data.network

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import tinkoff.fintech.lab.data.network.model.CurrentFilmResponse
import tinkoff.fintech.lab.data.network.model.FilmsResponse
import tinkoff.fintech.lab.util.API_PARAMETER_FILMS

interface KinopoiskApi {

    @GET("top")
    suspend fun getTopFilms(
        @Query("type") type: String = API_PARAMETER_FILMS,
        @Query("page") page: Int = 1
    ): FilmsResponse

    @GET("{id}")
    suspend fun getFilmById(@Path("id") filmId: Long): CurrentFilmResponse
}