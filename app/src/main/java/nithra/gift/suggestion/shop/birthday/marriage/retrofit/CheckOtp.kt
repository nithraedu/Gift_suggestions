package nithra.gift.suggestion.shop.birthday.marriage.retrofit

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class CheckOtp {
    @SerializedName("id")
    @Expose
    var id: String? = null

    @SerializedName("name")
    @Expose
    var name: String? = null

    @SerializedName("gmail")
    @Expose
    var gmail: String? = null

    @SerializedName("otp")
    @Expose
    var otp: String? = null

    @SerializedName("status")
    @Expose
    var status: String? = null
}