import com.example.melautapp.data.response.LocationResponse
import com.example.melautapp.data.response.ResultResponse
import com.example.melautapp.data.retrofit.ResultRequest
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query
import retrofit2.Response

interface WeatherService {
    @GET("weather")
    suspend fun getLocation(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double
    ): Response<LocationResponse> // Return Response instead of Call

    @POST("result")
    suspend fun postResult(
        @Body request: ResultRequest
    ): Response<ResultResponse> // Menggunakan @Body untuk mengirimkan lat dan lon


    @GET("result")
    suspend fun getPredictionResult(
        @Query("lat") lat: ResultRequest,
        @Query("lon") lon: Double
    ): Response<ResultResponse> // Return Response instead of Call

}
