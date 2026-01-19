import com.example.namatara.data.model.CategoryResponse
import com.example.namatara.data.model.LoginRequest
import com.example.namatara.data.model.LoginResponse
import com.example.namatara.data.model.RegisterRequest// Data class untuk request pendaftaran
import com.example.namatara.data.model.RegisterResponse // Data class untuk response pendaftaran
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService {

    @POST("api/auth/sign-in")
    fun loginUser(@Body request: LoginRequest): Call<LoginResponse>

    @POST("api/auth/sign-up")
    fun signUpUser(@Body request: RegisterRequest): Call<RegisterResponse>

    @GET("categories")
    fun getCategories(): Call<List<CategoryResponse>>
}