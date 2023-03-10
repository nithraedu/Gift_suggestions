package nithra.gift.suggestion.shop.birthday.marriage.Feedback

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    var retrofit: Retrofit? = null
        get() {
            field = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            return field
        }
        private set
    private const val BASE_URL = "https://www.nithra.mobi/apps/"
}