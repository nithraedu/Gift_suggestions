package nithra.tamil.word.game.giftsuggestions.Retrofit;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitApiClient {
    private static Retrofit retrofit;
    private static String BASE_URL="http://15.206.173.184/upload/gift_suggestion/api/";
    public static Retrofit getRetrofit(){
        retrofit=new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return retrofit;
    }
}
