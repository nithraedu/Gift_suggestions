package nithra.gift.suggestion.shop.birthday.marriage.feedback

import retrofit2.Call
import retrofit2.http.FieldMap
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface Method {
    @FormUrlEncoded
    @POST("appfeedback.php")
    fun getAlldata(@FieldMap data: HashMap<String, String?>): Call<List<Feedback>>
}