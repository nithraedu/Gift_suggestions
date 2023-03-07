package nithra.gift.suggestion.shop.birthday.marriage.Retrofit

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Androidid {
    @SerializedName("status")
    @Expose
    var status: String? = null

    @SerializedName("user_id")
    @Expose
    var userId: String? = null
}