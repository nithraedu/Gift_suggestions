package nithra.gift.suggestion.shop.birthday.marriage.Retrofit;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitApiClient {
    private static Retrofit retrofit;
    private static String BASE_URL="https://nithra.mobi/gift_app/api/";
    public static Retrofit getRetrofit(){
        retrofit=new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return retrofit;
    }
}
