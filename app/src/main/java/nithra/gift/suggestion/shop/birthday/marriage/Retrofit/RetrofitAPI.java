package nithra.gift.suggestion.shop.birthday.marriage.Retrofit;

import java.util.ArrayList;
import java.util.HashMap;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface RetrofitAPI {
    @FormUrlEncoded
    @POST("data.php")
    Call<ArrayList<SendOtppojo>> getotp(@FieldMap HashMap<String, String> data);

    @FormUrlEncoded
    @POST("data.php")
    Call<ArrayList<CheckOtp>> checkotp(@FieldMap HashMap<String, String> data);

    @FormUrlEncoded
    @POST("data.php")
    Call<ArrayList<AddSeller>> add_seller(
            @Part RequestBody action,
            @Part RequestBody user_id,
            @Part RequestBody shopname,
            @Part RequestBody seller_mobile,
            @Part RequestBody name,
            @Part RequestBody state,
            @Part RequestBody address,
            @Part RequestBody pincode,
            @Part RequestBody latitude,
            @Part RequestBody longitude,
            @Part RequestBody district,
            @Part RequestBody city,
            @Part MultipartBody.Part file);

    @FormUrlEncoded
    @POST("data.php")
    Call<ArrayList<AddGift>> add_gift(@FieldMap HashMap<String, String> data);

    @FormUrlEncoded
    @POST("data.php")
    Call<ArrayList<GiftFor>> gift_giftfor(@FieldMap HashMap<String, String> data);

    @FormUrlEncoded
    @POST("data.php")
    Call<ArrayList<GiftList>> gift_giftlist(@FieldMap HashMap<String, String> data);

    @FormUrlEncoded
    @POST("data.php")
    Call<ArrayList<Occasion>> gift_occasion(@FieldMap HashMap<String, String> data);

    @FormUrlEncoded
    @POST("data.php")
    Call<ArrayList<GiftEdit>> edit_gift(@FieldMap HashMap<String, String> data);

    @FormUrlEncoded
    @POST("data.php")
    Call<ArrayList<SellerProfilePojo>> profile(@FieldMap HashMap<String, String> data);

    @FormUrlEncoded
    @POST("data.php")
    Call<ArrayList<GetGift>> getgift(@FieldMap HashMap<String, String> data);

    @FormUrlEncoded
    @POST("data.php")
    Call<ArrayList<Gift_Cat>> gift_cat(@FieldMap HashMap<String, String> data);

    @FormUrlEncoded
    @POST("data.php")
    Call<ArrayList<Fav_Add_Del>> fav_add_del(@FieldMap HashMap<String, String> data);

    @FormUrlEncoded
    @POST("data.php")
    Call<ArrayList<Fav_view>> fav_view(@FieldMap HashMap<String, String> data);

    @FormUrlEncoded
    @POST("data.php")
    Call<ArrayList<Androidid>> androidid(@FieldMap HashMap<String, String> data);
}
