package nithra.gift.suggestion.shop.birthday.marriage.retrofit

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class DeleteGift {
    @SerializedName("status")
    @Expose
    var status: String? = null
}