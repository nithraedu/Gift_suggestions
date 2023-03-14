package nithra.gift.suggestion.shop.birthday.marriage.retrofit

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class GetCountry {
    @SerializedName("id")
    @Expose
    var id: String? = null

    @SerializedName("iso")
    @Expose
    var iso: String? = null

    @SerializedName("name")
    @Expose
    var name: String? = null

    @SerializedName("nicename")
    @Expose
    var nicename: String? = null

    @SerializedName("iso3")
    @Expose
    var iso3: String? = null

    @SerializedName("numcode")
    @Expose
    var numcode: String? = null

    @SerializedName("phonecode")
    @Expose
    var phonecode: String? = null

    @SerializedName("status")
    @Expose
    var status: String? = null
}