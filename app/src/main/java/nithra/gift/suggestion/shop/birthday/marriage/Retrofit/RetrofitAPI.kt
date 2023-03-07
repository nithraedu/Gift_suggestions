package nithra.gift.suggestion.shop.birthday.marriage.Retrofit

import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.FieldMap
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST
import retrofit2.http.Part

interface RetrofitAPI {
    @FormUrlEncoded
    @POST("data.php")
    fun getotp(@FieldMap data: HashMap<String, String?>): Call<ArrayList<SendOtppojo>>

    @FormUrlEncoded
    @POST("data.php")
    fun checkotp(@FieldMap data: HashMap<String, String?>): Call<ArrayList<CheckOtp>>

    @FormUrlEncoded
    @POST("data.php")
    fun add_seller(
        @Part action: RequestBody,
        @Part user_id: RequestBody,
        @Part shopname: RequestBody,
        @Part seller_mobile: RequestBody,
        @Part name: RequestBody,
        @Part state: RequestBody,
        @Part address: RequestBody,
        @Part pincode: RequestBody,
        @Part latitude: RequestBody,
        @Part longitude: RequestBody,
        @Part district: RequestBody,
        @Part city: RequestBody,
        @Part file: MultipartBody.Part
    ): Call<ArrayList<AddSeller>>

    @FormUrlEncoded
    @POST("data.php")
    fun add_gift(@FieldMap data: HashMap<String, String>): Call<ArrayList<AddGift>>

    @FormUrlEncoded
    @POST("data.php")
    fun gift_giftfor(@FieldMap data: HashMap<String, String>): Call<ArrayList<GiftFor>>

    @FormUrlEncoded
    @POST("data.php")
    fun gift_giftlist(@FieldMap data: HashMap<String, String?>): Call<ArrayList<GiftList>>

    @FormUrlEncoded
    @POST("data.php")
    fun gift_occasion(@FieldMap data: HashMap<String, String?>): Call<ArrayList<Occasion>>

    @FormUrlEncoded
    @POST("data.php")
    fun edit_gift(@FieldMap data: HashMap<String, String?>): Call<ArrayList<GiftEdit>>

    @FormUrlEncoded
    @POST("data.php")
    fun profile(@FieldMap data: HashMap<String, String?>): Call<ArrayList<SellerProfilePojo>>

    @FormUrlEncoded
    @POST("data.php")
    fun getgift(@FieldMap data: HashMap<String, String?>): Call<ArrayList<GetGift>>

    @FormUrlEncoded
    @POST("data.php")
    fun gift_cat(@FieldMap data: HashMap<String, String?>): Call<ArrayList<Gift_Cat>>

    @FormUrlEncoded
    @POST("data.php")
    fun fav_add_del(@FieldMap data: HashMap<String, String?>): Call<ArrayList<Fav_Add_Del>>

    @FormUrlEncoded
    @POST("data.php")
    fun fav_view(@FieldMap data: HashMap<String, String?>): Call<ArrayList<Fav_view>>

    @FormUrlEncoded
    @POST("data.php")
    fun androidid(@FieldMap data: HashMap<String, String?>): Call<ArrayList<Androidid>>

    @FormUrlEncoded
    @POST("data.php")
    fun delete_gift(@FieldMap data: HashMap<String, String?>): Call<ArrayList<DeleteGift>>

    @FormUrlEncoded
    @POST("data.php")
    fun country(@FieldMap data: HashMap<String, String>): Call<ArrayList<GetCountry>>
}