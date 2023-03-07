package nithra.gift.suggestion.shop.birthday.marriage.Retrofit

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Occasion {
    @SerializedName("id")
    @Expose
    var id: String? = null

    @JvmField
    @SerializedName("category")
    @Expose
    var category: String? = null

    @SerializedName("category_logo")
    @Expose
    var categoryLogo: String? = null

    @SerializedName("status")
    @Expose
    var status: String? = null
}