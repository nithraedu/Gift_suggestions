package nithra.gift.suggestion.shop.birthday.marriage.retrofit

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitApiClient {
    @JvmStatic
    var retrofit: Retrofit? = null
        get() {
            field = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            return field
        }
        private set
    private const val BASE_URL = "https://nithra.mobi/gift_app/api/"
}