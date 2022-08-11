package nithra.tamil.word.game.giftsuggestions.Retrofit;

import java.util.ArrayList;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface RetrofitAPI {
    @FormUrlEncoded
    @POST("data.php")
    Call<ArrayList<SendOtppojo>> getotp(@FieldMap HashMap<String, String> data);

    @FormUrlEncoded
    @POST("data.php")
    Call<ArrayList<CheckOtp>> checkotp(@FieldMap HashMap<String, String> data);

    @FormUrlEncoded
    @POST("data.php")
    Call<ArrayList<AddSeller>> add_seller(@FieldMap HashMap<String, String> data);
}
