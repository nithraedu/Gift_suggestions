package nithra.gift.suggestion.shop.birthday.marriage.retrofit

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Fav_Add_Del {
    @SerializedName("status")
    @Expose
    var status: String? = null

    @SerializedName("fv_action")
    @Expose
    var fvAction: Int? = null

    @SerializedName("msg")
    @Expose
    var msg: String? = null
}