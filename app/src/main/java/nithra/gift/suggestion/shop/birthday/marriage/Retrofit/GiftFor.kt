package nithra.gift.suggestion.shop.birthday.marriage.Retrofit

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class GiftFor {
    @SerializedName("id")
    @Expose
    var id: String? = null

    @JvmField
    @SerializedName("people")
    @Expose
    var people: String? = null

    @SerializedName("people_logo")
    @Expose
    var peopleLogo: String? = null

    @SerializedName("status")
    @Expose
    var status: String? = null
}